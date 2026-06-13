Feature: Secure Users API

  Scenario: Get all secure users without credentials
    When I secure call GET "/secure/users"
    Then secure response status should be 401

  Scenario: Get all secure users with valid credentials
    When I call GET "/secure/users" with username "user" and password "user123"
    Then secure response status should be 200

  Scenario: Get secure user by id
    When I call GET "/secure/users/Steve" with username "user" and password "user123"
    Then secure response status should be 404

  Scenario: Get unknown secure user
    When I call GET "/secure/users" with username "admin" and password "admin123"
    Then secure response status should be 200
    And secure response should contain users with ages:
      | 48 |
      | 64 |