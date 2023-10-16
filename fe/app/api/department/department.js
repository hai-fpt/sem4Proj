import axios from "axios";

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
        throw new Error('Verification failed')
    }
}

export const postDepartment = async (baseApiUrl, body) => {
    try{
        await axios.post(`${baseApiUrl}/api/admin/department`, body, )
    } catch(error) {
        throw new Error('Create failed');
    }
}

export const putDepartment = async (baseApiUrl, id, body) => {
    try {
        await axios
            .put(`${baseApiUrl}/api/admin/department/${id}`, body, )
    } catch (error) {
        throw new Error("Update failed");
    }
};

export const deleteDepartment = async (baseApiUrl, id) => {
    try {
        await axios.delete(`${baseApiUrl}/api/admin/department/${id}`, )
    } catch (error) {
        throw new Error('Delete failed');
    }
}