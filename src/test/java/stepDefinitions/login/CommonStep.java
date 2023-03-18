package stepDefinitions.login;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import util.BaseTest;

import java.util.List;

public class CommonStep extends BaseTest {

    @Given("^request url with endpoint: (.*)$")
    public void getUrlEndpoint(String urlEndpoint){
        endpoint = endpoints.getProperty(urlEndpoint);
    }

    @And("^request payload: (.*)$")
    public void getPayload(String reqPayload){
        readJSONPayload(reqPayload);
    }

    @When("^user send request with method: (.*)$")
    public void sendRequest(String requestMethod){
        apiRequest(requestMethod);
    }

    @Then("^response status code result should be: (.*)$")
    public void verifyStatusCode(int statusCode){
        Assert.assertEquals(responseCode, statusCode, "Response Status Code is Incorrect");
    }

    @And("^response body contains: (.*)$")
    public void verifyResponseBodyValue(String responseValues){
        List<String[]> resVal = responseValidatorData(responseValues);
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




}
