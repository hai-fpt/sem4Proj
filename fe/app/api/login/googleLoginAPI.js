import axios from 'axios';

export const fetchVerify = async (googleCredential, baseApiUrl) => {
  try {
    return await axios.post(`${baseApiUrl}/verify`, { token: googleCredential });
  } catch (error) {
    throw new Error('Verification failed');
  }
};

export const postLogin = async (decodedCredential, baseApiUrl) => {
  const requestData = {
    email: decodedCredential.email,
    name: decodedCredential.name
  };
  try {
    return await axios.post(`${baseApiUrl}/login`, requestData, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
  } catch (error) {
    throw new Error('Verification failed');
  }
};

