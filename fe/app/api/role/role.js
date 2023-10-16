import axios from "axios";

export const fetchRoles = async (baseApiUrl) => {
    try {
        return await axios.get(`${baseApiUrl}/api/admin/role`);
    } catch (error) {
        throw new Error(error);
    }
};