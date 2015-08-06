Rubia Forums
=============
Rubia Forums is a javaee 7 based application designed as standalone application, portlet, service layer and api.
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

This draft version has been tested on Wildfly 9.0.1.Final.

Requirements
------------

- JDK 1.8
- Maven 3.x

Supported application servers and distributions
------------

- Wildfly AS 8.0.0.Final
- Wildfly AS 8.1.0.Final
- Wildfly AS 8.2.0.Final
- Wildfly AS 9.0.0.Final
- Wildfly AS 9.0.1.Final


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

    -Pwildfly-remote-800               wildfly as 8.0.0
    -Pwildfly-remote-810               wildfly as 8.1.0
    -Pwildfly-remote-820               wildfly as 8.2.0
    -Pwildfly-remote-900               wildfly as 9.0.0
    -Pwildfly-remote-901               wildfly as 9.0.1

You can also choose the package mode using the profiles:

    -Pdevelopment
    -Pproduction
    
If you want install in production mode you must use:

    mvn clean install -P${distribution},production
    
If you want automatically install and deploy the jsf application in a local active wildfly server:

    mvn install -P${distribution},production,deploy-jsf
    
If you want automatically uninstall and undeploy the application in a local active wildfly server:

    mvn clean -P${distribution},production,deploy-jsf
    
If you want automatically reinstall and redeploy the application in a local active wildfly server:

    mvn clean install -P${distribution},production,deploy-jsf
    
As the same manner you can deploy the rest application instead of the jsf application using the goal deploy-rest. Here a sample:

    mvn clean install -P${distribution},production,deploy-rest

to deploy it with the shell command in Wildfly:

    $JBOSS_HOME/bin/jboss-cli.sh
    connect localhost
    deploy /xxxx/rubia-forums.war
   
 to create new users in Wildfly:

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

The tests are done using:

- Firefox 38.0.5 on wildfly 8.0.0
- Firefox 38.0.5 on wildfly 8.1.0
- Firefox 38.0.5 on wildfly 8.2.0
- Firefox 39.0   on wildfly 9.0.0
- Firefox 39.0   on wildfly 9.0.1
