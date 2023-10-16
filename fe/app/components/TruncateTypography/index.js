import React from 'react';
import PropTypes from 'prop-types';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles({
  truncate: {
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    whiteSpace: 'nowrap',
    maxWidth: 500,
    fontSize: '14px'
  },
});

const TruncateTypography = ({ text, component, color }) => {
  const classes = useStyles();

  return (
    <Typography
      className={classes.truncate}
      component={component}
      color={color}
    >
      {text}
    </Typography>
  );
};

TruncateTypography.propTypes = {
  text: PropTypes.string.isRequired,
  component: PropTypes.string,
  color: PropTypes.any,
}

export default TruncateTypography;
