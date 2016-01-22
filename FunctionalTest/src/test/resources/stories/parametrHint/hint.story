Meta: Positive scenario for hint parameter

Scenario: As a user I want to get some convert page after convert page 0
Given Office conversion service is up and running
When user sends request convert with hint parameter single page <pageNumber> office document <file> to PDF format
Then server must respond with status 200
Examples:
|file|pageNumber|
|Hint/testHint.doc|0|