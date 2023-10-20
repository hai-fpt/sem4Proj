import React, {useState, useEffect} from 'react';
import {format} from 'date-fns';
import {Helmet} from 'react-helmet';
import {
    PapperBlock,
    TabsNavigation,
    TabPanel,
    DataTableDropdownMenu,
    CustomFormElement,
    CustomNotification,
    TableOptionStyle,
    ButtonGroup,
    TableOptionsSetup,
    FileUploader,
} from 'enl-components';
import Paper from '@material-ui/core/Paper';
import Box from '@material-ui/core/Box';
import MUIDataTable from 'mui-datatables';
import UsersService from './service';
import Person from '@material-ui/icons/Person';
import {setTeam} from 'enl-redux/actions/teamActions';
import {setDepartment} from 'enl-redux/actions/departmentActions'
import {fetchTeam} from 'enl-api/team/teamApi';
import {fetchDepartment} from 'enl-api/department/department';
import {useSelector, useDispatch} from 'react-redux';
import moment from "moment";
import {injectIntl} from 'react-intl';
import messages from "enl-api/user/manageUserMessages";

const UsersManagement = ({intl}) => {
    const dispatch = useDispatch();
    const dataSetup = UsersService.dataSetupValue();
    const [reloadKey, setReloadKey] = useState(0);
    const [tabValue, setTabValue] = useState(0);
    const [formData, setFormData] = useState(
        {
            name: '',
            dateOfBirth: '',
            email: '',
            phone: '',
            university: '',
            universityCode: '',
            universityGraduateDate: '',
            skills: '',
            experienceDate: '',
            rank: 'EMPLOYEE',
            joinedDate: format(new Date(), 'yyyy-MM-dd HH:mm:ss'),
            workingTime: '',
            department: 'VDC',
            teams: [],
            status: '',
            resignedDate: format(new Date(), 'yyyy-MM-dd HH:mm:ss'),
            createdDate: format(new Date(), 'yyyy-MM-dd HH:mm:ss'),
            updatedDate: '',
        }
    )
    const [detailFormData, setDetailFormData] = useState();
    const [forceRender, setForceRender] = useState(false);
    const [openNotification, setOpenNotification] = useState(false);
    const [tableData, setTableData] = useState([]);
    const tabItems = UsersService.getTabItems(detailFormData);
    const baseApiUrl = useSelector((state) => state.env.BASE_API_URL);
    const UpdatedBy = useSelector((state) => state.detailProfile.email)
    const userId = useSelector((state) => state.detailProfile.id);
    const [userTab, setUserTab] = useState(0)
    const userRole = useSelector(state => state.detailProfile.userRoles)?.map(item => item.role.name);
    const handleTabValueProps = (propsFromChild) => {
        setTabValue(propsFromChild)
    };

    const handleFormValueProps = (data, field) => {
        setFormData((prevFormData) => ({
            ...prevFormData,
            [field]: data,
        }));
    };

    const handleEditProcessing = (item) => {
        UsersService.handleEditProcessing(item, handleTabValueProps, setDetailFormData, setFormData);
    }

    const handleEdit = (e) => {
        UsersService.handleEdit(e, baseApiUrl, formData, setOpenNotification, setReloadKey, UpdatedBy, handleTabValueProps);
    }

    const handleCreate = (e) => {
        UsersService.handleCreate(e, baseApiUrl, formData, setOpenNotification, setReloadKey, handleTabValueProps);
    }

    const handleCancelEdit = (e) => {
        UsersService.handleCancelEdit(e,
            setDetailFormData,
            setForceRender,
            setReloadKey,
            handleTabValueProps,
            detailFormData
        )
    }

    const deleteProcessing = async (id) => {
        try {
            await UsersService.handleDeleteUser(baseApiUrl, id, setReloadKey)
        } catch (error) {
            throw new Error(error);
        }
    }

    const changeStatus = async (id, status) => {
        try {
            await UsersService.handleChangeStatus(baseApiUrl, id, status, UpdatedBy, setReloadKey)
        } catch (error) {
            throw new Error(error);
        }
    }

    useEffect(() => {
        //dispath Team data
        const fetchTeamData = async () => {
            try {
                const response = await fetchTeam(baseApiUrl);
                await dispatch(setTeam(response.data));
            } catch (error) {
                console.error('Failed to fetch team:', error);
            }
        };
        const fetchDepartmentData = async () => {
            try {
                const response = await fetchDepartment(baseApiUrl);
                await dispatch(setDepartment(response));
            } catch (error) {
                console.error('Failed to fetch team:', error);
            }
        };
        fetchTeamData();
        fetchDepartmentData();
    }, [])

    useEffect(() => {
        if (userId !== undefined) {
            const fetchUsersData = async () => {
                try {
                    const response = await UsersService.fetchUser(baseApiUrl, userId, 0, 10);
                    setTableData(response.data.content);
                } catch (error) {
                    console.error('Failed to fetch users data:', error);
                }
            };
            fetchUsersData();
        }
    }, [reloadKey, userId])

    useEffect(() => {
    }, [formData, tabValue, detailFormData]);

    //data table setup
    const columns = [
        {
            name: "name",
            label: intl.formatMessage(messages.name),
            options: {
                filter: true,
                ...TableOptionStyle({ minWidth: 170, whiteSpace: "nowrap" }),
                customBodyRender: (value, rowValue) => {
                    return (
                        <Box display={'flex'} gridGap={'8px'} justifyContent={'flex-start'} alignItems={'flex-end'}>
                            <Person color={rowValue?.rowData[5] === true ? 'primary' : 'inherit'} />
                            {value}
                        </Box>
                    )
                },
            }
        },
        {
            name: "email",
            label: intl.formatMessage(messages.email),
            options: {
                filter: true,
                ...TableOptionStyle({ minWidth: 170, whiteSpace: "nowrap" }),
            }
        },
        {
            name: 'userTeams',
            label: intl.formatMessage(messages.team),
            options: {
                filter: true,
                ...TableOptionStyle(),
                customBodyRender: (value) => {
                    if (!value)
                        return null;
                    if (Array.isArray(value)) {
                        return value[0]?.team?.teamName;
                    }
                    return value?.toString();
                },
            }
        },
        {
            name: 'joinedDate',
            label: intl.formatMessage(messages.joined),
            options: {
                filter: true,
                ...TableOptionStyle(),
            }
        },
        {
            name: 'joinedDate',
            label: intl.formatMessage(messages.duration),
            options: {
                filter: true,
                ...TableOptionStyle(),
                customBodyRender: (value) => {
                    if (!value)
                        return null;
                    const totalMonths = Number.parseInt(moment().diff(value, 'months', true))
                    const year = Number.parseInt(totalMonths / 12)
                    const month = totalMonths % 12
                    return `${year} years - ${month} months `
                }
            }
        },
        {
            name: "status",
            options: {
                filter: false,
                display: 'excluded'
            }
        },
        {
            name: "id",
            options: {
                filter: false,
                display: 'excluded'
            }
        },
        {
            name: "userRoles",
            options: {
              filter: false,
              display: "excluded"
            }
        },
        {
            name: 'Action',
            label: intl.formatMessage(messages.action),
            options: {
                download: false,
                csv: false,
                filter: false,
                customBodyRender: (_, rowValue) => {
                    return (
                        <DataTableDropdownMenu delete edit status fullItem={tableData[rowValue.rowIndex]}
                                               ItemValue={rowValue} changeStatus={changeStatus}
                                               deleteProcessing={deleteProcessing}
                                               editProcessing={() => handleEditProcessing(tableData[rowValue.rowIndex])}></DataTableDropdownMenu>
                    )
                },
            }
        },
    ];
    useEffect(() => {
        if (tabValue === 0) {
            handleCancelEdit()
        }
    }, [tabValue])

    return (
        <>
            <div key={reloadKey}>
                <Helmet>
                    <title>Users management</title>
                </Helmet>
                <PapperBlock title="Users management"
                             whiteBg
                             icon="supervisor_account"
                             desc={intl.formatMessage(messages.title)}
                >
                    <TabsNavigation tabItems={tabItems} tabValuePropsFromChild={handleTabValueProps}
                                    tabValuePropsFromParent={tabValue}></TabsNavigation>
                </PapperBlock>
                <Paper elevation={2}>
                    <TabPanel tabIndex={0} tabValue={tabValue}>
                        <Box p={2}>
                            <TabsNavigation tabItems={[
                                {
                                    label: intl.formatMessage(messages.active),
                                    index: 0
                                },
                                {
                                    label: intl.formatMessage(messages.inactive),
                                    index: 1
                                },

                            ]} tabValuePropsFromChild={setUserTab}
                                            tabValuePropsFromParent={userTab}></TabsNavigation>
                            <MUIDataTable
                                data={tableData.filter(user =>  userTab === 0 ? user.status : !user.status)}
                                columns={columns}
                                options={TableOptionsSetup}
                            />
                            <div>Total: &nbsp; {tableData.length}</div>
                        </Box>
                    </TabPanel>

                    <TabPanel tabIndex={1} tabValue={tabValue} forceRender={forceRender}>
                        <Box p={2}>
                            <form onSubmit={detailFormData ? handleEdit : handleCreate}>
                                <Box display="grid"
                                     gridTemplateColumns={['1fr', 'repeat(2, 1fr)', 'repeat(3, 1fr)']} // Responsive grid columns
                                     gridRowGap={6} // Gap between rows
                                     gridColumnGap={24} // Gap between columns
                                >
                                    {/*{*/}
                                    {/*    Object.entries(dataSetup).map(([key, dataSetupItem]) => {*/}
                                    {/*        return (*/}
                                    {/*            <CustomFormElement*/}
                                    {/*                key={key}*/}
                                    {/*                isRequired={dataSetupItem.required}*/}
                                    {/*                detailFormData={detailFormData}*/}
                                    {/*                valueFormProps={handleFormValueProps}*/}
                                    {/*                field={dataSetupItem.field}*/}
                                    {/*                label={dataSetupItem.label}*/}
                                    {/*                formElementType={dataSetupItem.type}*/}
                                    {/*                disabled={dataSetupItem.disabled}*/}
                                    {/*            />*/}
                                    {/*        )*/}
                                    {/*    })*/}
                                    {/*}*/}
                                    {
                                        Object.entries(dataSetup).map(([key, dataSetupItem]) => {
                                            if (key === "userRole" && !userRole?.includes('ADMIN')) {
                                                return null;
                                            } else {
                                                return (
                                                    <CustomFormElement
                                                        key={key}
                                                        isRequired={dataSetupItem.required}
                                                        detailFormData={detailFormData}
                                                        valueFormProps={handleFormValueProps}
                                                        field={dataSetupItem.field}
                                                        label={dataSetupItem.label}
                                                        formElementType={dataSetupItem.type}
                                                        disabled={dataSetupItem.disabled}
                                                    />
                                                )
                                            }
                                        })
                                    }
                                </Box>
                                <ButtonGroup detail={detailFormData} handleCancelEdit={handleCancelEdit}></ButtonGroup>
                            </form>
                        </Box>
                    </TabPanel>

                    <TabPanel tabIndex={2} tabValue={tabValue} forceRender={forceRender}>
                        <Box p={3}>
                            <FileUploader/>
                        </Box>
                    </TabPanel>
                </Paper>
            </div>
            <CustomNotification
                open={openNotification}
                close={() => {
                    setOpenNotification(false)
                }}
                notificationMessage={'Great! an user was created successfully.'}
                severity={'success'}
            />
        </>
    );
}

export default injectIntl(UsersManagement);
