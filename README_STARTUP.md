# Project Startup Guide

## Prerequisites
To run this project, you must have the following services installed and running:

1.  **Redis** (v5.0+)
    *   Port: 6379
    *   Host: 127.0.0.1 (Localhost)
    *   Password: (Empty by default, configure in `application.properties` if needed)
2.  **MySQL** (v5.7 or v8.0)
    *   Port: 3306
    *   Host: 127.0.0.1 (Localhost)
    *   Database Name: `xiaomeng`
    *   Username: `root`
    *   Password: `123456` (Configure in `application.properties`)

## Database Initialization
The project requires a database schema. Since no SQL initialization script was found in the codebase, you must:
1.  Create a database named `xiaomeng`.
2.  Import your existing SQL backup or schema into this database.
3.  If you have migration scripts, place them in `src/main/resources/db/migration`.

## Configuration
The main configuration file is located at `src/main/resources/db/application.properties`.
Key settings have been updated with default local development values:
- `spring.profiles.active=dev`
- `spring.redis.host=127.0.0.1`
- `spring.datasource.url=jdbc:mysql://127.0.0.1:3306/xiaomeng...`

## Starting the Application
1.  Build the project:
    ```bash
    mvn clean package -DskipTests
    ```
2.  Run the JAR:
    ```bash
    java -jar target/xiaomeng-1.0-SNAPSHOT.jar
    ```

## Troubleshooting
- **Redis Connection Refused**: Ensure Redis is running locally on port 6379.
- **Database Connection Refused**: Ensure MySQL is running and the credentials in `application.properties` are correct.
- **Table doesn't exist**: Import the database schema.
