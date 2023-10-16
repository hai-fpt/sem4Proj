import { SET_NOTIFICATION, 
    SET_OPEN_NOTIFICATION,
    SET_NOTIFICATION_SEVERITY,
    SET_OPEN_DIALOG,
    SET_DIALOG_MESSAGE
} from '../constants/notifConstants';

export const setOpenNotification = (state) => {
  return {
    type: SET_OPEN_NOTIFICATION,
    payload: state,
  };
};
export const setNotification = (state) => {
    return {
      type: SET_NOTIFICATION,
      payload: state,
    };
};
export const setNotificationServerity = (state) => {
    return {
      type: SET_NOTIFICATION_SEVERITY,
      payload: state,
    };
};
export const setOpenDialog = (state) => {
    return {
      type: SET_OPEN_DIALOG,
      payload: state,
    };
};
export const setDialogMessage = (state) => {
    return {
      type: SET_DIALOG_MESSAGE,
      payload: state,
    };
};
