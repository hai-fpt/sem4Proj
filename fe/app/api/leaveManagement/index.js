import axios from "axios";

export const fetchLeaves = async (apiUrl,id, page, size) => {
    try {
        return await axios.get(`${apiUrl}/api/manager/leave/approval/${id}?page=${page}&size=${size}&sort=string`, )
    } catch (error) {
        throw new Error(error);
    }
};

export const fetchLeavesMothly = async (apiUrl, id, month, year, page, size) => {
    try {
        return await axios.get(`${apiUrl}/api/manager/leave/approval/month?id=${id}&date=${year}-${month}-01 00:00:00&page=${page}&size=${size}&sort=string`, )
    } catch (error) {
        throw new Error(error);
    }
};

export const leaveDecision = async (apiUrl, status, requestID, managerId, manager) => {
    const requestBody= {
        id:requestID,
        status: status,
        managerId: managerId,
        updatedBy: manager,
    }
    try {
        return await axios.put(`${apiUrl}/api/manager/leave/status_update`, requestBody, )
    } catch (error) {
        throw new Error(error);
    }
};
