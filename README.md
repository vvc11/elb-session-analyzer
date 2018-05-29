# elb-session-analyzer
This project creates session file from ELB access logs and finds out the average session time and most engaged users from the logs.

## Building the Project ##

**Prerequisites** <br/>
JDK 1.8 <br/>
Maven 3+ <br/>
Scala 2.11 <br/>

**Steps to build** <br/>
Move into the directory and execute "mvn clean install" command <br/>

**Running the application** <br/>
A folder called "target" will be created after the build, inside that you'll find "WebLogChallenge-1.0-SNAPSHOT.jar" <br />
Submit this jar using spark-submit https://spark.apache.org/docs/latest/submitting-applications.html with --class argument as "ELBSessionAnalyzer" <br />
The program takes additional 4 arguments in the following order : <br/>
1. Location of the input file <br/>
2. Destination of the output file (where the sessions will be stored) <br/>
3. Session timeout (in milliseconds). The gap between two activities which would still cont under one session <br/>
4. Destination of the summary file (has the average session duration in milliseconds, the number of sessions and the cumulative session duration of all the users in milliseconds) <br/>

## How was this problem solved ##
**User Identification** <br/>
I'm using a combination of IP address and port number to identify a user. That's what I learnt about Network Address Translation

**Page Identification** <br/>
Each page is identified on basis of its first path parameter. Eg https://paytm.com/shop/toothbrush-oralb is counted as the same page type as https://paytm/shop/jacket-adidas-sports . So if a user navigates across all products in the shop category, they are counted as 1 distinct page.

**Extracting Sessions** <br/>
The entries in the ELB session log are first sorted by timestamp <br/>
All of these entries are then grouped by IP address and port number <br />
Sessions are calculated based on the max allowable delay between 2 activities <br />
Once separated by the above logic, session duration is calculated by taking the difference of the minimum and maximum time of the sessions. <br />

**Calculating Average Session Length** <br/>
Only sessions where a user has browsed more than 1 page (may not be distinct) is calculated for getting the average session length

## Results ##
For an average session time of 15 minutes, I got these results : <br />
Average Session Length - 340541.4400845625 milliseconds (5.6 minutes) <br />
Total number of sessions (where more than 1 page was visited) - 166977

Most engaged users <br/>
203.191.34.178:10400,2015-07-22T10:30:30.999963Z,2015-07-22T11:04:56.345504Z,4 (34 minutes 26 seconds)
103.29.159.138:57045,2015-07-22T10:30:29.265198Z,2015-07-22T11:04:53.971526Z,3 (34 minutes 24 seconds)
213.239.204.204:35094,2015-07-22T10:30:29.067761Z,2015-07-22T11:04:51.351913Z,1 (34 minutes 22 seconds)
78.46.60.71:58504,2015-07-22T10:30:28.057027Z,2015-07-22T11:04:49.581560Z,1 (34 minutes 21 seconds)
54.169.191.85:15462,2015-07-22T10:30:32.171992Z,2015-07-22T11:04:53.859339Z,1 (34 minutes 21 seconds)
103.29.159.186:27174,2015-07-22T10:30:33.109884Z,2015-07-22T11:04:53.307275Z,2 (34 minutes 20 seconds)
122.169.141.4:50427,2015-07-22T10:30:35.881874Z,2015-07-22T11:04:54.152249Z,2 (34 minutes 19 seconds)

## Disclaimer Regarding Time calculation ##
I have omitted using milli seconds while calculating most engaged users.
