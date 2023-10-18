import React from 'react';
import Typography from '@material-ui/core/Typography';
import {  TruncateTypography } from 'enl-components';
import Box from '@material-ui/core/Box';
import Paper from '@material-ui/core/Paper';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles({
    box: {
        display: 'flex',
        justifyContent: 'start',
        alignItems:'start',
        flexDirection: 'column',
        rowGap: 12,
        overflowY: 'scroll',
        scrollSnapType: 'Y mandatory',
        maxHeight: 315,
        '&::-webkit-scrollbar': {
            width: 4,
        },
        '&::-webkit-scrollbar-thumb': {
            backgroundColor: 'transparent',
            borderRadius: '2px',
        },
        '&:hover::-webkit-scrollbar-thumb': {
            backgroundColor: 'rgba(0, 0, 0, 0.1)',
            borderRadius: '2px',
        },
    },
    item: {
      scrollSnapAlign: 'start',
      borderRadius: 12,
      width: 'calc(100% - 12px)',
      display: 'flex',
      flexDirection: 'column',
      rowGap: 8,
      alignItems: 'start',
      padding: 12,
      margin: '3px 0px 3px 3px',
      borderTop: '1px solid rgba(0, 0, 0, 0.05)'
    },
    itemHeader: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'baseline',
        width: '100%',
    },
    holidayName: {
        fontWeight: 600,
        color: '#03a9f4',
        lineHeight: '24px',
    },
    holidayDuration: {
        fontWeight: 500,
        fontSize: 12,
        color: 'rgba(0, 0, 0, 0.38)',
        lineHeight: '24px',
        fontStyle: 'italic'
    },
    selectedYear: {
        color: '#03a9f4',
        textDecoration: 'underline',
        fontSize: '16px'
    }

});

const HolidayItems = (props) => {
    const classes = useStyles();

    return (
        <Box className={classes.box} component={'div'}>
            {
                props.data.map((holiday, index) => {
                    return (
                        <Paper key={index} elevation={1} className={classes.item}>
                            <div className={classes.itemHeader}>
                                <Typography className={classes.holidayName}>
                                    {holiday.name}
                                </Typography>
                                <Typography className={classes.holidayDuration}>
                                    {holiday.fromDate}
                                    ~
                                    {holiday.toDate}
                                </Typography>
                            </div>
                            <TruncateTypography text={holiday.description !== '' ? holiday.description : 'No description...' } component={'p'}/>
                        </Paper>  
                    );
                })
            }
        </Box>
    );
};

export default HolidayItems;
