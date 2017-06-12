# Pico 

Microservices for the front end.

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

