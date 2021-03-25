const mongoose = require('mongoose');
const URI = require('../config/index');

mongoose.connect(process.env.MONGODB_URI || URI);

// When successfully connected
mongoose.connection.on('connected', () => {
	console.log('Established Connection To Biztech');
});

// When connection throws an error
mongoose.connection.on('error', err => {
	console.log('Biztech Default Connection Error : ' + err);
});
