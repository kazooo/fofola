import {Box, Grid, makeStyles, Typography} from '@material-ui/core';

const useStyles = makeStyles((theme) => ({
    box: {
        marginBottom: '30px',
    },
    content: {
        backgroundColor: 'rgb(255, 255, 255, 0.5)',
        borderRadius: '5px',
        border: '1px solid black',
        padding: '20px',
        justifyContent: 'center',
    }
}));

export const FeatureInfoBox = ({title, description}) => {

    const classes = useStyles();

    return (
        <Box className={classes.box}>
            <Grid container justifyContent='center'>
                <Grid item xs={6}>
                    <Box className={classes.content}>
                        <Typography variant='h6' align='center' gutterBottom>
                            {title}
                        </Typography>
                        {description}
                    </Box>
                </Grid>
            </Grid>
        </Box>
    );
};
