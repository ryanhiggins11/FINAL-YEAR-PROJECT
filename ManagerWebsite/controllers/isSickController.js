const IsSick = require('../models/IsSick');

/*
* Defining all methods and business logic for routes
*/
module.exports = {
    findAll: function(req, res) {
        IsSick.find(req.query)
            .then(isSick => res.json(isSick))
            .catch(err => res.status(422).json(err));
    },
    findById: function(req, res) {
        IsSick.findById(req.params.id)
            .then(isSick => res.json(isSick))
            .catch(err => res.status(422).json(err));
    },
    create: function(req, res) {
        IsSick.create(req.body)
            .then(isSick => res.json(isSick))
            .catch(err => res.status(422).json(err));
    },
    update: function(req, res) {
        IsSick.findOneAndUpdate({ _id: req.params.id }, req.body)
            .then(isSick => res.json(isSick))
            .catch(err => res.status(422).json(err));
    },
    remove: function(req, res) {
        IsSick.findById({ _id: req.params.id })
            .then(isSick => isSick.remove())
            .then(allisSicks => res.json(allisSicks))
            .catch(err => res.status(422).json(err));
    }
};