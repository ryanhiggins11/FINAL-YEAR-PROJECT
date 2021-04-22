const mongoose = require('mongoose');

const ClockInTimes = mongoose.model(
    "ClockInTime",
    new mongoose.Schema({
        name: String,
        clockedInTime: String
      })
    );


module.exports = ClockInTimes;


