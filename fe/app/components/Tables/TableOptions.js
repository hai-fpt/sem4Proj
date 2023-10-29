const options = {
    filterType: 'dropdown',
    responsive: 'standard',
    print: false,
    download: false,
    filter: false,
    viewColumns: false,
    rowsPerPage: 10,
    searchOpen: true,
    search: true,
    page: 0,
    elevation: 0,
    selectToolbarPlacement: 'none',
    selectableRows: 'none',
    setTableProps: () => {
        return {
            padding: 'none'
        };
    },
};

export default options;