const router = require('express').Router();
const breakTimesController = require("../controllers/breakTimesController");

router
	.route('/')
	.get(breakTimesController.findAll)
	.post(breakTimesController.create);

router
	.route('/:id')
	.get(breakTimesController.findById)
	.put(breakTimesController.update)
	.delete(breakTimesController.remove);

module.exports = router;