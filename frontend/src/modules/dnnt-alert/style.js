import {makeStyles} from '@material-ui/core';

const mainColor = 'rgb(212, 101, 3)';

export const createDashboardStyle = makeStyles((theme) => (
    {
        root: {
            backgroundColor: theme.palette.background.paper,
            flexGrow: 1,
            display: 'flex',
            minHeight: '520px',
            borderRadius: '5px',
            border: '1px solid rgb(212, 101, 3)',
            borderColor: mainColor,
        },
        alert: {
            position: 'absolute',
            top: '50%',
            left: '50%',
            transform: 'translate(-50%, -50%)',
            backgroundColor: 'white',
            border: '1px solid',
            borderColor: mainColor,
            borderRadius: '5px',
            boxShadow: 24,
            padding: 4,
            width: '70%',
            maxHeight: '80%',
            overflow: 'auto',
        },
        alertBox: {
            margin: '20px',
            borderColor: mainColor,
        },
        footer: {
            display: 'flex',
            justifyContent: 'flex-end',
        }
    }
));
