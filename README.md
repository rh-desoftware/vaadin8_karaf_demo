# Demo for deploying a simple Vaadin 8 application in Apache Karaf 4

This repository contains two examples for deploying a Vaadin 8 application into Apache Karaf.

Use Maven to build the project:

```bash
mvn install
```

The module `vaadin8_karaf_demo_war` contains a Vaadin application that is deployed war.
To install into Karaf 4 use this command in the Karaf console:

Installation on Karaf 4:

```bash
karaf@root()> feature:repo-add mvn:com.desoft/vaadin8_karaf_demo_war/1.0.0-SNAPSHOT/xml/features
Adding feature url mvn:com.desoft/vaadin8_karaf_demo_war/1.0.0-SNAPSHOT/xml/features
karaf@root()> feature:install vaadin8_karaf_demo_war
```

Got to the web browser and open http://localhost:8181/vaadin8demo/war

The module `vaadin8_karaf_demo_whiteboard` contains a Vaadin application that OSGI http white board

Installation on Karaf 4:

```bash
karaf@root()> feature:repo-add mvn:com.desoft/vaadin8_karaf_demo_whiteboard/1.0.0-SNAPSHOT/xml/features
Adding feature url mvn:com.desoft/vaadin8_karaf_demo_whiteboard/1.0.0-SNAPSHOT/xml/features
karaf@root()> feature:install vaadin8_karaf_demo_whiteboard
```

Got to the web browser and open http://localhost:8181/vaadin8demo/whiteboard





