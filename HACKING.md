# Welcome my dev friend

This file will explain you how to change what you want and everything about the application structure.

## The arborescence

The structure of the application is a basic MVC architecture. In `eida_SMART/SMART/src/main` you will find all the most important stuff to run the application.

### JAVA

If you go into `eida_SMART/SMART/src/main/java`  folder you will find everything about the Model and The Controller handlers.

In `eida_SMART/SMART/src/main/org/eida/SMART/c` you will find the `MainController` which is here to handle all Requests Mappings and Verifications for create/delete/update and read.

In the same folder you have the `MyErrorController`, this one is useful for all 404, 500, etc.. Errors. For now it only handles error 500.

The `eida_SMART/SMART/src/main/org/eida/SMART/m` folder contains all entities and DAO interfaces for the database requests.

You can find in `eida_SMART/SMART/src/main/org/eida/SMART/storage` all the files to handle storage like `FileSystemStorageService` or `StorageProperties`.


`eida_SMART/SMART/src/main/org/eida/SMART` contains the `SmartApplication` file which contains the Main method.

You have also all the files for Spring Security system with `AuthentificationHandler`, `SecurityConfiguration` and Session handler with `SmartUserDetails`.

In the same folder you got the `InitRunner` where the Super Admin is created when you launch Spring boot the first time. This file creates the user and clear all uploaded files if the database is empty.


### Resources

If you go back in the arborescence and go in `eida_SMART/SMART/src/main/resources` you will find all the html templates (in the `templates` folder) and all the static content like images, CSS, JS in the `static` folder.

But before going into the templates folder or the static folder, let's take a look into the resources folder: You will find 4 files. `application.properties` which defines all Spring, StorageService and Hibernate properties like the maximum size of the uploads files or the logs for the MySql database.

`Banner.txt` is the banner in the shell when you run Spring Boot with the command `./mvnw spring-boot:run`, pretty isn't it?

`justgage.js` and `raphael-2.1.4.min.js` are 2 resources files like bootstrap, don't change them.

In `eida_SMART/SMART/src/main/resources/templates` you got 9 html files for the project.

And in `eida_SMART/SMART/src/main/resources/static` you got 5 files for the static content. `css` folder contains all the CSS3 files, `fonts` folder for the Ghotam fonts, `images` for images, `js` for JavaScript content and `vendor` for all the tables in the application.

