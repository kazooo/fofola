import React from 'react';
import {Tab, Tabs} from '@material-ui/core';

export const Options = ({children, classes, value, handleChange}) => {

    const a11yProps = (index) => ({
        id: `vertical-tab-${index}`, 'aria-controls': `vertical-tabpanel-${index}`
    });

    return <Tabs
        orientation='vertical'
        variant='scrollable'
        value={value}
        onChange={handleChange}
        className={classes.options}
        TabIndicatorProps={{style: {background:'rgb(212, 101, 3)'}}}
    >
        {
            children.map((ch, index) =>
                <Tab key={index} label={ch} {...a11yProps(index)} />
            )
        }
    </Tabs>
};
