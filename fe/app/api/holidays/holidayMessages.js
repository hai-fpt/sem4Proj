import { defineMessages } from 'react-intl';

export const scope = "boilerplate.containers.Pages.Holiday";

export default defineMessages({
    title: {
        id: `${scope}.title`,
        defaultMessage: "This module allows admins to view and update holidays"
    },
    name: {
        id: `${scope}.name`,
        defaultMessage: "Name"
    },
    from: {
        id: `${scope}.from`,
        defaultMessage: "From date"
    },
    to: {
        id: `${scope}.to`,
        defaultMessage: "To date"
    },
    desc: {
        id: `${scope}.desc`,
        defaultMessage: "Description"
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
        defineMessages: "Great! a holiday was created successfully"
    },
    notificationCreateFail: {
        id: `${scope}.notificationCreateFail`,
        defineMessages: "Fail! a holiday was create failed"
    },
    notificationUpdateSuccessfully: {
        id: `${scope}.notificationUpdateSuccessfully`,
        defineMessages: "Great! a holiday was edited successfully"
    },
    notificationUpdateFail: {
        id: `${scope}.notificationUpdateFail`,
        defineMessages: "Fail! a holiday was edit failed"
    },
    notificationDeleteSuccessfully: {
        id: `${scope}.notificationDeleteSuccessfully`,
        defineMessages: "Great! a holiday was deleted successfully"
    },
    notificationDeleteFail: {
        id: `${scope}.notificationDeleteFail`,
        defineMessages: "Fail! a holiday was delete failed"
    }
});