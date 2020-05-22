zio-jasync-sql
=========
[![Apache License V.2](https://img.shields.io/badge/license-Apache%20V.2-blue.svg)](https://github.com/andriimartynov/scalatest-zio/blob/master/LICENSE)


Scala wrapper for [jasync-sql](https://github.com/jasync-sql/jasync-sql) a Simple, Netty based, asynchronous, performant and reliable database drivers for PostgreSQL and MySQL.


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
