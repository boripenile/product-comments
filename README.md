# Amazon products' comments and Facebook videos' Reviews downloader
# implementation using Spring-mvc, Jooq and Jsoup.

Instruction on running the source.

1. Download and import the source code into your favourite IDE for example eclipse.
2. Create a database with a name products_download in mysql databae engine. (please do not ask me why this name).
3. Import products_download.sql into your newly create database in step 2.
4. Set username and password of your database in the persistence.properties file located in the resources folder.
4. Change the following values located in this class - com.example.app.products.util.SendEmailService :
	SENDEREMAILID = "email_address_here"
    SENDERPASSWORD = "password_here"
    MAIL_HOST = "smtp.mailgun.org"
    MAIL_HOST_POST = "587"
5. Change the following value located this class - com.example.app.products.facebook.FacebookService
    key = "Facebook-api-key"
6. Build the project using:
   mvn clean install
7. Deploy on any Java web application server, for example Tomcat-7.0.86

Please note that java virtual machine much be allocated enough memory if you are download high volume of product's reviews and youtude comments.

Happy download.
   
