# Cassandra Docker Test Helper
A test helper package useful for testing Cassandra applications that may need to work with various versions of Cassandra. Enables testers to automatically spin up Docker instances of various versions of Cassandra which can then be tested against.

##
Status: In development.

## Building
mvn clean install

## Pre-Reqs for testing and usage
* A fairly powerful computer; spinning up various versions of Cassandra repeatedly can be expensive.
* Install Docker
* Add this line to your /etc/default/docker file:
DOCKER_OPTS="-H tcp://127.0.0.1:2375 -H unix:///var/run/docker.sock"
* Restart Docker
* Ensure you can run this: docker -H tcp://127.0.0.1:2375 version
* Build the docker test boxes: 
```
cd /src/test/resources/docker
./buildCassandraDockerInstances.sh
```

If something gets weird (memory, lag, etc), try restarting your docker service.

Your JUnit tests should be based on JUnit 4.11 or higher.

## Usage
Coming soon.
