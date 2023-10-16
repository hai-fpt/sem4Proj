import axios from "axios";

export const applyLeave = async (data, baseApiUrl) => {
    try {
        return await axios.post(`${baseApiUrl}/api/user_leave`, data, )
    } catch (error) {
        throw new Error(error);
    }
};

export const getLeaveType = async (baseApiUrl) => {
    try {
        return await axios.get(`${baseApiUrl}/api/leave`, );
    } catch (error) {
        throw new Error(error);
    }
};

export const getSelfUserLeave = async (id, baseApiUrl) => {
    try {
        return await axios.get(`${baseApiUrl}/api/user_leave/self?id=${id}`, );
    } catch (error) {
        throw new Error(error);
    }
};

export const getUserLeaveById = async (id, baseApiUrl) => {
    try {
        return await axios.get(`${baseApiUrl}/api/user_leave?id=${id}`, );
    } catch (error) {
        throw new Error(error);
    }
};