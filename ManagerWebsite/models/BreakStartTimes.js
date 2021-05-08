const mongoose = require('mongoose');
const Schema = mongoose.Schema;

/*
* This will resemble a schema of what to expect from the user when adding information to our application.
*/
const breakStartTimeSchema = new Schema({
	name: {
		type: String,
		required: true
	},
	breakStartedTime: {
		type: String,
		required: true
	}
});

const BreakStartTime = mongoose.model('BreakStartTime', breakStartTimeSchema);

module.exports = BreakStartTime;