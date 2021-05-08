const mongoose = require('mongoose');
const Schema = mongoose.Schema;

/*
* This will resemble a schema of what to expect from the user when adding information to our application.
*/
const breakFinishTimeSchema = new Schema({
	name: {
		type: String,
		required: true
	},
	breakFinishedTime: {
		type: String,
		required: true
	}
});

const BreakFinishTime = mongoose.model('BreakFinishTime', breakFinishTimeSchema);

module.exports = BreakFinishTime;