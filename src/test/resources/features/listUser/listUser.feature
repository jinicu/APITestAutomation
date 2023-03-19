@APITEST
Feature: List Users Test

  @listUser
  Scenario Outline: Successful Login <TCID>
    Given request url with endpoint: <endpoint>
    And request parameters: <parameters>
    When user send request with method: <requestMethod>
    Then response status code result should be: <responseCode>
    And response body contains: <responseBody>
    And response body is equal to expected response: <expectRes>

    Examples:
      | endpoint   | parameters | requestMethod | responseCode | responseBody   | expectRes      | TCID       |
      | listUser   | page~2     | GET           | 200          | data(0).id~7   | listUser/page2 |TC00008    |