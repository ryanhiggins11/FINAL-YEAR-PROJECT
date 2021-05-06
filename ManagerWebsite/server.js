const express = require('express');
const app = express();

const routes = require('./routes');

const PORT = process.env.PORT || 5000;

// require db connection
require('./models');


//var wwwhisper = require('connect-wwwhisper');
// app holds a reference to express or connect framework, it
// may be named differently in your source file.
//app.use(wwwhisper());

// Alternatively, if you don't want wwwhisper to insert
// a logout iframe into HTML responses use.
//app.use(wwwhisper(false));

// configure body parser for AJAX requests
app.use(express.urlencoded({ extended: true }));
app.use(express.json());

// add this line
app.use(express.static('client/build'));

app.use(routes);

// Bootstrap server
app.listen(PORT, () => {
	console.log(`Server listening on port ${PORT}.`);
});
