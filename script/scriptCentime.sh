
cd /home/ec2-user/centime/cetimeServices/centimeUtil
mvn clean install
java -jar target/concatenation-application-1.0-SNAPSHOT.jar 

cd /home/ec2-user/centime/cetimeServices/GreetingService
mvn clean install

cd /home/ec2-user/centime/cetimeServices/concatenationApplication
mvn clean install

cd /home/ec2-user/centime/cetimeServices/helloApplication
mvn clean install