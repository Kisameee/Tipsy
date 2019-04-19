# Tipsy  :
A scala project with akka http rest api to manage tips subs and survey for a gamer

###Requires :

##### name := "sbt"

##### version := "0.1"

##### scalaVersion := "2.12.8"

##### libraryDependencies ++= Seq(
 ######  "com.typesafe.akka" %% "akka-http" % "10.1.8",
  ###### "com.typesafe.akka" %% "akka-stream" % "2.5.19",
  ###### "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.8",
  ###### "org.scalatest" %% "scalatest" % "3.0.5" % "test"

### How it works :

Download the source code, add it to sbt project.
compile and then run the project on the sbt shell

## Rest Api available queries

#### Tips 

http://localhost:8080/tips/sumAllTips  
http://localhost:8080/tips/listedonors/(*UserId*)  
http://localhost:8080/tips/makeTip/(*id, amount*)  
http://localhost:8080/tips/cancelTip/(*id*))  
http://localhost:8080/tips/getAllTipsByUser/(*id*)  

#### Subs

http://localhost:8080/sub  
http://localhost:8080/blacklist  


#### Giveaways

http://localhost:8080/giveaways/createGiveAway  
http://localhost:8080/giveaways/RegisterToGiveAway  
http://localhost:8080/giveaways/getWinner/(*id*)  

#### Survey

http://localhost:8080/survey/createSurvey  
http://localhost:8080/survey/doSurvey  
http://localhost:8080/survey/getSurveyResult  

