import React, { useState, useEffect } from 'react';
import {
  TabsNavigation,
  TabPanel,
  DataTableDropdownMenu,
  CustomFormElement,
  DateTimeRangePicker,
  CustomNotification,
  TableOptionStyle,
  ButtonGroup,
  TableOptionsSetup,
  StatusChip,
} from 'enl-components';
import Paper from '@material-ui/core/Paper';
import Box from '@material-ui/core/Box';
import MUIDataTable from 'mui-datatables';
import MyLeaveTableService from './MyLeaveTableService';
import {useSelector} from 'react-redux';
import Button from "@material-ui/core/Button";


const MyLeaveTable = () => {
  const [openNotification, setOpenNotification] = useState(false);
  const [reloadKey, setReloadKey] = useState(0);
  const baseApiUrl = useSelector((state) => state.env.BASE_API_URL);
  const id = useSelector((state) => state.detailProfile.id)
  const email = useSelector((state) => state.detailProfile.email)
  const [tableData, setTableData] = useState();

  useEffect(() => {
    const fetchRequestLeaveData = async () => {
      if (baseApiUrl && id) {
        try {
          const response = await MyLeaveTableService.fetchRequest(baseApiUrl, id, 0, 10);
          setTableData(response.data.content);
        } catch (error) {
          console.error('Failed to fetch leave data:', error);
        }
      }
    };
    fetchRequestLeaveData();
  }, [baseApiUrl, id, reloadKey])

  const requestDecision = (requestId) => {
    MyLeaveTableService.putCancel(baseApiUrl, requestId, email, setReloadKey)
  }

  //data table setup
  const columns = [
    {
      name: 'id',
      options: {
        display: false
      }
    },
    {
      name: 'fromDate',
      label: "From date",
      options: {
        filter: true,
        ...TableOptionStyle(),
      }
    },
    {
      name: 'toDate',
      label: "To date",
      options: {
        filter: true,
        ...TableOptionStyle(),
      }
    },
    {
      name: "daysOff",
      label: "Day-off Duration",
      options: {
        filter: true,
        ...TableOptionStyle(),
      }
    },
    {
      name: "leave",
      label: "Leave Type",
      options: {
        filter: true,
        ...TableOptionStyle(),
        customBodyRender: (value) => {
          return value.name
        }
      }
    },
    {
      name: "status",
      label: "Status",
      options: {
        filter: true,
        ...TableOptionStyle(),
        customBodyRender: (value) => {
          return (
              <StatusChip status={value}/>
          )
        }
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
              value !== "CANCELLED" &&
              <Box display={"flex"} gridColumnGap={8}>
                <Button
                    onClick={() => {
                      requestDecision(rowValue.rowData[0])
                    }}
                    variant={"contained"}
                    size={"medium"}
                    style={{
                      textTransform: "capitalize",
                      lineHeight: "16px",
                      color: "white",
                      backgroundColor: "#808080"
                    }}
                >Cancel</Button>
              </Box>

          )
        }
        // customBodyRender: (_, rowValue) => {
        //   return (
        //     <DataTableDropdownMenu delete submit ItemValue={rowValue}></DataTableDropdownMenu>
        // )},
      }
    },
  ];


  return (
      <>
        <div style={{marginTop: 20}}>
          <Paper elevation={2}>
            <Box p={2}>
              <MUIDataTable
                  data={tableData ? tableData : []}
                  columns={columns}
                  options={TableOptionsSetup}
              />
              <div>Total: &nbsp; {tableData?.length}</div>
            </Box>
          </Paper>
        </div>
        <CustomNotification
            open={openNotification}
            close={() => {
              setOpenNotification(false)
            }}
            notificationMessage={'Great! a holiday was created successfully.'}
            severity={'success'}
        />
      </>
  );
}

export default MyLeaveTable;
