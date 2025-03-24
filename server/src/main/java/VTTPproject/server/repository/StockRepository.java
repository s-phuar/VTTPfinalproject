package VTTPproject.server.repository;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.kv.UpsertOptions;

import VTTPproject.server.model.StockSummary;
import VTTPproject.server.utils.Utils;
import jakarta.json.JsonObject;


@Repository
public class StockRepository {

    private final CouchbaseTemplate template;

    @Autowired
    public StockRepository(CouchbaseTemplate template) {
        this.template = template;
    }

    @Autowired
    JdbcTemplate templateSQL;
    
    private static final String SQL_INSERT = "INSERT INTO stocks (user_email, ticker, company_name) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE company_name = VALUES(company_name)";
    private static final String SQL_DELETE = "DELETE FROM stocks WHERE user_email = ? AND ticker = ?";
    private static final String SQL_SELECT_BY_EMAIL = "SELECT ticker, company_name FROM stocks WHERE user_email = ?";

    public void updateSQL(String email, String symbol, String companyName, boolean insert){
        if (insert) {
            templateSQL.update(SQL_INSERT, email, symbol, companyName);
        }else{
            templateSQL.update(SQL_DELETE, email, symbol);
        }
    }


    public List<StockSummary> getPortfolio(String email){
        List<StockSummary> stockSumList = new LinkedList<>();

        stockSumList = templateSQL.query(SQL_SELECT_BY_EMAIL, BeanPropertyRowMapper.newInstance(StockSummary.class), email);
        return stockSumList;
    }    


    //insert stock json string into couchbase
    public String insertStockJson(JsonObject jObj) {
        String symbol = jObj.getJsonObject("mi").getString("symbol");
        if (symbol == null) {
            throw new IllegalArgumentException("JSON must contain an 'symbol' field");
        }
        //upsert raw json string to couchbase
        //******************* update timer */
        UpsertOptions options = UpsertOptions.upsertOptions().expiry(Duration.ofMinutes(60));
        template.getCouchbaseClientFactory().getBucket().defaultCollection().upsert(symbol, jObj.toString(), options);        
        return symbol;
    }

    //get stock json by ticker from couchbase
    public Optional<JsonObject> findStockByTicker(String ticker){
        Collection collection = template.getCouchbaseClientFactory().getBucket().defaultCollection();
            try {
                // Retrieve document by key
                String jsonString = collection.get(ticker).contentAs(String.class);
                JsonObject jsonObj = Utils.strToJson(jsonString);
                return Optional.of(jsonObj);
            } catch (DocumentNotFoundException e) {
                return Optional.empty();
            }
    }

    


    
}
