const router = require('express').Router();
const clockInTimesController = require("../controllers/clockInTimesController");

router
	.route('/')
	.get(clockInTimesController.findAll)
	.post(clockInTimesController.create);

router
	.route('/:id')
	.get(clockInTimesController.findById)
	.put(clockInTimesController.update)
	.delete(clockInTimesController.remove);

module.exports = router;
