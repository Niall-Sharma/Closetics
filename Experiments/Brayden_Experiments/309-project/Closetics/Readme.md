# Spring Boot Example Using Local MySQL Database

I have a SQL Script that creates 6 different db tables 
the only one ive created API endpoints for is the user table.

These endpoints consist of a:

/addUser endpoint: This accepts a JSON body consisting of a username, password, 
first name, last name and auto creating a UUID and user creation date

/getUserById/{user_id} endpoint: This accepts a UUID and returns the corresponding data in JSON format

/getUser/{username} endpoint: This accepts a username and returns the corresponding data in JSON format

/updateUser endpoint: This accepts a JSON body containing a user_id, username, password,
first_name, and last_name field and updates the corresponding row with the new values provided

/deleteUser/{user_id}: This accepts a UUID and removes the corresponding row in the db