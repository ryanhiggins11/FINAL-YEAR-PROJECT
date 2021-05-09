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
	// Gets all clock out times
	getClockOutTimes: function() {
		return axios.get('/api/ClockOutTimes');
	},
	// Gets the clock out time with the given id
	getClockOutTime: function(id) {
		return axios.get('/api/ClockOutTimes/' + id);
	},
	// Deletes the clock out time with the given id
	deleteClockOutTime: function(id) {
		return axios.delete('/api/ClockOutTimes/' + id);
	},
	// Saves a clock out time to the database (don't need)
	saveClockOutTime: function(clockOutTimeData) {
		return axios.post('/api/ClockOutTimes', clockOutTimeData);
	},
	// Gets all break times
	getBreakTimes: function() {
		return axios.get('/api/BreakTimes');
	},
	// Gets the break time with the given id
	getBreakTime: function(id) {
		return axios.get('/api/BreakTimes/' + id);
	},
	// Deletes the break time with the given id
	deleteBreakTime: function(id) {
		return axios.delete('/api/BreakTimes/' + id);
	},
	// Saves a break time to the database (don't need)
	saveBreakTime: function(breakTimeData) {
		return axios.post('/api/BreakTimes', breakTimeData);
	},
	// Gets all is sick documents
    getIsSicks: function() {
        return axios.get('/api/IsSick');
    },
    // Gets the is sick doc with given id
    getIsSick: function(id) {
        return axios.get('/api/IsSick/' + id);
    },
    // Deletes the is sick doc with the given id
    deleteIsSick: function(id) {
        return axios.delete('/api/IsSick/' + id);
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
