import React from "react";
import Chip from '@material-ui/core/Chip';
import { makeStyles } from '@material-ui/core/styles';
import PropTypes from 'prop-types';

const useStyles = makeStyles({
  chip: {
    paddingTop: 2,
    height: 30,
    '& span': {
      lineHeight: '14px',
    }
  },
  approved: {
    backgroundColor: '#4CAF50',
  },
  reject: {
    backgroundColor: '#e65100',
  },
  pending: {
    backgroundColor: '#ffc107',
  },
  canceled: {
    backgroundColor: '#808080',
  },
});

const StatusChip = (props) => {
  const classes = useStyles();
  const getStatusBackground = () => {
    switch (props.status) {
      case 'APPROVED':
        return classes.approved;
      case 'REJECTED':
        return classes.reject;
      case 'PENDING':
        return classes.pending;
      case 'CANCELLED':
        return classes.canceled;
      default:
        return null;
    }
  };

  return (
    <Chip label={props.status} color="primary" className={`${classes.chip} + ${getStatusBackground()}`} />
  );
}

StatusChip.propTypes = {
    status: PropTypes.string.isRequired
}

export default StatusChip;
