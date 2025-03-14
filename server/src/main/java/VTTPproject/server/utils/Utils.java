package VTTPproject.server.utils;

import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Utils {
    



    public static JsonObject strToJson(String str){
        JsonReader reader = Json.createReader(new StringReader(str));
        JsonObject jObj = reader.readObject();

        return jObj;
    }



}
