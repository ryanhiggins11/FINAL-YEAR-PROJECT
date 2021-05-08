const router = require('express').Router();
const isSickController = require("../controllers/isSickController");

router
	.route('/')
	.get(isSickController.findAll)
	.post(isSickController.create);

router
	.route('/:id')
	.get(isSickController.findById)
	.put(isSickController.update)
	.delete(isSickController.remove);

module.exports = router;