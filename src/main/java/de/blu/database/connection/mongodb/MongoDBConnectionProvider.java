package de.blu.database.connection.mongodb;

import com.mongodb.ClientSessionOptions;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.session.ClientSession;
import lombok.Getter;
import org.bson.Document;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Getter
public final class MongoDBConnectionProvider implements MongoDBConnection {

  private ExecutorService executorService = Executors.newCachedThreadPool();

  private MongoClient client;
  private ClientSession session;
  private MongoDatabase database;

  private String host;
  private int port;
  private String databaseName;
  private String username;
  private String password;

  public void init(String host, int port, String databaseName, String username, String password) {
    this.host = host;
    this.port = port;
    this.databaseName = databaseName;
    this.username = username;
    this.password = password;
  }

  public void init(String host, int port, String databaseName) {
    this.host = host;
    this.port = port;
    this.databaseName = databaseName;
    this.username = "";
    this.password = "";
  }

  public void connect() {
    this.connect(true);
  }

  public void connect(boolean printException) {
    try {
      String uri =
          "mongodb://"
              + (username.isEmpty() ? "" : this.username)
              + (password.isEmpty() ? "" : ":" + this.password)
              + (username.isEmpty() ? "" : "@")
              + this.host
              + ":"
              + this.port
              + "/admin";

      System.out.println(
          "Connecting to MongoDB with uri: " + uri.replaceAll(this.password, "password_hidden"));

      this.client = new MongoClient(new MongoClientURI(uri));
      this.database = this.client.getDatabase(this.databaseName);
      this.session = this.client.startSession(ClientSessionOptions.builder().build());
    } catch (Exception e) {
      if (printException) {
        e.printStackTrace();
      }
    }
  }

  public void disconnect() {
    if (!this.isConnected()) {
      return;
    }

    this.session.close();
    this.session = null;
  }

  @Override
  public boolean isConnected() {
    return this.session != null;
  }

  @Override
  public MongoCollection<Document> getCollection(String collectionName) {
    return this.database.getCollection(collectionName);
  }

  @Override
  public void getCollectionAsync(
      String collectionName, Consumer<MongoCollection<Document>> collectionCallback) {
    this.getExecutorService()
        .execute(() -> collectionCallback.accept(this.getCollection(collectionName)));
  }
}
