import { fetchUsers, postUser, putUser, deleteUser, changeStatus } from "enl-api/user/userApi"

const UsersService = {
    getTabItems: (detailFormData) => {
        return [
          { label: 'List', index: 0 },
          { label: detailFormData ? 'Update' : 'Create', index: 1 },
          { label: 'Import data', index: 2 }
        ];
    },

    dataSetupValue: () => {
        const dataSetup = {
            name: {
                disabled: false,
                type: 'text',
                label: 'name',
                field: 'name',
                required: true,
            },
            birthday: {
                disabled: false,
                type: 'date',
                label: 'birthday',
                field: 'dateOfBirth'
            },
            email: {
                disabled: false,
                type: 'text',
                label: 'email',
                field: 'email',
                required: true,
            },
            phone: {
                disabled: false,
                type: 'text',
                label: 'phone',
                field: 'phone',
                required: true,
            },
            university: {
                disabled: false,
                type: 'text',
                label: 'University',
                field: 'university'
            },
            universityCode: {
                disabled: false,
                type: 'text',
                label: 'University code',
                field: 'universityCode'
            }, 
            universityGraduateDate: {
                disabled: false,
                type: 'date',
                label: 'Graduated date',
                field: 'universityGraduateDate'
            },
            experienceDate: {
                disabled: true,
                type: 'text',
                label: 'exp',
                field: 'experienceDate'
            },
            rank: {
                disabled: false,
                type: 'select_rank',
                label: 'level',
                field: 'rank',
                required: true,
            },
            joined_date:{
                disabled: false,
                type: 'date',
                label: 'joined date',
                field: 'joinedDate'
            },
            workingTime: {
                disabled: true,
                type: 'text',
                label: 'duration of employment',
                field: 'workingTime'
            },
            department: {
                disabled: true,
                type: 'select_department',
                label: 'department',
                field: 'department'
            },
            team: {
                disabled: true,
                type: 'multi_select_team',
                label: 'team',
                field: 'userTeams'
            },
            status: {
                disabled: true,
                type: 'status_radio',
                label: 'status',
                field: 'status',
                required: true,
            },
            resignedDate: {
                disabled: false,
                type: 'date',
                label: 'resigned date',
                field: 'resignedDate'
            },
            skills: {
                disabled: false,
                type: 'textarea',
                label: 'skills',
                field: 'skills'
            },
            createdDate: {
                disabled: true,
                type: 'today_date',
                label: 'Created date',
                field: 'createdDate'
            },
            updatedDate: {
                disabled: true,
                type: 'today_date',
                label: 'Updated date',
                field: 'updatedDate'
            },
        };
        return dataSetup;
    },

    handleCancelEdit: (e,setDetailFormData, setForceRender, setReloadKey, handleTabValueProps, detailFormData) => {
        e.preventDefault(); 
        setDetailFormData();
        setForceRender(detailFormData);
        setReloadKey(prevCount => prevCount + 1);
        handleTabValueProps(0)
    },

    handleCreate: async (e, baseApiUrl, formData, setOpenNotification, setReloadKey, handleTabValueProps) => {
        e.preventDefault();
        await postUser(baseApiUrl, formData);
        await setOpenNotification(true);
        await setReloadKey(prevCount => prevCount + 1);
        await handleTabValueProps(0)
    },
    handleEdit: async (e, baseApiUrl, formData, setOpenNotification, setReloadKey, handleTabValueProps) => {
        e.preventDefault();
        if (formData.userTeams) {
            delete formData.userTeams;
        }
        await putUser(baseApiUrl, formData);
        await setOpenNotification(true);
        await setReloadKey(prevCount => prevCount + 1);
        await handleTabValueProps(0)
    },

    handleEditProcessing: (item, handleTabValueProps, setDetailFormData, setFormData) => {
        handleTabValueProps(1);
        if (item.userTeams) {
            item.teams = item.userTeams.map(teamObj => ({teamName: teamObj.team.teamName}));
        }
        setDetailFormData(item);
        setFormData(item);
    },

    fetchUser: async (apiUrl, page, size) => {
        try {
            return await fetchUsers(apiUrl, page, size)
        } catch (error) {
            throw Error(error)
        }
    },

    handleDeleteUser: async (apiUrl, data, setReloadKey) => {
        try {
            
            const res = await deleteUser(apiUrl, data[5])
            await setReloadKey(prevCount => prevCount + 1)
            return res;
        } catch (error) {
            throw Error(error)
        }
    },

    handleChangeStatus: async (apiUrl, id, status, UpdatedBy, setReloadKey) => {
        try {
            const res = await changeStatus(apiUrl, id, status, UpdatedBy)
            await setReloadKey(prevCount => prevCount + 1)
            return res;
        } catch (error) {
            throw Error(error)
        }
    }
};

export default UsersService;
