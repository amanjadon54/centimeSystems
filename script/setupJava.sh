---- install Oracle JDK 1.8
$ wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u141-b15/336fa29ff2bb4ef291e347e091f7f4a7/jdk-8u141-linux-x64.rpm
$sudo yum install -y jdk-8u141-linux-x64.rpm

---- install OpenJDK
$ vi install.sh
yum install -y java-1.8.0-openjdk-devel
yum remove java-1.7.0-openjdk
wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo
yum install -y apache-maven
mvn --version

yum install -y git 

$ chmod 755 install.sh
$ sudo ./install.sh
