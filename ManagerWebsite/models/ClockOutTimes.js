const mongoose = require('mongoose');
const Schema = mongoose.Schema;

/*
* This will resemble a schema of what to expect from the user when adding information to our application.
*/
const clockOutTimeSchema = new Schema({
	name: {
		type: String,
		required: true
	},
	clockedOutTime: {
		type: String,
		required: true
	}
});

const ClockOutTime = mongoose.model('ClockOutTime', clockOutTimeSchema);

module.exports = ClockOutTime;
