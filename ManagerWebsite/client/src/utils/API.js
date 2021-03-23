import axios from 'axios';

export default {
	// Gets all clock in times
	getClockInTimes: function() {
		return axios.get('/api/ClockInTimes');
	},
	// Gets the clock in time with the given id
	getClockInTime: function(id) {
		return axios.get('/api/ClockInTimes/' + id);
	},
	// Deletes the clock in time with the given id
	deleteClockInTime: function(id) {
		return axios.delete('/api/ClockInTimes/' + id);
	},
	// Saves a clock in time to the database (don't need)
	saveClockInTime: function(clockInTimeData) {
		return axios.post('/api/ClockInTimes', clockInTimeData);
	}
};
