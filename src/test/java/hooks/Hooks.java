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
        endpoint.set(null);
        jsonPayload.set(null);
        payload.set(null);
        jsonResponseBodyMem.set(null);
        jsonResponseBody.set(null);
        responseBodyMem.set(null);
        responseCode.set(null);
        headers.set(null);
        parameters.set(null);
    }

    @Before("@TOKEN")
    public void getTokenCred(){
        comMap.put("email", emailMap);
        comMap.put("password", passwordMap);
        readEnvTD("email");
        readEnvTD("password");
    }


}
