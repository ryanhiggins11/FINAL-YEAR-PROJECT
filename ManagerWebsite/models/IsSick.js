const mongoose = require('mongoose');
const Schema = mongoose.Schema;

/*
* This will resemble a schema of what to expect from the user when adding information to our application.
*/
const isSicksSchema = new Schema({
	name: {
		type: String,
		required: true
	},
	isEmployeeSick: {
		type: String,
		required: true
	}
});

const IsSicks = mongoose.model('IsSicks', isSicksSchema);

module.exports = IsSicks;