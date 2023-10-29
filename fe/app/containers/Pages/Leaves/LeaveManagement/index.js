import React, {useState, useEffect} from 'react';
import {format} from 'date-fns';
import {Helmet} from 'react-helmet';
import {
    PapperBlock,
    TabsNavigation,
    TabPanel,
    CustomNotification,
    TableOptionStyle,
    TableOptionsSetup,
    StatusChip,
    LeaveManagementCalendar
} from 'enl-components';
import Paper from '@material-ui/core/Paper';
import Box from '@material-ui/core/Box';
import Button from '@material-ui/core/Button';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import Typography from '@material-ui/core/Typography';
import MUIDataTable from 'mui-datatables';
import LeavesManagementService from './service';
import {resetLeaveState} from 'enl-redux/actions/leaveManageActions'
import {useSelector, useDispatch} from 'react-redux';
import Popup from "react-popup";
import EventInfoDialog from "../../../../components/Calendar/EventInfoDialog";
import CircularProgress from "@material-ui/core/CircularProgress";
import RejectReason from "./rejectReason";
import {Done, Close} from '@material-ui/icons'
import {fetchLeavesManagers} from "../../../../api/leaveManagement";
import {injectIntl} from 'react-intl';
import messages from "enl-api/leaveManagement/manageLeaveMessages";

const LeaveManagement = ({intl}) => {
    const dispatch = useDispatch();
    const [reloadKey, setReloadKey] = useState(0);
    const [tabValue, setTabValue] = useState(0);
    const [openNotification, setOpenNotification] = useState(false);
    const [tableData, setTableData] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [rejectReason, setRejectReason] = useState("");
    const [listManagers, setListManagers] = useState([]);
    const tabItems = LeavesManagementService.getTabItems();
    const baseApiUrl = useSelector((state) => state.env.BASE_API_URL);
    const detailProfile = useSelector((state) => state.detailProfile);
    const dayLeaves = useSelector((state) => state.manageLeaves);

    const handleTabValueProps = (propsFromChild) => {
        setTabValue(propsFromChild)
    };

    const rejectDecision = (decision, requestId) => {
        let eventInfo = {
            id: requestId,
            decision: decision
        }
        Popup.create({
            title: intl.formatMessage(messages.rejectReason),
            content: <RejectReason eventInfo={eventInfo} requestDecision={requestDecision}/>
        })
    }

    const requestDecision = (decision, requestId, rejectReason) => {
        setIsLoading(true);
        LeavesManagementService.handleDecision(baseApiUrl,
            decision,
            requestId,
            detailProfile.id,
            rejectReason,
            detailProfile.email,
            setReloadKey,
        ).then(() => {
            setIsLoading(false)
            Popup.close()
        })

    };

    const fetchLeaveRequestData = async () => {
        if (!detailProfile.id) {
            return;
        }
        try {
            const response = await LeavesManagementService.handleFetchLeaves(baseApiUrl, detailProfile.id, 10, 0);
            setTableData(response.data.content);
        } catch (error) {
            console.error('Failed to fetch users data:', error);
        }
    };

    const fetchLeavesManagers = async (id) => {
        try {
            return await LeavesManagementService.handleFetchManagers(baseApiUrl, id);
        } catch (e) {
            throw new Error(e);
        }
    }

    const resetLeaveDay = () => {
        dispatch(resetLeaveState());
        setTabValue(1);
        setReloadKey(prevCount => prevCount + 1);
    }

    useEffect(() => {
        fetchLeaveRequestData();
    }, [reloadKey, detailProfile])

    useEffect(() => {
    }, [tabValue]);
    //data table setup
    const columns = [
        {
            name: "userLeave",
            label: intl.formatMessage(messages.name),
            options: {
                filter: true,
                ...TableOptionStyle({minWidth: 150}),
                customBodyRender: (value) => {
                    return value.user.name
                },
            }
        },
        {
            name: "userLeave",
            label: intl.formatMessage(messages.overallStatus),
            options: {
                filter: false,
                sort: false,
                ...TableOptionStyle(),
                customBodyRender: (value) => {
                    return <StatusChip status={value.status}/>;
                },
            },
        },
        {
            name: "userLeave",
            options: {
                filter: true,
                viewColumns: false,
                display: 'excluded',
                ...TableOptionStyle(),
                customBodyRender: (value) => {
                    return value.status
                },
            },
        },
        {
            name: 'userLeave',
            label: intl.formatMessage(messages.type),
            options: {
                filter: true,
                ...TableOptionStyle(),
                customBodyRender: (value) => {
                    return value.leave.name;
                },
            }
        },
        {
            name: 'userLeave',
            label: intl.formatMessage(messages.from),
            options: {
                filter: true,
                ...TableOptionStyle(),
                customBodyRender: (value) => {
                    return value.fromDate;
                },
            }
        },
        {
            name: 'userLeave',
            label: intl.formatMessage(messages.to),
            options: {
                filter: true,
                ...TableOptionStyle(),
                customBodyRender: (value) => {
                    return value.toDate;
                },
            }
        },
        {
            name: 'userLeave',
            label: intl.formatMessage(messages.reason),
            options: {
                filter: false,
                ...TableOptionStyle(),
                customBodyRender: (value) => {
                    return value.reason ? value.reason : 'No reason included...';
                },
            }
        },
        {
            name: 'status',
            label: intl.formatMessage(messages.action),
            options: {
                download: false,
                csv: false,
                filter: false,
                customBodyRender: (value, rowValue) => {
                    return (
                        value === 'PENDING' ? (
                            isLoading ? <CircularProgress/> :
                                <Box display={'flex'} gridColumnGap={8}>
                                    <Tooltip title={intl.formatMessage(messages.approve)} placement={"top"}>
                                        <IconButton
                                            onClick={() => {
                                                requestDecision('APPROVED', rowValue.rowData[0].id)
                                            }}
                                            variant="contained"
                                            size='small'
                                        >
                                            <Done/>
                                        </IconButton>
                                    </Tooltip>
                                    <Tooltip title={intl.formatMessage(messages.reject)} placement={"top"}>
                                        <IconButton
                                            onClick={() => {
                                                rejectDecision("REJECTED", rowValue.rowData[0].id)
                                            }}
                                            variant="contained"
                                            size='small'
                                        >
                                            <Close/>
                                        </IconButton>
                                    </Tooltip>
                                </Box>
                        ) : null
                    )
                },
            }
        },
    ];

    return (
        <>
            <div key={reloadKey}>
                <Helmet>
                    <title>Leaves management</title>
                </Helmet>
                <PapperBlock title="Leaves management"
                             whiteBg
                             icon="flight_takeoff"
                             desc={intl.formatMessage(messages.title)}
                >
                    <TabsNavigation tabItems={tabItems} tabValuePropsFromChild={handleTabValueProps}
                                    tabValuePropsFromParent={tabValue}></TabsNavigation>
                </PapperBlock>
                <Paper elevation={2}>
                    <TabPanel tabIndex={0} tabValue={tabValue}>
                        {
                            dayLeaves &&
                            <Box p={2} display={'flex'} justifyContent={'flex-end'} gridGap={20}
                                 alignItems={'baseline'}>
                                <p>The leaves request is scheduled for <Typography component={'span'}
                                                                                   color='primary'>{dayLeaves?.date}</Typography>
                                </p>
                                <Button onClick={() => resetLeaveDay()} variant='text' color='primary'>Reset</Button>
                            </Box>
                        }
                        <Box p={2}>
                            <MUIDataTable
                                data={dayLeaves?.data ? dayLeaves.data : tableData}
                                columns={columns}
                                options={{
                                    ...TableOptionsSetup,
                                    onCellClick: async (colData, cellMeta) => {
                                        // action coll
                                        if (cellMeta.colIndex >6) {
                                            return;
                                        }
                                        const tableDataSet = dayLeaves?.data ? dayLeaves.data : tableData
                                        const data = tableDataSet[cellMeta.dataIndex]
                                        if (!data)
                                            return;
                                        try {
                                            const response = await fetchLeavesManagers(data.userLeave.id);
                                            const managersData = response.data;
                                            setListManagers(managersData);

                                            Popup.create({
                                                title: intl.formatMessage(messages.popupTitle),
                                                content: (
                                                    <EventInfoDialog
                                                        eventInfo={{
                                                            //...data.userLeave,
                                                            id: data.userLeave.id,
                                                            status: data.status,
                                                            overallStatus: data.userLeave.status,
                                                            title: data.userLeave?.user?.name,
                                                            start: data.userLeave?.fromDate,
                                                            end: data.userLeave?.toDate,
                                                            file: data.userLeave.attachedFiles,
                                                            reason: data.userLeave.reason,
                                                            managers: managersData
                                                        }}
                                                        tabDefault={0}
                                                        requestDecision={requestDecision}
                                                    />
                                                ),
                                            });
                                    } catch (e) {
                                            throw new Error(e)
                                        }
                                    }
                                }}
                            />
                            <div>Total: &nbsp; {dayLeaves?.data ? dayLeaves.data.length : tableData.length}</div>
                        </Box>
                    </TabPanel>

                    <TabPanel tabIndex={1} tabValue={tabValue}>
                        <Box p={0}>
                            <LeaveManagementCalendar baseApiUrl={baseApiUrl}
                                                     id={detailProfile.id}
                                                     requestDecision={requestDecision}
                                                     handleTabValueProps={handleTabValueProps}
                            />
                        </Box>
                    </TabPanel>
                </Paper>
            </div>
            <Popup/>
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

export default injectIntl(LeaveManagement);
