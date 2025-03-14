package VTTPproject.server.repository;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.stereotype.Repository;

import com.couchbase.client.java.kv.InsertOptions;
import com.couchbase.client.java.query.QueryResult;

import VTTPproject.server.utils.Utils;
import jakarta.json.JsonObject;


@Repository
public class StockRepository {

    private final CouchbaseTemplate template;

    @Autowired
    public StockRepository(CouchbaseTemplate template) {
        this.template = template;
    }

    //insert stock json string into couchbase
    public void insertStockJson(JsonObject jObj) {
        String id = jObj.getString("ticker");
        if (id == null) {
            throw new IllegalArgumentException("JSON must contain an 'id' field");
        }
        //insert raw json string to couchbase
        //******************* update timer */
        InsertOptions options = InsertOptions.insertOptions().expiry(Duration.ofMinutes(5));
        template.getCouchbaseClientFactory().getBucket().defaultCollection().insert(id, jObj.toString(), options);        
    }

    //get stock json by ticker
    public Optional<JsonObject> findStockByTicker(String ticker){
        String query = "SELECT * FROM `" + template.getBucketName() + "` WHERE ticker = '" + ticker + "'";
        QueryResult result = template.getCouchbaseClientFactory().getCluster().query(query);

        if (result.rowsAsObject().isEmpty()) {
            return Optional.empty();
        }else{
            String jsonString = result.rowsAsObject().get(0).toString();
            JsonObject jsonObj = Utils.strToJson(jsonString);
            return Optional.of(jsonObj);
        }
    }

    


    
}
