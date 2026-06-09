Feature: Public Users API

  Scenario: Get all users
    When I call GET "/public/users"
    Then response status should be 200
    And response should contain 2 users
    And response should contain users with names:
      | John  |
      | Steve |