import { defineMessages } from 'react-intl';

export const scope = 'boilerplate.containers.Pages.Team';

export default defineMessages({
    title: {
        id: `${scope}.title`,
        defaultMessage: "This module allows admins to view and update teams"
    },
    name: {
        id: `${scope}.name`,
        defaultMessage: "Team name"
    },
    manager: {
        id: `${scope}.manager`,
        defaultMessage: "Manager"
    },
    created: {
        id: `${scope}.created`,
        defaultMessage: "Created Date"
    },
    desc: {
        id: `${scope}.desc`,
        defaultMessage: "Description"
    },
    noDescription: {
        id: `${scope}.noDesc`,
        defaultMessage: "No Description"
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
    updatedBy: {
        id: `${scope}.updatedBy`,
        defaultMessage: "Updated By"
    },
    members: {
        id: `${scope}.members`,
        defaultMessage: "Members"
    },
    notificationCreateSuccessfully: {
        id: `${scope}.notificationCreateSuccessfully`,
        defineMessages: "Great! a team was created successfully"
    },
    notificationCreateFail: {
        id: `${scope}.notificationCreateFail`,
        defineMessages: "Fail! a team was create failed"
    },
    notificationUpdateSuccessfully: {
        id: `${scope}.notificationUpdateSuccessfully`,
        defineMessages: "Great! a team was updated successfully"
    },
    notificationUpdateFail: {
        id: `${scope}.notificationUpdateFail`,
        defineMessages: "Fail! a team was updated failed"
    },
    notificationDeleteSuccessfully: {
        id: `${scope}.notificationDeleteSuccessfully`,
        defineMessages: "Great! a team was deleted successfully"
    },
    notificationDeleteFail: {
        id: `${scope}.notificationDeleteFail`,
        defineMessages: "Fail! a team was delete failed"
    },
});