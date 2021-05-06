const mongoose = require('mongoose');
const Schema = mongoose.Schema;

/*
* This will resemble a schema of what to expect from the user when adding information to our application.
*/
const clockInTimeSchema = new Schema({
	name: {
		type: String,
		required: true
	},
	clockedInTime: {
		type: String,
		required: true
	}
});

const ClockInTime = mongoose.model('ClockInTime', clockInTimeSchema);

module.exports = ClockInTime;
