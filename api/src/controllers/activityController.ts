import { Request, Response } from 'express';
import { now } from 'mongoose';

import Activity from '../models/activity';

class ActivityController {
  index = async(request: Request, response: Response) => {
    const { page = 1 } = request.query;
    const activities = await Activity.paginate({
      userId: response.locals.jwtPayload.id,
      deletedAt: null
    }, {
      page: Number(page),
      limit: 10,
      select: [
        'activity',
        'title',
        'distance',
        'elapsedTime',
        'speedAverage',
        'startDate',
        'endDate',
        'coordinateList',
        'resumeList',
        'speedList'
      ],
    });
    return response.json(activities);
  }

  store = async (request: Request, response: Response) => {
    const activity = await Activity.create({
      activity: request.body.activity,
      title: request.body.title,
      distance: request.body.distance,
      elapsedTime: request.body.elapsedTime,
      speedAverage: request.body.speedAverage,
      startDate: request.body.startDate,
      endDate: request.body.endDate,
      coordinateList: request.body.coordinateList,
      resumeList: request.body.resumeList,
      speedList: request.body.speedList,
      userId: response.locals.jwtPayload.id,
    });
    return response.json({ activity });
  }

  update = async (request: Request, response: Response) => {
    const activity = await Activity.findByIdAndUpdate(request.params.id, {
      activity: request.body.activity ? request.body.activity : undefined,
      title: request.body.title ? request.body.title : undefined,
      distance: request.body.distance ? request.body.distance : undefined,
      elapsedTime: request.body.elapsedTime ? request.body.elapsedTime : undefined,
      speedAverage: request.body.speedAverage ? request.body.speedAverage : undefined,
      startDate: request.body.startDate ? request.body.startDate : undefined,
      endDate: request.body.endDate ? request.body.endDate : undefined,
      coordinateList: request.body.coordinateList ? request.body.coordinateList : undefined,
      resumeList: request.body.resumeList ? request.body.resumeList : undefined,
      speedList: request.body.speedList ? request.body.speedList : undefined
    }, { new: true });
    return response.json(activity);
  }

  destroy = async(request: Request, response: Response) => {
    const activity = await Activity.findByIdAndUpdate(request.params.id, { deletedAt: now() }, { new: true });
    return response.json(activity);
  }
}

export default ActivityController;
