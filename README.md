quippy-wat v0.0.2
=============

A repository for memorable quips and quotes.

This project is a "simple" Java web application that stores your quips and quotes.
(Nothing is ever simple with Java.)

It is written in the [Ninja Framework][1].

## Prerequisites
You need a Java application server like Tomcat, a relational database, and some configuration
edits to make this project run.

## Installation
  * Copy `application.conf.sample` to `application.conf`
  * Add your database connectivity configuration
  * Run `mvn clean install`
  * Run `mvn ninja:run` to launch the site locally in SuperDevMode
  * Deploy the war file in ``target`` to your tomcat server.

[1]: http://www.ninjaframework.org/