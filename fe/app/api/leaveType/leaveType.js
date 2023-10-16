import axios from "axios"

export const fetchVerify = async (baseApiUrl) => {
    try{
        const response = await axios.get(`${baseApiUrl}/api/leave`, );
        return response.data.content;
    } catch(error) {
        throw new Error('Verification failed');
    }
}

export const postLeave = async (baseApiUrl, body) => {
    try{
    await axios.post(`${baseApiUrl}/api/leave`, body, )
    } catch(error) {
        throw new Error('Create failed');
    }
}

export const putLeave = async (baseApiUrl, id, body) => {
    try {
      await axios
        .put(`${baseApiUrl}/api/leave/${id}`, body, )
    } catch (error) {
      throw new Error("Update failed");
    }
}

export const deleteLeave = async (baseApiUrl, id) => {
    try {
        await axios.delete(`${baseApiUrl}/api/leave/${id}`, )
    } catch (error) {
        throw new Error("Delete failed");
    }
}
  