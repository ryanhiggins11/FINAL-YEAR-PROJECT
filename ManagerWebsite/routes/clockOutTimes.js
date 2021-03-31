const router = require('express').Router();
const clockOutTimesController = require("../controllers/clockOutTimesController");

router
	.route('/')
	.get(clockOutTimesController.findAll)
	.post(clockOutTimesController.create);

router
	.route('/:id')
	.get(clockOutTimesController.findById)
	.put(clockOutTimesController.update)
	.delete(clockOutTimesController.remove);

module.exports = router;
