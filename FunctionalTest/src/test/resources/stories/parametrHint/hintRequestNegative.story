Meta: Negative scenario for hint parameter

Scenario: As a user I want to get an error for a negative value "first" hint parameter

Given Office conversion service is up and running
When user sends a request with a negative value first <first> hint of parameter <last> to convert pages <pageNumber> office document <file>
Then server must respond with status 480
And error code must be <errorCode>
And error message must be <errorMessage>
Examples:
|file|pageNumber|first|last|errorCode|errorMessage|
|Hint/negativeValueFirst/page7.docx|0|-1|7|480000008|"message":"Parameter has incorrect value","parameter":"hint"|


Scenario: As a user I want to get an error for a negative value "last" hint parameter

Given Office conversion service is up and running
When user sends a request with a negative value last <last> hint of parameter <first> to convert pages <pageNumber> office document <file>
Then server must respond with status 480
Examples:
|file|pageNumber|first|last|errorCode|errorMessage|
|Hint/negativeValueLast/page7.docx|0|1|-3|480000008|"message":"Parameter has incorrect value","parameter":"hint"|


Scenario: As a user I want to get an error for value "first" hint parameter in less than NumberPage

Given Office conversion service is up and running
When user sends a request with hint parameter <last> (value first <first> in less than NumberPage) to convert pages <pageNumber> office document <file>
Then server must respond with status 200
Examples:
|file|pageNumber|first|last|
|Hint/firstLessPage/page7.docx|3|1|7|


Scenario: As a user I want to get an error for value "last" hint parameter more than max page

Given Office conversion service is up and running
When user sends a request with hint parameter <first> (value last <last> hint more than max page) to convert pages <pageNumber> office document <file>
Then server must respond with status 200
Examples:
|file|pageNumber|first|last|
|Hint/lastMoreMaxPage/page7.docx|0|1|15|


Scenario: As a user I want to get an error for missing value "first" hint parameter

Given Office conversion service is up and running
When user sends request with missing first hint parameter to convert pages <pageNumber> office document <file>
Then server must respond with status 480
Examples:
|file|pageNumber|
|Hint/missingFirstHint/page7.docx|0|


Scenario: As a user I want to get an error for missing value "last" hint parameter

Given Office conversion service is up and running
When user sends request with missing last hint parameter to convert pages <pageNumber> office document <file>
Then server must respond with status 480
Examples:
|file|pageNumber|
|Hint/missingLastHint/page7.docx|0|












