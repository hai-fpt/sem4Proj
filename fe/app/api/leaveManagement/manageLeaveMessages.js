import { defineMessages } from 'react-intl';

export const scope = 'boilerplate.containers.Pages.ManageLeave';

export default defineMessages({
    title: {
        id: `${scope}.title`,
        defaultMessage: "This module allows admins and managers to manage leave requests"
    },
    name: {
        id: `${scope}.name`,
        defaultMessage: "Name"
    },
    overallStatus: {
        id: `${scope}.overallStatus`,
        defaultMessage: "Overall Status"
    },
    type: {
        id: `${scope}.type`,
        defaultMessage: "Leave Type"
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
    action: {
        id: `${scope}.action`,
        defaultMessage: "Action"
    },
    list: {
        id: `${scope}.list`,
        defaultMessage: "List"
    },
    calendar: {
        id: `${scope}.calendar`,
        defaultMessage: "Calendar"
    },
    rejectReason: {
        id: `${scope}.rejectReason`,
        defaultMessage: "Reject reason"
    },
    approve: {
        id: `${scope}.approve`,
        defaultMessage: "Approve"
    },
    reject: {
        id: `${scope}.reject`,
        defaultMessage: "Reject"
    },
    cancel: {
        id: `${scope}.cancel`,
        defaultMessage: "Cancel"
    },
    formTitle: {
        id: `${scope}.formTitle`,
        defaultMessage: "Leave Application Form"
    },
    information: {
        id: `${scope}.information`,
        defaultMessage: "Information"
    },
    comment: {
        id: `${scope}.comment`,
        defaultMessage: "Comment"
    },
    requestedBy: {
        id: `${scope}.requestedBy`,
        defaultMessage: "Requested By"
    },
    status: {
        id: `${scope}.status`,
        defaultMessage: "Status"
    },
    manager: {
        id: `${scope}.manager`,
        defaultMessage: "Managers"
    },
    statusOptions: {
        approved: {
            id: `${scope}.approved`,
            defaultMessage: "APPROVED"
        },
        cancelled: {
            id: `${scope}.cancelled`,
            defaultMessage: "CANCELLED"
        },
        pending: {
            id: `${scope}.pending`,
            defaultMessage: "PENDING"
        },
        rejected: {
            id: `${scope}.rejected`,
            defaultMessage: "REJECTED"
        }
    },
    files: {
        id: `${scope}.files`,
        defaultMessage: "File(s)"
    },
    submit: {
        id: `${scope}.submit`,
        defaultMessage: "Submit"
    },
    popupTitle: {
        id: `${scope}.popup.title`,
        defaultMessage: "Leave Application Form"
    }
});
