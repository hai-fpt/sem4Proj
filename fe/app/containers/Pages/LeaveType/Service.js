const Service = {
    getTabItems: (detailFormData) => {
        return [
            { label: 'List', index: 0 },
            { label: detailFormData ? 'Update' : 'Create', index: 1 }
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
        const holidayObjectData = {
            id,
            name,
            affectsDaysOff,
            description,
        };
        handleTabValueProps(1);
        setDetailFormData(holidayObjectData)
        setFormData(holidayObjectData);
    },
};

export default Service;
