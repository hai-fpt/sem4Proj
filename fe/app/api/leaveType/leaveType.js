import axios from "axios"
import messages from "enl-api/leaveType/leaveTypeMessages";

export const fetchVerify = async (baseApiUrl) => {
    try {
        const response = await axios.get(`${baseApiUrl}/api/leave`,);
        return response.data.content;
    } catch (error) {
        throw new Error('Verification failed');
    }
}

export const postLeave = async (baseApiUrl, body, setOpenNotification, handleTabValueProps, setNotificationMessage, setNotificationSeverity, intl) => {
    try{
    const response = await axios.post(`${baseApiUrl}/api/leave`, body, )
        if (response.status === 201) {
            setNotificationMessage(intl.formatMessage(messages.notificationCreateSuccessfully));
            setNotificationSeverity('success');
            handleTabValueProps(0)
            setOpenNotification(true);
        }
    } catch(error) {
        setNotificationMessage(intl.formatMessage(messages.notificationCreateFail));
        setNotificationSeverity('error');
        setOpenNotification(true);
        throw new Error('Create failed');
    }
}

export const putLeave = async (baseApiUrl, id, body, setOpenNotification, handleTabValueProps, setNotificationMessage, setNotificationSeverity, intl) => {
    try {
      const response = await axios
        .put(`${baseApiUrl}/api/leave/${id}`, body, )
        if ( response.status === 200) {
            setNotificationMessage(intl.formatMessage(messages.notificationUpdateSuccessfully));
            setNotificationSeverity('success');
            handleTabValueProps(0);
            setOpenNotification(true);
        }
    } catch (error) {
        setNotificationMessage(intl.formatMessage(messages.notificationUpdateFail));
        setNotificationSeverity('error');
        setOpenNotification(true);
      throw new Error("Update failed");
    }
}

export const deleteLeave = async (baseApiUrl, id, setOpenNotification, setNotificationMessage, setNotificationSeverity, setReloadKey, intl) => {
    try {
        const response = await axios.delete(`${baseApiUrl}/api/leave/${id}`, )
        if (response.status === 200) {
            setNotificationMessage(intl.formatMessage(messages.notificationDeleteSuccessfully));
            setNotificationSeverity('success');
            setOpenNotification(true);
            setReloadKey((prevCount) => prevCount + 1);
        }
    } catch (error) {
        setNotificationMessage(intl.formatMessage(messages.notificationDeleteFail));
        setNotificationSeverity('error');
        setOpenNotification(true);
        throw new Error("Delete failed");
    }
}
export const fetchLeaveById = async (baseApiUrl,id) => {
    const {data} = await axios.get(`${baseApiUrl}/api/user_leave?id=${id}`,)
    return data
}
  