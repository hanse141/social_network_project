# Project 5, Option 3: Messaging application with Login & registering interfaces.
## By: Marina Beshay, Xipeng (Alex) Wang, Andrew Sovik, Derek Hansen, Haoxi Wu.

## Instructions to compile:
### 1. Via terminal, use "javac Server.java" and then do "javac Client.java"
### 2. To run the program, run the Server.java first in one window, then run the Client.java multiple times then it'll open up different GUI windows automatically.

## Submissions:
### 1. Derek Hansen submitted the report on Brightspace.
### 2. Marina Beshay submitted the Vocareum workspace.


## Client.java:
### Contains GUI and data of open Conversations. Also sends messages to the server.

## Server.java:
### Contains data of connected users, receives messages, sends open conversation to client.
### Also contains switch cases for the commands that user chooses.(i.e. create new message & conversations, hide conversations, delete/edit messages, creating new users, login users, close users, and delete Users.

## ClientHandler.java:
### This is a thread class that is used in the server and runs the different commands used.

## ServerConnection.java:
### This is a thread class that is used in the client and tests to see if gui data will be sent if the "gd" command is called.

## User.java:
### This is an object class of the user. It contains username, password, open chat, and an array list of conversations, along with their getters and setters.

## Message.java:
### This is an object class of the messages.This class has the sender, receiver, time stamp, and content fields with the appropriate getters and setters. This class also contains the edit message method, which can edit the message if User chooses to and updates the time stamp.

## Conversation.java:
### This class is an object class of the conversations and it implements the comparable conversations interface which is used in the compareTo method when displaying the messages based on the order of time stamps. This class also stores the conversations in a file.

## Gui.java:
### This class extends JComponent and implements Runnable. It contains the bulk of the GUI for the messages and implements the buttons with their appropriate functions.

## Login.java:
### This class extends JComponent and implements Runnable. It implements the Login and Register GUI for this project, with appropriate methods to ensure verification.

## TESTS PERFORMED:
### RunLocalTest.java provides several methods that can test the methods of each class. As stated in CampusWire, the previous test cases used in the assigments were used as a source to implement the test cases.

## GUI TESTING:
### To test the GUI, it is best to run the server and client (run the client multiple times) then use the given logins.txt file to enter login information. To register, just go to the appropriate button to register then enter the information. The sign up information should be added to the logins.txt file. When logged in, the message GUI should appear and users can send messages and they can see the messages concurrently. There will be a delete and edit button for each program that users can use to edit or delete their own messages. There is also a settings button that users can use to delete their account or change their password.
