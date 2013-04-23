#!/bin/bash

TOMCAT_HOME="/var/lib/tomcat6/webapps"

sudo service tomcat6 stop

#echo " * Exporting new Kalibro Service."
#ant -f build.xml

echo " * Updating Kalibro Service."
sudo rm -rf $TOMCAT_HOME/KalibroService
sudo chown tomcat6:tomcat6 KalibroService.war
sudo mv KalibroService.war $TOMCAT_HOME

echo " * Erasing old kalibro database entries."
echo -en "\t > Please, type your kalibro database password: "
stty -echo
read
stty echo
echo
export PGPASSWORD=$REPLY
psql -q -U postgres -c "drop database kalibro"
psql -q -U postgres -c "drop database kalibro_test"
psql -q -U postgres -c "create database kalibro with owner=kalibro"
psql -q -U postgres -c "create database kalibro_test with owner=kalibro"
sudo service tomcat6 start
