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
	},
	// Gets all clock in times
	getClockOutTimes: function() {
		return axios.get('/api/ClockOutTimes');
	},
	// Gets the clock in time with the given id
	getClockOutTime: function(id) {
		return axios.get('/api/ClockOutTimes/' + id);
	},
	// Deletes the clock in time with the given id
	deleteClockOutTime: function(id) {
		return axios.delete('/api/ClockOutTimes/' + id);
	},
	// Saves a clock in time to the database (don't need)
	saveClockOutTime: function(clockOutTimeData) {
		return axios.post('/api/ClockOutTimes', clockOutTimeData);
	},
	// Gets all clock in times
	getBreakStartTimes: function() {
		return axios.get('/api/BreakStartTimes');
	},
	// Gets the clock in time with the given id
	getBreakStartTime: function(id) {
		return axios.get('/api/BreakStartTimes/' + id);
	},
	// Deletes the clock in time with the given id
	deleteBreakStartTime: function(id) {
		return axios.delete('/api/BreakStartTimes/' + id);
	},
	// Saves a clock in time to the database (don't need)
	saveBreakStartTime: function(breakStartTimeData) {
		return axios.post('/api/BreakStartTimes', breakStartTimeData);
	},
	// Gets all clock in times
	getBreakFinishTimes: function() {
		return axios.get('/api/BreakFinishTimes');
	},
	// Gets the clock in time with the given id
	getBreakFinishTime: function(id) {
		return axios.get('/api/BreakFinishTimes/' + id);
	},
	// Deletes the clock in time with the given id
	deleteBreakFinishTime: function(id) {
		return axios.delete('/api/BreakFinishTimes/' + id);
	},
		// Saves a clock in time to the database (don't need)
	saveBreakFinishTime: function(breakFinishTimeData) {
		return axios.post('/api/BreakFinishTimes', breakFinishTimeData);
	},
	
	

	// Gets all users
	getUsers: function() {
		return axios.get('/api/User');
	},
	// Gets the user with the given id
	getUser: function(id) {
		return axios.get('/api/User/' + id);
	},
	// Deletes the user with the given id
	deleteUser: function(id) {
		return axios.delete('/api/User/' + id);
	},
	// Saves a user to the database
	saveUser: function(userData) {
		return axios.post('/api/User', userData);
	}
};
