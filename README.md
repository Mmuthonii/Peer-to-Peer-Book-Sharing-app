# Peer-to-Peer-Book-Sharing-app
A user-friendly app that connects book lovers, enabling them to borrow, lend, or exchange books with their community. 

#Set Up
📚 Book App - Setup Instructions (Spring Boot, Maven, IntelliJ IDEA) 🚀

Follow these steps to set up and run the Book App in IntelliJ IDEA with Spring Boot and Maven.

1️⃣ Prerequisites

Make sure you have the following installed:

✅ Java JDK 17+✅ Maven (comes with IntelliJ)✅ IntelliJ IDEA✅ Git

2️⃣ Clone the Repository

Open a terminal and run:

git clone https://github.com/Mmuthonii/bookapp.git
cd bookapp

3️⃣ Open Project in IntelliJ IDEA

Open IntelliJ IDEA.

Click "Open" and select the bookapp folder.

If prompted, import the Maven project (ensure IntelliJ detects pom.xml).

4️⃣ Configure Maven (If Needed)

Go to File → Settings → Build, Execution, Deployment → Build Tools → Maven.

Set the Maven home directory to the bundled IntelliJ version or manually point it to your Maven installation.

5️⃣ Run the Application

Open the BookAppApplication.java file (src/main/java/com/example/bookapp).

Click the green Run button or use:

mvn spring-boot:run

The application should start on http://localhost:8080.

6️⃣ Verify API Endpoints (Optional)

Check if the backend is running by visiting:

👉 http://localhost:8080/actuator/health👉 http://localhost:8080/api/books (Example endpoint)

7️⃣ Database Configuration (If Needed)

The app might use an H2 Database (in-memory) or MySQL/PostgreSQL.

Configure database settings in application.properties or application.yml.

Example (src/main/resources/application.properties):

spring.datasource.url=jdbc:mysql://localhost:3306/bookapp
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update

8️⃣ Build the Project (Optional)

If you want to create a JAR file:

mvn clean package

The JAR file will be in the target/ directory.

✅ You're All Set!
