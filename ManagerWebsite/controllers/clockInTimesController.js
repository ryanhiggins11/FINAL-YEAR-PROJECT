const ClockInTime = require('../models/ClockInTimes');

// Defining all methods and business logic for routes

module.exports = {
	findAll: function(req, res) {
		ClockInTime.find(req.query)
			.then(clockInTimes => res.json(clockInTimes))
			.catch(err => res.status(422).json(err));
	},
	findById: function(req, res) {
		ClockInTime.findById(req.params.id)
			.then(clockInTime => res.json(clockInTime))
			.catch(err => res.status(422).json(err));
	},
	create: function(req, res) {
		ClockInTime.create(req.body)
			.then(newBook => res.json(newBook))
			.catch(err => res.status(422).json(err));
	},
	update: function(req, res) {
		ClockInTime.findOneAndUpdate({ _id: req.params.id }, req.body)
			.then(book => res.json(book))
			.catch(err => res.status(422).json(err));
	},
	remove: function(req, res) {
		ClockInTime.findById({ _id: req.params.id })
			.then(book => book.remove())
			.then(allbooks => res.json(allbooks))
			.catch(err => res.status(422).json(err));
	}
};
