package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.junit.Before;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import util.BaseTest;

import java.io.FileReader;
import java.util.Properties;

@CucumberOptions(tags = "@sample",
        features = "src/test/resources/features",
        glue = {"stepDefinitions", "hooks"},
        plugin = {"pretty", "html:target/report/cucumber-report.html"})
public class RunCucumberTest extends AbstractTestNGCucumberTests{

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    @BeforeTest
    public void envProperties(){
        BaseTest baseTest = new BaseTest();
        try {
            String envDir = String.format("src/test/resources/properties/environments/%s.properties",System.getProperty("env"));
            baseTest.properties.load(new FileReader( envDir));
        }catch(Exception e){
            e.printStackTrace();
        }

        try {
            String endpointDir = "src/test/resources/properties/endpoints.properties";
            baseTest.endpoints.load(new FileReader( endpointDir));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}