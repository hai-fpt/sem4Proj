import axios from 'axios';

export const fetchProfile = async (id, baseApiUrl) => {
  try {
    return await axios.get(`${baseApiUrl}/api/user/${id}`);
  } catch (error) {
    throw new Error('Verification failed!');
  }
};

export const updateProfile = async (id, data, baseApiUrl) => {
  try {
    return await axios.put(`${baseApiUrl}/api/user/${id}`, data)
  } catch (error) {
    throw new Error(error);
  }
}