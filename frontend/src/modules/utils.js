import cronstrue from 'cronstrue';
import {isMoment} from 'moment';

export const isValidCron = (cronExpression) => {
    try {
        cronstrue.toString(cronExpression);
        return true;
    } catch (error) {
        return false;
    }
};

export const asStartOfDay = (date) => date && isMoment(date) && date.startOf('day');

export const asEndOfDay = (date) => date && isMoment(date) && date.endOf('day');

export const formatAsDateTime = (date) => date && isMoment(date) && date.format('YYYY-MM-DD HH:mm:ss.SSS');

export const convertMinutesToMilliseconds = (minutes) => minutes * 60 * 1000;

export const isNullOrUndefined = (value) => value === null || value === undefined;
