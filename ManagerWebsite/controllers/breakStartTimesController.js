const BreakStartTime = require('../models/BreakStartTimes');

/*
* Defining all methods and business logic for routes
*/
module.exports = {
	findAll: function(req, res) {
		BreakStartTime.find(req.query)
			.then(breakStartTimes => res.json(breakStartTimes))
			.catch(err => res.status(422).json(err));
	},
	findById: function(req, res) {
		BreakStartTime.findById(req.params.id)
			.then(breakStartTime => res.json(breakStartTime))
			.catch(err => res.status(422).json(err));
	},
	create: function(req, res) {
		BreakStartTime.create(req.body)
			.then(breakStartTime => res.json(breakStartTime))
			.catch(err => res.status(422).json(err));
	},
	update: function(req, res) {
		BreakStartTime.findOneAndUpdate({ _id: req.params.id }, req.body)
			.then(breakStartTime => res.json(breakStartTime))
			.catch(err => res.status(422).json(err));
	},
	remove: function(req, res) {
		BreakStartTime.findById({ _id: req.params.id })
			.then(breakStartTime => breakStartTime.remove())
			.then(allbreakStartTimes => res.json(allbreakStartTimes))
			.catch(err => res.status(422).json(err));
	}
};