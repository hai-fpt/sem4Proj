import React, { useState, useEffect } from 'react';
import CircularProgress from '@material-ui/core/CircularProgress';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';
import Paper from '@material-ui/core/Paper';
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import { fetchLeave } from 'enl-api/myleave/leaveTool';

const useStyles = makeStyles({
    paperLeaveCounter: {
      borderRadius: 24,
      height: '100%',
    },
    fakeCircular:{
        position: 'absolute',
        zIndex: 0,
        color: 'rgba(0, 0, 0, 0.15)',
    },
    leaveCounterLabel: {
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      flexDirection: 'column',
      rowGap:'10px',
      color: 'rgba(0, 0, 0, 0.75)',
      fontSize: 14,
      textTransform: 'uppercase',
      '& span':{
        color: '#03a9f4',
        fontSize: 32,
        lineHeight: '36px',
      }  
    },
    totalLeaveDaysChip: {
        background: 'linear-gradient(90deg,  rgba(0, 0, 0, 0.15) 50%, #03a9f4 50%)',
        width: 16,
        height: 16,
        borderRadius: '50%',
    },
    usedLeaveDaysChip: {
        backgroundColor: 'rgba(0, 0, 0, 0.15)',
        width: 16,
        height: 16,
        borderRadius: '50%',
    },
    selectedYear: {
        color: '#03a9f4',
        textDecoration: 'underline',
        fontSize: '16px'
    }

});

const LeaveCounter = (props) => {
    const classes = useStyles();
    const [selectedYear, setSelectedYear] = useState(props.year)
    const [leaveDayLeft, setLeaveDayLeft] = useState(0);
    const [displayDayLeftValue, setDisplayValue] = useState(0);
    const [totalLeaveDays, setTotalLeaveDays] = useState(12);
    const [delay, setDelay] = useState(800);
    const yearData = [props.year - 2, props.year - 1, props.year]

    const calculateProgress = () => {
        const progress = (displayDayLeftValue / totalLeaveDays) * 100;
        return progress;
    };

    useEffect(() => {
        const fetchLeaveData = async () => {
          if (props.baseApiUrl && props.id) {
            try {
              const response = await fetchLeave(props.baseApiUrl, 'days_off', props.id, selectedYear)
              setLeaveDayLeft(response.data.remainingDaysOff)
              setTotalLeaveDays(response.data.defaultDaysOff)
            } catch (error) {
              console.error('Failed to fetch leave data:', error);
            } 
          }
        };
        fetchLeaveData();
    }, [ props.id, selectedYear]);

    useEffect(() => {
        const delayTimeout = setTimeout(() => {
            const interval = setInterval(() => {
              setDisplayValue((prevValue) => {
                if (prevValue >= leaveDayLeft) {
                  clearInterval(interval);
                  return leaveDayLeft;
                }
                return prevValue + 1;
              });
            }, 50);
        
            return () => clearInterval(interval);
          }, delay);
        
        return () => clearTimeout(delayTimeout);
    }, [leaveDayLeft, selectedYear]);

    return (
        <Paper elevation={1} className={classes.paperLeaveCounter}>
            <Box px={2} pt={1} display={'flex'} justifyContent={'center'} gridColumnGap={8} overflow={'auto'}>
                {
                    yearData.map((item, index) => {
                        return (
                            <Button variant='text' 
                            key={index}
                            className={item === selectedYear ? classes.selectedYear : ''} 
                            onClick={() => {
                                setSelectedYear(item) 
                                setDelay(0)
                            }}
                            >
                                {item}
                            </Button>
                        )
                    })
                }
            </Box>
            <Box p={2} position={'relative'} display={'flex'} justifyContent={'center'} alignItems={'center'}>
                <CircularProgress 
                    //fake Circular for background
                    classes={{
                       root: classes.fakeCircular
                    }}
                    variant="determinate"
                    value={100}
                    size={200} // Adjust the size of the circular progress indicator
                    thickness={1} // Adjust the thickness of the circular progress indicator
                />
                <CircularProgress
                    variant="determinate"
                    value={calculateProgress()}
                    size={200} // Adjust the size of the circular progress indicator
                    thickness={1} // Adjust the thickness of the circular progress indicator
                />
                <Typography variant="h6" component="div" className={classes.leaveCounterLabel} style={{position: 'absolute'}}>
                    You have
                    <span>{displayDayLeftValue}</span>
                    day(s) off left.
                </Typography>
            </Box>
            <Box px={4} py={2} display={'grid'} gridGap={10}>
                <Box display={'flex'} justifyContent={'start'} alignItems={'center'} gridGap={'8px'}>
                    <div className={classes.totalLeaveDaysChip}></div>
                    <Typography>
                        Total day(s) off: &nbsp; {totalLeaveDays}
                    </Typography>
                </Box>
                <Box display={'flex'} justifyContent={'start'} alignItems={'center'} gridGap={'8px'}>
                    <div className={classes.usedLeaveDaysChip}></div>
                    <Typography>
                        Total used day(s): &nbsp; {totalLeaveDays - displayDayLeftValue}
                    </Typography>
                </Box>
            </Box>
        </Paper>
    );
};

export default LeaveCounter;
