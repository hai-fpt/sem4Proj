import axios from "axios";
import axiosConfig from "../../../../../api/config/config";

const MyLeaveTableService = {
    getTabItems: (detailFormData) => {
        return [
            { label: 'List', index: 0 },
            { label: detailFormData ? 'Update' : 'Create', index: 1 }
        ];
    },

    handleCancelEdit: (e,setDetailFormData, setForceRender, setReloadKey, handleTabValueProps, detailFormData) => {
        e.preventDefault();
        setDetailFormData();
        setForceRender(detailFormData);
        setReloadKey(prevCount => prevCount + 1);
        handleTabValueProps(0)
    },

    handleFormSubmit: (e, setOpenNotification, setReloadKey, handleTabValueProps) => {
        e.preventDefault();
        setOpenNotification(true);
        setReloadKey(prevCount => prevCount + 1);
        handleTabValueProps(0)
    },

    handleEditProcessing: (item, handleTabValueProps, setDetailFormData) => {
        handleTabValueProps(1);
        setDetailFormData(item);
    },

    fetchRequest: async (apiUrl, id, page, size) => {
        try {
            return axios.get(`${apiUrl}/api/dashboard/request?id=${id}&page=${page}&size=${size}&sort=string`, axiosConfig)
        } catch (error) {
            throw Error(error)
        }
    },

    putCancel: async (apiUrl, id, email, setReloadKey, setIsLoading) => {
        try {
            const res = await axios.put(`${apiUrl}/api/user_leave/cancel`, {
                "id": id,
                "updatedBy": email
            })
            await setIsLoading(false)
            await setReloadKey(prevCount => prevCount + 1);
            return res;
        } catch (error) {
            throw Error(error)
        }

    },
    postAttachment: async (attachment, id, updatedBy, baseApiUrl) => {
        try {
            const formData = new FormData();
            for (let i = 0; i < attachment.length; i++) {
                formData.append('files', attachment[i]);
            }
            formData.append('requestId', id);
            formData.append('updatedBy', updatedBy);
            return await axios.post(`${baseApiUrl}/api/file/upload`, formData)
        } catch (error) {
            throw new Error(error)
        }
    },
};

export default MyLeaveTableService;
