Feature: User API

  Scenario: Get users
    When I call "GET" "/users"
    Then response status should be 200
    And response should contain at least 2 users

  Scenario: Get user
    When I call "GET" "/users/U0192"
    Then response status should be 200
    And response should contain user with id "U0192"

  Scenario: Create user
    When I call "POST" "/users" with body
      | id    | name  | email              |
      | U0312 | Randy | rko_viper@test.com |
    Then response status should be 201
    And response should contain user with id "U0312"

  Scenario: Update user
    When I call "PUT" "/users/U0192" with body
      | id    | name   | email               |
      | U0192 | Austin | stone_cold@test.com |
    Then response status should be 200
    And response should contain user with id "U0192"

  Scenario: Patch user partially
    When I call "PATCH" "/users/U0216" with body
      | name | email                    |
      | Cena | thechamp.ishere@test.com |
    Then response status should be 200
    And response should contain user with id "U0216"

  Scenario: Get non-existing user
    When I call "GET" "/users/U0000"
    Then response status should be 404

  Scenario: Create duplicate user
    When I call "POST" "/users" with body
      | id    | name  | email              |
      | U0192 | Steve | sc_saysso@test.com |
    Then response status should be 409