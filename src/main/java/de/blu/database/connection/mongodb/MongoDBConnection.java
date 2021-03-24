package de.blu.database.connection.mongodb;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.function.Consumer;

public interface MongoDBConnection {

  /**
   * Check if is connected
   *
   * @return true if is connected
   */
  boolean isConnected();

  /**
   * Get the Collection
   *
   * @return collection
   */
  MongoCollection<Document> getCollection(String collectionName);

  /**
   * Get the Collection async
   *
   * @return collection
   */
  void getCollectionAsync(
      String collectionName, Consumer<MongoCollection<Document>> collectionCallback);
}
