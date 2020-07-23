zio-jasync-sql
=========
[ ![Download](https://api.bintray.com/packages/andriimartynov/maven/zio-jasync-sql/images/download.svg) ](https://bintray.com/andriimartynov/maven/zio-jasync-sql/_latestVersion)
[![Build Status](https://travis-ci.org/andriimartynov/zio-jasync-sql.svg)](https://travis-ci.org/andriimartynov/zio-jasync-sql)
[![Apache License V.2](https://img.shields.io/badge/license-Apache%20V.2-blue.svg)](https://github.com/andriimartynov/scalatest-zio/blob/master/LICENSE)


Scala wrapper for [jasync-sql](https://github.com/jasync-sql/jasync-sql) a Simple, Netty based, asynchronous, performant and reliable database drivers for PostgreSQL and MySQL.

## Getting zio-jasync-sql

To resolve artifacts through Artifactory, simply add the following code snippet to your build.sbt file:

```scala
resolvers += Resolver.jcenterRepo
```

The current version is 1.1.1, which is cross-built against Scala 2.11.x, 2.12.x and 2.13.x.

```scala
libraryDependencies += "com.github.andriimartynov" %% "zio-jasync-sql" % "1.1.1"
```

## Usage example
```scala
// Connection to MySQL DB
val connection = MySQLConnectionBuilder.createConnectionPool(
               "jdbc:mysql://$host:$port/$database?user=$username&password=$password");
     
// Connection to PostgreSQL DB    
val connection = PostgreSQLConnectionBuilder.createConnectionPool(
               "jdbc:postgresql://$host:$port/$database?user=$username&password=$password");

val layer: Layer[Throwable, ConnectionService] =
    ConnectionService.live(connection)

val rio: RIO[ConnectionService, QueryResult] = for {
queryResult <- sendPreparedStatement("select * from test limit 2")
} yield queryResult

val task: Task[QueryResult] = rio.provideLayer(layer)
```
See a full example at [zio-jasync-mysql-example](https://github.com/andriimartynov/zio-jasync-example/tree/master/zio-jasync-mysql-example) and [zio-jasync-postgresql-example](https://github.com/andriimartynov/zio-jasync-example/tree/master/zio-jasync-postgresql-example).
