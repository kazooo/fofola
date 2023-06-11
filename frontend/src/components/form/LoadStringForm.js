import {useState} from 'react';
import {Box, Grid, TextField} from '@material-ui/core';
import {IconButton} from 'components/button';

export const LoadStringForm = ({size, label, placeholder, button, submitFunc}) => {

    const [text, setText] = useState('');

    const handleOnClick = (e) => {
        e.preventDefault();
        if (text)
            submitFunc(text);
        setText('');
    }

    return <Box>
        <Grid container
              spacing={1}
              alignItems='center'
        >
            <Grid item>
                <TextField
                    id='outlined-basic'
                    label={label}
                    value={text}
                    variant='outlined'
                    placeholder={placeholder}
                    onChange={e => setText(e.target.value)}
                    size={size}
                    inputProps={{ maxLength: 100 }}
                />
            </Grid>
            <Grid item>
                <IconButton onClick={handleOnClick}>
                    {button}
                </IconButton>
            </Grid>
        </Grid>
    </Box>
};
