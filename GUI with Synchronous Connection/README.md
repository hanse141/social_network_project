# PJ05

#Messaging Application

#-GUI - Marina/Derek
#--JFrame w/ several JPanels
#--Input messages with JTextFields; create a new Message object with each String inputed from the JTextField
#--All messages would be stored in a csv file
#--Messages contain Message sender, message receiver, timestamp, etc.; allocate each conversation accordingly
#---Group name would be the "to/message receiver" field

#-Store messages server-side

#-Error statements
#--Include a series of error codes; client-side would run under try-catch block, and if an error occurs, it would print an error message

#-Accounts -Alex/Xipeng
#--Sign up/login when you first open the application
#--Account can store username, password, and a list of openable conversations
#--Have some page within the program to edit accounts
#--Username must be unique; store all usernames in an ArrayList; check the username with that ArrayList to ensure that it is unique

#-Message Editing/Deletion - Andrew
#--Program only displays edit and deletion options for own message
#--Start with barebones approach (always show icons/buttons for edit and delete message); if time permits, add a way to hide messages
#--Should delete the message both client and server side, rather than just hiding it in the UI

#-Concurrency
#--Thread to handle message sending and receiving
#--Thread for account creation
#--Thread for checking message ownership (for editing and deletion)
#--When finding messages to export, have one thread for sent messages and another for received messages

#-Export messages through csv files
#--Similar to previous output assignments, but should be done with commas rather than spaces
#--Separate csv file for each user; contains every message sent to them or by them

#-See conversations in one place
#--All conversations should be tied with a username and stored server-side
#--Conversation selections (i.e. which ones are open) should be done client-side

#-Should be able to delete conversations from list
#--Conversation list can be stored as an ArrayList with a Message object
#--Message object would store participants, username, time, and message contents

#-Commands to set between client and server
#--Messages are toString of the object
#--"edit oldmessage newmessage" -> Edits existing message
#--new -> Add message to list
#--delete-> deletes message

#-Command Parser/Interpreter
#--Split command by spaces
#--Contains a switch statement based around the first word
#---Runs the method with the same name as the first word
#---default would do nothing

#Low Priority 
#-User notification
#-Custom messaging environments
#-Moderation
