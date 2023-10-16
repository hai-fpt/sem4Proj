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
import MUIDataTable from 'mui-datatables';
import LeavesManagementService from './service';
import {useSelector, useDispatch} from 'react-redux';
import Popup from "react-popup";
import EventInfoDialog from "../../../../components/Calendar/EventInfoDialog";

const LeaveManagement = () => {
    const dispatch = useDispatch();
    const [reloadKey, setReloadKey] = useState(0);
    const [tabValue, setTabValue] = useState(0);
    const [forceRender, setForceRender] = useState(false);
    const [openNotification, setOpenNotification] = useState(false);
    const [tableData, setTableData] = useState([]);
    const tabItems = LeavesManagementService.getTabItems();
    const baseApiUrl = useSelector((state) => state.env.BASE_API_URL);
    const detailProfile = useSelector((state) => state.detailProfile)

    const handleTabValueProps = (propsFromChild) => {
        setTabValue(propsFromChild)
    };

    const requestDecision = (decision, requestId) => {
        LeavesManagementService.handleDecision(baseApiUrl,
            decision,
            requestId,
            detailProfile.id,
            detailProfile.email,
            setReloadKey,
        ).then(() => {
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

    useEffect(() => {
        fetchLeaveRequestData();
    }, [reloadKey, detailProfile])

    useEffect(() => {
    }, [tabValue]);

    //data table setup
    const columns = [
        {
            name: "userLeave",
            label: 'Name',
            options: {
                filter: true,
                ...TableOptionStyle(),
                customBodyRender: (value) => {
                    return value.user.name
                },
            }
        },
        {
            name: "userLeave",
            label: 'Overall Status',
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
            name: "status",
            label: 'Status',
            options: {
                filter: false,
                sort: false,
                ...TableOptionStyle(),
                customBodyRender: (value) => {
                    return <StatusChip status={value}/>;
                },
            },
        },
        {
            name: "status",
            label: 'Status',
            options: {
                filter: true,
                viewColumns: false,
                display: 'excluded',
                ...TableOptionStyle(),
                customBodyRender: (value) => {
                    return value.status;
                },
            },
        },
        {
            name: "userLeave",
            label: 'Overall Status',
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
            label: 'Type',
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
            label: 'From Date',
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
            label: 'To Date',
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
            label: 'Reason',
            options: {
                filter: false,
                ...TableOptionStyle(),
                customBodyRender: (value) => {
                    return value.reason ?
                        value.reason
                        : 'No reason included...';
                },
            }
        },
        {
            name: 'status',
            label: 'Action',
            options: {
                download: false,
                csv: false,
                filter: false,
                customBodyRender: (value, rowValue) => {
                    return (
                        value === 'PENDING' &&
                        <Box display={'flex'} gridColumnGap={8}>
                            <Button
                                onClick={() => {
                                    requestDecision('APPROVED', rowValue.rowData[0].id)
                                }}
                                variant="contained"
                                size='small'
                                style={{
                                    textTransform: 'capitalize',
                                    lineHeight: '16px',
                                    color: 'white',
                                    backgroundColor: '#4CAF50',
                                }}
                            >
                                Approve
                            </Button>
                            <Button
                                onClick={() => {
                                    requestDecision('REJECTED', rowValue.rowData[0].id)
                                }}
                                variant="contained"
                                size='small'
                                style={{
                                    textTransform: 'capitalize',
                                    lineHeight: '16px',
                                    color: 'white',
                                    backgroundColor: '#e65100',
                                }}
                            >
                                Reject
                            </Button>
                        </Box>
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
                             desc="This module allows admins and manager to manage leave requests."
                >
                    <TabsNavigation tabItems={tabItems} tabValuePropsFromChild={handleTabValueProps}
                                    tabValuePropsFromParent={tabValue}></TabsNavigation>
                </PapperBlock>
                <Paper elevation={2}>
                    <TabPanel tabIndex={0} tabValue={tabValue}>
                        <Box p={2}>
                            <MUIDataTable
                                data={tableData}
                                columns={columns}
                                options={{
                                    ...TableOptionsSetup,
                                    onCellClick : (colData, cellMeta) => {
                                        // action coll
                                        if(cellMeta.colIndex === 9) {
                                            return;
                                        }
                                        const data = tableData[cellMeta.dataIndex]
                                        if (!data)
                                            return;
                                        Popup.create({
                                            title: data.userLeave?.user?.name,
                                            content: <EventInfoDialog eventInfo={data.userLeave} tabDefault={1}
                                                                      requestDecision={requestDecision}/>,
                                        });
                                    }
                                }}
                            />
                            <div>Total: &nbsp; {tableData.length}</div>
                        </Box>
                    </TabPanel>

                    <TabPanel tabIndex={1} tabValue={tabValue} forceRender={forceRender}>
                        <Box p={0}>
                            <LeaveManagementCalendar baseApiUrl={baseApiUrl} id={detailProfile.id}
                                                     requestDecision={requestDecision}/>
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

export default LeaveManagement;
