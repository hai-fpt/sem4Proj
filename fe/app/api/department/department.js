import axios from "axios";
import messages from "enl-api/department/departmentMessages";
export const fetchDepartment = async (baseApiUrl) => {
    try{
            const response = await axios.get(`${baseApiUrl}/api/admin/department`, );
            const res = response.data.content;
            return res;
    } catch(error) {
        throw new Error('Verification failed');
    }
};

export const fetchManager = async (baseApiUrl) => {
    try {
        const response = await axios.get(`${baseApiUrl}/api/admin/team/manager`, );
        return response;
    } catch (error) {
        throw new Error('Verification failed');
    }
}

export const fetchTeam = async (baseApiUrl) => {
    try {
        const response = await axios.get(`${baseApiUrl}/api/admin/team`);
        return response
    }catch (error) {
        throw new Error('Verification failed');
    }
}

export const postDepartment = async (baseApiUrl, body, setOpenNotification, setNotificationMessage, setNotificationSeverity, handleTabValueProps, intl) => {
    try{
        const response = await axios.post(`${baseApiUrl}/api/admin/department`, body, )
        if (response.status === 201) {
            setNotificationMessage(intl.formatMessage(messages.notificationCreateSuccessfully));
            setNotificationSeverity('success');
            handleTabValueProps(0);
            setOpenNotification(true);
        }
    } catch(error) {
        setNotificationMessage(intl.formatMessage(messages.notificationCreateFail));
        setNotificationSeverity('error');
        setOpenNotification(true);
        throw new Error('Create failed');
    }
}

export const putDepartment = async (baseApiUrl, id, body, setOpenNotification, setNotificationMessage, setNotificationSeverity, handleTabValueProps, intl) => {
    try {
        const response = await axios
            .put(`${baseApiUrl}/api/admin/department/${id}`, body, )
        if (response.status === 200) {
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
};

export const deleteDepartment = async (baseApiUrl, id, setOpenNotification, setNotificationMessage, setNotificationSeverity, setReloadKey, intl) => {
    try {
        const response = await axios.delete(`${baseApiUrl}/api/admin/department/${id}`, )
        if (response.status === 200) {
            setNotificationMessage(intl.formatMessage(messages.notificationDeleteSuccessfully));
            setNotificationSeverity('success');
            setReloadKey((prevCount) => prevCount + 1);
            setOpenNotification(true);
        }
    } catch (error) {
        setNotificationMessage(intl.formatMessage(messages.notificationDeleteFail));
        setNotificationSeverity('error');
        setOpenNotification(true);
        throw new Error('Delete failed');
    }
}