import React, { useState } from 'react';
import PropTypes from 'prop-types';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles({
    tabs: {
      backgroundColor: '#fafafa', // custom background color
      width: 'fit-content'
    },
    selectedTab:{
      color: 'white !important',
      backgroundColor: '#03a9f4',
    }

});


const TabsNavigation = (props) => {
    const classes = useStyles();
    const {
      tabItems
    } = props;

    const [value, setValue] = useState(0);


    const handleChange = (event, newValue) => {
      setValue(newValue);
    };
  
    function a11yProps(index) {
      return {
        id: `scrollable-auto-tab-${index}`,
        'aria-controls': `scrollable-auto-tabpanel-${index}`,
      };
    }
  
    return (
      <Tabs
        value={value}
        onChange={handleChange}
        indicatorColor="primary"
        textColor="primary"
        variant="scrollable"
        scrollButtons="auto"
        aria-label="scrollable auto tabs example"
        className={classes.tabs}
        >
          { 
            tabItems.map((tabItem) => {
              return (
                <Tab 
                  key={tabItem.value} 
                  classes={{ selected: classes.selectedTab }} 
                  label={tabItem.label} 
                  {...a11yProps(tabItem.value)}
                />
              )
            })
          }
      </Tabs>
    );
  }
  
  TabsNavigation.propTypes = {
    tabItems: PropTypes.array.isRequired,
  };

  export default TabsNavigation;