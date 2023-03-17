package stepDefinitions.login;

import io.cucumber.java.en.Given;
import util.BaseTest;

public class Login extends BaseTest {

    @Given("^user has email: (.*) and password: (.*)$")
    public void credentialSetter(String email, String password) {
        System.out.println(email);
        System.out.println(password);
        System.out.println(properties.get("baseUrl"));
    }

}
