const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const clockInTimeSchema = new Schema({
	employee: {
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
