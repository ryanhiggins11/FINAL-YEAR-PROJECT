const IsSicks = require('../models/IsSick');

/*
* Defining all methods and business logic for routes
*/
module.exports = {
	findAll: function(req, res) {
		IsSicks.find(req.query)
			.then(isSick => res.json(isSick))
			.catch(err => res.status(422).json(err));
	},
	findById: function(req, res) {
		IsSicks.findById(req.params.id)
			.then(isSicks => res.json(isSicks))
			.catch(err => res.status(422).json(err));
	},
	create: function(req, res) {
		IsSicks.create(req.body)
			.then(isSicks => res.json(isSicks))
			.catch(err => res.status(422).json(err));
	},
	update: function(req, res) {
		IsSicks.findOneAndUpdate({ _id: req.params.id }, req.body)
			.then(isSicks => res.json(isSicks))
			.catch(err => res.status(422).json(err));
	},
	remove: function(req, res) {
		IsSicks.findById({ _id: req.params.id })
			.then(isSicks => isSicks.remove())
			.then(allisSick => res.json(allisSick))
			.catch(err => res.status(422).json(err));
	}
};