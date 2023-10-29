import axios from 'axios';
import {useIntl} from "react-intl";
import messages from "enl-api/team/teamMessages"

export const fetchTeam = async (baseApiUrl) => {
  try {
    return await axios.get(`${baseApiUrl}/api/admin/team?page=0&size=50&sort=string`, );
  } catch (error) {
    throw new Error('Verification failed');
  }
};

export const fetchTeamDetail = async (id, baseApiUrl) => {
  try {
    return await axios.get(`${baseApiUrl}/api/admin/team/${id}`, )
  } catch (error) {
    throw new Error(error);
  }
};

export const fetchTeamManagers = async (baseApiUrl) => {
  try {
    return await axios.get(`${baseApiUrl}/api/admin/team/manager`, );
  } catch (error) {
    throw new Error(error);
  }
}

export const fetchTeamUserList = async (id, baseApiUrl) => {
  try {
    return await axios.get(`${baseApiUrl}/api/admin/team/users/${id}`, );
  } catch (error) {
    throw new Error(error);
  }
};

export const createTeam = async (data, baseApiUrl, setNotificationSeverity, setNotificationMessage, setOpenNotification, handleTabValueProps) => {
  const intl = useIntl();
  try {
    const response = await axios.post(`${baseApiUrl}/api/admin/team`, data, );
    if (response.status === 201) {
      setNotificationSeverity('success');
      setNotificationMessage(intl.formatMessage(messages.notificationCreateSuccessfully));
      setOpenNotification(true);
      handleTabValueProps(0);
    }
    return response;
  } catch (error) {
    setNotificationSeverity('error');
    setNotificationMessage(intl.formatMessage(messages.notificationCreateFail));
    setOpenNotification(true);
    throw new Error(error);
  }
};

export const updateTeam = async (id, data, baseApiUrl, setNotificationSeverity, setNotificationMessage, setOpenNotification, handleTabValueProps) => {
  const intl = useIntl();
  try {
    const response = await axios.put(`${baseApiUrl}/api/admin/team/${id}`, data, );
    if (response.status === 200) {
      setNotificationSeverity('success');
      setNotificationMessage(intl.formatMessage(messages.notificationCreateSuccessfully));
      setOpenNotification(true);
      handleTabValueProps(0);
    }
    return response;
  } catch (error) {
    setNotificationSeverity('error');
    setNotificationMessage(intl.formatMessage(messages.notificationCreateFail));
    setOpenNotification(true);
    throw new Error(error);
  }
};

export const deleteTeam = async (id, baseApiUrl, setNotificationSeverity, setNotificationMessage, setOpenNotification, setReloadKey) => {
  const intl = useIntl();
  try {
    const response = await axios.delete(`${baseApiUrl}/api/admin/team/${id}`, );
    if (response.status === 204) {
      setNotificationSeverity('success');
      setNotificationMessage(intl.formatMessage(messages.notificationDeleteSuccessfully));
      setOpenNotification(true);
      setReloadKey((prevCount) => prevCount + 1);
    }
    return response;
  } catch (error) {
    setNotificationSeverity('error');
    setNotificationMessage(intl.formatMessage(messages.notificationDeleteFail));
    setOpenNotification(true);
    throw new Error(error);
  }
};