import React, { useState, useEffect } from 'react';
import Box from '@material-ui/core/Box';
import Paper from '@material-ui/core/Paper';
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import HolidayItems from './item';
import { fetchHoliday } from 'enl-api/myleave/leaveTool';

const useStyles = makeStyles({
    paper: {
      borderRadius: 24,
      height: '100%',
    },
    selectedYear: {
        color: '#03a9f4',
        textDecoration: 'underline',
        fontSize: '16px'
    }

});

const Holidays = (props) => {
    const classes = useStyles();
    const [selectedYear, setSelectedYear] = useState(props.year)
    const yearData = [props.year - 2, props.year - 1, props.year]
    const [Holidays, setHolidays] = useState([])

    useEffect(() => {
        const fetchLeaveData = async () => {
          if (props.baseApiUrl) {
            try {
              const response = await fetchHoliday(props.baseApiUrl, selectedYear)
              setHolidays(response.data.content)
            } catch (error) {
              console.error('Failed to fetch leave data:', error);
            } 
          }
        };
        fetchLeaveData();
    }, [ selectedYear]);

    return (
        <Paper elevation={1} className={classes.paper}>
            <Box px={2} pt={1} display={'flex'} justifyContent={'center'} gridColumnGap={8} overflow={'auto'}>
                {
                    yearData.map((item) => {
                        return (
                            <Button variant='text' 
                            key={item}
                            className={item === selectedYear ? classes.selectedYear : ''} 
                            onClick={() => setSelectedYear(item)}
                            >
                                {item}
                            </Button>
                        )
                    })
                }
            </Box>
            <Box p={2} pr={'6px'}>
                <HolidayItems data={Holidays}/>
            </Box>
        </Paper>
    );
};

export default Holidays;
