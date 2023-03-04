import cronstrue from 'cronstrue';

export const isValidCron = (cronExpression) => {
    try {
        cronstrue.toString(cronExpression);
        return true;
    } catch (error) {
        return false;
    }
};

export const asStartOfDay = (date) => date && date.startOf('day');

export const asEndOfDay = (date) => date && date.endOf('day');

export const formatAsDateTime = (date) => date && date.format('YYYY-MM-DD HH:mm:ss.SSS');

export const convertMinutesToMilliseconds = (minutes) => minutes * 60 * 1000;
