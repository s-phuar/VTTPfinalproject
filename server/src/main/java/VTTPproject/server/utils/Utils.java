package VTTPproject.server.utils;

import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Utils {
    
    public static JsonObject strToJson(String str){
        JsonReader reader = Json.createReader(new StringReader(str));
        JsonObject jObj = reader.readObject();

        return jObj;
    }

    
    public static JsonArray strToJsonArray(String str){
        JsonReader reader = Json.createReader(new StringReader(str));
        JsonArray jArray = reader.readArray();

        return jArray;
    }


    public static JsonObject stockInfoParser(String details) throws NullPointerException, IndexOutOfBoundsException{
        JsonReader reader = Json.createReader(new StringReader(details));
        JsonArray jArray = reader.readArray();
        JsonObject jObj = jArray.getJsonObject(0); // may hit nullpointer exception


        return jObj;
    }


    public static double roundValue(double value){
        return BigDecimal.valueOf(value)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }

    public static String currEpochMilli(){
        return String.valueOf(new Date().getTime());
    }


}
