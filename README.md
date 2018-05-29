# elb-session-analyzer
This project creates session file from ELB access logs and finds out the average session time and most engaged users from the logs.

## Building the Project ##

**Prerequisites**
JDK 1.8 <br/>
Maven 3+ <br/>
Scala 2.11 <br/>

**Steps to build**
Move into the directory and execute "mvn clean install" command <br/>

**Running the application**
A folder called "target" will be created after the build, inside that you'll find "WebLogChallenge-1.0-SNAPSHOT.jar" <br />
Submit this jar using spark-submit https://spark.apache.org/docs/latest/submitting-applications.html with --class argument as "ELBSessionAnalyzer" <br />
The program takes additional 4 arguments in the following order : <br/>
1. Location of the input file <br/>
2. Destination of the output file (where the sessions will be stored) <br/>
3. Session timeout (in milliseconds). The gap between two activities which would still cont under one session <br/>
4. Destination of the summary file (has the average session duration in milliseconds, the number of sessions and the cumulative session duration of all the users in milliseconds) <br/>

## How was this problem solved ##
**User Identification**
I'm using a combination of IP address and port number to identify a user. That's what I learnt about Network Address Translation

**Page Identification**
Each page is identified on basis of its first path parameter. Eg https://paytm.com/shop/toothbrush-oralb is counted as the same page type as https://paytm/shop/jacket-adidas-sports . So if a user navigates across all products in the shop category, they are counted as 1 distinct page.

**Extracting Sessions**
The entries in the ELB session log are first sorted by timestamp <br/>
All of these entries are then grouped by IP address and port number <br />
Sessions are calculated based on the max allowable delay between 2 activities <br />
Once separated by the above logic, session duration is calculated by taking the difference of the minimum and maximum time of the sessions. <br />

**Calculating Average Session Length**
Only sessions where a user has browsed more than 1 page (may not be distinct) is calculated for getting the average session length

## Results ##
For an average session time of 15 minutes, I got these results : <br />
Average Session Length - 340541.4400845625 milliseconds (5.6 minutes) <br />
Total number of sessions (where more than 1 page was visited) - 166977


78.46.60.71:58504,2015-07-22T10:30:28.057027Z,2015-07-22T11:04:52.919648Z,1 (34 minutes 24 seconds)
213.239.204.204:35094,2015-07-22T10:30:29.067761Z,2015-07-22T11:04:47.927924Z,1 (34 minutes 18 seconds)
103.29.159.138:57045,2015-07-22T10:30:30.027519Z,2015-07-22T11:04:31.881531Z,3 (34 minutes 1 second)
203.191.34.178:10400,2015-07-22T10:30:38.106085Z,2015-07-22T11:04:35.920730Z,4 (33 minutes 57 seconds)
122.169.141.4:50427,2015-07-22T10:30:47.039496Z,2015-07-22T11:04:42.956242Z,2 (33 minutes 55 seconds)
122.169.141.4:11486,2015-07-22T10:31:21.003947Z,2015-07-22T11:04:54.960846Z,3 (33 minutes 33 seconds)
103.29.159.62:55416,2015-07-22T10:30:38.040441Z,2015-07-22T11:04:04.924424Z,2 (33 minutes 26 seconds)
