@apiTest @listResources
Feature: List Resources Test

  Scenario Outline: List Resources Requests <TCID>
    Given request url with endpoint: <endpoint>
    When user send request with method: <requestMethod>
    Then response status code result should be: <responseCode>
    And response body is equal to expected response: <expectRes>

    Examples:
      | endpoint        | requestMethod | responseCode | expectRes                         | TCID       |
      | listResources   | GET           | 200          | listResources/listResources.json  | TC000019   |

  Scenario Outline: Single Resources Requests <TCID>
    Given request url with endpoint: <endpoint>
    And endpoint path: <path>
    When user send request with method: <requestMethod>
    Then response status code result should be: <responseCode>
    And response body is equal to expected response: <expectRes>

    Examples:
      | endpoint        | path | requestMethod | responseCode | expectRes                             | TCID       |
      | listResources   | /2   | GET           | 200          | listResources/singleResource/2.json   | TC000020   |
      | listResources   | /3   | GET           | 200          | listResources/singleResource/3.json   | TC000021   |

  Scenario Outline: Single User Requests Not Found <TCID>
    Given request url with endpoint: <endpoint>
    And endpoint path: <path>
    When user send request with method: <requestMethod>
    Then response status code result should be: <responseCode>
    And response body contains keys with value: <responseBody>

    Examples:
      | endpoint        | path | requestMethod | responseCode | responseBody         | TCID       |
      | listResources   | /23  | GET           | 404          | data(0).id~null      | TC00022    |
      | listResources   | /50  | GET           | 404          | data(0).id~null      | TC00023    |

