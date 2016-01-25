Meta: Positive scenario for hint parameter

Scenario: As a user I want to get some convert page use with hint parameter after convert pageNumber
Given Office conversion service is up and running
When user sends a request with hint parameter first <first> and last <last> to convert pages <pageNumber> office document <file>
Then server must respond with status 200
And number of converted PDF files for current office document is equal to <amount>
Examples:
|file|pageNumber|first|last|amount|destination|timeOut|
|common/ods/CalcPage5.ods|0|1|3|4|Hint/PositiveHintParameter|3000|
|common/odg/DrawPage4.odg|0|1|3|4|Hint/PositiveHintParameter|3000|
|common/xlsx/ExcelPage4.xlsx|0|1|3|4|Hint/PositiveHintParameter|3000|
|common/odp/ImpressPage4.odp|0|1|3|4|Hint/PositiveHintParameter|3000|
|common/pptx/PowerPointPage5.pptx|0|1|3|4|Hint/PositiveHintParameter|3000|
|common/docx/WordPage5.docx|0|1|4|5|Hint/PositiveHintParameter|3000|
|common/odt/WriterPage5.odt|0|1|4|5|Hint/PositiveHintParameter|3000|


Scenario: Value "first" of hint parameter is less than page number

Given Office conversion service is up and running
When user sends a request with hint parameter first <first> and last <last> to convert pages <pageNumber> office document <file>
Then server must respond with status 200
And number of converted PDF files for current office document is equal to <amount>
Examples:
|file|pageNumber|first|last|amount|destination|timeOut|
|common/ods/CalcPage5.ods|2|1|3|3|Hint/firstLessPage|3000|
|common/odg/DrawPage4.odg|2|1|3|3|Hint/firstLessPage|3000|
|common/xlsx/ExcelPage4.xlsx|3|2|3|2|Hint/firstLessPage|3000|
|common/odp/ImpressPage4.odp|2|1|3|3|Hint/firstLessPage|3000|
|common/pptx/PowerPointPage5.pptx|2|1|3|3|Hint/firstLessPage|3000|
|common/docx/WordPage5.docx|2|1|4|4|Hint/firstLessPage|3000|
|common/odt/WriterPage5.odt|2|1|4|4|Hint/firstLessPage|3000|

Scenario: Value "last" hint parameter more then max page must be ignored

Given Office conversion service is up and running
When user sends a request with hint parameter first <first> and last <last> to convert pages <pageNumber> office document <file>
Then server must respond with status 200
And number of converted PDF files for current office document is equal to <amount>
Examples:
|file|pageNumber|first|last|amount|destination|timeOut|
|common/ods/CalcPage5.ods|0|3|15|2|Hint/lastMoreMaxPage|3000|
|common/odg/DrawPage4.odg|0|1|10|4|Hint/lastMoreMaxPage|3000|
|common/xlsx/ExcelPage4.xlsx|0|2|10|3|Hint/lastMoreMaxPage|3000|
|common/odp/ImpressPage4.odp|0|1|10|4|Hint/lastMoreMaxPage|3000|
|common/pptx/PowerPointPage5.pptx|0|1|10|5|Hint/lastMoreMaxPage|3000|
|common/docx/WordPage5.docx|1|1|10|4|Hint/lastMoreMaxPage|3000|
|common/odt/WriterPage5.odt|1|1|10|4|Hint/lastMoreMaxPage|3000|
