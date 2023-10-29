import axios from "axios";
import messages from "enl-api/holidays/holidayMessages";

export const fetchVerify = async (baseApiUrl) => {
    try{
            const response = await axios.get(`${baseApiUrl}/api/admin/holiday`, );
            const res = response.data.content;
            return res;
    } catch(error) {
        throw new Error('Verification failed');
    }
};

export const postHoliday = async (baseApiUrl, body, setNotificationMessage, setOpenNotification, handleTabValueProps, setNotificationSeverity, intl) => {
    try{
        const response = await axios.post(`${baseApiUrl}/api/admin/holiday`, body, )
        if (response.status === 201) {
            setNotificationMessage(intl.formatMessage(messages.notificationCreateSuccessfully));
            setNotificationSeverity('success');
            setOpenNotification(true);
            handleTabValueProps(0);
        }
    } catch(error) {
        setNotificationMessage(intl.formatMessage(messages.notificationCreateFail));
        setNotificationSeverity('error');
        setOpenNotification(true);
        throw new Error('Create failed');
    }
}

export const putHoliday = async (baseApiUrl, id, body, setNotificationMessage, setOpenNotification, handleTabValueProps, setNotificationSeverity, intl) => {
    try {
        const response = await axios
            .put(`${baseApiUrl}/api/admin/holiday/${id}`, body, )
        if (response.status === 200) {
            setNotificationMessage(intl.formatMessage(messages.notificationUpdateSuccessfully));
            setNotificationSeverity('success');
            setOpenNotification(true);
            handleTabValueProps(0);
        }
    } catch (error) {
        setNotificationMessage(intl.formatMessage(messages.notificationUpdateFail));
        setNotificationSeverity('error');
        setOpenNotification(true);
        throw new Error("Update failed");
    }
};

export const deleteHoliday = async (baseApiUrl, id, setNotificationMessage, setOpenNotification, setNotificationSeverity, setReloadKey, intl) => {
    try {
        const response = await axios.delete(`${baseApiUrl}/api/admin/holiday/${id}`, )
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
        throw new Error('Delete failed');
    }
}