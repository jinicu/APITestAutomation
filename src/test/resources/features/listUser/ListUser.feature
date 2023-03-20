@APITEST @listUser
Feature: List Users Test


  Scenario Outline: List User Requests <TCID>
    Given request url with endpoint: <endpoint>
    And request parameters: <parameters>
    When user send request with method: <requestMethod>
    Then response status code result should be: <responseCode>
    And response body is equal to expected response: <expectRes>

    Examples:
      | endpoint   | parameters | requestMethod | responseCode | expectRes           | TCID       |
      | listUser   | page~2     | GET           | 200          | listUser/page2.json | TC00008    |


  Scenario Outline: User is in List User Response <TCID>
    Given request url with endpoint: <endpoint>
    And request parameters: <parameters>
    When user send request with method: <requestMethod>
    Then response status code result should be: <responseCode>
    And response array path: <arrayPath> contains keys with value: <expectRes>

    Examples:
      | endpoint   | parameters | requestMethod | responseCode | arrayPath | expectRes                             | TCID       |
      | listUser   | page~2     | GET           | 200          | data      | first_name~Lindsay, last_name~Ferguson| TC00009    |
      | listUser   | page~2     | GET           | 200          | data      | first_name~Byron, last_name~Fields    | TC00010    |
      | listUser   | page~2     | GET           | 200          | data      | email~george.edwards@reqres.in        | TC00011    |
      | listUser   | page~1     | GET           | 200          | data      | first_name~George, last_name~Bluth    | TC00012    |

  Scenario Outline: Single User Requests <TCID>
    Given request url with endpoint: <endpoint>
    And endpoint path: <path>
    When user send request with method: <requestMethod>
    Then response status code result should be: <responseCode>
    And response body is equal to expected response: <expectRes>

    Examples:
      | endpoint   | path | requestMethod | responseCode | expectRes                  | TCID       |
      | listUser   | /2   | GET           | 200          | listUser/singleUser/2.json | TC00013    |
      | listUser   | /3   | GET           | 200          | listUser/singleUser/3.json | TC00014    |


  Scenario Outline: Single User Response has same details with List User <TCID>
    Given user get List User for page <page>
    And request url with endpoint: <endpoint>
    And endpoint path: <path>
    When user send request with method: <requestMethod>
    Then response status code result should be: <responseCode>
    And response body value of: <responseValue> is equal to stored response: <storeResKey> value of: <storeResVal>

    Examples:
      | page  | endpoint   | path | requestMethod | responseCode | responseValue | storeResKey | storeResVal | TCID       |
      | 1     | listUser   | /2   | GET           | 200          | data          | listUser    | data(1)     | TC00015    |
      | 1     | listUser   | /3   | GET           | 200          | data          | listUser    | data(2)     | TC00016    |

  Scenario Outline: Single User Requests Not Found <TCID>
    Given request url with endpoint: <endpoint>
    And endpoint path: <path>
    When user send request with method: <requestMethod>
    Then response status code result should be: <responseCode>
    And response body contains keys with value: <responseBody>

    Examples:
      | endpoint   | path | requestMethod | responseCode | responseBody         | TCID       |
      | listUser   | /23  | GET           | 404          | data(0).id~null      | TC00017    |
      | listUser   | /50  | GET           | 404          | data(0).id~null      | TC00018    |

  Scenario Outline: Create user <TCID>
    Given request url with endpoint: <endpoint>
    And request payload: <payload> with file type: json
    And assign json payload values: <payloadValues>
    When user send request with method: <requestMethod>
    Then response status code result should be: <responseCode>
    And response body contains keys with value: <responseBody>

    Examples:
      | endpoint   | payload               | payloadValues                          | requestMethod | responseCode | responseBody                                              | TCID       |
      | listUser   | listUser/create.json  | name~morpheus, job~leader              | POST          | 201          | name~morpheus, job~leader, id~notNull, createdAt~notNull  | TC00029    |

  Scenario Outline: Update user: <Description> <TCID>
    Given request url with endpoint: <endpoint>
    And endpoint path: <path>
    And request payload: <payload> with file type: json
    And assign json payload values: <payloadValues>
    When user send request with method: <requestMethod>
    Then response status code result should be: <responseCode>
    And response body contains keys with value: <responseBody>

    Examples:
      | endpoint   | path | payload               | payloadValues                          | requestMethod | responseCode | responseBody                                              | Description | TCID       |
      | listUser   | /2   | listUser/create.json  | name~morpheus, job~zion resident       | PUT           | 200          | name~morpheus, job~zion resident, updatedAt~notNull       | PUT         | TC00030    |
      | listUser   | /2   | listUser/create.json  | name~morpheus, job~zion resident       | PATCH         | 200          | name~morpheus, job~zion resident, updatedAt~notNull       | PATCH       | TC00031    |

  Scenario Outline: Delete user <TCID>
    Given request url with endpoint: <endpoint>
    And endpoint path: <path>
    When user send request with method: <requestMethod>
    Then response status code result should be: <responseCode>
    And response body is equal to expected response: <responseBody>

    Examples:
      | endpoint   | path | requestMethod | responseCode | responseBody    | TCID       |
      | listUser   | /2   | DELETE        | 204          | empty           | TC00032    |
