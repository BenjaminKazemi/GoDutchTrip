Set Up Play! Framework
----------------------
   Download [Play! 1.2.4](http://download.playframework.org/releases/play-1.2.4.zip).

   Unzip it into a directory and you are done! For simplicity, add the path of Play! directory to your DOS 
   path environment variable (on Windows machines) so that you can run Play! commands easily.

> For running Play! commands you had better go to the project directory and then executes those command.

Set Up Database
---------------
   We use MySQL to develope this application, you can simply look at the application.conf file to get 
   how the database is set up. For simplicity, the most important db configurations are shown here:
   
    db.url=jdbc:mysql://localhost:3306/godutchtrip
    db.driver=com.mysql.jdbc.Driver
    db.user=godutchtrip
    db.pass=hGMyVZSC5xdTSQy8
    jpa.ddl=update

> If you follow the SQL scripts instruction, you will be able to easily set up your database.
> The only thing you need is to run your MySQL service and follow the instructions.

   Run SQL scripts in the following order:
 
    * db-user-setup.sql
     1. Sets up your database, called 'godutchtrip'
     2. Creates a user, named 'godutchtrip' with password 'hGMyVZSC5xdTSQy8'
     3. Grants the privileges to that user

    * schema.sql
     Creates the tables (you ommit this pat but you have to insert the data.sql after the app is run and 
     tables are created by the app automatically)

    * data.sql
     Inserts the app user info to the tables. username: 'admin', password:'admin', role:'admin'

Set Up Development Environemnt
------------------------------

   Go to the project directory and run the following Play! commands one by one to get the project ready 
   to run and be opened by IntelliJIDEA.

    > play dependencies
    > play idealize

   Then you will be able to open the project in IntelliJIDEA.
   Now, set a JDK for the project. If you do not set a JDK for the project it works just fine with play 
   commands but you will see many compile erros in IntelliJIDEA. In order to get rid of those compile 
   errors you need to set a JDK for the project.

Run The Application
-------------------

   Go to the app directory and execute the follwoing Play! command in order to run the application 

    > play run


Use The Application
-------------------
   Use the users admin/admin or guest/guest to see the home page and admin/admin to work with the admin page

    Go to http://localhost:9000/ to see the app home page
    Go to http://localhost:9000/admin to see the app admin page

