package in.main.ratelimit;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BlockingBucket;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

public class Client {

  private final static String SERVER_1 = "http://localhost:8080";
  private final static String SERVER_2 = "http://localhost:8081";

  private final static HttpClient httpClient = HttpClient.newHttpClient();

  public static void main(String[] args) throws InterruptedException {
     helloClient();
  }

  private static void helloClient() {
    System.out.println("/hello");
    for (int i = 0; i < 11; i++) {
      get(SERVER_1 + "/hello");
    }
  }

  private static void get(String url) {
    get(url, null);
  }

  private static void get(String url, String apiKey) {
    try {
      Builder builder = HttpRequest.newBuilder().uri(URI.create(url)).GET();
      if (apiKey != null) {
        builder.header("X-api-key", apiKey);
      }
      HttpRequest request = builder.build();
      HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
      System.out.print(response.statusCode());
      if (response.statusCode() == 200) {
        String remaining = response.headers().firstValue("X-Rate-Limit-Remaining")
            .orElse(null);
        System.out.println(" Remaining: " + remaining);
      }
      else {
        String retry = response.headers()
            .firstValue("X-Rate-Limit-Retry-After-Milliseconds").orElse(null);
        System.out.println(" retry after seconds: " + retry);
      }
    }
    catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

}
