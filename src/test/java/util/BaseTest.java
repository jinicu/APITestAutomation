package util;

import io.cucumber.cienvironment.internal.com.eclipsesource.json.Json;
import io.cucumber.cienvironment.internal.com.eclipsesource.json.JsonArray;
import io.cucumber.cienvironment.internal.com.eclipsesource.json.JsonObject;

import io.cucumber.cienvironment.internal.com.eclipsesource.json.JsonValue;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

public class BaseTest extends API{
    public static Map<String, Map<String, String>> comMap = new HashMap<>();
    public static final Map<String, String> emailMap = new HashMap<>();
    public static final Map<String, String> passwordMap = new HashMap<>();



    public void readEnvTD(String sheetName){
        XSSFWorkbook wb = new XSSFWorkbook();
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new
                    File(String.format("src/test/resources/testData/%std.xlsx", System.getProperty("env"))));
            EncryptionInfo info = new EncryptionInfo(fs);
            Decryptor d = Decryptor.getInstance(info);
            try {
                if (d.verifyPassword(System.getProperty("env") + properties.get("tdAuthenticator"))) {
                   wb = new XSSFWorkbook(d.getDataStream(fs));
                } else {
                    // Password is wrong
                }
            }catch(GeneralSecurityException e){
                e.printStackTrace();
            }
        }catch(IOException es){
            es.printStackTrace();
        }
        Sheet sheet = wb.getSheet(sheetName);
        int lastRow = sheet.getLastRowNum();
        for(int i = 0; i <= lastRow; i++){
            Cell cellKey = sheet.getRow(i).getCell(0);
            Cell cellValue = sheet.getRow(i).getCell(1);
            comMap.get(sheetName).put(cellKey.toString(), cellValue.toString());
        }

    }

    public void readPayloadFile(String reqPayload, String fileType ){
        try {
            if(fileType.equalsIgnoreCase("json")){
                jsonPayload.set((JsonObject) Json.parse(new FileReader(
                     String.format("src/test/resources/payloads/%s", reqPayload))));
                payload.set(jsonPayload.get().toString());
                String[] contType = {"Content-Type", "application/json"};
                setHeader(contType);
            } else if (fileType.equalsIgnoreCase("text")) {
                payload.set(new FileReader(String.format("src/test/resources/payloads/%s", reqPayload)).toString());
                String[] contType = {"Content-Type", "text/plain"};
                setHeader(contType);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public JsonObject readJSONResponseFile(String expectedResponse){
        try {
            return (JsonObject) Json.parse(new FileReader(
                    String.format("src/test/resources/responses/%s", expectedResponse)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setHeader(String[] head){
        List<String[]> header = new ArrayList<>();
        header.add(head);
        if(headers.get() == null){
            headers.set(header);
        }else{
            headers.get().add(head);
        }
    }

    public void storeResponseBody(String responseKey, JsonObject jsonRes){
        if(responseBodyMem.get() == null){
            Map<String, JsonObject> res = new HashMap<>();
            res.put(responseKey, jsonRes);
            responseBodyMem.set(res);
        }else{
            responseBodyMem.get().put(responseKey, jsonRes);
        }
        jsonResponseBody.set(null);
    }

    public void storeResponseValue(String responseKey, JsonValue jsonRes){
        if(responseValueMem.get() == null){
            Map<String, JsonValue> res = new HashMap<>();
            res.put(responseKey, jsonRes);
            responseValueMem.set(res);
        }else{
            responseValueMem.get().put(responseKey, jsonRes);
        }
        jsonResponseBody.set(null);
    }

    public void assignValueJsonPayload(String keyName, String value){
        String[] keyNameDir = keyName.split("[.]");
        JsonObject keyHolder = jsonPayload.get();

        for(int i = 0; i < keyNameDir.length; i++){
            String[] keyDir = keyNameDir[i].split("[(]");
            if(keyHolder.get(keyDir[0]) instanceof JsonObject){
                keyHolder = (JsonObject) keyHolder.get(keyDir[0]);
            } else if (keyHolder.get(keyDir[0]) instanceof JsonArray) {
                keyHolder = (JsonObject) ((JsonArray) keyHolder
                        .get(keyDir[0])).get(Integer.valueOf(keyDir[1].replace(")", "")));
            }
        }
        keyHolder.set(keyNameDir[keyNameDir.length-1], value);
    }

    public JsonValue getJsonResponseValue(String keyName){
        String[] keyNameDir = keyName.split("[.]");
        JsonValue valueKey;
        jsonResponseBodyMem.set(jsonResponseBody.get());
        ThreadLocal<JsonObject> keyHolder = jsonResponseBody;
        String jsonType = null;
        for(int i = 0; i < keyNameDir.length; i++){
            String[] keyDir = keyNameDir[i].split("[(]");
            if(keyHolder.get().get(keyDir[0]) instanceof JsonObject){
                keyHolder.set( (JsonObject) keyHolder.get().get(keyDir[0]));
                jsonType = "object";
            } else if (keyHolder.get().get(keyDir[0]) instanceof JsonArray) {
                if(keyDir.length == 1){
                    valueKey = keyHolder.get().get(keyDir[0]);
                    return valueKey;
                }
                keyHolder.set((JsonObject) ((JsonArray) keyHolder.get()
                        .get(keyDir[0])).get(Integer.valueOf(keyDir[1].replace(")", ""))));
                jsonType = "array";
            } else {
                jsonType = "string";
            }
        }
        valueKey = keyHolder.get().get(keyNameDir[keyNameDir.length-1]);
        if((valueKey == null) && !jsonType.equals("string") && (jsonType.equals("object") || jsonType.equals("array"))){
            valueKey = keyHolder.get();
        }
        jsonResponseBody.set(jsonResponseBodyMem.get());
        return valueKey;
    }

    public JsonValue getJsonValue(JsonObject objectJson, String keyName){
        String[] keyNameDir = keyName.split("[.]");
        JsonValue valueKey;
        JsonObject keyHolder = objectJson;
        String jsonType = null;
        for(int i = 0; i < keyNameDir.length; i++){
            String[] keyDir = keyNameDir[i].split("[(]");
            if(keyHolder.get(keyDir[0]) instanceof JsonObject){
                keyHolder = (JsonObject) keyHolder.get(keyDir[0]);
                jsonType = "object";
            } else if (keyHolder.get(keyDir[0]) instanceof JsonArray) {
                if(keyDir.length == 1){
                    valueKey = keyHolder.get(keyDir[0]);
                    return valueKey;
                }
                keyHolder =(JsonObject) ((JsonArray) keyHolder.get(keyDir[0]))
                        .get(Integer.valueOf(keyDir[1].replace(")", "")));
                jsonType = "array";
            }else{
                jsonType = "string";
            }
        }
        valueKey = keyHolder.get(keyNameDir[keyNameDir.length-1]);
        if((valueKey == null) && !jsonType.equals("string") && (jsonType.equals("object") || jsonType.equals("array"))){
                valueKey = keyHolder;
        }
        return valueKey;
    }


    public List<String[]> separatorKeyValue(String keysValues){
        List<String[]> keyVal = new ArrayList<>();
        String[] entries = keysValues.split(",");
        for(String en: entries){
            keyVal.add(en.split("~"));
        }
        return keyVal;
    }


}
