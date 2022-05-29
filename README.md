# CozyCave.ch Server

Made in Zürich with love! Made by Students of the Software Engineering Praktikum Course at the departments of Informatics @ UZH. 

## Introduction

Cozy Cave is designed to provide a platform for people looking for a place to live, to connect with landlords and shared accommodations that are looking for prospective tenants. Those looking for tenants will be able to create new listings, and tenants can apply to these AD with the details they have in their profile. Students can easily apply to a listing with a single click and in turn the landlords can see at a glance who applied to a specific listing and can approve or deny them. 

Especially intuitive is the way that students get directly identified through their email address and that only students can apply to properties. Furthermore the real time collaborative feature allows students to connect with other students who then in turn can apply for a listing. 

With the unique filtering, students can find the properties they’re interested in, let’s say a student wants to live in a house with other people of the same gender, Cozy Cave got the student covered through its filtering mechanism.

## Contributors

@davidcoita, @lukasherz,  @Slava-Berasneu, @matthias-imhof

## Technologies & Blueprint

* JAVA Version 17 and additional Frameworks:
  
  * Lombok https://projectlombok.org/
  
  * WebSockets
  
  * ....

## High-level components

Identify your project’s 3-5 main components. What is their role?

How are they correlated? Reference the main class, file, or function in the README text

with a link.

The [main class](https://github.com/sopra-fs22-group-15/cozycave-server/blob/main/src/main/java/ch/uzh/ifi/fs22/sel/group15/cozycave/server/Application.java) is starting the server
* For the [User](https://github.com/sopra-fs22-group-15/cozycave-server/blob/main/src/main/java/ch/uzh/ifi/fs22/sel/group15/cozycave/server/Application.java) we have splitted it up into three parts, the [authentication]
* For the [Listing]()
* For the [Applications]()
* For the [Picture]() We're using the 
* For our RealtimeCollaborative Feature [Gathertogether](https://github.com/sopra-fs22-group-15/cozycave-server/blob/main/src/main/java/ch/uzh/ifi/fs22/sel/group15/cozycave/server/websockets/handler/UserProfileHandler.java) we're using Java Websockets, such that the clients and prospective tenants can easily connect with one another. We defined our own message packages which are sent between the subscribers, such that not the whole profile with all of their details is exposed and first has to be approved by the other user. 


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

LOREM IPSUM

## File Storage

LOREM IPSUM





# Roadmap

### CREATE GROUPS & GROUP APPLICATION

This is one of our User-Stories, which still needs to be implemented. Unfortunately we couldn't achieve this, because we were lacking a fifth programmer in our group since Milestone 1.


### Picture Upload Safety & Ethical Concerns

As a matter of fact, a lot of such services are being abused to distribute illegal content such as pornographic material, drug distribution etc. Thus we would need some kind of logic to especially check the uploaded pictures, such that our platform, does not distribute these content over the web.


### IMPLEMENTATION OF A BUSINESS MODELL

For our customers, which in return needs to make a revenue with Cozy Cave, we need to design and implement a business model. For instance, the easiest and most simple way would be to display commercials next to the AD of our listings. A more advanced and unethical way is to sell the profile data and search preferences of our customers to third parties (e.g. what a customer would be willing to pay for a flat, where the student is looking for etc.).


### SCALE UP

The application needs to be stress tested for a lot of requests and users. Also it would make sense to have a failover cluster and use a loadbalancer to ensure the availability of Cozy Cave.



# License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
