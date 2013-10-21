#!/bin/bash

git clone git@gitorious.org:kalibro/libraries.git
git clone git@gitorious.org:kalibro/resources.git

mv libraries Libraries
mv resources Resources

mkdir -pv $HOME/.kalibro
rm -rf $HOME/.kalibro/tests
tar -xvf Resources/tests.tar.gz -C $HOME/.kalibro

echo "Should be done on Eclipse:"
echo "1 - Install plugins Eclemma 1.5.3 and Checkstyle"
echo "2 - File->Import->Preferences for Resources/*.epf"
echo "3 - Window->Preferences->Checkstyle->New->External Configuration File with Resources/kalibro_checks.xml"
echo "4 - Window->Preferences->Java->Build Path->User Libraries->Import with Libraries/Libraries.userlibraries"
echo "5 - File->Import->General->Existing Projects into Workspace->Browse Kalibro directory"

exit 0
