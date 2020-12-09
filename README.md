# simple-java-database-elastic-springboot
This is a small example of using SQL database along with ElasticSearch.So SQL database can be used to store relations and other information while ElasticSearch can be used for 
searching data effectively.This project  shows how  to synchronize data in database to ElasticSearch in near realtime.

#### Prerequisite
- Maven
- JDK
- Docker
- Docker Compose
#### Steps
1. Clone the Project
2. Execute run.sh

#### Working
- Synchronizing data to ElasticSearch
  - The synchronizing data  to elastic search is done by three tasks
    1. Full Index 
       - It takes all non deleted items from the sql database to elastic search.It is normally executed one time when the application is deployed.
    2. Periodic Update
       - It moves all the documents that were updated after the last was Periodic Update executed.This helps in syncing changes made to data in database not through
the application.It is scheduled periodically.
    3. Instant Update
       - The Changes made to sql database usning application is immediately updated in the Elastic Search also.
- Executing jobs
  - Indexing documents to Elastic search is done by tasks.Initially these task requests are populated in database where application instances peridocally poll.The 
  instances will lock some tasks and execute it.
- Rest End point
  - The CRUD operations uses SQL Database while searching and highlighting of entity uses elastic search.
#### Examples
Application instances are exposed in port 8081 and 8082
  - Create 
`curl -X POST http://localhost:8081/suggestion -i -d '{"suggestion":"Test this" }' -H "Content-Type: application/json";`

  - Update
`curl -X PUT http://localhost:8081/suggestion/1 -i -d '{"suggestion":"Test this" }' -H "Content-Type: application/json";`

  - Retrieve
`curl -X GET http://localhost:8081/suggestion/1 -i -H "Content-Type: application/json";`

  - Delete
`curl -X DELETE http://localhost:8081/suggestion/1 -i -d '{"suggestion":"Test this" }' -H "Content-Type: application/json";`

  - Search
`curl -X POST http://localhost:8081/suggestion/_search -i -d '{ "query":{"operation":"search","search":"string to be searched","field":"suggestion" ,"highlight":true }}' -H "Content-Type: application/json";`
