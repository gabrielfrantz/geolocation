import express from 'express';
import Middleware from '../middleware';

import ActivityController from '../controllers/activityController';
const activityController = new ActivityController();

const router = express.Router();

router.get('/activities', Middleware.validateToken, activityController.index);
router.post('/activities', Middleware.validateToken, activityController.store);
router.patch('/activities/:id', Middleware.validateToken, activityController.update);
router.delete('/activities/:id', Middleware.validateToken, activityController.destroy);

export { router as activityRouter };
