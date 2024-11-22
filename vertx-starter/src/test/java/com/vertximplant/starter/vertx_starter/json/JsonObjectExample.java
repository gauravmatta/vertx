package com.vertximplant.starter.vertx_starter.json;

import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonObjectExample {

  @Test
  void jsonObjectCanBeMapped(){
    JsonObject myJson = new JsonObject();
    myJson.put("id",1);
    myJson.put("name","Gaurav");
    myJson.put("loves_vertex",true);
   assertEquals("{\"id\":1,\"name\":\"Gaurav\",\"loves_vertex\":true}",myJson.encode());
   final String encoded = myJson.encode();
    JsonObject decodedJsonObject = new JsonObject(encoded);
    assertEquals(myJson,decodedJsonObject);
  }

  @Test
  void jsonObjectCanBeCreatedFromMap(){
    final Map<String,Object> myMap = new HashMap<>();
    myMap.put("id",1);
    myMap.put("name","Gaurav");
    myMap.put("loves_vertx",true);
    final JsonObject asJsonObject = new JsonObject(myMap);
    assertEquals(myMap,asJsonObject.getMap());
    assertEquals(1, asJsonObject.getInteger("id"));
    assertEquals("Gaurav",asJsonObject.getString("name"));
    assertEquals(true,asJsonObject.getBoolean("loves_vertx"));
  }
}
