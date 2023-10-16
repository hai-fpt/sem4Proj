import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';

const TabPanel = (props) => {
  const [renderKey, setRenderKey] = useState(0);
  const {
    tabIndex, tabValue, children, forceRender
  } = props;

  useEffect(() => {
    if (forceRender) {
      setRenderKey(prevKey => prevKey + 1);
    }
  }, [forceRender]);

  return (
      <div
        key={`${tabIndex}-${renderKey}`}
        role="tabpanel"
        hidden={tabValue !== tabIndex}
        id={`scrollable-force-tabpanel-${tabIndex}`}
        aria-labelledby={`scrollable-force-tab-${tabIndex}`}
      >
        {tabValue === tabIndex && (
          <>
            {children}
          </>
        )}
      </div>
    );
  }
  
  TabPanel.propTypes = {
    tabIndex: PropTypes.number.isRequired,
    tabValue: PropTypes.number.isRequired,
    children: PropTypes.node.isRequired,
    forceRender: PropTypes.any,
  };

  export default TabPanel;