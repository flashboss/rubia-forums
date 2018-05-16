Rubia Forums
=============
Is a javaee 7 based application designed as standalone application, portlet, service layer and api.
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

This draft version has been tested on Wildfly 12.0.0.Final.

Requirements
------------

- JDK 1.8
- Maven 3.5.x

Supported application servers and distributions
------------

- Wildfly AS 10.0.0.Final
- Wildfly AS 10.1.0.Final
- Wildfly AS 11.0.0.Final
- Wildfly AS 12.0.0.Final


Build
-----

In development mode:

    mvn clean install -P${distribution},development

... and see a ready to run distribution under `rubia-forums/target/rubia-forums.war`

list of distribution profiles. With this you build an application ready to be deployed to an external WildFly AS:

    -Pwildfly-1000              wildfly as 10.0.0
    -Pwildfly-1010              wildfly as 10.1.0
    -Pwildfly-1100              wildfly as 11.0.0
    -Pwildfly-1200              wildfly as 12.0.0

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
    
If you want start automatically a WildFly instance you can use the JSF start profiles:

    -Pstart-1000-jsf              wildfly as 10.0.0
    -Pstart-1010-jsf              wildfly as 10.1.0
    -Pstart-1100-jsf              wildfly as 11.0.0
    -Pstart-1200-jsf              wildfly as 12.0.0
    
or the REST start profiles:

    -Pstart-1000-rest             wildfly as 10.0.0
    -Pstart-1010-rest             wildfly as 10.1.0
    -Pstart-1100-rest             wildfly as 11.0.0
    -Pstart-1200-rest             wildfly as 12.0.0
    
If you want to start a WildFly instance and execute the deploy of the JSF application:

    mvn clean install -P${distribution},production,start-${distribution}-jsf,deploy-jsf
    
Or for the REST application:

    mvn clean install -P${distribution},production,start-${distribution}-rest,deploy-rest

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
Re-enter Password : password2
What roles do you want this user to belong to? (Please enter a comma separated list, or leave blank for none) : users
The username 'admin' is easy to guess
Are you sure you want to add user 'admin' yes/no? yes


to test it with selenium:

    deploy the application in a server
    mvn -P${distribution},selenium test

If your web application uses a default locale different by the english you must set in the 'selenium' profile in the pom.xml:

	mvn -P${distribution},selenium test -Duser.language=it -Duser.region=IT
		
In this sample you must set the testing in the italian language.

Since Firefox 47.x you must download the Geck Driver to use Selenium. To test the application with Firefox 50.0.2 download the Gecko driver here according your operative system:

https://github.com/mozilla/geckodriver/releases/tag/v0.11.1

Unzip it and add it in your folder for example: /Application

The mvn command become:

    mvn -P${distribution},selenium test -Duser.language=it -Duser.region=IT -Dwebdriver.gecko.driver=/Applications/geckodriver

To debug the application using Eclipse you can put this parameter:

    mvn -Dmaven.surefire.debug test

It will start on the 5005 port.

The tests are done using:

- Firefox 46.0.1 on Wildfly 10.0.0.Final
- Firefox 54.0.1 (64-bit) on Wildfly 10.1.0.Final
- Firefox 56.0.2 (64-bit) on Wildfly 11.0.0.Final
- Firefox 60.0 (64-bit) on Wildfly 12.0.0.Final
