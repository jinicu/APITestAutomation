package stepDefinitions.login;

import io.cucumber.cienvironment.internal.com.eclipsesource.json.JsonObject;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import util.BaseTest;

public class Login extends BaseTest {

    @Given("authorization for user: (.*)$")
    public void getAuthenticator(String userId){
        getLoginResponse(userId);
        JsonObject loginRes = responseBodyMem.get().get("login");
        String token = getJsonValue(loginRes, "token").toString().replace("\"", "");
        System.out.println(token);
        String[] authorization = {"Authorization", String.format("Bearer %s", token)};
        setHeader(authorization);
        System.out.println(headers.get().get(0)[0]);
        System.out.println(headers.get().get(0)[1]);
    }
    @Given("login for user: (.*)$")
    public void getLoginResponse(String userId){
        endpoint.set( endpoints.getProperty("login"));
        readPayloadFile("login/login.json", "json");
        addValidCredLoginPayload(userId);
        apiRequest("POST");
        storeResponseBody("login", jsonResponseBody.get());
    }
    @Given("^user with user identity (.*)$")
    public void getUserIdentity(String userId){
        System.out.println(emailMap.get(userId));
        System.out.println(passwordMap.get(userId));
    }

    @When("^user with email for: (.*) and password for: (.*)$")
    public void getUserIdentityDifferentUsernameAndPassword(String emailUID, String passwordUID){
        if(!emailUID.isEmpty()){
            assignValueJsonPayload("email", emailMap.get(emailUID));
        }else{
            assignValueJsonPayload("email","");
        }
        if(!emailUID.isEmpty()){
            assignValueJsonPayload("password", passwordMap.get(passwordUID));
        }else{
            assignValueJsonPayload("password","");
        }
        payload.set( jsonPayload.get().toString());
    }

    @And("^user email and password of user: (.*)$")
    public void addValidCredLoginPayload(String user){
        assignValueJsonPayload("email", emailMap.get(user));
        assignValueJsonPayload("password", passwordMap.get(user));
        payload.set( jsonPayload.get().toString());
    }


}
