import {applyLeave, getLeaveType, getSelfUserLeave, getUserLeaveById} from "../../../api/applyleave/applyLeaveApi";
import {fetchUsers} from "../../../api/user/userApi";
import axios from "axios";

const ApplyLeaveService = {
    postApplyLeave: async (data, baseApiUrl) => {
        try {
            return await applyLeave(data, baseApiUrl);
        } catch (error) {
            throw new Error(error);
        }
    },
    postAttachment: async (attachment, data, baseApiUrl) => {
        try {
            const formData = new FormData();
            for (let i = 0; i < attachment.length; i++) {
                formData.append('files', attachment[i]);
            }
            formData.append('requestId', data.data.id);
            formData.append('updatedBy', data.data.updatedBy);
            return await axios.post(`${baseApiUrl}/api/file/upload`, formData)
        } catch (error) {
            throw new Error(error)
        }
    },

    getUsers: async (baseApiUrl) => {
        try {
            return await fetchUsers(baseApiUrl);
        } catch (error) {
            throw new Error(error);
        }
    },

    getLeaveType: async (baseApiUrl) => {
        try {
            return await getLeaveType(baseApiUrl);
        } catch (error) {
            throw new Error(error);
        }
    },

    getSelfLeave: async (id, baseApiUrl) => {
        try {
            return await getSelfUserLeave(id, baseApiUrl);
        } catch (error) {
            throw new Error(error);
        }
    },

    getUserLeaveById: async (id, baseApiUrl) => {
        try {
            return await getUserLeaveById(id, baseApiUrl);
        } catch (error) {
            throw new Error(error);
        }
    },
}

export default ApplyLeaveService;