# Pico 

Microservices for the front end. 

In microservice style architecture, instead of having a huge monolithic application that performs all functions, this is split into separate, isolated and decoupled microservices that can be deployed separately. This, in theory makes it much easier to make changes to parts of the system without having to redeploy the entire system.

This ease does not usually extend to the GUI part of the system, which tends to be deployed as a single monolith, communicating with all different areas of the system via JSON.  A new GUI function usually requires update of the Microservice in question, and a full redeploy of the front end GUI.


This project is an attempt to solve this issue by splitting the GUI into several different web applications all running independently. Pico then presents these web applications in its own iframe, adding and removing applications as needed as they are enabled/disabled.

This framework does not replace the need or compete with javascript front end frameworks like Ember.js or Angular.js.  Rather, it allows several different web applications using different front end frameworks, or no framework at all!,  to be presented as a single unified application to the user.  By doing it this way it becomes easier to migrate between front end frameworks by changing a single module at a time, rather than big bang change the lot.   

It also allows pico modules to be moved between servers with zero downtime or interuption to the user.

Service discovery is handled using Multicast messaging for ease of development. In future other means of discovery will be added to handle environments like Docker.


### Pico Core
PicoCore - the core framework that handles broadcasting of a pico enabled web application, and lookup of other pico web applications on the network.

### Pico Root
This is a simple Web application that makes use of PicoCore to discover PicoModules running on the local network, and presents them as a single WebApp. There are two versions:

PicoRoot - A simple Servlet 3 application, compiling as a WAR to be deployed to a servlet container.

PicoRootSpringBoot - the same application packaged as a Spring Boot stand alone jar.


### Example Hello World
PicoHelloWorld - A Pico enabled JavaEE web Application

PicoHelloWorldSpringBoot - A pico enabled Spring Boot web application.


## Getting Started

### With Java EE

1) Place PicoROOT/target/ROOT.war file in the webapps directory of your servlet container, and navigate to http://localhost:8080 . This will show an Empty Pico root application as there is no modules currently running.


2) Place PicoHelloWorld/target/PicoHelloWorld.war also into the webapps directory.    Now refresh http://localhost:8080   You will see the Hello World application has installed into the PicoRoot application.


### With Spring Boot.

1) Run PicoRootSpringBoot jar.  ( java -jar PicoRootSpringBoot/target/gs-spring-boot-0.1.9.jar )
navigate to http://localhost:8712 to show an Empty Pico Root application project.

2) Run PicoHelloWorldSpringBoot  (java -jar PicoHelloWorldSpringBoot/target/gs-spring-boot-0.1.9.jar)
Now refresh http://localhost:8712    You will see the Hello World application has installed into the PicoRoot application.


## Authors

* **Tony Weston** -


## License

This project is licensed under the Apache Public License. - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

Nanojson 
https://github.com/mmastrac/nanojson

