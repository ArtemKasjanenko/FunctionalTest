Meta: Positive scenario for hint parameter

Scenario: As a user I want to get some convert page after convert page 0
Given Office conversion service is up and running
When user sends request convert with hint parameter <first> <last> single page <pageNumber> office document <file> to PDF format
Then server must respond with status 200
Examples:
|file|pageNumber|first|last|
|Hint/testHint.doc|0|1|5|