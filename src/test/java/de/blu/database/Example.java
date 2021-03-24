package de.blu.database;

import de.blu.database.connection.mongodb.MongoDBConnectionProvider;
import de.blu.database.connection.redis.RedisConnectionProvider;
import org.bson.Document;

import java.util.Collection;

public class Example {

  public static void main(String[] args) {
    //redisExample();
    mongoDbExample();
  }

  private static void mongoDbExample() {
    MongoDBConnectionProvider mongoDBConnection = new MongoDBConnectionProvider();
    mongoDBConnection.init("localhost", 27017, "admin", "", "");
    mongoDBConnection.connect();

    if (!mongoDBConnection.isConnected()) {
      System.out.println("Something went wrong while connecting to MongoDB.");
      return;
    }

    System.out.println("Connected to MongoDB!");

    mongoDBConnection.getCollection("testCollection").insertOne(new Document("key1", "value1"));

    mongoDBConnection.disconnect();
  }

  private static void redisExample() {
    RedisConnectionProvider redisConnection = new RedisConnectionProvider();
    redisConnection.init("localhost", 6379, "");
    redisConnection.connect(false);
    if (!redisConnection.isConnected()) {
      System.out.println("Something went wrong while connecting to Redis.");
      return;
    }

    System.out.println("Connected to Redis!");

    System.out.println("Keys:");
    Collection<String> keys = redisConnection.getKeys("", true);
    for (String key : keys) {
      System.out.println("- " + key);
    }

    redisConnection.set("testKey", "testValue", 5);

    System.out.println("Keys2:");
    keys = redisConnection.getKeys("", true);
    for (String key : keys) {
      System.out.println("- " + key);
    }

    redisConnection.disconnect();
  }
}
