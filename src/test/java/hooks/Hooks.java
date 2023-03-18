package hooks;


import io.cucumber.java.After;
import io.cucumber.java.Before;
import util.BaseTest;

public class Hooks extends BaseTest {
    @Before("@TOKEN")
    public void getTokenCred(){
        comMap.put("email", emailMap);
        comMap.put("password", passwordMap);
        readEnvTD("email");
        readEnvTD("password");
    }


}
