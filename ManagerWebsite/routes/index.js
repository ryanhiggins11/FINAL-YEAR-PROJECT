const router = require('express').Router();

const clockInTimesRoutes = require('./clockInTimes');
const clockOutTimesRoutes = require('./clockOutTimes');
const userRoutes = require('./user');
const path = require('path');
router.use('/api/ClockInTimes', clockInTimesRoutes);
router.use('/api/ClockOutTimes', clockOutTimesRoutes);
router.use('/api/User', userRoutes);

// If no API routes are hit, send the React app
router.use(function(req, res) {
	res.sendFile(path.join(__dirname, '../client/build/index.html'));
});

module.exports = router;
