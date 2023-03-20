package stepDefinitions.listUser;

import io.cucumber.java.en.Given;
import stepDefinitions.CommonStep;
import util.BaseTest;

public class ListUser extends BaseTest {
    CommonStep comStep = new CommonStep();

   @Given("^user get List User for page (.*)$")
    public void getListOfUsersResponse(String page){
       comStep.getUrlEndpoint("listUser");
       comStep.setRequestParameters("page~" + page);
       comStep.apiRequest("GET");
       storeResponseBody("listUser", jsonResponseBody.get());
   }

}
