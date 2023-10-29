import axios from "axios";

export const fetchUsersRole = async (baseApiUrl, userId) => {
    try {
        return await axios.get(`${baseApiUrl}/api/user/role?id=${userId}`)
    } catch (error) {
        throw new Error(error);
    }
};

export const fetchUsers = async (baseApiUrl) => {
    try {
        return await axios.get(`${baseApiUrl}/api/user`);
    } catch (error) {
        throw new Error(error);
    }
}

export const postUser = async (baseApiUrl, data) => {
    try {
        return await axios.post(`${baseApiUrl}/api/user`, data)
    } catch (error) {
        throw new Error(error);
    }
};

export const putUser = async (baseApiUrl, data) => {
    try {
        return await axios.put(`${baseApiUrl}/api/user/${data.id}`, data)
    } catch (error) {
        throw new Error(error);
    }
};

export const deleteUser = async (baseApiUrl, id) => {
    try {
        return await axios.delete(`${baseApiUrl}/api/user/${id}`)
    } catch (error) {
        throw new Error(error);
    }
};

export const changeStatus = async (baseApiUrl, id, status , UpdatedBy) => {
    const requestBody = { status: status, updatedBy: UpdatedBy}
    try {
        return await axios.put(`${baseApiUrl}/api/user/status/${id}`,requestBody)
    } catch (error) {
        throw new Error(error);
    }
};