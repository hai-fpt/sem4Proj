import axios from "axios";

export const fetchVerify = async (baseApiUrl) => {
    try{
            const response = await axios.get(`${baseApiUrl}/api/admin/holiday`, );
            const res = response.data.content;
            return res;
    } catch(error) {
        throw new Error('Verification failed');
    }
};

export const postHoliday = async (baseApiUrl, body, setNotificationMessage, setOpenNotification, handleTabValueProps, setNotificationSeverity) => {
    try{
        const response = await axios.post(`${baseApiUrl}/api/admin/holiday`, body, )
        if (response.status === 201) {
            setNotificationMessage('Great! a holiday was create successfully');
            setNotificationSeverity('success');
            setOpenNotification(true);
            handleTabValueProps(0);
        }
    } catch(error) {
        setNotificationMessage('Fail! a holiday was create failed.');
        setNotificationSeverity('error');
        setOpenNotification(true);
        throw new Error('Create failed');
    }
}

export const putHoliday = async (baseApiUrl, id, body, setNotificationMessage, setOpenNotification, handleTabValueProps, setNotificationSeverity) => {
    try {
        const response = await axios
            .put(`${baseApiUrl}/api/admin/holiday/${id}`, body, )
        console.log('res', response)
        if (response.status === 200) {
            setNotificationMessage('Great! a holiday was edit successfully.');
            setNotificationSeverity('success');
            setOpenNotification(true);
            handleTabValueProps(0);
        }
    } catch (error) {
        setNotificationMessage('Fail! a holiday was edit failed.');
        setNotificationSeverity('error');
        setOpenNotification(true);
        throw new Error("Update failed");
    }
};

export const deleteHoliday = async (baseApiUrl, id, setNotificationMessage, setOpenNotification, setNotificationSeverity, setReloadKey) => {
    try {
        const response = await axios.delete(`${baseApiUrl}/api/admin/holiday/${id}`, )
        console.log('res', response)
        if (response.status === 200) {
            setNotificationMessage('Great! a holiday was delete successfully.');
            setNotificationSeverity('success');
            setOpenNotification(true);
            setReloadKey((prevCount) => prevCount + 1);
        }
    } catch (error) {
        setNotificationMessage('Fail! a holiday was delete failed.');
        setNotificationSeverity('error');
        setOpenNotification(true);
        throw new Error('Delete failed');
    }
}