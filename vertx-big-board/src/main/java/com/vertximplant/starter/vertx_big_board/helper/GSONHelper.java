package com.vertximplant.starter.vertx_big_board.helper;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GSONHelper {
  public static Gson gson = new Gson();
  public static JsonParser jsonParser = new JsonParser();

  public static <T> T stringToGSON(String jsonString,Class<T> clazz){
    return gson.fromJson(jsonString,clazz);
  }

  public static String gsonToString(Object object){
    return gson.toJson(object);
  }

  public static JsonObject stringToJsonObject(String str){
    return jsonParser.parse(str).getAsJsonObject();
  }
}
