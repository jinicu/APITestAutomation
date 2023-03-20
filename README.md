# APITestAutomation
This framework is created to mainly and initially automate REST API Test Cases with the use of java and an additional layer of Cucumber for a more eye pleasing Gherkin interface. Saying that it is intially made for API automation means that it can cater a lot more possibilities by having additional codes and architecture. UI automation can also be integrated as Cucumber also works with UI automation tools such as Selenium.

"Cucumber"? Why not just use Karatae DSL? It is true that Karate DSL is the more taylored version of Cucumber in terms of API Automation. However, a well taylored product also means that it is fitted to function for a certain design which can introduce limitations. Sometimes, we want flexibility and that's what this framework is aiming for.

# Author
Github Id: jinicu

# Initial Capabilities
The framework could initially do the following:

1. Send POST, GET, PUT, PATCH, DELETE Requests
2. Receive Response Body and Response Code
3. Write Test Cases using Gherkin
4. Find value from json Response
5. Compare json values
6. Customize StepDefinitions depending on your need
7. Run in single thread and multiple threads
8. Set environment variables
9. Set API Request timeout

These are intially the notable capabalities. More functions and integrations can be added.

# Setup

Note: This framework is created with a perspective view of an IntelliJ user. Setup that will be listed below will be biased on how it is created. Other IDEs is also applicable.

**Install and/or Download Necessary Applications**

1. IDE (IntelliJ IDEA Community Edition 2022.2)
2. Download Maven 3.9.0
3. Java 14.0.1
4. Git

**Add to Environment Variables >> System Variables in your Device**
Variable: M2_HOME  
Value: Path of where your maven is downloaded
![image](https://user-images.githubusercontent.com/123305916/226377101-49e32351-cc8f-4579-ba1e-5ef1f5e19b3e.png)

**Add M2_HOME/bin to Variables >> System Variables >> Path**

![image](https://user-images.githubusercontent.com/123305916/226378345-aeaa7601-2af6-47c5-84ad-55af2a45e91a.png)

**Clone Repository**

Repository Link: https://github.com/jinicu/APITestAutomation.git

You can use 

        git clone https://github.com/jinicu/APITestAutomation.git
        
Or in Intellij: Project from Version Control

![image](https://user-images.githubusercontent.com/123305916/226379799-3798d367-326a-433e-84f6-5d5febc08079.png)


**Reload Maven Project and resolve Dependency issues then you are ready to start**

![image](https://user-images.githubusercontent.com/123305916/226380290-276a5ee9-ea6a-4d50-9e65-d450529a4ace.png)

# Project Structure

1. Almost all files are under src/test
2. hooks contains Hooks class that has methods we can assign scenarios to do in a certain period of the test.
3. runners contains the test runner file that we can run to execute our test.
4. stepDefinitions contains the stepDefinition used for Gherkin scripts
5. util contains classes that are used by different classes all throughout the project.
6. features contains the Gherkin Tests
7. payloads contains the files of the payloads to be use in the test
8. properties contains properties files of the environment and the endpoints.
9. responses contains the files of requests that is used for comparison and verification of results.
10. testData contains data used in the tests that should not be exposed in plain sight. The files are password encrypted.


![image](https://user-images.githubusercontent.com/123305916/226382849-7f3d4872-ca54-4264-823c-88d3ea77e1fc.png)


# Run Tests

**How to run?**

1. Via runner file

Just run this file.

![image](https://user-images.githubusercontent.com/123305916/226386212-98597ddb-5151-4746-9356-d0819bf52c8c.png)

Edit values such as tags to tags of your desire and parallel to false if single thread run is desired.

![image](https://user-images.githubusercontent.com/123305916/226387011-02df8a95-b600-4b2e-b52b-eb9987554ab7.png)


In pom.xml, you can also edit the env and apiTimeoutVal before running the runner.

![image](https://user-images.githubusercontent.com/123305916/226387278-4e2bfb97-213d-4ea9-bf8d-ba83b6b1e726.png)

2. Via mvn clean install

Use the syntax below. No editing to files is needed. The important parameters are the "cucumber.filter.tags" and the thread. They assign the tags to be run and number of threads to be used for the run respectively.

       mvn clean install -D"cucumber.filter.tags"="@APITEST"  -Dthread=10 -DapiTimeoutVal="4000" -Denv="env"
       
**Report**

Report is located under target.

![image](https://user-images.githubusercontent.com/123305916/226388665-3c64259f-0b25-41fb-a30c-e82c5786ea1d.png)


# Step Definitions and syntaxes that might be unique for this framework

1. And request parameters: <parameters>

This method add the parameters you need for your API request. <parameters> can be formated like this.

      page~2 where page is the key and 2 is the value seperated by (~).
      page~2, id~1 where multiple parameters are separated by a (,).

2. And request headers: <headers>
    
Works exactly as parameters in item 1. However, to add flexibily, we can also add pre-set headers as hooks and just used the tag for the test that needs it. Just use:

      setRequestHeaders(String head)
      
3. Custom Tags

Add **@APITEST** to all API test scenarios. It has predifined hooks for API. This is also done fo future proofing if we decided to integrate UI Automation.

Add **@TOKEN** to all Scenarios that uses username and/or password. This will get the data from the password protected files in testData. Steps like

    Given authorization for user: <user>
    
4. Tags

**Tags that are in ALL CAPS are assigned to a specific hook**

        @TOKEN
        
**Tags that are in camelcase are for test tagging purposes only to aid cucumber runner**

        @login
        
5. Json Value Verification

For methods like this:

    And response body contains keys with value: <responseBody>
    
responseBody can be assigned with a value of:

    model.data(0).id~6
    
Which means that in (jsonObject) model has a (jsonArray) data and in index 0 has an id with a value of 6.

(number) indicates array index. You can remove (number) to return the array itself. Like for the method below:

    And response array path: data contains keys with value: first_name~Lindsay







