Meta: check the service response time with different amount of instance

Scenario: check the server response time for the round of instance
Given Office conversion service is up and running
When number of instances is set to 1
And pcc is stoped and started in background
And response for get info request is arrived
And service is started compleatelly
When number of instances is set to 2
And pcc is stoped and started in background
And response for get info request is arrived
And service is started compleatelly
When number of instances is set to 3
And pcc is stoped and started in background
And response for get info request is arrived
And service is started compleatelly
When number of instances is set to 4
And pcc is stoped and started in background
And response for get info request is arrived
And service is started compleatelly
Then max getInfo response time must be not more the then <difference> percents from first get response time
Examples:
|difference|
|0.1|