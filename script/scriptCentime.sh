
cd /home/ec2-user/centimeGit/centimeSystems
mvn clean install

cd /home/ec2-user/centimeGit/centimeGreetingService
mvn clean install
java -jar target/GreetingService-1.0-SNAPSHOT.jar

cd /home/ec2-user/centimeGit/centimeConcatenationService
mvn clean install
java -jar target/concatenation-application-1.0-SNAPSHOT.jar

cd /home/ec2-user/centimeGit/centimeHelloApplication
mvn clean install
java -jar target/hello-application-1.0-SNAPSHOT.jar