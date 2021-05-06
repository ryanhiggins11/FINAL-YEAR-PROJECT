const ClockOutTime = require('../models/ClockOutTimes');

/*
* Defining all methods and business logic for routes
*/

module.exports = {
	findAll: function(req, res) {
		ClockOutTime.find(req.query)
			.then(clockOutTimes => res.json(clockOutTimes))
			.catch(err => res.status(422).json(err));
	},
	findById: function(req, res) {
		ClockOutTime.findById(req.params.id)
			.then(clockOutTime => res.json(clockOutTime))
			.catch(err => res.status(422).json(err));
	},
	create: function(req, res) {
		ClockOutTime.create(req.body)
			.then(clockOutTime => res.json(clockOutTime))
			.catch(err => res.status(422).json(err));
	},
	update: function(req, res) {
		ClockOutTime.findOneAndUpdate({ _id: req.params.id }, req.body)
			.then(clockOutTime => res.json(clockOutTime))
			.catch(err => res.status(422).json(err));
	},
	remove: function(req, res) {
		ClockOutTime.findById({ _id: req.params.id })
			.then(clockOutTime => clockOutTime.remove())
			.then(allclockOutTimes => res.json(allclockOutTimes))
			.catch(err => res.status(422).json(err));
	}
};
