# Getting Started

### Note that in this project deleting and updating(you can only update using spreadsheet) objects type of Student wasn't implemented.

In this project in every ten minutes database will be updated accordingly to spreadsheet.
For now, you can update Student table in spreadsheet. In the future, I'll add REST API to update Student table using
their id's.

#### For more explanations read documentation of this project

## You can view the spreadsheet here:

* [Students](https://docs.google.com/spreadsheets/d/12BLCE2P3WZHsY5XpbsxFhPxZD4J2ZAPRKnRJhVfJod8/edit#gid=0)

And you have to set up [Google Cloud](https://console.cloud.google.com/) and create a project there to
get [Credentials](src/main/resources/sheet_credentials.json).

* Go to APIs & Service > Enabled APIs & Services > Enable APIs & Services and enable Google Spreadsheet API.
* Then go to APIs & Services > OAuth consent screen and create a web app.
* Then go to APIs & Services > Credentials and create OAuth client ID.

## This instruction may not be up to date. So for more accurate information, please read official documentation of the google cloud service.
#### Note that if you are not logging in as owner of spreatsheet, app may fail because of setted range protection for id column. And if you don't want to log in as owner than I recommend to delete lines 89-99 in this [file](src/main/java/uz/pdp/googleapitest/service/StudentSheetServiceImpl.java).

### Reference Documentation

For further reference, please consider the following sections:

* [Google Sheets API Overview](https://developers.google.com/sheets/api/guides/concepts)
* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.0-SNAPSHOT/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.0-SNAPSHOT/gradle-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.2.0-SNAPSHOT/reference/htmlsingle/index.html#web)
* [Spring Web Services](https://docs.spring.io/spring-boot/docs/3.2.0-SNAPSHOT/reference/htmlsingle/index.html#io.webservices)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Producing a SOAP web service](https://spring.io/guides/gs/producing-web-service/)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)
