const mongoose = require('mongoose');
const Schema = mongoose.Schema;

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
