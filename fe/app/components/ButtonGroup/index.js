import React from 'react';
import Box from '@material-ui/core/Box';
import Button from '@material-ui/core/Button';
import {injectIntl} from 'react-intl';
import messages from './buttonMessage';


const ButtonGroup = (props) => {
  const {detail, handleCancelEdit, intl} = props;

  return (
    <Box display={'flex'} justifyContent={'flex-end'} marginTop={12} gridGap={12}>
        {
            detail && (
                <Button type='submit' variant="contained" size="large" color="primary">
                    {intl.formatMessage(messages.update)}
                </Button>
            )
        }
        <Button  
            type={detail ? 'button' : 'submit'}
            onClick={detail ? handleCancelEdit : undefined}
            variant={detail ? 'text' : 'contained'}
            size="large" 
            color={detail ? 'secondary' : 'primary'}
        >
                { detail ? intl.formatMessage(messages.cancel) : intl.formatMessage(messages.create)}
        </Button>
    </Box>
  );
}

export default injectIntl(ButtonGroup);
