@APITEST @register @TOKEN
Feature: Application Registration Tests

  Scenario Outline: Successful Registration <TCID>
    Given request url with endpoint: <endpoint>
    And request payload: <payload> with file type: json
    And user email and password of user: <userID>
    When user send request with method: <requestMethod>
    Then response status code result should be: <responseCode>
    And response body contains keys with value: <responseBody>

    Examples:
      | endpoint   | payload                | userID     | requestMethod | responseCode | responseBody               | TCID       |
      | register   | register/register.json | us00002    | POST          | 200          | id~notNull, token~notNull  | TC00024    |

  Scenario Outline: Unsuccessful Registration: <Description> <TCID>
    Given request url with endpoint: <endpoint>
    And request payload: <payload> with file type: json
    And user with email for: <emailUID> and password for: <passwordUID>
    When user send request with method: <requestMethod>
    Then response status code result should be: <responseCode>
    And response body contains keys with value: <responseBody>

    Examples:
      | endpoint   | payload                | emailUID    | passwordUID | requestMethod | responseCode | responseBody                                        | Description                | TCID       |
      | register   | register/register.json |             |             | POST          | 400          | error~Missing email or username                     | Missing Email and Password | TC00025    |
      | register   | register/register.json |             | us00002     | POST          | 400          | error~Missing email or username                     | Missing Email              | TC00026    |
      | register   | register/register.json | us00002     |             | POST          | 400          | error~Missing password                              | Missing Password           | TC00027    |
      | register   | register/register.json | invalidUser | us00002     | POST          | 400          | error~Note: Only defined users succeed registration | Invalid Email              | TC00028    |
