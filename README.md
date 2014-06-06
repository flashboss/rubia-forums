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
- Maven 3.x

Supported application servers and distributions
------------

- JBoss AS 7.1.1.Final
- Gatein 3.6.0.Final
- Gatein 3.7.0.Final
- Gatein 3.8.1.Final
- EAP 6.1.0.Final
- EAP 6.2.0.Final
- JBoss Portal 6.1.0.Final


Maven repositories
------------------

See example `settings.xml` file in this directory. In the most common case you will want to copy this file to `$HOME/.m2`
(`%HOMEPATH%\.m2` on Windows) with the following content:

Build
-----

In development mode:

    mvn clean install -P${distribution},development

... and see a ready to run distribution under `rubia-forums/target/rubia-forums.war`

list of distribution profiles:

    -Pjbossas-remote-711               jboss as 7.1.1
    -Pgatein-portlet-remote-360        gatein 3.6.0 as portlet
    -Pgatein-web-remote-360            gatein 3.6.0 as web application  
    -Pjbossportal-portlet-remote-610   jboss portal 6.1.0 as portlet
    -Pjbossportal-web-remote-610       jboss portal 6.1.0 as web application
    -Peap-remote-610                   EAP 6.1.0
    -Peap-remote-620                   EAP 6.2.0

You can also choose the deploy mode using the profiles:

    -Pdevelopment
    -Pproduction
    
If you want deploy in production mode you must use:

    mvn clean install -P${distribution},production
    
to deploy it with the shell command in jboss:

    $JBOSS_HOME/bin/jboss-cli.sh
    connect localhost
    deploy /xxxx/rubia-forums.war
   
 to create new users in jboss 7:

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


to test it with selenium:

    deploy the application in a server
    mvn -P${distribution},selenium test

If your web application uses a default locale different by the english you must set in the 'selenium' profile in the pom.xml:

	mvn -P${distribution},selenium test -Duser.language=it -Duser.region=IT
		
In this sample you must set the testing in the italian language.

To debug the application using Eclipse you can put this parameter:

    mvn -Dmaven.surefire.debug test

It will start on the 5005 port.