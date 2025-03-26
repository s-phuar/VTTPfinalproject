package VTTPproject.server.repository;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;


import VTTPproject.server.model.StockSummary;
import VTTPproject.server.utils.Utils;
import VTTPproject.server.utils.sql;
import jakarta.annotation.PostConstruct;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;


@Repository
public class StockRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void init() {
        mongoTemplate.indexOps("stocks").ensureIndex(
            new org.springframework.data.mongodb.core.index.Index()
                .on("createdAt", Sort.Direction.ASC)
                .expire(Duration.ofMinutes(1440)) 
        );
    }

    
    @Autowired @Qualifier("redis-string")
    private RedisTemplate<String, String> templateRedis;

    @Autowired
    JdbcTemplate templateSQL;

    public void updateSQL(String email, String symbol, String companyName, boolean insert){
        if (insert) {
            templateSQL.update(sql.sql_insert, email, symbol, companyName);
            templateRedis.delete("portfolio:"+ email); //clear redis for the user
            // System.out.println("reached redis portfolio insert");

        }else{
            templateSQL.update(sql.sql_delete, email, symbol);
            templateRedis.delete("portfolio:"+ email); //clear redis for the user
            // System.out.println("reached redis portfolio delete");
        }
    }


    public List<StockSummary> getPortfolio(String email){

        String cachedPortfolio = templateRedis.opsForValue().get("portfolio:"+ email);
        if (cachedPortfolio != null) {
            // System.out.println("reached redis portfolio get");
            try {
                // building List<StockSummary> from raw json string
                List<StockSummary> stockSumList = new LinkedList<>();
                JsonArray jArray = Utils.strToJsonArray(cachedPortfolio);
                for (JsonValue jsonValue : jArray) {
                    JsonObject jsonObj = jsonValue.asJsonObject();
                    StockSummary stock = new StockSummary();
                    stock.setTicker(jsonObj.getString("ticker"));
                    stock.setCompanyName(jsonObj.getString("companyName"));
                    stockSumList.add(stock);
                }

                return stockSumList;
            } catch (Exception e) {
                System.out.println("Error deserializing portfolio from Redis: " + e.getMessage());
            }
        }

        //mySQL
        List<StockSummary> stockSumList = new LinkedList<>();
        stockSumList = templateSQL.query(sql.sql_selectByEmail, BeanPropertyRowMapper.newInstance(StockSummary.class), email);
        
        // Convert List<StockSummary> to JSON string and cache in Redis
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        stockSumList.stream()
            .forEach(stock -> {
                JsonObject temp = Json.createObjectBuilder()
                .add("ticker", stock.getTicker())
                .add("companyName", stock.getCompanyName())
                .build();

                jsonArrayBuilder.add(temp);
            });
        JsonArray jArray = jsonArrayBuilder.build();


        String jsonString = jArray.toString();
        templateRedis.opsForValue().set("portfolio:"+ email, jsonString, Duration.ofMinutes(15));
        
        
        return stockSumList;
    }    


    //insert stock json string into couchbase + redis
    public String insertStockJson(JsonObject jObj) {
        String symbol = jObj.getJsonObject("mi").getString("symbol");
        if (symbol == null) {
            throw new IllegalArgumentException("json object must contain an 'symbol' field");
        }

        //upsert raw json string to couchbase, 1 day
        String jsonString = jObj.toString();

        Document doc = new Document("_id", symbol)
            .append("data", jsonString)
            .append("createdAt", Instant.now());
        mongoTemplate.save(doc, "stocks");


        //store in redis raw json string, 15 min
        // System.out.println("inserted in to redis");
        templateRedis.opsForValue().set(symbol, jsonString, Duration.ofMinutes(15));

        return symbol;
    }

    //get stock json by ticker from couchbase/redis
    public Optional<JsonObject> findStockByTicker(String ticker){
        // Check Redis first for the raw JSON string
        String cachedJsonString = templateRedis.opsForValue().get(ticker);
        if (cachedJsonString != null) {
            JsonObject jsonObj = Utils.strToJson(cachedJsonString);
            return Optional.of(jsonObj);
        }

        //if not check couchbase
        Query query = new Query(Criteria.where("_id").is(ticker));
        Document doc = mongoTemplate.findOne(query, Document.class, "stocks");
        if (doc != null) {
            String jsonString = doc.getString("data");
            JsonObject jsonObj = Utils.strToJson(jsonString);
            return Optional.of(jsonObj);
        }
        return Optional.empty();
    }

    


    
}
