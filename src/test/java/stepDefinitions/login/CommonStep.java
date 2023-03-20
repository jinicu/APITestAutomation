package stepDefinitions.login;

import io.cucumber.cienvironment.internal.com.eclipsesource.json.JsonArray;
import io.cucumber.cienvironment.internal.com.eclipsesource.json.JsonObject;
import io.cucumber.cienvironment.internal.com.eclipsesource.json.JsonValue;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONParser;
import org.testng.Assert;
import util.BaseTest;

import java.util.List;

public class CommonStep extends BaseTest {

    @Given("^request url with endpoint: (.*)$")
    public void getUrlEndpoint(String urlEndpoint){
        endpoint.set( endpoints.getProperty(urlEndpoint));
    }

    @And("^endpoint path: (.*)$")
    public void addEnpointPath(String endpointPath){
        endpoint.set( endpoint.get().concat(endpointPath));
    }

    @And("^request payload: (.*) with file type: (.*)$")
    public void getPayload(String reqPayload, String fileType){
        readPayloadFile(reqPayload, fileType);
    }

    @When("^user send request with method: (.*)$")
    public void sendRequest(String requestMethod){
        apiRequest(requestMethod);
    }

    @And("^request parameters: (.*)$")
    public void setRequestParameters(String param){
        parameters.set(separatorKeyValue(param));
    }

    @And("^request headers: (.*)$")
    public void setRequestHeaders(String head){
        for(String[] heads : separatorKeyValue(head)){
            setHeader(heads);
        };
    }

    @Then("^response status code result should be: (.*)$")
    public void verifyStatusCode(int statusCode){
        Assert.assertEquals(responseCode.get(), statusCode, "Response Status Code is Incorrect");
    }

    @And("^response body contains keys with value: (.*)$")
    public void verifyResponseBodyValue(String responseValues){
        List<String[]> resVal = separatorKeyValue(responseValues);
        for(String[] res: resVal){
            if(res[1].equalsIgnoreCase("notEmpty")){
                Assert.assertTrue(!getJsonResponseValue(res[0].trim()).toString().isEmpty(), "Value is Empty");
            } else if (res[1].equalsIgnoreCase("empty")) {
                Assert.assertTrue(getJsonResponseValue(res[0].trim()).toString().isEmpty(), "Value is not Empty");
            } else if (res[1].equalsIgnoreCase("notNull")){
                Assert.assertTrue(!(getJsonResponseValue(res[0].trim()) == null), "Value is Null");
            } else if (res[1].equalsIgnoreCase("null")){
                Assert.assertTrue(getJsonResponseValue(res[0].trim()) == null, "Value is not Null");
            } else {
                Assert.assertEquals(getJsonResponseValue(res[0].trim()).toString().replace("\"", ""),
                        res[1], "Response value is not equal");
            }
        }

    }

    @And("^response array path: (.*) contains keys with value: (.*)$")
    public void verifyResponseArrayContainsKeysValue(String arrayPath, String responseValues){
        JsonArray arrayJson = (JsonArray) getJsonResponseValue(arrayPath);
        List<String[]> resVal = separatorKeyValue(responseValues);
        for(String[] res: resVal){
            boolean responseContains = false;
            for(JsonValue objRes: arrayJson){
                if(objRes instanceof JsonObject){
                    String value = String.valueOf(getJsonValue((JsonObject) objRes, res[0].trim()));
                    if(!(value == null)) {
                        if (res[1].trim().equals(value.replace("\"", "").trim())) {
                            responseContains = true;
                        }
                    }
                }
            }
            Assert.assertTrue(responseContains, String.format("Key: %s with Value: %s is not found", res[0], res[1]));
        }
    }

    @And("^response body contains keys with json value: (.*)$")
    public void verifyResponseBodyJsonValue(String responseValues){
        List<String[]> resVal = separatorKeyValue(responseValues);
        try {
            for (String[] res : resVal) {
                if (res[1].contains(".json")) {
                    JSONAssert.assertEquals((JSONObject) JSONParser.parseJSON(String.valueOf(getJsonResponseValue(res[0]))),
                            (JSONObject) JSONParser.parseJSON(String.valueOf(readJSONResponseFile(res[1]))), true);
                } else {
                    JSONAssert.assertEquals(String.valueOf(getJsonResponseValue(res[0])), String.valueOf(res[1]), true);
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @And("^response body is equal to expected response: (.*)$")
    public void verifyResponseEqualExpected(String expectedRes){
        try {
            if(!expectedRes.equalsIgnoreCase("empty")) {
                JSONAssert.assertEquals((JSONObject) JSONParser.parseJSON(String.valueOf(jsonResponseBody.get())),
                        (JSONObject) JSONParser.parseJSON(String.valueOf(readJSONResponseFile(expectedRes))), true);
            }else{
                Assert.assertTrue(jsonResponseBody.get() == null, "Response Body is not empty and has value of: "
                    + String.valueOf(jsonResponseBody.get()));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @And("^response body value of: (.*) is equal to stored response: (.*) value of: (.*)$")
    public void verifyStoredResponseValues(String resBodyVal, String storedResKey, String storedResValue){
        JsonValue resBody = jsonResponseBody.get();
        JsonValue storResponse = responseBodyMem.get().get(storedResKey);
        if(!(resBodyVal == null)){
            resBody = getJsonValue(jsonResponseBody.get(), resBodyVal);
        }
        if(!(storedResValue == null)){
            storResponse = getJsonValue(responseBodyMem.get().get(storedResKey), storedResValue);
        }

        try {
            if(resBody instanceof JsonObject && storResponse instanceof  JsonObject){
                JSONAssert.assertEquals((JSONObject) JSONParser.parseJSON(String.valueOf(resBody)),
                        (JSONObject) JSONParser.parseJSON(String.valueOf(storResponse)), true);
            } else if (resBody instanceof JsonArray && storResponse instanceof  JsonArray) {
                JSONAssert.assertEquals((JSONArray) JSONParser.parseJSON(String.valueOf(resBody)),
                        (JSONArray) JSONParser.parseJSON(String.valueOf(storResponse)), true);
            } else {
                JSONAssert.assertEquals(String.valueOf(resBody), String.valueOf(storResponse), true);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @And("^assign json payload values: (.*)$")
    public void assignJsonPayloadValues(String payloadValues){
        List<String[]> payVarVal = separatorKeyValue(payloadValues);
        for(String[] payV: payVarVal){
            assignValueJsonPayload(payV[0].trim(), payV[1].trim());
        }
        payload.set( jsonPayload.get().toString());
    }

}
