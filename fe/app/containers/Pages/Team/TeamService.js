import {
    createTeam,
    deleteTeam,
    fetchTeam,
    fetchTeamManagers,
    fetchTeamUserList,
    updateTeam
} from "../../../api/team/teamApi";
import {fetchUsers} from "../../../api/user/userApi";
import {useIntl} from "react-intl";
import messages from "enl-api/team/teamMessages"
import {fetchDepartment} from "../../../api/department/department";


const TeamService = {
    getTabItems: (detailFormData) => {
        const intl = useIntl();
        return [
            { label: intl.formatMessage(messages.list), index: 0 },
            { label: detailFormData ? intl.formatMessage(messages.update) : intl.formatMessage(messages.create), index: 1 }
        ];
    },

    formDataSetup: () => {
        const dataSetup = {
            name: {
                disabled: false,
                type: 'text',
                label: 'team name',
                field: 'teamName',
                required: true,
            },
            manager: {
                disabled: false,
                type: 'select',
                label: 'manager',
                field: 'manager',
                required: true,
            },
            createdDate: {
                disabled: true,
                type: 'today_date',
                label: 'created date',
                field: 'createdDate'
            },
            updatedDate: {
                disabled: true,
                type: 'today_date',
                label: 'updated date',
                field: 'updatedDate'
            },
            members: {
                disabled: false,
                type: 'multi_select',
                label: 'members',
                field: 'members'
            },
            updatedBy: {
                disabled: true,
                type: 'text',
                label: 'updated by',
                field: 'updatedBy'
            },
            description: {
                disabled: false,
                type: 'textarea',
                label: 'description',
                field: 'description'
            },
            department: {
                disabled: false,
                type: "select",
                label: "department",
                field: "department"
            }
        };
        return dataSetup;
    },

    formatDate: (datetime) => {
        const date = new Date(datetime);
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = date.getFullYear();
        return `${day}-${month}-${year}`;
    },
    handleEditProcessing: (item, handleTabValueProps, setDetailFormData, setFormData) => {
        const filteredData = item.filter((value) => value !== undefined);
        const userList = filteredData[5].map(obj => obj.id);

        const teamObjectData = {
            id: filteredData[0],
            teamName: filteredData[1],
            manager: filteredData[2].id,
            createdDate: filteredData[3],
            description: filteredData[4],
            members: userList,
            updatedBy: filteredData[6],
            department: filteredData[7].id
        }
        setFormData(teamObjectData);
        setDetailFormData(teamObjectData);
        handleTabValueProps(1);
    },

    handleCancelEdit: (e,setDetailFormData, setForceRender, setReloadKey, handleTabValueProps, detailFormData) => {
        e.preventDefault(); 
        setDetailFormData();
        setForceRender(detailFormData);
        setReloadKey(prevCount => prevCount + 1);
        handleTabValueProps(0)
    },
      
    handleTeamFormSubmit: (e, setOpenNotification, setReloadKey, handleTabValueProps) => {
        e.preventDefault();
        setOpenNotification(true);
        setReloadKey(prevCount => prevCount + 1);
        handleTabValueProps(0);
    },

    getTeams: async (baseApiUrl) => {
        try {
            return await fetchTeam(baseApiUrl);
        } catch (error) {
            throw new Error(error);
        }
    },

    postTeam: async (data, baseApiUrl, setNotificationSeverity, setNotificationMessage, setOpenNotification, handleTabValueProps) => {
        try {
            return await createTeam(data, baseApiUrl, setNotificationSeverity, setNotificationMessage, setOpenNotification, handleTabValueProps);
        } catch (error) {
            throw new Error(error);
        }
    },

    getManagers: async(baseApiUrl) => {
        try {
            return await fetchTeamManagers(baseApiUrl);
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

    getUserList: async (id, baseApiUrl) => {
        try {
            return await fetchTeamUserList(id, baseApiUrl)
        } catch (error) {
            throw new Error(error);
        }
    },

    putTeam: async (id, data, baseApiUrl, setNotificationSeverity, setNotificationMessage, setOpenNotification, handleTabValueProps) => {
        try {
            return await updateTeam(id, data, baseApiUrl, setNotificationSeverity, setNotificationMessage, setOpenNotification, handleTabValueProps);
        } catch (error) {
            throw new Error(error);
        }
    },

    deleteTeam: async (id, baseApiUrl, setNotificationSeverity, setNotificationMessage, setOpenNotification, setReloadKey) => {
        try {
            return await deleteTeam(id, baseApiUrl, setNotificationSeverity, setNotificationMessage, setOpenNotification, setReloadKey);
        } catch (error) {
            throw new Error(error);
        }
    },

    getDepartments : async (baseApiUrl) => {
        try {
            return await fetchDepartment(baseApiUrl);
        } catch (error) {
            throw new Error(error);
        }
    }
};

export default TeamService;
