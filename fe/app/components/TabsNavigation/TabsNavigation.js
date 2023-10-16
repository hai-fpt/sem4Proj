import React, { useState } from 'react';
import PropTypes from 'prop-types';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles({
    tabs: {
      backgroundColor: 'rgba(0, 0, 0, 0.05)', // custom background color
      width: 'fit-content',
      fontSize: 12,
      minHeight: '36px !important',
      lineHeight: 1,
    },
    childTabs: {
      backgroundColor: 'rgba(0, 0, 0, 0.05)', // custom background color
      width: 'fit-content',
      fontSize: 12,
      minHeight: '36px !important',
      lineHeight: 1,
    },
    selectedTab:{
      color: 'white !important',
      backgroundColor: '#03a9f4 !important',
      borderRight: 'none !important',
    },
    borderedTab: {
      borderRight: '1px solid white',
    }

});

const TabsNavigation = (props) => {
    const classes = useStyles();
    const {
      tabItems, tabValuePropsFromChild, tabValuePropsFromParent
    } = props;

    const handleChange = (event, newValue) => {
      tabValuePropsFromChild(newValue);
    };

  
    function a11yProps(index) {
      return {
        id: `scrollable-auto-tab-${index}`,
        'aria-controls': `scrollable-auto-tabpanel-${index}`,
      };
    }
  
    return (
      <Tabs
        value={tabValuePropsFromParent}
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
              const isLastTab = tabItem.index === tabItems.length - 1;
              const tabClasses = `${classes.childTabs} ${!isLastTab && tabItems.length > 2 ? classes.borderedTab : ''}`;
              return (
                <Tab 
                  key={tabItem.index} 
                  classes={{ 
                    root: tabClasses,
                    selected: classes.selectedTab,
                  }} 
                  label={tabItem.label} 
                  {...a11yProps(tabItem.index)}
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