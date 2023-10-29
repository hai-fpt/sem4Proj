import {useIntl} from "react-intl";
import messages from "enl-api/holidays/holidayMessages";

const holidayService = {
    getTabItems: (detailFormData) => {
        const intl = useIntl();
        return [
            { label: intl.formatMessage(messages.list), index: 0 },
            { label: detailFormData ? intl.formatMessage(messages.update) : intl.formatMessage(messages.create), index: 1 }
        ];
    },

    formatDate: (datetime) => {
        const date = new Date(datetime);
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = date.getFullYear();
        return `${day}-${month}-${year}`;
    },

    handleCancelEdit: (e, setDetailFormData, setForceRender, setReloadKey, handleTabValueProps, detailFormData) => {
        e.preventDefault();
        setDetailFormData();
        setForceRender(detailFormData);
        setReloadKey(prevCount => prevCount + 1);
        handleTabValueProps(0)
    },

    handleHolidayFormSubmit: (e, setOpenNotification, setReloadKey, handleTabValueProps) => {
        e.preventDefault();
        setOpenNotification(true);
        setReloadKey(prevCount => prevCount + 1);
        handleTabValueProps(0)
    },

    handleEditProcessing: (item, handleTabValueProps, setDetailFormData, setFormData) => {
        const filteredData = item.filter((value) => value !== undefined);
        const [id, name, fromDate, toDate, description] = filteredData;
        const holidayObjectData = {
            id,
            name,
            fromDate,
            toDate,
            description,
        };
        handleTabValueProps(1);
        setDetailFormData(holidayObjectData);
        setFormData(holidayObjectData);
    },
};

export default holidayService;
