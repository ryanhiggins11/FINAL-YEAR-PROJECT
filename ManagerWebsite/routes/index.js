const router = require('express').Router();

const clockInTimesRoutes = require('./clockInTimes');
const clockOutTimesRoutes = require('./clockOutTimes');
const breakStartTimesRoutes = require('./breakStartTimes');
const breakFinishTimesRoutes = require('./breakFinishTimes');
const userRoutes = require('./user');
const path = require('path');
router.use('/api/ClockInTimes', clockInTimesRoutes);
router.use('/api/ClockOutTimes', clockOutTimesRoutes);
router.use('/api/BreakStartTimes', breakStartTimesRoutes);
router.use('/api/BreakFinishTimes', breakFinishTimesRoutes);
router.use('/api/User', userRoutes);

// If no API routes are hit, send the React app
router.use(function(req, res) {
	res.sendFile(path.join(__dirname, '../client/build/index.html'));
});

module.exports = router;
