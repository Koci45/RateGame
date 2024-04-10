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
  - TWITCHID -> twitch id
  - TWITCHSECRET -> twitch secret key
  - LIMIT_GAME_IMPORT_TESTING -> if "TRUE" will limit the import of games to 500/hour, otherwise it will download all 270,000 at once which can take up few minutes
  - MYSQL_HOST -> name of mysql host
  - MYSQL_PORT -> port of mysql service
  - MYSQL_DB_NAME -> name of the database

To configure the application you can change these enviroment variables in docker-compose.yml. For testing purposes i reccomend leaving the 'LIMIT_GAME_IMPORT_TESTING' variable to true. In the compose file all of these are already filled up with default values. For the simplicity of testing i left ZROB ZMIENNE DLA EMAIL TEZ!!!


