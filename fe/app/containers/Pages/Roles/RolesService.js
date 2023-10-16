import axios from "axios";
import {fetchRoles} from "../../../api/role/role";

const rolesService = {
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
        const filteredData = item.filter((value) => value !== undefined);
        const [roles, description] = filteredData;
        const holidayObjectData = {
          roles,
          description,
        };
        handleTabValueProps(1);
        setDetailFormData(holidayObjectData);
    },
    getRoles: async(baseApiUrl) => {
        try {
            return await fetchRoles(baseApiUrl);
        } catch (error) {
            throw new Error(error);
        }
    }
};

export default rolesService;
