package app;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class Scraper {
  private final String url;
  private String params;

  public Scraper(String url, String params) {
    this.url = url;
    this.params = params;
  }

  public Scraper(String url) {
    this(url, "");
  }

  public void setParams(String params) {
    this.params = URLEncoder.encode(params, StandardCharsets.UTF_8);
  }

  public String getResponse() throws IOException, InterruptedException, URISyntaxException {
    URI requestUrl = new URI(this.url + this.params);
    System.out.println("Download: " + requestUrl.toString());
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request =
        HttpRequest.newBuilder().GET().header("accept", "application/json").uri(requestUrl).build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    String responseString = response.body();
    if (responseString.equals("{\"Message\":\"An error has occurred.\"}")) {
      return "[]";
    }
    return responseString;
  }
}
