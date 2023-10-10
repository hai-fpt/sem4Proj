import React from 'react';
import PropTypes from 'prop-types';

const TabPanel = (props) => {
    const {
      children, value, index, ...other
    } = props;
  
    return (
      <div
        role="tabpanel"
        hidden={value !== index}
        id={`scrollable-auto-tabpanel-${index}`}
        aria-labelledby={`scrollable-auto-tab-${index}`}
        {...other}
      >
        {value === index && (
            <>
                {children}
            </>
        )}
      </div>
    );
  }
  
  TabPanel.propTypes = {
    children: PropTypes.node.isRequired,
    index: PropTypes.any.isRequired,
    value: PropTypes.any.isRequired,
  };

  export default TabPanel;