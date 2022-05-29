# CozyCave.ch Server

![our logo](https://raw.githubusercontent.com/sopra-fs22-group-15/cozycave-server/main/pictures/logo.svg)

Made in Zürich with love! 
Made by Students of the Software Engineering Praktikum Course at the departments of Informatics @ UZH. 

## Introduction

Cozy Cave is designed to provide a platform for people looking for a place to live, to connect with landlords and shared accommodations that are looking for prospective tenants. Those looking for tenants will be able to create new listings, and students can apply to these listings with the details they have in their profile - this only takes a single click. In turn, the landlords can see at a glance who applied to one of their listing, view their profile, and can accept or deny them - in both cases, the student is notified about the decision. 

Especially intuitive is the way that students get automatically identified through their email address by the system, which then ensures that only students can apply to properties. Furthermore, the real-time collaborative feature allows students to connect with each other and exchange contact details, for example to look for accomodation together, or just as a way to find like-minded people to form a new flatshare with.

With the unique filtering, students can find the properties they’re interested in. - e.g. if someone wants to live in a house with only other people of the same gender, Cozy Cave got the student covered through its filtering mechanism.

Last but not least, external APIs are used to display the location of listings on a map for the user, and also calculate travel times from the user's important addresses (e.g. work address) to the listing's location, which is very convenient to avoid repetitive Google Maps searches when browsing through listings.

## Illustrations
![this is how an connection to the websocket is used](https://raw.githubusercontent.com/sopra-fs22-group-15/cozycave-server/main/pictures/websockets.gif)
![this is our great api](https://raw.githubusercontent.com/sopra-fs22-group-15/cozycave-server/main/pictures/API.png)

## Technologies & Blueprint

* JAVA Version 17 and additional Frameworks:
  * Project Lombok
  * WebSockets
  * Spring Boot
  * Spring Security with JWT
  * JPA, Hibernate
  * ...

## High-level components

The [main class](https://github.com/sopra-fs22-group-15/cozycave-server/blob/main/src/main/java/ch/uzh/ifi/fs22/sel/group15/cozycave/server/Application.java) is starting the server
* For the [User](https://github.com/sopra-fs22-group-15/cozycave-server/blob/main/src/main/java/ch/uzh/ifi/fs22/sel/group15/cozycave/server/entity/users/User.java) we have split it up into two parts, the [authentication](https://github.com/sopra-fs22-group-15/cozycave-server/tree/main/src/main/java/ch/uzh/ifi/fs22/sel/group15/cozycave/server/security) and the [user management](https://github.com/sopra-fs22-group-15/cozycave-server/blob/main/src/main/java/ch/uzh/ifi/fs22/sel/group15/cozycave/server/controller/UserController.java).
* For the [Listing](https://github.com/sopra-fs22-group-15/cozycave-server/blob/main/src/main/java/ch/uzh/ifi/fs22/sel/group15/cozycave/server/entity/listings/Listing.java) 
* For the [Applications](https://github.com/sopra-fs22-group-15/cozycave-server/blob/main/src/main/java/ch/uzh/ifi/fs22/sel/group15/cozycave/server/entity/applications/Application.java)
* For the [Picture](https://github.com/sopra-fs22-group-15/cozycave-server/blob/main/src/main/java/ch/uzh/ifi/fs22/sel/group15/cozycave/server/entity/Picture.java) We're using the 
* For our RealtimeCollaborative Feature [GatherTogether](https://github.com/sopra-fs22-group-15/cozycave-server/blob/main/src/main/java/ch/uzh/ifi/fs22/sel/group15/cozycave/server/websockets/handler/UserProfileHandler.java) we're using Java Websockets, such that the clients and prospective tenants can easily connect with one another. We defined our own message packages which are sent between the subscribers, such that not the whole profile with all of their details is exposed and first has to be approved by the other user. 


## Launch & Deployment

### API Endpoints for Developers

The documentation for the endpoints can be found on: [https://docs.cozycave.ch/](https://docs.cozycave.ch/)

### Building with Gradle

You  can use the local Gradle Wrapper to build the application.

- macOS: `./gradlew`
- Linux: `./gradlew`
- Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

#### Build

```bash
./gradlew build
```

#### Run

```bash
./gradlew bootRun
```

#### Test

```bash
./gradlew test
```

#### Development Mode

You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed and you save the file.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`

### Debugging

If something is not working and/or you don't know what is going on. We highly recommend that you use a debugger and step
through the process step-by-step.

To configure a debugger for SpringBoot's Tomcat servlet (i.e. the process you start with `./gradlew bootRun` command),
do the following:

1. Open Tab: **Run**/Edit Configurations
2. Add a new Remote Configuration and name it properly
3. Start the Server in Debug mode: `./gradlew bootRun --debug-jvm`
4. Press `Shift + F9` or the use **Run**/Debug"Name of your task"
5. Set breakpoints in the application where you need it
6. Step through the process one step at a time

## Database

A MySQL database is required. The login credentials has to be configured in the [application.properties](https://github.com/sopra-fs22-group-15/cozycave-server/blob/main/src/main/resources/application.properties).

## File Storage

An FTP upload storage is also required.
To configure the ftp upload, the login credentials have to be set in the [PictureService](https://github.com/sopra-fs22-group-15/cozycave-server/blob/main/src/main/java/ch/uzh/ifi/fs22/sel/group15/cozycave/server/service/PictureService.java).
The path to the webserver has to be set in [Picture](https://github.com/sopra-fs22-group-15/cozycave-server/blob/main/src/main/java/ch/uzh/ifi/fs22/sel/group15/cozycave/server/entity/Picture.java).


# Roadmap

### CREATE GROUPS & GROUP APPLICATION

This is one of our User-Stories, which still needs to be implemented. Unfortunately we couldn't achieve this, because we were lacking a fifth programmer in our group since Milestone 1.


### PICTURE UPLOAD SAFETY & ETHICAL CONCERNS

As a matter of fact, a lot of such services are being abused to distribute illegal content such as pornographic material, drug distribution etc. Thus we would need some kind of logic to check the uploaded pictures, such that our platform does not distribute these contents over the web.


### IMPLEMENTATION OF A BUSINESS MODEL

For our customers (e.g Universities), which could need to make a revenue through Cozy Cave to offset maintenance costs, we need to design and implement a business model. For instance, the easiest and most simple way would be to display commercials next to the AD of our listings. A more advanced and unethical way is to sell the profile data and search preferences of our customers to third parties (e.g. what a customer would be willing to pay for a flat, where the student is looking for accomodation etc.).


### SCALE UP

The application needs to be stress tested for a lot of requests and users. Also, it would make sense to have a failover cluster and use a loadbalancer to ensure the availability of Cozy Cave.

# Authors and acknowledgment

[David Coita](https://github.com/davidcoita), [Lukas Herz](https://github.com/lukasherz),  [Slava-Berasneu](https://github.com/Slava-Berasneu), [Matthias Imhof](https://github.com/matthias-imhof)

# License

The project license can be seen in the [LICENSE.md](LICENSE.md) file.
The logo is copyrighted by Pascal Kanzinger.
