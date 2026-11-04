# GameFlix - Login and Registration Endpoints



## How To Run:
### Prerequisites
- Java 17 or compatible version.
- MySQL Server and MySQL Workbench (or another MySQL client).
- Maven, or the Maven wrapper included with the project.
### Setting Up + Configuring
- Start MySQL and create the database by running `CREATE DATABASE demo30;`. This application expects a database named `demo30`. If you use a different name, update it in the configuration.
- The default username in my development configuration is `root`, however, if you are using a different MySQL account, you will have to update the configuration in `application.properties`.
- Set the Database password using `application.properties.example` as a guide. Set `DB_PASSWORD` to your own MySQL password.
- Generate a random and secure key and set it as the `JWT_SECRET` environment variable.
- Run the Main class of the application in IntelliJ IDEA. Make sure MySQL is running, and that all the configurations are set up before this step.
### Testing the Endpoints
- This application runs on `http://localhost:8080` by default, so this will be included below. You can use Postman to test the endpoints:
  - POST `http://localhost:8080/api/auth/register` - This creates a new user.
  - POST `http://localhost:8080/api/auth/login` - This checks login details.
  - GET `http://localhost:8080/api/users/me` - This returns the logged-in user's profile when there is a valid JWT.

## Information:
Each requirement in the assigment description has been listed and given its own bullet, where I've explained how I've fulfilled the requirement.

### Persistence Requirements
- The users are persisted in MySQL.
- I made sure the password column is sized to be 255 characters to accommodate for the password hash.
- I added a unique constraint to the email column to prevent emails being used in multiple registrations.
- In the .gitignore file, I've added `application-local.properties`. I also created `application.properties.example` to show the key as a placeholder value (not the actual key).
- My setting for `spring.jpa.hibernate.ddl-auto` is `update` because it updates the database schema automatically during development.
- Output of SHOW CREATE TABLE users;
  - 'CREATE TABLE `users` (
    - `id` bigint NOT NULL AUTO_INCREMENT,
    - `created_at` datetime(6) NOT NULL,
    - `email` varchar(255) NOT NULL,
    - `password` varchar(255) NOT NULL,
    - `username` varchar(100) NOT NULL,
    - PRIMARY KEY (`id`),
    - UNIQUE KEY `uk_users_email` (`email`)

    ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci'

### Security Requirements
- I used bcrypt for password hashing. This is because it is relatively easy to implement, and straightforward. I set the work factor for this to 12; this is a little higher than the default (10) and it provides stronger protection, and though it sacrifices some performance, it is still reasonable. Raising it any higher would make the registration slower and more frustrating, and lowering it would sacrifice the security.
- The JWT is set to expire after 30 minutes (1800 seconds). I chose this because I felt it was long enough that people would not get frustrated at getting logged out after being inactive for just a short period of time, yet not so long that the security is greatly compromised.
- The key for encrypting passwords is not hardcoded into any source files. 
- Failed logins give the generic message "Invalid email or password." This is so that hackers will not be able to determine which email addresses are registered.
- CORS is configured only for the origin `http://localhost:3000` not for *. I also restricted the allowed HTTPS methods and headers.
- I made sure there was no confidental information in the token payload.

### Use of AI
AI coding tools were used to implement and troubleshoot. Alongside tutorials online, I used them to generate code, which I then altered and annotated with comments.
I extensively tested the code to make sure the functionalities were properly fulfilled, and that the password hashing was correctly done.

Some things I changed from the initial AI generated code:
- I set the work factor for BCrypt to 12. This is because I wanted a little bit more security than just the default (10), but I did not want to sacrifice application performance too much.
- The initial request I submitted for the code did not include the proper hashing and encoding methods. Because of this, I altered the code to no longer include this sensitive information.
- I adjusted the JWT expiration lifeline to be 30 minutes, instead of the shorter time initially given to me. This is because I wanted to allow users a little more time before they were forced to log back in, but didn't want to sacrifice safety too much.
- I adjusted password length limits to the code to be within a more precise range as the originally generated rage was too vague and allowed for passwords I felt that were much too short, or much too long. 
