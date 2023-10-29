import { defineMessages } from 'react-intl';

export const scope = 'boilerplate.containers.Pages.LeaveType';

export default defineMessages({
    title: {
        id: `${scope}.title`,
        defaultMessage: "This module allow admins to create, view and update leave types"
    },
    name: {
        id: `${scope}.name`,
        defaultMessage: "Leave type"
    },
    desc: {
        id: `${scope}.desc`,
        defaultMessage: "Description"
    },
    affect: {
        id: `${scope}.affect`,
        defaultMessage: "Affect days off"
    },
    action: {
        id: `${scope}.action`,
        defaultMessage: "Action"
    },
    list: {
        id: `${scope}.list`,
        defaultMessage: "List"
    },
    create: {
        id: `${scope}.create`,
        defaultMessage: "Create"
    },
    update: {
        id: `${scope}.update`,
        defaultMessage: "Update"
    },
    notificationCreateSuccessfully: {
        id: `${scope}.notificationCreateSuccessfully`,
        defineMessages: "Great! a leave was created successfully"
    },
    notificationCreateFail: {
        id: `${scope}.notificationCreateFail`,
        defineMessages: "Fail! a leave was create failed"
    },
    notificationUpdateSuccessfully: {
        id: `${scope}.notificationUpdateSuccessfully`,
        defineMessages: "Great! a leave was edited successfully"
    },
    notificationUpdateFail: {
        id: `${scope}.notificationUpdateFail`,
        defineMessages: "Fail! a leave was edit failed"
    },
    notificationDeleteSuccessfully: {
        id: `${scope}.notificationDeleteSuccessfully`,
        defineMessages: "Great! a leave was deleted successfully"
    },
    notificationDeleteFail: {
        id: `${scope}.notificationDeleteFail`,
        defineMessages: "Fail! a leave was delete failed"
    }
});