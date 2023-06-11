import {Tooltip, withStyles} from '@material-ui/core';

export const mainColor = 'rgb(212, 101, 3)';

export const StyledTooltip = withStyles({
    tooltip: {
        fontSize: '0.7em',
        backgroundColor: 'white',
        color: 'black',
        borderColor: mainColor,
        borderStyle: 'solid',
        borderWidth: '1px',
    }
})(Tooltip);
