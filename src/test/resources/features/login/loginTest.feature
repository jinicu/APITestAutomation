Feature: Application Login Tests


  @login
  Scenario Outline: Successful Login <TCID>
    Given user has email: <email> and password: <password>

    Examples:
      | email                 | password           | TCID     |
      | eve.holt@reqres.in    | cityslicka         | 00001    |