Meta:  check the server response time on Info request with multiple office instances

Scenario: check the server response time on Info request with multiple office instances
Given Office conversion service is up and running
When number of office instances is set to <numberOfOfficeInstances>
And pccis is stoped and started in background
And response for get info request is arrived
Then pccis is started compleatelly
And service response time must be not more the then <maxServiceResponseTimeDelta> seconds from response time with one ocs instance

Examples:
|numberOfOfficeInstances|maxServiceResponseTimeDelta|
|1|0|
|2|3|
|4|12|
|10|30|
|20|60|