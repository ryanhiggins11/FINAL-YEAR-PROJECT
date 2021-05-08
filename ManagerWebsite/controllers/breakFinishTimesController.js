const BreakFinishTime = require('../models/BreakFinishTimes');

/*
* Defining all methods and business logic for routes
*/

module.exports = {
	findAll: function(req, res) {
		BreakFinishTime.find(req.query)
			.then(breakFinishTimes => res.json(breakFinishTimes))
			.catch(err => res.status(422).json(err));
	},
	findById: function(req, res) {
		BreakFinishTime.findById(req.params.id)
			.then(breakFinishTime => res.json(breakFinishTime))
			.catch(err => res.status(422).json(err));
	},
	create: function(req, res) {
		BreakFinishTime.create(req.body)
			.then(breakFinishTime => res.json(breakFinishTime))
			.catch(err => res.status(422).json(err));
	},
	update: function(req, res) {
		BreakFinishTime.findOneAndUpdate({ _id: req.params.id }, req.body)
			.then(breakFinishTime => res.json(breakFinishTime))
			.catch(err => res.status(422).json(err));
	},
	remove: function(req, res) {
		BreakFinishTime.findById({ _id: req.params.id })
			.then(breakFinishTime => breakFinishTime.remove())
			.then(allbreakFinishTimes => res.json(allbreakFinishTimes))
			.catch(err => res.status(422).json(err));
	}
};