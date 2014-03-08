Rubia Forums
=============
Rubia Forums is a JSF (Java Server Faces) based application designed as standalone application and as portlet.
It is an open source project. This means you can download the Rubia Forums package, throw it into your deploy directory and gain fully featured Forums Application or Portlet.

It covers the following features:

- Browsing categories.
- Browsing forums.
- Browsing topics.
- Posting and replying.
- Creating and voting on polls.
- Posting attachments.
- Administration.
- Moderation of forum.
- User preferences.
- Personalized view of subscribed topics and forums.
- E-mail notifications.
- RSS/ATOM feeds.

This draft version has been tested on JBoss 7.1.1.Final.

Requirements
------------

- JDK 1.7
- Maven 3.0.x
- JBoss 7.1.1.Final - installed automatically by Maven during build


Maven repositories
------------------

See example `settings.xml` file in this directory. In the most common case you will want to copy this file to `$HOME/.m2`
(`%HOMEPATH%\.m2` on Windows) with the following content:

Build
-----

    mvn clean install

... and see a ready to run distribution under `rubia-forums-distribution/target/rubia-forums-*-dist.zip`

to deploy in an existent jboss distribution:

    export JBOSS_HOME=.....
    mvn clean -Pjbossas-remote-7

to test all it in jboss:

    export JBOSS_HOME=.....
    mvn clean
    mvn -Pjbossas-embedded-7 install

to test it with a single test in jboss:

    export JBOSS_HOME=.....
    mvn -Pjbossas-embedded-7 test -Dtest=xxxxxTest

to test it with selenium:

    download selenium-server from http://selenium.googlecode.com/files/selenium-server-standalone-2.25.0.jar
    deploy the application in a server
    start the server with 8080 port
    java -jar selenium-server-standalone-2.25.0.jar
    deploy the application with the command: mvn jboss-as:deploy assuming the JBOSS_HOME system variable is correct
    mvn -Pftest test

to test it with a single test in jboss:
    mvn -Pftest test -Dtest=xxxxxTest

to deploy it with the shell command in jboss:

   $JBOSS_HOME/bin/jboss-cli.sh
   connect localhost
   deploy /xxxx/rubia-forums.war
   
 to create new users in jboss 7:

1-

$JBOSS_HOME/bin/add_user.sh

What type of user do you wish to add? 
 a) Management User (mgmt-users.properties) 
 b) Application User (application-users.properties)
(a): b

Enter the details of the new user to add.
Realm (ApplicationRealm) : 
Username : user2
Password : password2
Re-enter Password : password
What roles do you want this user to belong to? (Please enter a comma separated list, or leave blank for none) : users
The username 'admin' is easy to guess
Are you sure you want to add user 'admin' yes/no? yes