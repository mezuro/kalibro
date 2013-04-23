#!/bin/bash

validate() {
	unset ANSWER
	while ! [ "$ANSWER" == "y" ]
	do	
		echo -e "\t > Are you done? (y)"
		read ANSWER
	done
}

TOMCAT_HOME="/var/lib/tomcat6/webapps"

echo "* Exporting Kalibro .war file."
#ant -f build.xml
sudo service tomcat6 stop
sudo rm -rf $TOMCAT_HOME/KalibroService
sudo mv KalibroService.war $TOMCAT_HOME

echo "* Please, drop your kalibro database now."
validate
echo "* Please, create your kalibro database again."
validate
sudo service tomcat6 start
