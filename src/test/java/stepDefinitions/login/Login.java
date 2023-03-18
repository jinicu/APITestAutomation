package stepDefinitions.login;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import util.BaseTest;

public class Login extends BaseTest {

    @Given("^user with user identity (.*)$")
    public void getUserIdentity(String userId){
        System.out.println(emailMap.get(userId));
        System.out.println(passwordMap.get(userId));
    }

    @When("^user with email for: (.*) and password for: (.*)$")
    public void getUserIdentityDifferentUsernameAndPassword(String emailUID, String passwordUID){
        if(!emailUID.isEmpty()){
            assignValueJson("email", emailMap.get(emailUID));
        }else{
            assignValueJson("email","");
        }
        if(!emailUID.isEmpty()){
            assignValueJson("password", passwordMap.get(passwordUID));
        }else{
            assignValueJson("password","");
        }
        payload.set( jsonPayload.get().toString());
    }

    @And("^user email and password of user: (.*)$")
    public void addValidCredLoginPayload(String user){
        assignValueJson("email", emailMap.get(user));
        assignValueJson("password", passwordMap.get(user));
        payload.set( jsonPayload.get().toString());
    }

}
