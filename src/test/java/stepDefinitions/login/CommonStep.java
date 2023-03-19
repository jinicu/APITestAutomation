package stepDefinitions.login;

import io.cucumber.cienvironment.internal.com.eclipsesource.json.JsonObject;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
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

    @And("^response body contains: (.*)$")
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

    @And("^response body is equal to expected response: (.*)$")
    public void responseEqualExpected(String expectedRes){
        try {
            JSONAssert.assertEquals((JSONObject) JSONParser.parseJSON(String.valueOf(jsonResponseBody.get())),
                    (JSONObject) JSONParser.parseJSON(String.valueOf(readJSONResponseFile(expectedRes))), true);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


}
