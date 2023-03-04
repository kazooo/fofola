import {makeStyles} from "@material-ui/core";

export const createFormStyles = () => {
    const styles = {
        form: {
            position: 'absolute',
            top: '50%',
            left: '50%',
            transform: 'translate(-50%, -50%)',
            backgroundColor: 'white',
            border: '1px solid rgb(212, 101, 3)',
            borderRadius: '5px',
            boxShadow: 24,
            padding: 4,
            width: '30%',
        },
        formBox: {
            margin: '20px',
        },
        fieldWrap: {
            width: '100%',
        },
        field: {
            width: '100%',
        },
    };
    return makeStyles(styles)();
}

export const containerRowStyle = {
    container: true,
    alignItems: 'center',
    direction: 'row',
    spacing: 2,
}

export const containerColumnStyle = {
    container: true,
    alignItems: 'center',
    direction: 'column',
    spacing: 2,
}
