import { defineMessages } from 'react-intl';

export const scope = 'boilerplate.containers.Pages.Departments';

export default defineMessages({
    title: {
        id: `${scope}.title`,
        defaultMessage: "This module allow admins to create, view and update departments"
    },
    name: {
        id: `${scope}.name`,
        defaultMessage: "Department"
    },
    manager: {
        id: `${scope}.manager`,
        defaultMessage: "Manager"
    },
    created: {
        id: `${scope}.created`,
        defaultMessage: "Created Date"
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
    team: {
        id: `${scope}.team`,
        defineMessages: "Team"
    },
    notificationCreateSuccessfully: {
        id: `${scope}.notificationCreateSuccessfully`,
        defineMessages: "Great! a department was created successfully"
    },
    notificationCreateFail: {
        id: `${scope}.notificationCreateFail`,
        defineMessages: "Fail! a department was create failed"
    },
    notificationUpdateSuccessfully: {
        id: `${scope}.notificationUpdateSuccessfully`,
        defineMessages: "Great! a department was edited successfully"
    },
    notificationUpdateFail: {
        id: `${scope}.notificationUpdateFail`,
        defineMessages: "Fail! a department was edit failed"
    },
    notificationDeleteSuccessfully: {
        id: `${scope}.notificationDeleteSuccessfully`,
        defineMessages: "Great! a department was deleted successfully"
    },
    notificationDeleteFail: {
        id: `${scope}.notificationDeleteFail`,
        defineMessages: "Fail! a department was delete failed"
    }
});