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

Exploring these files will allow you to see all of the source code of our project. Next you should open android studio and choose to open an existing project. Here you should choose the project folder you have cloned and this will load the project in android studio. Now inside your android studio our source code and different files will be loacted on the right hand side of the screen and can be accessed an examined easily.

# RUNNING THE WEBSITE
Assuming you have the repository already cloned to your machine you will need to move into the "ManagerWebsite" folder. Next you will need to run some commands via the command line interface. 
### Commands inside "ManagerWebsite folder"
• "npm install" - Installs necessary packages.

• "npm start" - This will start the website.

• "cd client" should be done once the other commands are run. This will bring you into the client folder where we run the next commands.
### Commands inside "client folder"

• "npm install" - Installs necessary packages.

• "npm start server" - This will start the server needed for the website data.

Once you have these commands running and the server and websites are running navigate to the URL http://localhost:3000/ in your browser. You should now see the running website.
