Set Up Play! Framework
----------------------
   Download [Play! 1.2.5](http://download.playframework.org/releases/play-1.2.5.zip).

   Unzip it into a directory and you are done! For simplicity, add the path of Play! directory to your DOS 
   path environment variable (on Windows machines) so that you can run Play! commands easily.

> For running Play! commands you had better go to the project directory and then executes those command.

Set Up Database
---------------
   I am using H2, Play! embedded db (file system, check out applocation.config file and Play! documents 
   for more inforamtion.) So, there is no need to setup a db for development, just run the app. I am using
   mem db in test mode.
   
    Go to http://localhost:9000/@db to manage and manipulate the db
    > database url for the mem-db: jdbc:h2:mem:play;MODE=MYSQL;LOCK_MODE=0
    > database url for the filesystem-db (fs): jdbc:h2:C:\Benjamin\Projects\GoDutchTrip\db\h2\play;MODE=MYSQL

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
    > Play test (for running the tests)


Use The Application
-------------------
   Use the users admin/admin or guest/guest to see the home page and admin/admin to work with the admin page

    Go to http://localhost:9000/ to see the app home page
    Go to http://localhost:9000/@tests to see and run the test cases
