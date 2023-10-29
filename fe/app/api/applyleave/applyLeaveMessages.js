import { defineMessages } from 'react-intl';

export const scope = 'boilerplate.containers.Pages.ApplyLeave';

export default defineMessages({
    title: {
        id: `${scope}.title`,
        defaultMessage: "Request for leave"
    },
    name: {
        id: `${scope}.name`,
        defaultMessage: "Full name"
    },
    type: {
        id: `${scope}.type`,
        defaultMessage: "Leave Type"
    },
    request: {
        id: `${scope}.request`,
        defaultMessage: "Request to"
    },
    inform: {
        id: `${scope}.inform`,
        defaultMessage: "Inform to"
    },
    fileSize: {
        id: `${scope}.fileSize`,
        defaultMessage: "File size should not exceed 10MB"
    },
    from: {
        id: `${scope}.from`,
        defaultMessage: "From Date"
    },
    to: {
        id: `${scope}.to`,
        defaultMessage: "To Date"
    },
    reason: {
        id: `${scope}.reason`,
        defaultMessage: "Reason"
    },
    cancel: {
        id: `${scope}.cancel`,
        defaultMessage: "Cancel"
    },
    save: {
        id: `${scope}.save`,
        defaultMessage: "Save"
    }

});