const mongoose = require("mongoose");
const Schema = mongoose.Schema;

/*
* This will resemble a schema of what to expect from the user when adding information to our application.
*/
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