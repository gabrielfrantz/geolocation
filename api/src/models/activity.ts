import mongoose from 'mongoose';
import mongoosePaginate from 'mongoose-paginate';

interface ICoordinateList extends mongoose.Document {
  latitude: string;
  longitude: string;
}

interface IResumeList extends mongoose.Document {
  startDate: string;
  endDate: string;
}

interface ISpeedList extends mongoose.Document {
  speed: string;
}

interface IActivity extends mongoose.Document {
  activity: string;
  title: string;
  distance: number;
  elapsedTime: string;
  speedAverage: string;
  startDate: string;
  endDate: string;
  coordinateList: Array<ICoordinateList>;
  resumeList: Array<IResumeList>;
  speedList: Array<ISpeedList>;
  userId: string;
}

const ActivitySchema = new mongoose.Schema({
  activity: {
    type: String,
    required: true,
  },
  title: {
    type: String,
    required: true,
  },
  distance: {
    type: Number,
  },
  elapsedTime: {
    type: String
  },
  speedAverage: {
    type: String
  },
  startDate: {
    type: String,
    required: true,
  },
  endDate: {
    type: String
  },
  coordinateList: {
    type: Array
  },
  resumeList: {
    type: Array
  },
  speedList: {
    type: Array
  },
  userId: {
    type: String,
    required: true
  },
  createdAt: {
    type: Date,
    default: Date.now(),
  },
  updatedAt: {
    type: Date,
    default: Date.now(),
  },
  deletedAt: {
    type: Date
  },
});

ActivitySchema.plugin(mongoosePaginate);

const Activity = mongoose.model<IActivity>('Activity', ActivitySchema);
export default Activity;
