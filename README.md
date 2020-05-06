# MS3_Application

INTRODUCTION

MS3_Application is a java application, which takes a CSV file, parse the data from CSV to Java, 
then send the lines with valid data into the SQLite database, invalid data goes to a new CSV file (name of the file should match with the name of CSV file, but ends with -bad.csv). 
At the end of each running statistics of valid, invalid, and total data is generated in a log file(name of the file should match with the name of CSV file).
The application can take multiple CSV files and do the same steps for all of them at the same time. 


STEPS FOR GETTING APPLICATION RUN

1. Clone this repo to your local machine using https://github.com/CurlyG/MS3_App.git
2. Download DB browser for SQLite from https://sqlitebrowser.org/, if there isn't any on your device. It will help to see the created database with valid data.
3. Follow the path src/main/java/Code/MS3App.java, in the MS3App java class, you can see the main method, where you can run the application.
4. In the resources/InputFiles you can see the files created after running - ms3Interview - Jr Challenge 2.db, ms3Interview - Jr Challenge 2-bad.csv and ms3Interview - Jr Challenge 2.log.


OVERVIEW OF THE APPROACH, DESIGN CHOICES AND ASSUMPTIONS

APPROACH

My approach was to create an application that will be easy to read, with reusable functions. 
Even though I clarify further that requirement was to make the run with only one file, but before asking I already make it the way, that multiple files can go with the same steps and decided to keep it.

Each time when the application is run, it goes to configuration.properties take a path of "InputFiles", finds the CSV file with the help of method listNamesOfAllFiles() method, 
process the CSV file, which means parsing CSV file, verify if the data is valid, adds it to List, if not to create a -bad.csv file and add them into it, creates a .log file and add statistics there.
In the end, creates a new table in the database and add the list of data to the database.


DESIGN

The application contains two packages:

Code - The place where the main method is created and java application can be run.
Exceptions are handled with try/catch blocks and I use try-resources, so no need to flush, close or disconnect at the end. 

"Utilities" - In the "Utilities" package usually reusable functions stored. My project was quite small but I decided to create the design in this way so in the future it can be extendable. :)
In "Utilities" I have:

"Configuration.Reader" - with the help of this class I can access and read data by key name which I store in configuration.properties.

"DBUtils" - the methods which connects to database, create a new table, execute SQL query and insert data.

"OtherUtils" - I store methods which take my file name and create other files based on its name, where my statistics and invalid data is stored. 
In addition processCSV() method which parses valid data from CSV into java, storing it into a multidimensional list.
"OtherUtils" extends "DBUtils", by that inheriting "insert() and createNewTable() methods.

"MS3_App" - runs the application, by creating an instance of a class and calling instance methods from "OtherUtils", which are accessible since "OtherUtils" class is extended by "MS3_App".


ASSUMPTIONS

In "ListNamesOfAllFiles I assume that the original CSV file cannot contain "-bad" in case we have different files with different names.


P.S.
I would like to thank you for the assignment, I really gain some new skills. Even though it takes time, I enjoy the process very much!!!
In case I miss some details feel free to contact me I will be happy to discuss it.




