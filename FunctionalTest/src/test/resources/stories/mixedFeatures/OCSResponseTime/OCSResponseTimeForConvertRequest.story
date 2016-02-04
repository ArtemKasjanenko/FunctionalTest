Meta: check the server response time on Convert request with multiple office instances

Scenario: check the server response time on Convert request with multiple office instances
Given Office conversion service is up and running
When number of instances is set to <numberOfOfficeInstances>
And pccis is stoped and started in background
And response for convert request is arrived
Then pccis is started compleatelly
And service response time must be not more the then <maxServiceResponseTimeDelta> seconds from response time with one ocs instance

Examples:
|file|numberOfOfficeInstances|maxServiceResponseTimeDelta|
|common/txt/1.page.txt|1|0|
|common/txt/1.page.txt|2|3|
|common/txt/1.page.txt|4|12|
|common/txt/1.page.txt|10|30|
|common/txt/1.page.txt|20|60|