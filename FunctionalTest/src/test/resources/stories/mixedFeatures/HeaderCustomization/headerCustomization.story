Meta: Header Customization for TXT and RTF files 

Scenario: Positive customization of header with different header properties

Given Office conversion service is up and running
When user sends request to convert page <pageNumber> of office document <file> to <outputFolder> with header customization <headerCustomization>
Then server must respond with status 200
And conversion result matches with template <referenceImage> (difference less 0.1 percent)

Examples:
|file|pageNumber|outputFolder|referenceImage|headerCustomization|
|common/txt/1.page.txt|0|HeaderCustomization/basic|HeaderCustomization/basic/common/txt/1.page.txt.page.0.pdf.png|{"lines":["From: Thomans Munz","Subject: Medical Collection Management Policy", "Sent: January 24, 2015 10:00 PM","To: Sam Villa"],"fontSize": "11","fontFamily": "LiberationMono","color": "#ff0000"}|
|common/txt/8.pages.txt|0|HeaderCustomization/basic|HeaderCustomization/basic/common/txt/8.pages.txt.page.0.pdf.png|{"lines":["From: Thomans Munz","Subject: License", "Sent: January 24, 2015 10:00 PM","To: Sam Villa"],"fontSize": "16","fontFamily": "TimesNewRoman","color": "#00ff00"}|
|common/txt/8.pages.txt|1|HeaderCustomization/basic|HeaderCustomization/basic/common/txt/8.pages.txt.page.1.pdf.png|{"lines":["From: Thomans Munz","Subject: Test", "Sent: January 24, 2015 10:00 PM","To: Sam Villa"],"fontSize": "12","fontFamily": "TimesNewRoman","color": "#ff0000"}|
|common/rtf/2.pages.rtf|0|HeaderCustomization/basic|HeaderCustomization/basic/common/rtf/2.pages.rtf.page.0.pdf.png|{"lines":["From:     Thomans Munz","Subject:  Worksheet", "Sent:     January 24, 2015 10:00 PM","To:       Sam Villa"],"fontSize": "10","fontFamily": "LiberationMono","color": "#000000"}|



Scenario: Positive customization of header with empty lines

Given Office conversion service is up and running
When user sends request to convert page <pageNumber> of office document <file> to <outputFolder> with header customization <headerCustomization>
Then server must respond with status 200
And conversion result matches with template <referenceImage> (difference less 0.1 percent)

Examples:
|file|pageNumber|outputFolder|referenceImage|headerCustomization|
|common/txt/1.page.txt|0|HeaderCustomization/emptyLines|HeaderCustomization/emptyLines/common/txt/1.page.txt.page.0.pdf.png|{"lines":["From: Thomans Munz","","To: Sam Villa","","Subject: Medical Collection Management Policy","", "Sent: January 24, 2015 10:00 PM", "", ""],"fontSize": "16","fontFamily": "LiberationMono","color": "#00ff00"}|
|common/txt/8.pages.txt|0|HeaderCustomization/emptyLines|HeaderCustomization/emptyLines/common/txt/8.pages.txt.page.0.pdf.png|{"lines":["From:     Thomans Munz", "To:       Sam Villa", "","Subject:  License","Sent:     January 24, 2015 10:00 PM", "", ""],"fontSize": "10","fontFamily": "LiberationMono","color": "#000000"}|
|common/txt/8.pages.txt|1|HeaderCustomization/emptyLines|HeaderCustomization/emptyLines/common/txt/8.pages.txt.page.1.pdf.png|{"lines":["From: Thomans Munz","","Subject: Test","", "Sent: January 24, 2015 10:00 PM","To: Sam Villa", "", ""],"fontSize": "8","fontFamily": "TimesNewRoman","color": "#00ff00"}|
|common/rtf/2.pages.rtf|0|HeaderCustomization/emptyLines|HeaderCustomization/emptyLines/common/rtf/2.pages.rtf.page.0.pdf.png|{"lines":["From: Thomans Munz","","Subject: Worksheet","", "Sent: January 24, 2015 10:00 PM","To: Sam Villa", "", ""],"fontSize": "10","fontFamily": "Arial","color": "#0000ff"}|

Scenario: Positive customization of header with multiple lines and multiple languages 

Given Office conversion service is up and running
When user sends request to convert page <pageNumber> of office document <file> to <outputFolder> with header customization <headerCustomization>
Then server must respond with status 200
And conversion result matches with template <referenceImage> (difference less 0.1 percent)

Examples:
|file|pageNumber|outputFolder|referenceImage|headerCustomization|
|common/txt/1.page.txt|0|HeaderCustomization/languages|HeaderCustomization/languages/common/txt/1.page.txt.page.0.pdf.png|{"lines":["Japan: 有名な内部告発者","Kor: 사회과학원 어학연구소", "Gre: γρήγορη", "France: Fermentum vitae donec cél des àc vitaé consectetur", "Rus: Текст на русском", "German: Keine Gebühren haben gebracht worden", "Slovak: Novinár, ktorý vystavení klasifikované", "China-Trad: 格倫·格林沃爾德說", "China-Simp: 格伦·格林沃尔德说", "English: English Text"],"fontSize": "11","fontFamily": "TimesNewRoman","color": "#0000ff"}|
|common/rtf/2.pages.rtf|0|HeaderCustomization/languages|HeaderCustomization/languages/common/rtf/2.pages.rtf.page.0.pdf.png|{"lines":["Japan: 有名な内部告発者","Kor: 사회과학원 어학연구소", "Gre: γρήγορη", "France: Fermentum vitae donec cél des àc vitaé consectetur", "Rus: Текст на русском", "German: Keine Gebühren haben gebracht worden", "Slovak: Novinár, ktorý vystavení klasifikované", "China-Trad: 格倫·格林沃爾德說", "China-Simp: 格伦·格林沃尔德说", "English: English Text"],"fontSize": "11","fontFamily": "TimesNewRoman","color": "#0000ff"}|

