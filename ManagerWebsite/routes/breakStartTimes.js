const router = require('express').Router();
const breakStartTimesController = require("../controllers/breakStartTimesController");

router
	.route('/')
	.get(breakStartTimesController.findAll)
	.post(breakStartTimesController.create);

router
	.route('/:id')
	.get(breakStartTimesController.findById)
	.put(breakStartTimesController.update)
	.delete(breakStartTimesController.remove);

module.exports = router;