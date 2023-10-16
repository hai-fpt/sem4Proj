import React from 'react';
import Box from '@material-ui/core/Box';
import Button from '@material-ui/core/Button';

const ButtonGroup = (props) => {
  const {detail, handleCancelEdit} = props;

  return (
    <Box display={'flex'} justifyContent={'flex-end'} marginTop={12} gridGap={12}>
        {
            detail && (
                <Button type='submit' variant="contained" size="large" color="primary">
                    Update
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
                { detail ? 'Cancel' : 'Create'}
        </Button>
    </Box>
  );
}

export default ButtonGroup;
