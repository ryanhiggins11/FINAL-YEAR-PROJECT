const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const UserSchema = new Schema({
  name: {
    type: String,
    required: true
  },
  firstName: {
    type: String,
    required: true
  },
  secondName: {
    type: String,
    required: true
  },
  dateOfBirth: {
    type: String,
    required: true
  },
  emergencyContact: {
    type: String,
    required: true
  }
});

const User = mongoose.model('User', UserSchema);

module.exports = User;