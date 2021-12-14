1. Tomcat and idea configuration to work with uploaded images view:

Add the following new entry to Tomcat's /conf/server.xml inside <Host>:

<Context docBase="/your/path/to/images" path="/auction-images" />

That`s enough if you will place .war in tomcat /webapps, 
but if intellij idea will start application you should go to Run->Edit Configurations...->Server
and enable parameter "Deploy application configured by tomcat instance".

2. Mysql configuration: 

Event scheduler is not started by default. To check whether it is started:
select @@event_scheduler;

To start it:
SET GLOBAL event_scheduler = ON;