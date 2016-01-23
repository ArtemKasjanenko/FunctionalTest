Meta: Negative scenario for hint parameter

Scenario: As a user I want to get an error for a negative value "first" hint parameter

Given Office conversion service is up and running
When user sends a request with hint parameter first <first> and last <last> to convert pages <pageNumber> office document <file>
Then server must respond with status 480
And error code must be <errorCode>
And error message must be <errorMessage>
Examples:
|file|pageNumber|first|last|errorCode|errorMessage|
|Hint/negativeValueFirst/page7.docx|0|-1|7|480000008|"message":"Parameter has incorrect value","parameter":"hint"|


Scenario: As a user I want to get an error for a negative value "last" hint parameter

Given Office conversion service is up and running
When user sends a request with hint parameter first <first> and last <last> to convert pages <pageNumber> office document <file>
Then server must respond with status 480
And error code must be <errorCode>
And error message must be <errorMessage>
Examples:
|file|pageNumber|first|last|errorCode|errorMessage|
|Hint/negativeValueLast/page7.docx|0|1|-3|480000008|"message":"Parameter has incorrect value","parameter":"hint"|


Scenario: As a user I want to get an error for missing value "first" hint parameter

Given Office conversion service is up and running
When user sends a request with hint parameter first <first> and last <last> to convert pages <pageNumber> office document <file>
Then server must respond with status 480
And error code must be <errorCode>
And error message must be <errorMessage>
Examples:
|file|pageNumber|first|last|errorCode|errorMessage|
|Hint/missingFirstHint/page7.docx|0|false|5|480000008|"message":"Parameter has incorrect value","parameter":"hint"|


Scenario: As a user I want to get an error for missing value "last" hint parameter

Given Office conversion service is up and running
When user sends a request with hint parameter first <first> and last <last> to convert pages <pageNumber> office document <file>
Then server must respond with status 480
And error code must be <errorCode>
And error message must be <errorMessage>
Examples:
|file|pageNumber|first|last|errorCode|errorMessage|
|Hint/missingLastHint/page7.docx|0|1|false|480000008|"message":"Parameter has incorrect value","parameter":"hint"|


Scenario: As a user I want to get an error for float value "first" hint parameter

Given Office conversion service is up and running
When user sends a request with hint parameter first <first> and last <last> to convert pages <pageNumber> office document <file>
Then server must respond with status 480
And error code must be <errorCode>
And error message must be <errorMessage>
Examples:
|file|pageNumber|first|last|errorCode|errorMessage|
|Hint/missingLastHint/page7.docx|0|0.1|1|480000008|"message":"Parameter has incorrect value","parameter":"hint"|


Scenario: As a user I want to get an error for float value "last" hint parameter

Given Office conversion service is up and running
When user sends a request with hint parameter first <first> and last <last> to convert pages <pageNumber> office document <file>
Then server must respond with status 480
And error code must be <errorCode>
And error message must be <errorMessage>
Examples:
|file|pageNumber|first|last|errorCode|errorMessage|
|Hint/missingLastHint/page7.docx|0|2|0.6|480000008|"message":"Parameter has incorrect value","parameter":"hint"|









