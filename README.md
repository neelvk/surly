# surly
This is a URL Shortener service.

## Requirements
- Scala
- SBT
- MySQL database

## To run
- Create new schema in MySQL server by using db/schema.sql
- Overwrite dummy configuration of db properties in application.conf
- Invoke 'sbt run'.  This will run the application on port 9000.
- To create a short URL, invoke: 
    `curl localhost:9000/v1/shorten -X POST -d '{"target":"foo.com"}'`
    This should give you a response like http://localhost:9000/1
- To retrieve a short URL, invoke:
    `curl localhost:9000/1 -v`
    This should give you a response of 307 Temporary Redirect with Location = foo.com
- If you are running in production, overwrite the base.url property in application.conf to somethig more useful.

## Bugs
- DB-based unit test is failing

## Future Enhancements
- Caching
- UI
