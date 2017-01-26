# commercetools-sunrise-java-training

Project for use in the Sunrise training (WIP)

## Bug fixing exercises

### Http Context and CompletionStage
* nothing to do with dependency injection
* see httpcontextexercise.HttpContextController

## Creation exercises

| exercise  | [GitHub Stream](app/githubstream/README.md) | [last viewed products](app/lastviewedproducts/README.md) |[better titles](app/bettertitles/README.md) |[bulky goods](app/bulkygoods/README.md) |
| ----------| --------------------------------------------|----------------------| ----------------------| ----------------------|
|request related hooks| yes | yes |no | no |
|page data hooks| yes | yes |yes |no |
|variant hooks|no|yes|no | no |
|adding hook interfaces required | no | yes |yes |yes |
| wiring into controller required  | no |no |multi|no |
| configuration should be externalized  | no |yes |no|no |
| need to inject instances  | no | yes |no|no |

### didemo
* DiDemoController to illustrate the scope for request scoped objects
* Task use injectionSubject in singleton scope

### bulky goods

* add a custom line item if there are more than 3 line items


## Pre-training material & reading

### Handlebars

To learn how to write Handlebars templates, please check the [Handlebars.js](http://handlebarsjs.com/) documentation. In particular, the sections about [Expressions](http://handlebarsjs.com/expressions.html), [Built-In Helpers](http://handlebarsjs.com/builtin_helpers.html) and [@data Variables](http://handlebarsjs.com/reference.html#data).

Try it out interactively: [tryhandlebarsjs.com](http://tryhandlebarsjs.com/)

### Java 8

[Lamdba Expressions](https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html), [Streams](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html) and [Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html) are recurring Java 8 features in Sunrise.

[CompletionStage](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletionStage.html) is heavily used in Sunrise to enable asynchronous computation. Read the [Guide to Java 8 CompletionStage](http://www.nurkiewicz.com/2013/05/java-8-definitive-guide-to.html) to learn how to work with it and take a look at the [CompletableFutureUtils.java](http://commercetools.github.io/commercetools-jvm-sdk/apidocs/io/sphere/sdk/utils/CompletableFutureUtils.html) class provided by the [JVM SDK](https://github.com/commercetools/commercetools-jvm-sdk), which contains some useful methods such as `recoverWith` and `exceptionallyCompleted`.

* A general guide on the highlights of Java 8 can be found in this [Java 8 Tutorial](http://winterbe.com/posts/2014/03/16/java-8-tutorial/).
* Another good overview is ["Java SE 8 for the Really Impatient"](http://www.horstmann.com/java8/index.html).
* Our [Java 8 Cheatsheet](https://we.tl/lmnvCPmqTT) is a helpful one-pager to put on your desk.

### Play Framework

Sunrise is built on top of [Play Web Framework](https://www.playframework.com/). If you're interested in the ideas behind the Play Framework it's worth checking [this presentation by the creator](http://www.slideshare.net/jboner/going-reactive-eventdriven-scalable-resilient-systems).

Play offers Dependency Injection using [Guice](https://github.com/google/guice), which is the main way to override the default behaviour of Sunrise. Read further information on how to use [Java Dependency Injection](https://www.playframework.com/documentation/2.5.x/JavaDependencyInjection) and [how to test](https://www.playframework.com/documentation/2.5.x/JavaFunctionalTest#Injecting-tests) with it.

Since Play 2.5, `CompletionStage` may arise sometimes a `No HTTP Context available` error. To understand why and learn how to fix it, check the [Java Thread Locals](https://www.playframework.com/documentation/2.5.x/ThreadPools#Java-thread-locals) documentation.

* The official [Play 2.5.x Documentation](https://www.playframework.com/documentation/2.5.x/Home). You can skip the "advanced topics" and everything concerning persistence and databases.
* The ["Play for Java" Book](http://www.manning.com/leroux/).
* Dependency Injection:
  * [In the Play Documentation](https://www.playframework.com/documentation/2.5.x/JavaDependencyInjection) 
  * [Getting Started on Guice](https://github.com/google/guice/wiki/GettingStarted) 
  * [Guice Bindings](https://github.com/google/guice/wiki/Bindings)

### SBT (Simple Build Tool)

[SBT 0.13 Tutorial](http://www.scala-sbt.org/0.13/tutorial/index.html)

### Play 2.5.x migration (in case that is relevant to your project) 

Play 2.5 switched some of its classes to the Java 8 counterpart, for example now `F.Promise` is replaced by `CompletionStage`. To see all changes in detail, check the [Migration Guide to 2.5](https://www.playframework.com/documentation/2.5.x/JavaMigration25).
