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
        console.log(item);
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

    putCancel: async (apiUrl, id, email, setReloadKey) => {
        try {
            await axios.put(`${apiUrl}/api/user_leave/cancel`, {
                "id": id,
                "updatedBy": email
            })
        } catch (error) {
            throw Error(error)
        }
        await setReloadKey(prevCount => prevCount + 1);
    }
};

export default MyLeaveTableService;
