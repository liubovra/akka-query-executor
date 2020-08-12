# Akka Http + Slick - Generic Query Executor

The service gets query and exports the query's results to a Zipped CSV file.

The request body will be as follow:
{
	"query" : "select * from table where id > 23",
	"fileName" : "/tmp/testdata.csv"
}


## Build

Use sbt command

```bash
sbt stage
```

## Install

1. copy akka-http-quickstart-scala_2.13-0.1.0-SNAPSHOT.jar from target\scala-2.13 to to docker-scripts\lib
```bash
copy target\scala-2.13\akka-http-quickstart-scala_2.13-0.1.0-SNAPSHOT.jar docker-scripts\lib
```
2. build docker image

```bash
cd docker-scripts
docker build -t akka-http-query-executor .
```

3. run the docker with the db configuration
```bash
docker run -e DB_IP="jdbc:postgresql://hh-pgsql-public.ebi.ac.uk:5432/pfmegrnargs" -e DB_USER="reader" -e DB_PASSWORD="NWDMCE5xdipIjRrp" -e DB_DRIVER="org.postgresql.Driver" -e LIMIT_SIZE=7 -p 1234:1234 akka-http-query-executor:latest
```
## Usage
Invoke the service using Rest client or curl
```bash
curl -d "{\"query\":\"SELECT * FROM rnacen.rna ORDER BY upi ASC\",\"fileName\":\"/tmp/testdata.csv\"}" -H "Content-Type: application/json" http://localhost:1234/execute-query
```
