# **𝔹𝕠𝕠𝕜 𝕊𝕥𝕠𝕣𝕖**

1. LINK: http://ec2-3-88-178-6.compute-1.amazonaws.com/swagger-ui/index.html#/
2. GitHub: https://github.com/SSXcorp/Book-Store-Final

Introduction:

According to Eurostat, over 20% of Europeans buy books online, and this number keeps growing. In response, this project focuses on the backend development of a Java-based website for selling books online. It is designed to ensure high performance, strong security, and scalability, enabling the system to handle growing traffic and protect user data. The platform lays a solid foundation for a reliable and efficient online book shopping experience.

Eurostat collected data:
![img_1.png](img_1.png)

**Technology Stack:**
* Java (v17 JetBrains)
* Spring Framework (Web, Security, Validation, Data-jpa)
* MySQL, Liquibase
* Lombok, Mapstruct
* Maven
* JWT
* Junit, Mockito
* Swagger
* Docker
* AWS

![DiagramBackend.jpg](src/main/resources/images/DiagramBackend.jpg)

**Functionalities of the Controllers:**
* AuthenticationController manages user registration and login, enforcing validation and secure authentication through Spring Security.
* BookController offers advanced features like pageable search with filtering and sorting, CRUD operations for books, and access restrictions based on user roles.
* CategoryController supports full CRUD for book categories and allows fetching books by category, providing a modular, category-based navigation experience.
* OrderController enables users to manage their orders, including adding orders, retrieving user-specific order history, accessing individual order items, and allowing admins to update order status.
* ShoppingCartController implements a complete shopping cart system, letting users add, update, or remove items and ensuring each operation is secure and scoped to the authenticated user.

Here is the postman public link for your custom tests. Feel free to use it:
* PostmanLink : https://www.postman.com/starlight-ssx/bookstorepublic/collection/kqn67j5/bookstoreapp?action=share&creator=17715692

**All tests for each endpoint are sorted into folders with the basic parameters already set. Don't forget to set the authorization type to "Bearer Token" and specify the token of the logged in user.

Swagger should be accessible all the time (similar like on a screenshot):
![SwaggerScr.jpg](src/main/resources/images/SwaggerScr.jpg)

**I appreciate your time viewing my project. In case of ideas on how to improve the project or find bugs/errors, as well as suggestions to join other projects, you can contact me by mail:
bohdandimov94@gmail.com or GitHub: @SSXcorp**


