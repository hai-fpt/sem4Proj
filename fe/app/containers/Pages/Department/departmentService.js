import {useIntl} from "react-intl";
import messages from "enl-api/department/departmentMessages";

const departmentService = {
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
        const [id, name, team, manager] = filteredData;
        const teamId = team.map(team => team.id)
        const departmentObjectData = {
            id: id,
            name: name,
            team: teamId,
            manager: manager.id,
        };
        handleTabValueProps(1);
        setDetailFormData(departmentObjectData);
        setFormData(departmentObjectData);
    },
};

export default departmentService;
