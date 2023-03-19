@APITEST
Feature: List Users Test

  @listUser
  Scenario Outline: List User Requests <TCID>
    Given request url with endpoint: <endpoint>
    And request parameters: <parameters>
    When user send request with method: <requestMethod>
    Then response status code result should be: <responseCode>
    And response body is equal to expected response: <expectRes>

    Examples:
      | endpoint   | parameters | requestMethod | responseCode | expectRes      | TCID       |
      | listUser   | page~2     | GET           | 200          | listUser/page2 | TC00008    |

  @listUser
  Scenario Outline: Single User Requests <TCID>
    Given request url with endpoint: <endpoint>
    And endpoint path: <path>
    When user send request with method: <requestMethod>
    Then response status code result should be: <responseCode>
    And response body is equal to expected response: <expectRes>

    Examples:
      | endpoint   | path | requestMethod | responseCode | expectRes             | TCID       |
      | listUser   | /2   | GET           | 200          | listUser/singleUser/2 | TC00009    |
      | listUser   | /3   | GET           | 200          | listUser/singleUser/3 | TC00010    |

  @listUser
  Scenario Outline: Single User Response has same details with List User <TCID>
    Given user get List User for page <page>
    And request url with endpoint: <endpoint>
    And endpoint path: <path>
    When user send request with method: <requestMethod>
    Then response status code result should be: <responseCode>
    And response body value of: <responseValue> is equal to stored response: <storeResKey> value of: <storeResVal>

    Examples:
      | page  | endpoint   | path | requestMethod | responseCode | responseValue | storeResKey | storeResVal | TCID       |
      | 1     | listUser   | /2   | GET           | 200          | data          | listUser    | data(1)     | TC00011    |
      | 1     | listUser   | /3   | GET           | 200          | data          | listUser    | data(2)     | TC00012    |