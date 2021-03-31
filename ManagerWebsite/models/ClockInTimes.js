const mongoose = require('mongoose');
const Schema = mongoose.Schema;

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
