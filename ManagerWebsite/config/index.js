const dbuser = 'user';
const dbpassword = 'user';

//const MONGODB_URI = `mongodb+srv://${dbuser}:${dbpassword}@cluster0.m26ej.mongodb.net/test?retryWrites=true&w=majority`;

// Connecting our Biztech manager website page to MongoDB
const MONGODB_URI = `mongodb+srv://${dbuser}:${dbpassword}@cluster0.eyke5.mongodb.net/tracker?retryWrites=true&w=majority`;

module.exports = MONGODB_URI;
