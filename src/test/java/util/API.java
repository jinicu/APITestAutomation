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

    public static String endpoint;

    public static JsonObject jsonPayload;

    public static String payload;
    public static JsonObject jsonResponseBody;

    public static int responseCode;

    public void apiRequest(String requestMethod){
        HttpURLConnection connection = null;
        try {
            URL url = new URL(properties.getProperty("baseUrl").concat(endpoint));
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            if(!(payload == null)){
                byte[] out = payload.getBytes(Charset.forName("UTF-8"));
                OutputStream stream = connection.getOutputStream();
                stream.write(out);
            }
            responseCode = connection.getResponseCode();
            jsonResponseBody = (JsonObject) Json.parse(getResponse(connection));
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            connection.disconnect();
            payload = null;
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
