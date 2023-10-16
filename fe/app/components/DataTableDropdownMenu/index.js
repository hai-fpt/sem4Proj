import React, { useState } from 'react';
import PropTypes from 'prop-types';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import IconButton from '@material-ui/core/IconButton';
import MoreVertIcon from '@material-ui/icons/MoreVert';
import Box from '@material-ui/core/Box';
import Delete from '@material-ui/icons/Delete';
import Edit from '@material-ui/icons/Edit';
import CheckCircleIcon from '@material-ui/icons/CheckCircle';
import Typography from '@material-ui/core/Typography';
import PlayArrow from '@material-ui/icons/PlayArrow';
import Pause from '@material-ui/icons/Pause';
import { makeStyles } from '@material-ui/core/styles';
import { CustomNotification, WarningDialog } from 'enl-components';


const useStyles = makeStyles({
    menuItemBox: {
      width: 'max-content',
      display: 'flex',
      columnGap: 20,
      padding: '8px 5px',
    },
});

const DataTableDropdownMenu = (props) => {
    const classes = useStyles();
    const {
        ItemValue, 
        delete: deleteBoolean,
        edit,
        submit,
        status,
        editProcessing,
        deleteProcessing,
        changeStatus,
        fullItem
    } = props
    //dropdown status (expanded status)
    const [anchorEl, setAnchorEl] = React.useState(null);
    const [openWarningDialog, setOpenWarningDialog] = useState(false);
    const [openNotification, setOpenNotification] = useState(false);
    const [dialogMessage, setDialogMessage] = useState('');
    const [notificationMessage, setNotificationMessage] = useState('');
    const [notificationSeverity, setNotificationSeverity] = useState('');
    const [deleteValue, setDeleteValue] = useState();
    const [currentAction, setCurrentAction] = useState('');

    const handleExpandMenuDropdown = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleCloseMenuDropdown = () => {
        setAnchorEl(null);
    };

    //action in menu dropdown
    const handleDelete = (value) => {
        handleCloseMenuDropdown();
        setCurrentAction('delete')
        setOpenWarningDialog(true);
        setDialogMessage('Are you sure want to delete this item?');
        setNotificationMessage('Item deleted successfully!')
        setDeleteValue(value);
    }
    const handleEdit = (value) => {
        handleCloseMenuDropdown();
        editProcessing(value)
    }
    const handleSubmit = (value) => {
        handleCloseMenuDropdown();
        setOpenWarningDialog(true);
        setDialogMessage('Are you sure want to submit your works?');
        setNotificationMessage('Success! Your submission has been completed.');
    }

    //dialog service
    const handleCloseWarningDialog = () => {
        setOpenWarningDialog(false);
    };

    const handleConfirmDelete = () => {
        // Perform delete action
        deleteProcessing(deleteValue);
        // setOpenNotification(true); // Display the notification
        // setNotificationSeverity('success')
        handleCloseMenuDropdown();
    };

    const handleChangStatus = () => {
        setCurrentAction('status')
        setOpenWarningDialog(true);
        setDialogMessage('Are you sure want to change this user status works?');
        handleCloseMenuDropdown();
    }
    const handleConfirmChangeStatus = () => {
        changeStatus(fullItem.id, !fullItem.status)
        setOpenNotification(true); // Display the notification
        setNotificationSeverity('success');
        setNotificationMessage("User's status changed!");
    };
    
  return (
    <div>
      <IconButton onClick={handleExpandMenuDropdown}>
        <MoreVertIcon />
      </IconButton>
      <Menu
        anchorEl={anchorEl}
        keepMounted
        open={Boolean(anchorEl)}
        onClose={handleCloseMenuDropdown}
      > 
        {
            deleteBoolean && (
                <MenuItem onClick={() => {handleDelete(ItemValue.rowData)}}>
                    <Box className={classes.menuItemBox}>
                        <Delete fontSize='small'/>
                        <Typography>Delete</Typography>
                    </Box>
                </MenuItem>
            )
        }
        {
            edit && (
                <MenuItem onClick={() => {handleEdit(ItemValue.rowData)}}>
                    <Box className={classes.menuItemBox}>
                        <Edit fontSize='small'/>
                        <Typography>Edit</Typography>
                    </Box>
                </MenuItem>
            )
        }
        {
            submit && (
                <MenuItem onClick={() => {handleSubmit(ItemValue.rowData)}}>
                    <Box className={classes.menuItemBox}>
                        <CheckCircleIcon fontSize='small'/>
                        <Typography>Submit</Typography>
                    </Box>
                </MenuItem>
            )
        }
        {   
            status && (
                <MenuItem onClick={() => {handleChangStatus()}}>
                    <Box className={classes.menuItemBox}>
                        {
                            ItemValue.rowData[4] === true?  <Pause /> : <PlayArrow /> 
                        }
                        <Typography>
                            {
                                ItemValue.rowData[4] === true ? 'Inactive' : 'Active'
                            }
                        </Typography>
                    </Box>
                </MenuItem>
            )
        }
        
      </Menu>
        <WarningDialog
            open={openWarningDialog}
            onClose={handleCloseWarningDialog}
            onConfirm={currentAction === 'delete' ? handleConfirmDelete : handleConfirmChangeStatus}
            dialogMessage={dialogMessage}
        />
        <CustomNotification
          open={openNotification}
          close={() => {setOpenNotification(false)}}
          notificationMessage={notificationMessage}
          severity={notificationSeverity}
        />
    </div>
  );
};

DataTableDropdownMenu.propTypes = {
    delete: PropTypes.bool,
    edit: PropTypes.bool,
    view: PropTypes.bool,
    update: PropTypes.bool,
    submit: PropTypes.bool,
    ItemValue: PropTypes.any,
    editProcessing: PropTypes.func,
    deleteProcessing: PropTypes.func,
    fullItem: PropTypes.any
}

export default DataTableDropdownMenu;
