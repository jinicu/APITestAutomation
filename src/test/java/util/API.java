package util;

import io.cucumber.cienvironment.internal.com.eclipsesource.json.Json;
import io.cucumber.cienvironment.internal.com.eclipsesource.json.JsonObject;
import org.apache.commons.httpclient.NameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class API {
    public static final Properties properties = new Properties();
    public static final Properties endpoints = new Properties();
    public static ThreadLocal<String> endpoint = new ThreadLocal<>();
    public static ThreadLocal<JsonObject> jsonPayload = new ThreadLocal<>();
    public static ThreadLocal<String> payload = new ThreadLocal<>();
    public static ThreadLocal<JsonObject> jsonResponseBodyMem = new ThreadLocal<>();
    public static ThreadLocal<JsonObject> jsonResponseBody = new ThreadLocal<>();

    public static ThreadLocal<Map<String, JsonObject>> responseBodyMem = new ThreadLocal<>();
    public static ThreadLocal<Integer> responseCode = new ThreadLocal<>();
    public static ThreadLocal<List<String[]>> headers = new ThreadLocal<>();
    public static ThreadLocal<List<String[]>> parameters = new ThreadLocal<>();

    public void apiRequest(String requestMethod){
        HttpURLConnection connection = null;
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
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setDoOutput(true);
            if(!(headers.get() == null)) {
                for (String[] head : headers.get()) {
                    connection.setRequestProperty(head[0], head[1]);
                }
            }
            if(!(payload.get() == null)){
                byte[] out = payload.get().getBytes(Charset.forName("UTF-8"));
                OutputStream stream = connection.getOutputStream();
                stream.write(out);
            }
            responseCode.set(connection.getResponseCode());
            jsonResponseBody.set((JsonObject) Json.parse(getResponse(connection)));
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally{
            connection.disconnect();
            payload.set(null);
            headers.set(null);
            parameters.set(null);
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
