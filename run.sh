mvn clean package -DskipTests
docker build .  --tag=springio/simple-elastic-project:latest --rm=true
docker-compose up 
