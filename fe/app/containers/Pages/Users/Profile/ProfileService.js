import { fetchProfile, updateProfile } from 'enl-api/user/myProfile.js';
import {injectIntl} from 'react-intl';
import messages from "enl-api/user/myProfileMessages"


const userProfileService = {
    dataSetupValue: (intl) => {
        const dataSetup = {
            name: {
                disabled: false,
                type: 'text',
                label: intl.formatMessage(messages.name),
                field: 'name',
                require: true,
            },
            birthday: {
                disabled: false,
                type: 'date',
                label: intl.formatMessage(messages.dob),
                field: 'dateOfBirth'
            },
            email: {
                disabled: true,
                type: 'text',
                label: intl.formatMessage(messages.email),
                field: 'email',
                require: true,
            },
            phone: {
                disabled: false,
                type: 'tel',
                label: intl.formatMessage(messages.phone),
                field: 'phone',
                require: true,
            },
            university: {
                disabled: false,
                type: 'text',
                label: intl.formatMessage(messages.university),
                field: 'university'
            },
            universityCode: {
                disabled: false,
                type: 'text',
                label: intl.formatMessage(messages.universityCode),
                field: 'universityCode'
            },
            universityGraduateDate: {
                disabled: false,
                type: 'date',
                label: intl.formatMessage(messages.graduate),
                field: 'universityGraduateDate'
            },
            experienceDate: {
                disabled: true,
                type: 'text',
                label: intl.formatMessage(messages.exp),
                field: 'experienceDate'
            },
            rank: {
                disabled: true,
                type: 'text',
                label: intl.formatMessage(messages.rank),
                field: 'rank'
            },
            joinedDate: {
                disabled: true,
                type: 'text',
                label: intl.formatMessage(messages.joined),
                field: 'joinedDate'
            },
            workingTime: {
                disabled: true,
                type: 'text',
                label: intl.formatMessage(messages.duration),
                field: 'workingTime'
            },
            department: {
                disabled: true,
                type: 'text',
                label: intl.formatMessage(messages.department),
                field: 'department'
            },
            userTeam: {
                disabled: true,
                type: 'text',
                label: intl.formatMessage(messages.team),
                field: 'userTeams'
            },
            status: {
                disabled: true,
                type: 'text',
                label: intl.formatMessage(messages.active),
                field: 'status'
            },
            resignedDate: {
                disabled: true,
                type: 'text',
                label: intl.formatMessage(messages.resign),
                field: 'resignedDate'
            },
            createdDate: {
                disabled: true,
                type: 'text',
                label: intl.formatMessage(messages.created),
                field: 'createdDate'
            },
            updatedBy: {
                disabled: true,
                type: 'text',
                label: intl.formatMessage(messages.updatedBy),
                field: 'updatedBy'
            },
            updatedDate: {
                disabled: true,
                type: 'text',
                label: intl.formatMessage(messages.updated),
                field: 'updatedDate'
            },
            skills: {
                disabled: false,
                type: 'textarea',
                label: intl.formatMessage(messages.skill),
                field: 'skills'
            }
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
        try {
          return await updateProfile(id, data, baseApiUrl)
        } catch (error) {
          throw new Error(error);
        }
    }

};

export default userProfileService;
