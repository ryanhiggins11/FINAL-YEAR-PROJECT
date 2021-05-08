const mongoose = require('mongoose');
const Schema = mongoose.Schema;

/*
* This will resemble a schema of what to expect from the user when adding information to our application.
*/
const breakTimeSchema = new Schema({
	name: {
		type: String,
		required: true
	},
	breakStartTime: {
		type: String,
		required: true
	},
	breakFinishTime: {
		type: String,
		required: true
	}
});

const BreakTime = mongoose.model('BreakTime', breakTimeSchema);

module.exports = BreakTime;