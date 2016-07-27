# Wildfly Wrapper

## Introduction

Wildfly wrapper is used as maven dependency.

Common use of this dependency is in projects that will be deployed to Wildfly/Jboss.

Using this dependency you can create datasource connections (or check for existing ones) on target wildfly/jboss server.

## Common use

Create `Main` method in your project (it sounds weird to have it in `war` application - but keep on). 

After `war` file is created you can execute `war` file with standard java command to execute `Main` method.

This `Main` method can call `Wildfly wrapper` to check for existing database connection and if necessary create new one.

## What actions are supported

Using `Wildfly wrapper` you can perform these actions:

* check for existing datasource name
* check for existing JNDI name
* check for existing database connection driver
* create new database connection
* delete existing database connection
* alternatively you can execute any custom `jboss cli` supported command

## How to use

### Add dependency

`Wildfly wrapper` is published to internal InfoDation _Artifactory_ server, so you can incorporate it in your project by adding dependency in `pom.xml` file:

```xml
<dependency>
    <groupId>nl.infodation</groupId>
    <artifactId>wildflywrapper</artifactId>
    <version>0.1.0</version>
</dependency>
```

In addition to dependency definition, you must also include _Artifactory_ repository information:

```xml
<repository>
    <snapshots>
        <enabled>false</enabled>
    </snapshots>
    <id>central</id>
    <name>libs-release</name>
    <url>http://10.4.1.218:8091/artifactory/libs-release</url>
</repository>
```

Note that this IP address is only available from InfoDation private network.

### Use in code

You need to perform several steps:

Initalize `WildflyConnector`:

```java
WildflyConnector wildfly = new WildflyConnector("10.4.1.218", 9601, "admin", "password");
// "10.4.1.218" - is IP address of Wildfly server - given as string
// 9601 - management port of Wildfly server - given as integer
// "admin" is username of Wildfly management account - given as string
// "password" is password of Wildfly management account - given as string
```

Once you have it initialized you can now initialize `DataSourceActions`

```java
DataSourceActions actions = new DataSourceActions("mariaDS", "java:/MariaDBDS", "mysql", "10.4.1.218", "3306", "test", "root", "root");
// This one takes more parameters:
// "mariaDS" is name of connection
// "java:/MariaDBDS" is JNDI name
// "mysql" is the name of the JDBC driver
// "10.4.1.218" is IP address of MySQL/MariaDB server
// "3306" is MySQL/MariaDB port
// "test" is name of database
// "root" is username to connect to database
// "root" is password to connect to database
```

Now when you have `DataSourceActions` initialized you can perform actions:

#### Check for existing datasource name

```java
boolean result = actions.dataSourceNameExist(wildfly)
// true - data source with that name already exist
// false - data source with that name doesn't exist
```

You call method `dataSourceExist` and provide `WildflyConnector` object.

This will connect to wildfly server and run set of commandst to check if datasource name exist (it will use the name that was used to initialize `DataSourceActions` object)

#### Check for existing JNDI name

JNDI is also parameter that cannot be used in different database connections, so you have to check if it exists:

```java
boolean result = actions.jndiNameExist(wildfly)
// true - jndi name is already used
// false - jndi name doesn't exist
```
Same as above, it will check for existing JNDI name

### Check for existing JDBC driver

Before you make new connection you must use existing JDBC driver

```java
boolean result = actions.jdbcDriverExist(wildfly)
// true - driver exists
// false - driver doesn't exist
```

### Create new connection

This method will execute all checks listed above to make sure that connection can be made. It will make sure that:

* Connection name is **not already used** AND
* JNDI name is **not already used** AND
* JDBC driver **already exist**

So, if you just want to create new connection, this is the only method that you need, it will make all necessary checks:

```java
boolean result = action.createJDBCDataConnection(wildfly)
// true - connection is created
// false - connection is not created
```

### Delete existing connection

If you want, you can also delete existing database connection:

```java
boolean result = action.removeJDBCDataSource(wildfly)
// true - connection is created
// false - connection is not created
```

There is additional functionality to this command. When removing datasource response from Wildfly can contain information that `reload` is required. If that message is received, this method will also perform `reload` on Wildfly.

## Code is made using

* Eclipse (Neon)
* JUnit 4.12 (unit testing)
* SureFire (test results collection)
* JaCoCo (code coverage)
