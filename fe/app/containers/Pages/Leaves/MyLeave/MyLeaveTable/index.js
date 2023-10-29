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
import CircularProgress from "@material-ui/core/CircularProgress";
import {injectIntl} from 'react-intl';
import messages from "enl-api/myleave/myLeaveMessages";
import CloudUploadIcon from '@material-ui/icons/CloudUpload';
import PublishIcon from '@material-ui/icons/Publish';
import {Close} from '@material-ui/icons'
import Popup from "react-popup";
import EventInfoDialog from "../../../../../components/Calendar/EventInfoDialog";




const MyLeaveTable = ({intl}) => {
  const [openNotification, setOpenNotification] = useState(false);
  const [reloadKey, setReloadKey] = useState(0);
  const baseApiUrl = useSelector((state) => state.env.BASE_API_URL);
  const id = useSelector((state) => state.detailProfile.id)
  const email = useSelector((state) => state.detailProfile.email)
  const [tableData, setTableData] = useState();
  const [isLoading, setIsLoading] = useState(false);
  const [selectedFile, setSelectedFile] = useState(null);
  const [fileRowId, setFileRowId] = useState(0);

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
    setIsLoading(true)
    MyLeaveTableService.putCancel(baseApiUrl, requestId, email, setReloadKey, setIsLoading)
  }

  const handleFileChange = (e, rowId) => {
    setSelectedFile(e.target.files);
    setFileRowId(rowId);
  }

  const handleFileUpload = (file, id) => {
    MyLeaveTableService.postAttachment(file, id, email, baseApiUrl)
        .then(() => clearSelectedFile());
  }

  const clearSelectedFile = () => {
    setFileRowId(0);
    setSelectedFile(null);
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
      label: intl.formatMessage(messages.tableFromDate),
      options: {
        filter: true,
        ...TableOptionStyle(),
      }
    },
    {
      name: 'toDate',
      label: intl.formatMessage(messages.tableToDate),
      options: {
        filter: true,
        ...TableOptionStyle(),
      }
    },
    {
      name: "daysOff",
      label: intl.formatMessage(messages.daysOff),
      options: {
        filter: true,
        ...TableOptionStyle(),
      }
    },
    {
      name: "leave",
      label: intl.formatMessage(messages.leaveType),
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
      label: intl.formatMessage(messages.status),
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
      label: intl.formatMessage(messages.action),
      options: {
        download: false,
        csv: false,
        filter: false,
        sort: false,
        customBodyRender: (value, rowValue) => {
          return (
              value === "PENDING" ?
              (isLoading ? <CircularProgress /> :
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
                    >{intl.formatMessage(messages.cancel)}</Button>
                  </Box>) : null

          )
        }
      }
    },
    {
      name: "Action",
      label: intl.formatMessage(messages.fileUpload),
      options: {
        download: false,
        csv: false,
        filter: false,
        sort: false,
        customBodyRender: (value, rowValue) => {
          if (selectedFile == null) {
            return (
                <>
                  <label htmlFor="fileInput">
                    <CloudUploadIcon
                        style={{
                          marginLeft: "15px",
                          color: "#03a9f4",
                          cursor: "pointer",
                        }}
                    />
                  </label>
                  <input
                      type="file"
                      id="fileInput"
                      multiple
                      style={{display: "none"}}
                      onChange={(e) => handleFileChange(e, rowValue.rowData[0])}
                  />

                </>
            );
          } else if (selectedFile !== null && fileRowId === rowValue.rowData[0]) {
            return (
                <>
                  <PublishIcon
                      style={{
                        marginRight: "8px",
                        color: "#03a9f4",
                        cursor: "pointer",
                      }}
                      onClick={(e) => handleFileUpload(selectedFile, rowValue.rowData[0])}
                  />
                  <Close
                      style={{
                        marginLeft: "8px",
                        cursor: "pointer",
                      }}
                      onClick={clearSelectedFile}
                  />
                </>
            );
          } else {
            return null;
          }
        }
      }
    }
  ];


  return (
      <>
        <div style={{marginTop: 20}}>
          <Paper elevation={2}>
            <Box p={2}>
              {
                tableData?.length ?
                    <>
                      <MUIDataTable
                          data={tableData ? tableData : []}
                          columns={columns}
                          options={{
                            ...TableOptionsSetup,
                            onCellClick: async (colData, cellMeta) => {
                              if (cellMeta.colIndex > 5) {
                                return;
                              }
                              const tableDataSet = tableData;
                              const data = tableDataSet[cellMeta.dataIndex];
                              if (!data) {
                                return;
                              }
                              Popup.create({
                                title: intl.formatMessage(messages.popupTitle),
                                content: (
                                    <EventInfoDialog
                                        eventInfo={{
                                          id: data.id,
                                          status: data.status,
                                          start: data.fromDate,
                                          end: data.toDate,
                                          file: data.attachedFiles,
                                          reason: data.reason
                                        }}
                                        tabDefault={0}
                                    />
                                )
                              })

                            },
                            customFooter: tableData?.length ? null : () => null
                          }}
                      />
                      <div>Total: &nbsp; {tableData?.length}</div>
                    </>
                    :
                    <p style={{textAlign:"center"}}>
                      No records found
                    </p>
              }

            </Box>
          </Paper>
        </div>
        <Popup/>
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

export default injectIntl(MyLeaveTable);
