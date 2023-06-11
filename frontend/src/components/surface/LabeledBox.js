import {useTranslation} from 'react-i18next';
import {Box, makeStyles} from '@material-ui/core';

export const LabeledBox = ({
    label,
    boxStyle,
    labelStyle,
    contentStyle,
    children,
}) => {
    const {t} = useTranslation();
    const classes = createStyle(boxStyle, labelStyle, contentStyle)();

    return (
        <Box className={classes.box}>
            <Box className={classes.label}>
                {t(label)}
            </Box>
            <Box className={classes.content}>
                {children}
            </Box>
        </Box>
    )
};

const createStyle = (boxStyle, labelStyle, contentStyle) => makeStyles((theme) => ({
    box: {
        position: 'relative',
        borderColor: boxStyle?.borderColor || 'rgb(212, 101, 3)',
        borderStyle: boxStyle?.borderStyle || 'solid',
        borderWidth: boxStyle?.borderWidth || '1px',
        borderRadius: boxStyle?.borderRadius || '5px',
        width: boxStyle?.width || 'auto',
        height: boxStyle?.height || 'auto',
        padding: boxStyle?.padding || '10px',
        backgroundColor: boxStyle?.backgroundColor || theme.palette.background.paper,
    },
    label: {
        position: 'absolute',
        top: '-0.6rem',
        left: '0.8rem',
        padding: '0 0.5rem',
        fontSize: labelStyle?.fontSize || '0.8rem',
        color: labelStyle?.color || 'rgb(212, 101, 3)',
        backgroundColor: boxStyle?.backgroundColor || theme.palette.background.paper,
    },
    content: {
        width: contentStyle?.width || '100%',
        height: contentStyle?.height || '100%',
        alignItems: contentStyle?.alignItems || 'center',
        justifyContent: contentStyle?.justifyContent || 'center',
        alignContent: contentStyle?.alignContent || 'center',
        textAlign: contentStyle?.textAlign || 'center',
        overflow: contentStyle?.overflow || 'auto',
    },
}));
