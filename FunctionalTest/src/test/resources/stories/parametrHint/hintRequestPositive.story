Meta: Positive scenario for hint parameter

Scenario: As a user I want to get some convert page after convert pageNumber
Given Office conversion service is up and running
When user sends a request with hint parameter first <first> and last <last> to convert pages <pageNumber> office document <file>
Then server must respond with status 200
Examples:
|file|pageNumber|first|last|
|Hint/testHint.doc|0|1|5|

Scenario: Value "first" hint parameter in less than NumberPage must be ignored

Given Office conversion service is up and running
When user sends a request with hint parameter first <first> and last <last> to convert pages <pageNumber> office document <file>
Then server must respond with status 200
Examples:
|file|pageNumber|first|last|
|Hint/firstLessPage/page7.docx|3|1|7|


Scenario: Value "last" hint parameter more then max page must be ignored

Given Office conversion service is up and running
When user sends a request with hint parameter first <first> and last <last> to convert pages <pageNumber> office document <file>
Then server must respond with status 200
Examples:
|file|pageNumber|first|last|
|Hint/lastMoreMaxPage/page7.docx|0|1|15|