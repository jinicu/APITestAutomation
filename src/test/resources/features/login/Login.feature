@APITEST @login @TOKEN
Feature: Application Login Tests

  Scenario Outline: Successful Login <TCID>
    Given request url with endpoint: <endpoint>
    And request payload: <payload> with file type: json
    And user email and password of user: <userID>
    When user send request with method: <requestMethod>
    Then response status code result should be: <responseCode>
    And response body contains keys with value: <responseBody>

    Examples:
      | endpoint   | payload               | userID     | requestMethod | responseCode | responseBody   | TCID       |
      | login      | login/login.json      | us00001    | POST          | 200          | token~notNull  | TC00001    |

  Scenario Outline: Unsuccessful Login: <Description> <TCID>
    Given request url with endpoint: <endpoint>
    And request payload: <payload> with file type: json
    And user with email for: <emailUID> and password for: <passwordUID>
    When user send request with method: <requestMethod>
    Then response status code result should be: <responseCode>
    And response body contains keys with value: <responseBody>

    Examples:
      | endpoint   | payload               | emailUID    | passwordUID | requestMethod | responseCode | responseBody                       | Description                | TCID       |
      | login      | login/login.json      |             |             | POST          | 400          | error~Missing email or username    | Missing Email and Password | TC00002    |
      | login      | login/login.json      |             | us00001     | POST          | 400          | error~Missing email or username    | Missing Email              | TC00003    |
      | login      | login/login.json      | us00001     |             | POST          | 400          | error~Missing password             | Missing Password           | TC00004    |
      | login      | login/login.json      | invalidUser | us00001     | POST          | 400          | error~user not found               | Invalid Email              | TC00005    |
      | login      | login/login.json      | us00001     | invalidUser | POST          | 400          | error~Invalid username or password | Invalid Password           | TC00006    |
      | login      | login/login.json      | invalidUser | invalidUser | POST          | 400          | error~user not found               | Invalid Email and Password | TC00007    |