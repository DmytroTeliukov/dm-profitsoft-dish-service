### Running Application

#### Prerequisites
- Make sure you have Docker and Docker Compose installed on your machine.

#### Usage
To run the application, follow these steps:
1. Build Docker Compose:
````
docker-compose build
````
2. Run Docker Compose:
````
docker-compose run
````
3. Once the containers are up and running, you can access the Spring Boot application at http://localhost:9090.

#### Accessing PostgreSQL
- Host: localhost
- Port: 2000
- Username: dmytro
- Password: qwerty1234
- Database: restaurant_db

Also, you can change variables in env file.

#### Shutting Down
To stop the containers and remove the Docker network, run:
````
docker-compose down
````

### Domain Description
This project involves the development of a console script that parses a list of JSON files representing the main entity and generates statistics based on one of its attributes. The main entity, "Dish," has several attributes such as name, description, price, calories, weight, category, ingredients, cuisines, and dietary specifics. Additionally, there is a secondary entity, "Category," which relates to the main entity in a many-to-one relationship.

