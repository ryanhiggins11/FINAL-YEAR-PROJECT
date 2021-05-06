<p align="center">
  <img width="300" height="300"src="https://i.ibb.co/6B5HKyK/logo-generator-for-a-mobile-gaming-company-with-a-lock-clipart-885c-el1.png">
</p>

# FINAL YEAR PROJECT
This is the repository for our final year project called "Biztech". This application is developed by Daniel Gallagher, Ryan Higgins, Shane McCormack, and Jack McNamee. The project is an application developed in Kotlin to allowed employees of companies to clock into their workplace. We decided to design this to help stop the spread of covid-19 in the workplace and to also reduce wait times at clock in machines at start and ends of shifts.

# PROJECT OVERVIEW
The app we developed was a clock-in application to use for employees to clock in using their own device rather than a clock-in station. As we are all aware Covid-19 is a huge problem in not only in the workplace but in everyday life. The application was coded in android studio using Koitlin. The app uses MongoDB to store the details used for our application. The managers of the business have exclusive access to the manager functions both inside the application and are the only people who have access to the website to view people's information or look at clock-in and out times.
The manager website was created using react, mern, node, and mongoDB express.  We created this site as a handy way for managers to be able to see what staff have clocked in and out of the workplace and at what time they did. This feature is extremely useful and secure as we have used WWwhisper to ensure that employees and people who are not authorized cannot use the site. Wwwhisper is built into Heroku where our website is hosted and only allowed authenticated emails to access the site. Another feature we have added to the application is that users can now use Biometric Authentication to Login to the app. This means that instead of typing in the username or password all they have to do is use there fingerprint which is great for convenience but also as a safety feature. 

# EXPLORE THE CODE
Firstly you will need to clone this repository to your local machine. This can be done by clicking the link in the top of the repository , entering a folder you would like to clone it to and going into the command line. Then using the command "git clone (Repository Link)". This will clone all files onto your machine.

Exploring these files will allow you to see all of the source code of our project. Next you should open android studio and choose to open an existing project. Here you should choose the project folder you have cloned and this will load the project in android studio. Now inside your android studio our source code and different files will be loacted on the left hand side of the screen and can be accessed an examined easily.

# RUNNING THE WEBSITE
Assuming you have the repository already cloned to your machine you will need to move into the "ManagerWebsite" folder. Next you will need to run some commands via the command line interface. 
#### Commands inside "ManagerWebsite folder"
• "npm install" - Installs necessary packages.

• "npm start" - This will start the website.

• "cd client" should be done once the other commands are run. This will bring you into the client folder where we run the next commands.
#### Commands inside "client folder"

• "npm install" - Installs necessary packages.

• "npm start server" - This will start the server needed for the website data.

Once you have these commands running and the server and websites are running navigate to the URL http://localhost:3000/ in your browser. You should now see the running website.

# RUNNING THE APPLICATION

• Git clone this repository

• Open Android Studio and select open an existing project. Search for the folder created when cloning our repository and select it. Wait for Android Studio to finish building the gradle files.

• To sign in as an employee, use the following credentials to sign in
  
  -> Email: example@biztech.com 
  
  -> Password: example123

• To sign in as a manager, use the following credentials to sign in: 
    
  -> Email: admin@biztech.com 
  
  -> Password: admin123

# ACCESSING THE SITE
The website for our project can be accessed at https://biztech-management.herokuapp.com/. Unfortunately if your email is not added to our website as being allowed to view it you will not be able to see anything on the site. This is due to the security features we have added to the site in order to keep any sensitive employee information safe from being viewed bby anyone other than authorized accounts or management. The best way to see the site in action would be by looking at our screencast of our project which will be linked below.

# OUR REPOSITORY
Inside the repository there are a number of different folders. In this section we will break down what is contained in the repository and where different things can be found and give a simple breakdown. The main folders which are of interest are "Dissertation" , "ManagerWebsite" and "kotlin-app". Inside the dissertation folder there is our dissertation.pdf. This is the complete writeup and dissertation from our team regarding the project. Inside the "ManagerWebsite" folder we have the all files which are used for the website side of things for our project. Lastly inside the "kotlin-app" folder there is the files for our phone application which was developed in android studio. This folder contains all the source code for the phone application.

# USEFUL LINKS
Download android studio - https://developer.android.com/studio

Running the emulator - https://developer.android.com/studio/run/emulator
