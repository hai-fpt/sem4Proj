import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import DateFnsUtils from '@date-io/date-fns';
import { format } from 'date-fns';
import Typography from '@material-ui/core/Typography';
import {
  MuiPickersUtilsProvider,
  DateTimePicker,
} from '@material-ui/pickers';
import IconButton from '@material-ui/core/IconButton';
import CalendarTodayIcon from '@material-ui/icons/CalendarToday';

const useStyles = makeStyles({
    datePicker: {
        display: 'flex',
        flexDirection: 'column',
        rowGap: 8,
    },
    inputFromDate: {
       marginBottom: 5,
       width:'100%',
       height: 36.6
    },
    inputToDate: {
        marginBottom: 30,
        width:'100%',
        height: 36.6
     },
    requiredElement: {
        color: 'red',
    },
    typo: {
        paddingLeft: 8,
    }
});

const DateRangePicker = (props) => {
    const classes = useStyles();
    const [fromDate, setFromDate] = useState(new Date());
    const [toDate, setToDate] = useState(new Date());
    const { fromDateisRequired, toDateisRequired, valueFormProps, detailFormData} = props;
    
    const handleFromDateValueProps = (e) => {
        const formattedDate = format(e, 'MM-dd-yyyy hh:mm:ss')
        setFromDate(formattedDate)
        valueFormProps(formattedDate,'fromDate');
    };
      
    const handleToDateValueProps = (e) => {
        const formattedDate = format(e, 'MM-dd-yyyy hh:mm:ss')
        setToDate(formattedDate)
        valueFormProps(formattedDate,'toDate');
    };

    const checkFromDateDetailValueProp = () =>{
        if (!props.detailFormData || props.detailFormData === {}) {
            return ;
        }
        return props.detailFormData.fromDate;
    }

    const checkToDateDetailValueProp = () =>{
        if (!props.detailFormData || props.detailFormData === {}) {
            return ;
        }
        return props.detailFormData.toDate;
    }
    
    return (
        <MuiPickersUtilsProvider utils={DateFnsUtils}>
            <Grid container spacing={2}>
                <Grid item xs={12} md={6} xl={6} className={classes.datePicker}>
                    <Typography className={classes.typo}>
                        From date
                        {
                            fromDateisRequired && (
                                <span className={classes.requiredElement}>
                                    (*)
                                </span>
                            )
                        }
                    </Typography>
                    <DateTimePicker
                        className={classes.inputFromDate}
                        disableToolbar
                        variant="inline"
                        format="MM-dd-yyyy hh:mm:ss"
                        required={fromDateisRequired? true : false}
                        value={detailFormData ? checkFromDateDetailValueProp() : fromDate}
                        onChange={handleFromDateValueProps}
                        maxDate={detailFormData ? checkFromDateDetailValueProp() : toDate}
                        InputProps={{
                            endAdornment: (
                              <IconButton>
                                <CalendarTodayIcon />
                              </IconButton>
                            ),
                          }}
                    />         
                </Grid>
                <Grid item xs={12} md={6} xl={6} className={classes.datePicker}>
                    <Typography className={classes.typo}>
                        To date
                        {
                            toDateisRequired && (
                                <span className={classes.requiredElement}>
                                    (*)
                                </span>
                            )
                        }
                    </Typography>
                    <DateTimePicker
                        disableToolbar
                        className={classes.inputToDate}
                        variant="inline"
                        required={toDateisRequired? true : false}
                        format="MM-dd-yyyy hh:mm:ss"
                        value={detailFormData ? checkToDateDetailValueProp() : toDate}
                        onChange={handleToDateValueProps}
                        minDate={detailFormData ? checkFromDateDetailValueProp() : fromDate}
                        InputProps={{
                            endAdornment: (
                              <IconButton>
                                <CalendarTodayIcon />
                              </IconButton>
                            ),
                        }}
                    />
                </Grid>
            </Grid>
        </MuiPickersUtilsProvider>
    )
}

DateRangePicker.propTypes ={
    fromDateisRequired: PropTypes.bool,
    toDateisRequired: PropTypes.bool,
    valueFormProps: PropTypes.func.isRequired,
    detailFormData: PropTypes.any,
}

DateRangePicker.defaultProps = {
    toDateisRequired: false,
    fromDateisRequired: false,
  };

export default DateRangePicker;