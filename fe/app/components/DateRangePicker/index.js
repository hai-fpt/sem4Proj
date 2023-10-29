import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import DateFnsUtils from '@date-io/date-fns';
import { format } from 'date-fns';
import Typography from '@material-ui/core/Typography';
import {
  MuiPickersUtilsProvider,
  DatePicker,
} from '@material-ui/pickers';
import IconButton from '@material-ui/core/IconButton';
import CalendarTodayIcon from '@material-ui/icons/CalendarToday';
import {injectIntl} from 'react-intl';
import messages from "enl-api/holidays/holidayMessages";

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
    const {intl} = props;
    const classes = useStyles();
    const [fromDate, setFromDate] = useState(new Date());
    const [toDate, setToDate] = useState(new Date());
    const { fromDateisRequired, toDateisRequired, valueFormProps, detailFormData} = props;
    
    const handleFromDateValueProps = (e) => {
        const formattedDate = format(e, 'MM-dd-yyyy')
        setFromDate(formattedDate)
        valueFormProps(formattedDate,'fromDate');
    };
      
    const handleToDateValueProps = (e) => {
        const formattedDate = format(e, 'MM-dd-yyyy')
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
                        {intl.formatMessage(messages.from)}
                        {
                            fromDateisRequired && (
                                <span className={classes.requiredElement}>
                                    (*)
                                </span>
                            )
                        }
                    </Typography>
                    <DatePicker
                        className={classes.inputFromDate}
                        disableToolbar
                        variant="inline"
                        format="MM-dd-yyyy"
                        required={fromDateisRequired? true : false}
                        value={detailFormData ? checkFromDateDetailValueProp() : fromDate}
                        onChange={handleFromDateValueProps}
                        maxDate={detailFormData ? checkToDateDetailValueProp() : toDate}
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
                        {intl.formatMessage(messages.to)}
                        {
                            toDateisRequired && (
                                <span className={classes.requiredElement}>
                                    (*)
                                </span>
                            )
                        }
                    </Typography>
                    <DatePicker
                        disableToolbar
                        className={classes.inputToDate}
                        variant="inline"
                        required={toDateisRequired? true : false}
                        format="MM-dd-yyyy"
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

export default injectIntl(DateRangePicker);