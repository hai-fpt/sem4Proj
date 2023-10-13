import React, {useState} from 'react';
import { Helmet } from 'react-helmet';
import { PapperBlock, TabsNavigation } from 'enl-components';
import Paper from '@material-ui/core/Paper';

const Holiday = () => {
  const title = 'Holiday management';

  const tabItems = [
    { label: 'Holiday List', value: 0 },
    { label: 'Holiday Management', value: 1 }
  ]

  return (
    <div>
      <Helmet>
        <title>{title}</title>
      </Helmet>
      <PapperBlock title="Holidays" 
        whiteBg 
        icon="local_airport" 
        desc="This module allows admins to view and update holidays."
      >
        <TabsNavigation tabItems={tabItems} ></TabsNavigation>
      </PapperBlock>
      <Paper>

      </Paper>
    </div>
  );
}

export default Holiday;
