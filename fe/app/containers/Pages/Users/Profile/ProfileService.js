import { fetchProfile, updateProfile } from 'enl-api/user/myProfile.js';

const userProfileService = {
    dataSetupValue: () => {
        const dataSetup = {
            name: {
                disabled: false,
                type: 'text',
                label: 'name',
                field: 'name',
                require: true,
            },
            birthday: {
                disabled: false,
                type: 'date',
                label: 'birthday',
                field: 'dateOfBirth'
            },
            email: {
                disabled: true,
                type: 'text',
                label: 'email',
                field: 'email',
                require: true,
            },
            phone: {
                disabled: false,
                type: 'tel',
                label: 'phone',
                field: 'phone',
                require: true,
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
                disabled: true,
                type: 'text',
                label: 'rank',
                field: 'rank'
            },
            joinedDate:{
                disabled: true,
                type: 'text',
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
                type: 'text',
                label: 'department',
                field: 'department'
            },
            userTeam: {
                disabled: true,
                type: 'text',
                label: 'team',
                field: 'userTeams'
            },
            status: {
                disabled: true,
                type: 'text',
                label: 'Active',
                field: 'status'
            },
            resignedDate: {
                disabled: true,
                type: 'text',
                label: 'resigned date',
                field: 'resignedDate'
            },
            createdDate: {
                disabled: true,
                type: 'text',
                label: 'Created date',
                field: 'createdDate'
            },
            updatedBy: {
                disabled: true,
                type: 'text',
                label: 'Updated by',
                field: 'updatedBy'
            },
            updatedDate: {
                disabled: true,
                type: 'text',
                label: 'Updated date',
                field: 'updatedDate'
            },
            skills: {
                disabled: false,
                type: 'textarea',
                label: 'skills',
                field: 'skills'
            },
        };
        return dataSetup;
    },
    getProfile: async (id, baseApiUrl) => {
        try {
            return await fetchProfile(id, baseApiUrl)
        } catch (error) {
            throw Error('Get user Detail failed:')
        }
    },
    updateProfile: async (id, data, baseApiUrl) => {
        console.log(data);
        // try {
        //   return await updateProfile(id, data, baseApiUrl)
        // } catch (error) {
        //   throw new Error(error);
        // }
    }

};

export default userProfileService;
