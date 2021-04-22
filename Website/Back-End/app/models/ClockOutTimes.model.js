const mongoose = require('mongoose');

const ClockOutTimes = mongoose.model(
    "ClockOutTime",
    new mongoose.Schema({
        name: String,
        clockedOutTime: String
      })
    );


module.exports = ClockOutTimes;
