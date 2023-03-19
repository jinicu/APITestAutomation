package stepDefinitions.login;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import util.BaseTest;

public class ListUser extends BaseTest {
    CommonStep comStep = new CommonStep();

   @Given("^user get List User for page (.*)$")
    public void listOfUsers(String page){
       comStep.getUrlEndpoint("listUser");
       comStep.setRequestParameters("page~" + page);
       comStep.apiRequest("GET");
       storeResponseBody("listUser", jsonResponseBody.get());
   }
}
