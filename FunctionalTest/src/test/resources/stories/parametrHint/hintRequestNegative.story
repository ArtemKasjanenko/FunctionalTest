Meta: Negative scenario for hint parameter

Scenario: As a user I want to get an error for a negative value "first" hint parameter

Given Office conversion service is up and running
When user sends a request with hint parameter first <first> and last <last> to convert pages <pageNumber> office document <file>
Then server must respond with status 480
And error code must be <errorCode>
And error message must be <errorMessage>
And file should not be created
Examples:
|file|destination|pageNumber|first|last|errorCode|errorMessage|
|common/ods/CalcPage5.ods|Hint/NegativeHintParameter|0|-1|4|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odg/DrawPage4.odg|Hint/NegativeHintParameter|0|-3|3|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/xlsx/ExcelPage4.xlsx|Hint/NegativeHintParameter|0|-5|3|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odp/ImpressPage4.odp|Hint/NegativeHintParameter|0|-3|3|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/pptx/PowerPointPage5.pptx|Hint/NegativeHintParameter|0|-1|4|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/docx/WordPage5.docx|Hint/NegativeHintParameter|0|-2|4|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odt/WriterPage5.odt|Hint/NegativeHintParameter|0|-1|4|480000008|"message":"Parameter has incorrect value","parameter":"hint"|


Scenario: As a user I want to get an error for a negative value "last" hint parameter

Given Office conversion service is up and running
When user sends a request with hint parameter first <first> and last <last> to convert pages <pageNumber> office document <file>
Then server must respond with status 480
And error code must be <errorCode>
And error message must be <errorMessage>
And file should not be created
Examples:
|file|destination|pageNumber|first|last|errorCode|errorMessage|
|common/ods/CalcPage5.ods|Hint/NegativeHintParameter|0|1|-3|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odg/DrawPage4.odg|Hint/NegativeHintParameter|0|1|-3|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/xlsx/ExcelPage4.xlsx|Hint/NegativeHintParameter|0|1|-3|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odp/ImpressPage4.odp|Hint/NegativeHintParameter|0|1|-3|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/pptx/PowerPointPage5.pptx|Hint/NegativeHintParameter|0|1|-3|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/docx/WordPage5.docx|Hint/NegativeHintParameter|0|1|-3|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odt/WriterPage5.odt|Hint/NegativeHintParameter|0|1|-3|480000008|"message":"Parameter has incorrect value","parameter":"hint"|


Scenario: As a user I want to get an error for missing value "first" hint parameter

Given Office conversion service is up and running
When user sends a request with hint parameter first <first> and last <last> to convert pages <pageNumber> office document <file>
Then server must respond with status 480
And error code must be <errorCode>
And error message must be <errorMessage>
And file should not be created
Examples:
|file|destination|pageNumber|first|last|errorCode|errorMessage|
|common/ods/CalcPage5.ods|Hint/NegativeHintParameter|0|false|5|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odg/DrawPage4.odg|Hint/NegativeHintParameter|0|false|5|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/xlsx/ExcelPage4.xlsx|Hint/NegativeHintParameter|0|false|5|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odp/ImpressPage4.odp|Hint/NegativeHintParameter|0|false|5|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/pptx/PowerPointPage5.pptx|Hint/NegativeHintParameter|0|false|5|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/docx/WordPage5.docx|Hint/NegativeHintParameter|0|false|5|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odt/WriterPage5.odt|Hint/NegativeHintParameter|0|false|5|480000008|"message":"Parameter has incorrect value","parameter":"hint"|


Scenario: As a user I want to get an error for missing value "last" hint parameter

Given Office conversion service is up and running
When user sends a request with hint parameter first <first> and last <last> to convert pages <pageNumber> office document <file>
Then server must respond with status 480
And error code must be <errorCode>
And error message must be <errorMessage>
And file should not be created
Examples:
|file|destination|pageNumber|first|last|errorCode|errorMessage|
|common/ods/CalcPage5.ods|Hint/NegativeHintParameter|0|1|false|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odg/DrawPage4.odg|Hint/NegativeHintParameter|0|1|false|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/xlsx/ExcelPage4.xlsx|Hint/NegativeHintParameter|0|1|false|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odp/ImpressPage4.odp|Hint/NegativeHintParameter|0|1|false|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/pptx/PowerPointPage5.pptx|Hint/NegativeHintParameter|0|1|false|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/docx/WordPage5.docx|Hint/NegativeHintParameter|0|1|false|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odt/WriterPage5.odt|Hint/NegativeHintParameter|0|1|false|480000008|"message":"Parameter has incorrect value","parameter":"hint"|


Scenario: As a user I want to get an error for float value "first" hint parameter

Given Office conversion service is up and running
When user sends a request with hint parameter first <first> and last <last> to convert pages <pageNumber> office document <file>
Then server must respond with status 480
And error code must be <errorCode>
And error message must be <errorMessage>
And file should not be created
Examples:
|file|destination|pageNumber|first|last|errorCode|errorMessage|
|common/ods/CalcPage5.ods|Hint/NegativeHintParameter|0|0.1|1|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odg/DrawPage4.odg|Hint/NegativeHintParameter|0|0.2|1|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/xlsx/ExcelPage4.xlsx|Hint/NegativeHintParameter|0|1.1|1|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odp/ImpressPage4.odp|Hint/NegativeHintParameter|0|4.1|1|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/pptx/PowerPointPage5.pptx|Hint/NegativeHintParameter|0|1.5|1|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/docx/WordPage5.docx|Hint/NegativeHintParameter|0|2.3|1|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odt/WriterPage5.odt|Hint/NegativeHintParameter|0|2.1|1|480000008|"message":"Parameter has incorrect value","parameter":"hint"|


Scenario: As a user I want to get an error for float value "last" hint parameter

Given Office conversion service is up and running
When user sends a request with hint parameter first <first> and last <last> to convert pages <pageNumber> office document <file>
Then server must respond with status 480
And error code must be <errorCode>
And error message must be <errorMessage>
And file should not be created
Examples:
|file|destination|pageNumber|first|last|errorCode|errorMessage|
|common/ods/CalcPage5.ods|Hint/NegativeHintParameter|0|2|0.1|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odg/DrawPage4.odg|Hint/NegativeHintParameter|0|2|1.1|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/xlsx/ExcelPage4.xlsx|Hint/NegativeHintParameter|0|2|1.1|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odp/ImpressPage4.odp|Hint/NegativeHintParameter|0|2|2.1|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/pptx/PowerPointPage5.pptx|Hint/NegativeHintParameter|0|2|1.6|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/docx/WordPage5.docx|Hint/NegativeHintParameter|0|2|0.1|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odt/WriterPage5.odt|Hint/NegativeHintParameter|0|2|0.2|480000008|"message":"Parameter has incorrect value","parameter":"hint"|


Scenario: As a user I want to get an error if value hint parameter "last" less than "first"

Given Office conversion service is up and running
When user sends a request with hint parameter first <first> and last <last> to convert pages <pageNumber> office document <file>
Then server must respond with status 480
And error code must be <errorCode>
And error message must be <errorMessage>
And file should not be created
Examples:
|file|destination|pageNumber|first|last|errorCode|errorMessage|
|common/ods/CalcPage5.ods|Hint/NegativeHintParameter|0|4|1|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odg/DrawPage4.odg|Hint/NegativeHintParameter|0|3|2|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/xlsx/ExcelPage4.xlsx|Hint/NegativeHintParameter|0|3|1|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odp/ImpressPage4.odp|Hint/NegativeHintParameter|0|3|0|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/pptx/PowerPointPage5.pptx|Hint/NegativeHintParameter|0|4|2|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/docx/WordPage5.docx|Hint/NegativeHintParameter|0|4|3|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odt/WriterPage5.odt|Hint/NegativeHintParameter|0|4|1|480000008|"message":"Parameter has incorrect value","parameter":"hint"|


Scenario: As a user I want to get an error for text value "first" hint parameter 

Given Office conversion service is up and running
When user sends a request with hint parameter first <first> and last <last> to convert pages <pageNumber> office document <file>
Then server must respond with status 480
And error code must be <errorCode>
And error message must be <errorMessage>
And file should not be created
Examples:
|file|destination|pageNumber|first|last|errorCode|errorMessage|
|common/ods/CalcPage5.ods|Hint/NegativeHintParameter|0|text|4|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odg/DrawPage4.odg|Hint/NegativeHintParameter|0|text|3|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/xlsx/ExcelPage4.xlsx|Hint/NegativeHintParameter|0|text|3|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odp/ImpressPage4.odp|Hint/NegativeHintParameter|0|text|3|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/pptx/PowerPointPage5.pptx|Hint/NegativeHintParameter|0|text|4|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/docx/WordPage5.docx|Hint/NegativeHintParameter|0|text|4|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odt/WriterPage5.odt|Hint/NegativeHintParameter|0|text|4|480000008|"message":"Parameter has incorrect value","parameter":"hint"|


Scenario: As a user I want to get an error for text value "last" hint parameter 

Given Office conversion service is up and running
When user sends a request with hint parameter first <first> and last <last> to convert pages <pageNumber> office document <file>
Then server must respond with status 480
And error code must be <errorCode>
And error message must be <errorMessage>
And file should not be created
Examples:
|file|destination|pageNumber|first|last|errorCode|errorMessage|
|common/ods/CalcPage5.ods|Hint/NegativeHintParameter|0|1|text|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odg/DrawPage4.odg|Hint/NegativeHintParameter|0|1|text|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/xlsx/ExcelPage4.xlsx|Hint/NegativeHintParameter|0|1|text|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odp/ImpressPage4.odp|Hint/NegativeHintParameter|0|1|text|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/pptx/PowerPointPage5.pptx|Hint/NegativeHintParameter|0|1|text|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/docx/WordPage5.docx|Hint/NegativeHintParameter|0|1|text|480000008|"message":"Parameter has incorrect value","parameter":"hint"|
|common/odt/WriterPage5.odt|Hint/NegativeHintParameter|0|1|text|480000008|"message":"Parameter has incorrect value","parameter":"hint"|

