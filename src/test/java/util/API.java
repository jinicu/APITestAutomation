package util;

import io.cucumber.cienvironment.internal.com.eclipsesource.json.Json;
import io.cucumber.cienvironment.internal.com.eclipsesource.json.JsonObject;
import io.cucumber.cienvironment.internal.com.eclipsesource.json.JsonValue;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class API {
    public static final Properties properties = new Properties();
    public static final Properties endpoints = new Properties();
    public static ThreadLocal<String> endpoint = new ThreadLocal<>();
    public static ThreadLocal<JsonObject> jsonPayload = new ThreadLocal<>();
    public static ThreadLocal<String> payload = new ThreadLocal<>();
    public static ThreadLocal<JsonObject> jsonResponseBodyMem = new ThreadLocal<>();
    public static ThreadLocal<JsonObject> jsonResponseBody = new ThreadLocal<>();

    public static ThreadLocal<Map<String, JsonObject>> responseBodyMem = new ThreadLocal<>();

    public static ThreadLocal<Map<String, JsonValue>> responseValueMem = new ThreadLocal<>();
    public static ThreadLocal<Integer> responseCode = new ThreadLocal<>();
    public static ThreadLocal<List<String[]>> headers = new ThreadLocal<>();
    public static ThreadLocal<List<String[]>> parameters = new ThreadLocal<>();
    private static Logger logger = Logger.getLogger(API.class.getName());
    public void apiRequest(String requestMethod){
        String urlString = properties.getProperty("baseUrl").concat(endpoint.get());
        if(!(parameters.get() == null)) {
            String[] param;
            for(int i = 0; i < parameters.get().size(); i++){
                param = parameters.get().get(i);
                if(i == 0){
                    urlString = urlString.concat(String.format("?%s=%s", param[0], param[1]));
                }else{
                    urlString = urlString.concat(String.format("&%s=%s", param[0], param[1]));
                }
            }
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(urlString));

        if (!(headers.get() == null)) {
            for (String[] head : headers.get()) {
                requestBuilder.headers(head[0].trim(), head[1].trim());
            }
        }
        if (!(payload.get() == null)) {
            requestBuilder.method(requestMethod, HttpRequest.BodyPublishers.ofString(payload.get()));
        }else{
            requestBuilder.method(requestMethod, HttpRequest.BodyPublishers.noBody());
        }
        if(Boolean.parseBoolean(System.getProperty("apiTimeoutSet")) == true){
            requestBuilder.timeout(Duration.ofMillis(Long.parseLong(System.getProperty("apiTimeoutVal"))));
        }
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch(HttpTimeoutException e){
            logger.log(Level.INFO,
                    String.format("HttpTimeoutException for tomeout: %s milliseconds. Try reconsider changing apiTimeoutVal"
                            , System.getProperty("apiTimeoutVal")) );
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        responseCode.set(response.statusCode());
        if(!response.body().isEmpty()) {
            jsonResponseBody.set((JsonObject) Json.parse(response.body()));
        }else{
            jsonResponseBody.set(null);
        }
        payload.set(null);
        headers.set(null);
        parameters.set(null);
    }

}
