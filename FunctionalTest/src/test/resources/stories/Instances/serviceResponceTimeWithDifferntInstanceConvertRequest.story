Meta: check the service convert request response time with different amount of instance

Scenario: check the convert request response time for the round of instance
Given Office conversion service is up and running
When number of instances is set to 1
And pcc is stoped and started in background
And response for get convert request is arrived
And service is started compleatelly

When number of instances is set to 2
And pcc is stoped and started in background
And response for get convert request is arrived
And service is started compleatelly
Then max service response time must be not more the then 3 sec from first get response time

When number of instances is set to 4
And pcc is stoped and started in background
And response for get convert request is arrived
And service is started compleatelly
Then max service response time must be not more the then 12 sec from first get response time

When number of instances is set to 10
And pcc is stoped and started in background
And response for get convert request is arrived
And service is started compleatelly
Then max service response time must be not more the then 30 sec from first get response time
Examples:
|file|
|testInstances.docx|