package com.vertximplant.starter.vertx_starter.json;

import com.vertximplant.starter.vertx_starter.entity.Person;
import io.vertx.core.json.JsonArray;
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

  @Test
  void jsonArrayCanBeMapped() {
  final JsonArray myJsonArray =  new JsonArray();
  myJsonArray
    .add(new JsonObject().put("id",1))
    .add(new JsonObject().put("id",2))
    .add(new JsonObject().put("id",3))
    .add("randomvalue");
  assertEquals("[{\"id\":1},{\"id\":2},{\"id\":3},\"randomvalue\"]",myJsonArray.encode());
  }

  @Test
  void canMapJavaObjects(){
    final Person person = new Person(1,"Gaurav",true);
    final JsonObject gaurav = JsonObject.mapFrom(person);
    assertEquals(person.getId(),gaurav.getInteger("id"));
    assertEquals(person.getName(),gaurav.getString("name"));
    assertEquals(person.isLovesVertx(),gaurav.getBoolean("lovesVertx"));
    Person person1 = gaurav.mapTo(Person.class);
    assertEquals(person.getName(),person1.getName());
    assertEquals(person.getId(),person1.getId());
    assertEquals(person.isLovesVertx(),person1.isLovesVertx());
  }
}
