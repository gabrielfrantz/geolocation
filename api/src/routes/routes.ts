import express from 'express';

import { homeRouter } from './home';
import { authRouter } from './auth';
import { userRouter } from './user';
import { activityRouter } from './activity';

const routes = express.Router();
routes.use(homeRouter);
routes.use(authRouter);
routes.use(userRouter);
routes.use(activityRouter);

export default routes;
