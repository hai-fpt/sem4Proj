import React, { useState, useEffect } from 'react';
import { MuiPickersUtilsProvider, DatePicker } from '@material-ui/pickers';
import DateFnsUtils from '@date-io/date-fns';
import { makeStyles } from '@material-ui/core/styles';
import { format } from 'date-fns';
import Box from '@material-ui/core/Box';
import Paper from '@material-ui/core/Paper';
import { fetchLeave } from 'enl-api/myleave/leaveTool';

const useStyles = makeStyles({
  today: {
    '& button': {
      color: '#03a9f4 !important',
      backgroundColor: 'transparent'
    },
    '& :hover':{
      backgroundColor: 'transparent !important'
    }
  },
  leaveDay: {
    '& button': {
      color: 'white !important',
      borderRadius: '50%',
      backgroundColor:'#03a9f4 !important',
    },
  },
  calendarBox: {
    height: '100%',
    '& div.MuiPickersStaticWrapper-staticWrapperRoot':{
      justifyContent: 'center',
      flexDirection: 'unset',
      height: '100%'
    },
    '& div.MuiPickersBasePicker-pickerView':{
      height: '100%',
    },
  },
  paperCalendar: {
    borderRadius: 24,
    height: '100%',
  }
});

const LeaveCalendar = (props) => {
  const classes = useStyles();
  const [leaveDates, setleaveDates] = useState([])
  const leaveDate = useState(null);

  useEffect(() => {
    const fetchLeaveData = async () => {

      if (props.baseApiUrl && props.id) {
        try {
          const response = await fetchLeave(props.baseApiUrl, 'calendar', props.id, props.year)
          setleaveDates(response.data.leaveRequests)
        } catch (error) {
          console.error('Failed to fetch leave data:', error);
        } 
      }
    };
    fetchLeaveData();
  }, [props.baseApiUrl, props.id]);

  const checkIsLeaveDate = (date) => {
    const formattedDate = format(date, 'yyyy-MM-dd');
    return leaveDates.includes(formattedDate)
  };

  const checkisToday = (date) => {
    const today = format(new Date(), 'dd-MM-yyyy');
    const formattedDate = format(date, 'dd-MM-yyyy');
    return today === formattedDate;
  }

  return (
    <Paper elevation={1} className={classes.paperCalendar}>
      <Box px={1} py={2} className={classes.calendarBox}>
        <MuiPickersUtilsProvider utils={DateFnsUtils}>
          <DatePicker
            disableToolbar
            variant='static'
            value={leaveDate}
            onChange={() => {return null;}}
            renderDay={(day, _, isInCurrentMonth, dayComponent) => {
              const isLeaveDay = checkIsLeaveDate(day) ? classes.leaveDay : ''
              const isToday = checkisToday(day) ? classes.today : ''

              if (!isInCurrentMonth) {
                // For days outside the current month
                return <>{dayComponent}</>
              }
              
              return (
                <div className={`${isLeaveDay} ${isToday}`}>
                  {dayComponent}
                </div>
              );
            }}
            classes={{ root: classes.calendar }}
          />
        </MuiPickersUtilsProvider>
      </Box>
    </Paper>
  );
};

export default LeaveCalendar;
