package util;

import io.cucumber.cienvironment.internal.com.eclipsesource.json.Json;
import io.cucumber.cienvironment.internal.com.eclipsesource.json.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;

public class API {
    public static final Properties properties = new Properties();
    public static final Properties endpoints = new Properties();
    public static ThreadLocal<String> endpoint = new ThreadLocal<>();
    public static ThreadLocal<JsonObject> jsonPayload = new ThreadLocal<>();
    public static ThreadLocal<String> payload = new ThreadLocal<>();
    public static ThreadLocal<JsonObject> jsonResponseBody = new ThreadLocal<>();
    public static ThreadLocal<Integer> responseCode = new ThreadLocal<>();

    public void apiRequest(String requestMethod){
        HttpURLConnection connection = null;
        try {
            URL url = new URL(properties.getProperty("baseUrl").concat(endpoint.get()));
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            if(!(payload.get() == null)){
                byte[] out = payload.get().getBytes(Charset.forName("UTF-8"));
                OutputStream stream = connection.getOutputStream();
                stream.write(out);
            }
            responseCode.set(connection.getResponseCode());
            jsonResponseBody.set((JsonObject) Json.parse(getResponse(connection)));
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            connection.disconnect();
            payload.set(null);
        }

    }

    public String getResponse(HttpURLConnection connection) throws IOException {
        BufferedReader br;
        if(connection.getResponseCode() >= 400){
            br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }else {
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();
        return sb.toString();
    }

}
