import React from 'react';
import Button from '@material-ui/core/Button';
import Snackbar from '@material-ui/core/Snackbar';
import MuiAlert from '@material-ui/lab/Alert';
import { makeStyles } from '@material-ui/core/styles';
import PropTypes from 'prop-types';

function Alert(props) {
  return <MuiAlert elevation={6} variant="filled" {...props} />;
}

const useStyles = makeStyles((theme) => ({
  root: {
    width: '100%',
    '& > * + *': {
      marginTop: theme.spacing(2),
    },
  },
  notification: {
    fontSize: 18,
    padding:'15px 30px',
    color: '#FFFFFF',
  },
  success: {
    backgroundColor: '#4CAF50',
  },
  error: {
    backgroundColor: '0#f44336',
  },
  warning: {
    backgroundColor: '#ff980',
  },
  
}));

const CustomNotification = (props) => {
  const classes = useStyles();

  const { severity } = props;

  const handleClose = (event, reason) => {
    if (reason === 'clickaway') {
      return;
    }
    props.close()
  };

  let severityClass;
  if (severity === 'success') {
    severityClass = classes.success;
  } else if (severity === 'error') {
    severityClass = classes.error;
  } else if (severity === 'warning') {
    severityClass = classes.warning;
  }

  return (
    <Snackbar
      open={props.open}
      autoHideDuration={5000}
      onClose={handleClose}
      anchorOrigin={{
        vertical: 'bottom',
        horizontal: 'right',
      }}
    >
      <Alert
        onClose={handleClose}
        severity={props.severity}
        className={`${classes.notification} ${severityClass}`}
      >
        {props.notificationMessage}
      </Alert>
    </Snackbar>
  );
}

CustomNotification.propTypes = {
  open: PropTypes.bool.isRequired,
  close: PropTypes.func.isRequired,
  notificationMessage: PropTypes.any.isRequired,
  severity: PropTypes.string
};


export default CustomNotification;
