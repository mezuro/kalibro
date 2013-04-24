#!/bin/bash

TOMCAT_HOME="/var/lib/tomcat6/webapps"

error() {
  echo -en "\e[00;31m"
  echo "Parameters error."
  echo "Please, choose a valid database: [mysql|postgres]"
  echo -en "\e[01;29m"
  exit
}

if [ $# -ne 1 ]; then
  error
fi

echo " * Exporting Kalibro .war file."
ant -f build.xml

echo " * Erasing old kalibro database entries."
if [ $1 == "mysql" ]; then
  sudo service tomcat6 stop
  sudo mysql kalibro -e "drop database kalibro; 
                         drop database kalibro_test;
                         create database kalibro;
                         create database kalibro_test;
                         grant all on kalibro.* to 'kalibro'@'localhost';
                         grant all on kalibro_test.* to 'kalibro'@'localhost';
                         flush privileges;"
elif [ $1 == "postgres" ]; then
  sudo service tomcat6 stop
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
else
  error
fi

echo " * Updating Kalibro Service."
sudo rm -rf $TOMCAT_HOME/KalibroService
sudo chown tomcat6:tomcat6 KalibroService.war
sudo mv KalibroService.war $TOMCAT_HOME
sudo chown tomcat6.tomcat6 $TOMCAT_HOME/KalibroService.war

sudo service tomcat6 start
