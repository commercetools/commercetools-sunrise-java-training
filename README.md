# commercetools-sunrise-java-training

project for use in the Sunrise training (WIP)

## Bug fixing exercises

### Http Context and CompletionStage
* nothing to do with dependency injection
* see httpcontextexercise.HttpContextController

## Creation exercises

### didemo
* DiDemoController to illustrate the scope for request scoped objects
* task use injectionSubject in singleton scope

### last viewed products component

* contains a controller which shows the 4 last seen products
* LastViewedProductsComponent and LastViewedProductsController

### bulky goods

* add a custom line item if there are more than 3 line items


## Pre-training material

#### Handlebars
To learn how to write Handlebars templates, please check the [Handlebars.js](http://handlebarsjs.com/) documentation. In particular, the sections about [Expressions](http://handlebarsjs.com/expressions.html), [Built-In Helpers](http://handlebarsjs.com/builtin_helpers.html) and [@data Variables](http://handlebarsjs.com/reference.html#data).

Try it out: http://tryhandlebarsjs.com/

#### Java 8
A general guide on the highlights of Java 8 can be found in this [Java 8 Tutorial](http://winterbe.com/posts/2014/03/16/java-8-tutorial/). Lamdba Expressions, Streams and Optionals are especially useful for Sunrise.

A bigger topic is `CompletionStage`, which is constantly used by Sunrise and Play 2.5. Read the [Guide to Java 8 CompletionStage](http://www.nurkiewicz.com/2013/05/java-8-definitive-guide-to.html) and take a look at the [CompletableFutureUtils.java](http://commercetools.github.io/commercetools-jvm-sdk/apidocs/io/sphere/sdk/utils/CompletableFutureUtils.html) utils class provided by JVM SDK, which contains some useful methods used in Sunrise, such as `recoverWith`.

[Java 8 Cheatsheet](https://drive.google.com/a/commercetools.de/file/d/0B6I3hlPXzkVhUlJKLXZoTTZKaDQ/view)

#### Play 2.5.x
Play 2.5 switched some of its classes to the Java 8 counterpart, for example now `F.Promise` is replaced by `CompletionStage`. To see all changes in detail, check the [Migration Guide to 2.5](https://www.playframework.com/documentation/2.5.x/JavaMigration25).

Migrating to Play 2.5 `CompletionStage` may arise sometimes a `No HTTP Context available` error. To understand why and learn how to fix it, check the [Java Thread Locals](https://www.playframework.com/documentation/2.5.x/ThreadPools#Java-thread-locals) documentation.

Play 2.4 offered Dependency Injection using Guice by default, which is heavily used in Sunrise. Check what is affected by this change in the [Migration Guide to 2.4](https://www.playframework.com/documentation/2.5.x/Migration24#Dependency-Injection). Read further information on how to use [Java Dependency Injection](https://www.playframework.com/documentation/2.5.x/JavaDependencyInjection) and [how to test](https://www.playframework.com/documentation/2.5.x/JavaFunctionalTest#Injecting-tests) with it.
