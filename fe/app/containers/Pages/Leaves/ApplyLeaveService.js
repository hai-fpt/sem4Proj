import {applyLeave, getLeaveType, getSelfUserLeave, getUserLeaveById} from "../../../api/applyleave/applyLeaveApi";
import {fetchUsers} from "../../../api/user/userApi";

const ApplyLeaveService = {
    postApplyLeave: async (data, baseApiUrl) => {
        try {
            return await applyLeave(data, baseApiUrl);
        } catch (error) {
            throw new Error(error);
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