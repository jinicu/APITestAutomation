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

    public void readJSONPayload(String reqPayload){
        try {
            jsonPayload.set((JsonObject) Json.parse(new FileReader(
                    String.format("src/test/resources/payloads/%s.json", reqPayload))));
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public void assignValueJson(String keyName, String value){
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
        ThreadLocal<JsonObject> keyHolder = jsonResponseBody;

        for(int i = 0; i < keyNameDir.length; i++){
            String[] keyDir = keyNameDir[i].split("[(]");
            if(keyHolder.get().get(keyDir[0]) instanceof JsonObject){
                keyHolder.set( (JsonObject) keyHolder.get().get(keyDir[0]));
            } else if (keyHolder.get().get(keyDir[0]) instanceof JsonArray) {
                keyHolder.set((JsonObject) ((JsonArray) keyHolder.get()
                        .get(keyDir[0])).get(Integer.valueOf(keyDir[1].replace(")", ""))));
            }
        }
        return keyHolder.get().get(keyNameDir[keyNameDir.length-1]);
    }

    public List<String[]> responseValidatorData(String responseValues){
        List<String[]> resVal = new ArrayList<>();
        String[] entries = responseValues.split(",");
        for(String en: entries){
            resVal.add(en.split("~"));
        }
        return resVal;
    }
}
