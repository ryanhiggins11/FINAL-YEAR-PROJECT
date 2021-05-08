const router = require('express').Router();
const breakFinishTimesController = require("../controllers/breakFinishTimesController");

router
	.route('/')
	.get(breakFinishTimesController.findAll)
	.post(breakFinishTimesController.create);

router
	.route('/:id')
	.get(breakFinishTimesController.findById)
	.put(breakFinishTimesController.update)
	.delete(breakFinishTimesController.remove);

module.exports = router;