const BreakTime = require('../models/BreakTimes');

/*
* Defining all methods and business logic for routes
*/
module.exports = {
	findAll: function(req, res) {
		BreakTime.find(req.query)
			.then(breakTimes => res.json(breakTimes))
			.catch(err => res.status(422).json(err));
	},
	findById: function(req, res) {
		BreakTime.findById(req.params.id)
			.then(breakTime => res.json(breakTime))
			.catch(err => res.status(422).json(err));
	},
	create: function(req, res) {
		BreakTime.create(req.body)
			.then(breakTime => res.json(breakTime))
			.catch(err => res.status(422).json(err));
	},
	update: function(req, res) {
		BreakTime.findOneAndUpdate({ _id: req.params.id }, req.body)
			.then(breakTime => res.json(breakTime))
			.catch(err => res.status(422).json(err));
	},
	remove: function(req, res) {
		BreakTime.findById({ _id: req.params.id })
			.then(breakTime => breakTime.remove())
			.then(allbreakTimes => res.json(allbreakTimes))
			.catch(err => res.status(422).json(err));
	}
};