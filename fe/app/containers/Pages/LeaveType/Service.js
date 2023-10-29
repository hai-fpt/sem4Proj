import {useIntl} from "react-intl";
import messages from "enl-api/leaveType/leaveTypeMessages";


const Service = {
    getTabItems: (detailFormData) => {
        const intl = useIntl();
        return [
            { label: intl.formatMessage(messages.list), index: 0 },
            { label: detailFormData ? intl.formatMessage(messages.update) : intl.formatMessage(messages.create), index: 1 }
        ];
    },

    formatAffectsDaysOff: value => {
      return value ? 'True' : 'False'
    },

    checkAffectsDaysOff: value => {
        return value === 'false' ? false : (value !== false)
    },

    handleCancelEdit: (e, setDetailFormData, setForceRender, setReloadKey, handleTabValueProps, detailFormData) => {
        e.preventDefault();
        setDetailFormData();
        setForceRender(detailFormData);
        setReloadKey(prevCount => prevCount + 1);
        handleTabValueProps(0)
    },

    handleFormSubmit: (e, setOpenNotification, setReloadKey, handleTabValueProps) => {
        e.preventDefault();
        setOpenNotification(true);
        setReloadKey(prevCount => prevCount + 1);
        handleTabValueProps(0)
    },

    handleEditProcessing: (item, handleTabValueProps, setDetailFormData, setFormData) => {
        const filteredData = item.filter((value) => value !== undefined);
        const [id, name, affectsDaysOff, description] = filteredData;
        const leaveObjectData = {
            id,
            name,
            affectsDaysOff,
            description,
        };
        handleTabValueProps(1);
        setDetailFormData(leaveObjectData)
        setFormData(leaveObjectData);
    },
};

export default Service;
