package hooks;


import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import util.BaseTest;

import java.io.FileReader;

public class Hooks extends BaseTest {



    @After("@APITEST")
    public void clearAPIData(){
        jsonResponseBody.set(null);
        responseCode.set(null);
    }

    @Before("@TOKEN")
    public void getTokenCred(){
        comMap.put("email", emailMap);
        comMap.put("password", passwordMap);
        readEnvTD("email");
        readEnvTD("password");
    }


}
