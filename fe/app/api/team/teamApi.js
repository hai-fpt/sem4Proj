import axios from 'axios';

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

export const createTeam = async (data, baseApiUrl) => {
  try {
    return await axios.post(`${baseApiUrl}/api/admin/team`, data, );
  } catch (error) {
    throw new Error(error);
  }
};

export const updateTeam = async (id, data, baseApiUrl) => {
  try {
    return await axios.put(`${baseApiUrl}/api/admin/team/${id}`, data, );
  } catch (error) {
    throw new Error(error);
  }
};

export const deleteTeam = async (id, baseApiUrl) => {
  try {
    return await axios.delete(`${baseApiUrl}/api/admin/team/${id}`, );
  } catch (error) {
    throw new Error(error);
  }
};