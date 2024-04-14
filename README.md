# RateGame
RateGame is a backend application that allows it's users to browse and review over 270,000 games!
The game database is updated every hour making sure that the user will have every game they could imagine.
Users can review and rate games, they can like other users reviews, browse reviews for every game and sort them by the amount of likes they got.
In case a user finds a review vulgar or Inappropriate they can report them. Application admin can print a raport of all reported reviews sorted by report amount and decide what to do with them and the authour.

 

## Functionalities
  - User registration with confirmation email
  - User login with Spring-Security
  - Auto updating game database with over 270,000 games
  - Review and rate games
  - Browse reviews for games
  - Game browser
  - Review reporting system
  - User ban system


## Tech 
Code: <br>
![Static Badge](https://img.shields.io/badge/java_17-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![Static Badge](https://img.shields.io/badge/Spring_Boot_3-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Static Badge](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
<br>

Tests: <br>
![image](https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![image](https://img.shields.io/badge/Mockito-78A641?style=for-the-badge)
<br>

Other: <br>
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![image](https://img.shields.io/badge/maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)


## Installation and configuration
### Requirements:
  - Docker


### Enviroment variables:
  - MYSQL_ROOT_PASSWORD -> sets password for database
  - MYSQL_DATABASE -> creates database with provided name
  - DATA_BASE_PASSWORD -> gives the password to database to the main app
  - DATA_BASE_USERNAME -> gives the username to database to the main app
  - SMTP_PASSWORD -> secret key to smtp server
  - SMTP_HOST -> smtp host
  - SMTP_PORT -> smtp port
  - SMTP_USERNAME -> smtp username
  - TWITCHID -> twitch id
  - TWITCHSECRET -> twitch secret key
  - LIMIT_GAME_IMPORT_TESTING -> if "TRUE" will limit the import of games to 500/hour, otherwise it will download all 270,000 at once which can take up few minutes
  - MYSQL_HOST -> name of mysql host
  - MYSQL_PORT -> port of mysql service
  - MYSQL_DB_NAME -> name of the database

To configure the application you can change these enviroment variables in docker-compose.yml. For testing purposes i reccomend leaving the 'LIMIT_GAME_IMPORT_TESTING' variable to 'TRUE'. In the compose file all of these are already filled up with default values. For the simplicity of testing i created dummy accounts for twitch and gmail smtp and left the credentials in the file so you don't have to create accounts. Hovewer in the case they stop working in the future (which is possible since they are free tier accounts, or something bad happens to them) you can fill them up with your own credientials

### Run the application
While in the project folder run the following command:

```
docker-compose up
```
Then wait for the images to be downloaded. By default the application runs on port 8070


## Endpoints


|       ENDPOINT                                | METHOD  | AUTHORITY |         REQUEST                         |       RESPONSE                      |                    FUNCTION                                         |
|:---------------------------------------------:|:-------:|:---------:|:---------------------------------------:|:-----------------------------------:|:-------------------------------------------------------------------:|
|     /registration                             |  POST   |    NONE   |  JSON BODY (registration request)       | String (Verification Status)        | Registers new accounts                                              |
| /registration/verifyEmailn                    |  GET    |    NONE   |  Request parameter (Verification Token) | String (Verification Status)        | Activates new accounts                                              |
| /users                                        |  GET    |    ADMIN  |                                         | JSON (List of all users)            | Returns list of all users                                           |
| /users/byId/{id}                              |  GET    |    ADMIN  |  Path variable (id)                     | JSON (user)                         | Returns user by id                                                  |
| /users/ban/{userId}/{duration}                |  GET    |    ADMIN  |  Path variable (id)/(duration(days))    | JSON (userBan)                      | Bans users for specified amount of days                             |
| /users/{id}                                   | DELETE  |    ADMIN  |  Path variable (id)                     | String (Deletion status)            | Deletes user by id                                                  |
| /users/byId/{id}                              |  GET    |    ADMIN  |  Path variable (id)                     | JSON (user)                         | Returns user by id                                                  |
| /users/tokens                                 |  GET    |    ADMIN  |                                         | JSON (VeryficationToken)            | Returns list of all veryfication tokens                             |
| /users/tokens/{id}                            |  GET    |    ADMIN  |  Path variable (id)                     | JSON (VeryficationToken)            | Returns verification token by id                                    |
| /games                                        |  POST   |    ADMIN  |  JSON BODY (game)                       | JSON (game)                         | Manually adds games                                                 |
| /games                                        |  GET    |    NONE   |                                         | JSON (list of all games)            | Returns list of all games                                           | 
| /games/byId/{id}                              |  GET    |    NONE   |  Path variable (id)                     | JSON (game)                         | Returns game by id                                                  | 
| /games/byKeyWord/{keyword}                    |  GET    |    NONE   |  Path variable (keyword)                | JSON (list of matching games)       | Returns list of matching games                                      | 
| /reviews                                      |  POST   |    USER   |  JSON BODY (reviewRequest)              | JSON (review)                       | Posts reviews for games                                             | 
| /reviews                                      |  GET    |    NONE   |                                         | JSON (List of all reviews)          | Returns list of all reviews                                         |
| /reviews/ByUserId/{id}                        |  GET    |    NONE   |  Path variable (id)                     | JSON (List of all matching reviews) | Returns list of all reviews made by user with specified id          |
| /reviews/ByGameId/{id}                        |  GET    |    NONE   |  Path variable (id)                     | JSON (List of all matching reviews) | Returns list of all reviews made for game with specified id         |
| /reviews /ByUserIdAndGameId/{userId}/{gameId} |  GET    |    NONE   |  Path variable (userid)/(gameid)        | JSON (List of all matching reviews) | Returns list of all reviews made for game by user with specified id |
| /reviews/averageGameRating/{id}               |  GET    |    NONE   |  Path variable (id)                     | Byte (avg game rating)              | Returns average rating for game with specified id                   |
| /reviews/deleteById/{id}                      |  DELETE |    ADMIN  |  Path variable (id)                     | String (Deletion status)            | Deletes review by id                                                |
| /reviewLikes                                  |  POST   |    USER   |  JSON BODY (reviewLike)                 | JSON (ReviewLike)                   | Post likes/dislikes for review                                      |
| /reviewLikes/likes/{id}                       |  GET    |    NONE   |  Path variable (id)                     | Int (Like count)                    | Returns count of likes for review with specified id                 |
| /reviewLikes/dislikes/{id}                    |  GET    |    NONE   |  Path variable (id)                     | Int (Dislike count)                 | Returns count of dislikes for review with specified id              |
| /reviews/ByGameIdAndOrderByLikes/{id}/{page}  |  GET    |    NONE   |  Path variable (gameId)/(page number)   | JSON (List of sorted and paged reviews) | Returns page of sorted by likes reviews                         |
| /covers/{gameId}                              |  GET    |    NONE   |  Path variable (id)                     | JSON (cover)                        | Returns cover for game with specified id                            |
| /reports                                      |  POST   |    USER   |  JSON BODY (ReviewReportRequest)        | JSON(Report of review)              | Post ReviewReport                                                   |
| /reports                                      |  GET    |    ADMIN  |                                         |JSON (List of all reports of reviews)| Returns list of all reporst of reviews                              |
| /reports/byId/{id}                            |  GET    |    ADMIN  |  Path variable (id)                     | JSON (ReviewReport)                 | Returns report of review by report id                               |
| /report/raport                                |  GET    |    ADMIN  |                                         | JSON (Raoprt)                       | Returns raport of reported reviews (sorted by the amountof reports  |



