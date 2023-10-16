import React, { useState, useEffect } from 'react';
import { Helmet } from 'react-helmet';
import {
  PapperBlock,
  TabsNavigation,
  TabPanel,
  TruncateTypography,
  DataTableDropdownMenu,
  CustomFormElement,
  DateRangePicker,
  CustomNotification,
  TableOptionStyle,
  ButtonGroup,
  TableOptionsSetup,
} from 'enl-components';
import Paper from '@material-ui/core/Paper';
import Box from '@material-ui/core/Box';
import MUIDataTable from 'mui-datatables';
import holidayService from './holidayService'
import { useSelector } from 'react-redux';
import { fetchVerify } from 'enl-api/holidays/holiday';
import {putHoliday, postHoliday, deleteHoliday} from "../../../api/holidays/holiday";
import moment from "moment";


const Holiday = () => {
  const [reloadKey, setReloadKey] = useState(0);
  const [tabValue, setTabValue] = useState(0);
  const [formData, setFormData] = useState({
    holiday: '',
    fromDate: new Date(),
    toDate: new Date(),
    description: '',
  })
  const [detailFormData, setDetailFormData] = useState();
  const [forceRender, setForceRender] = useState(false);
  const [openNotification, setOpenNotification] = useState(false);
  const tabItems = holidayService.getTabItems(detailFormData);
  const [data, setData] = useState();
  const [notificationMessage, setNotificationMessage] = useState('');
  const [notificationSeverity, setNotificationSeverity] = useState('');

  const handleTabValueProps = (propsFromChild) => {
    setTabValue(propsFromChild)
  };

  const handleFormValueProps = (data, field) => {
    setFormData((prevFormData) => ({
      ...prevFormData,
      [field]: data,
    }));
    setDetailFormData((prevFormData) => ({
      ...prevFormData,
      [field]: data,
    }));
  };

  const handleEditProcessing = (item) => {
    holidayService.handleEditProcessing(item, handleTabValueProps, setDetailFormData, setFormData);
  }

  const handleHolidayFormSubmit = (e) => {
    e.preventDefault();
    const { id } = formData;
    if (id === undefined) {
      handlePostHoliday();
    }
    if (id !== undefined) {
      handlePutHoliday();
    }
  }

  const handlePutHoliday = async () => {
    const { id } = formData;
    const body = {
      name: formData.name,
      fromDate: moment(formData.fromDate).format('YYYY-MM-DD HH:mm:ss'),
      toDate: moment(formData.toDate).format('YYYY-MM-DD HH:mm:ss'),
      description: formData.description,
    };
    await putHoliday(baseApiUrl, id, body, setNotificationMessage, setOpenNotification, handleTabValueProps, setNotificationSeverity);
  };

  const handlePostHoliday = async () => {
    const body = {
      name: formData.name,
      fromDate: moment(formData.fromDate).format('YYYY-MM-DD HH:mm:ss'),
      toDate: moment(formData.toDate).format('YYYY-MM-DD HH:mm:ss'),
      description: formData.description,
    };
    await postHoliday(baseApiUrl, body, setNotificationMessage, setOpenNotification, handleTabValueProps, setNotificationSeverity);
  };
  const handleCancelEdit = (e) => {
    holidayService.handleCancelEdit(e,
        setDetailFormData,
        setForceRender,
        setReloadKey,
        handleTabValueProps,
        detailFormData
    )
  }

  const handleDeleteProcessing = async (value) => {
    const id = value[0]
    await deleteHoliday(baseApiUrl, id, setNotificationMessage, setOpenNotification, setNotificationSeverity, setReloadKey)
  }

  const baseApiUrl = useSelector(state => state.env.BASE_API_URL);

  useEffect(async () => { setData(await fetchVerify(baseApiUrl)) }, [tabValue, reloadKey]);

  //data table setup
  const columns = [
    {
      name: 'id',
      options: {
        filter: true,
        ...TableOptionStyle(),
        display: false
      }
    },
    {
      name: "name",
      options: {
        filter: true,
        ...TableOptionStyle(),
      }
    },
    {
      name: 'fromDate',
      options: {
        filter: true,
        ...TableOptionStyle(),
        customBodyRender: (value) => {
          return holidayService.formatDate(value);
        }
      }
    },
    {
      name: 'toDate',
      options: {
        filter: true,
        ...TableOptionStyle(),
        customBodyRender: (value) => {
          return holidayService.formatDate(value);
        }
      }
    },
    {
      name: 'description',
      options: {
        filter: true,
        customBodyRender: (value) => (
            <TruncateTypography text={value} component={'p'} />
        ),
        ...TableOptionStyle(),
      }
    },
    {
      name: 'Action',
      options: {
        download: false,
        csv: false,
        filter: false,
        customBodyRender: (_, rowValue) => {
          return (
              <DataTableDropdownMenu delete edit ItemValue={rowValue} editProcessing={handleEditProcessing} deleteProcessing={handleDeleteProcessing}></DataTableDropdownMenu>
          )
        },
      }
    },
  ];

  return (
      <>
        <div key={reloadKey}>
          <Helmet>
            <title>Holiday management</title>
          </Helmet>
          <PapperBlock title="Holidays"
                       whiteBg
                       icon="local_airport"
                       desc="This module allows admins to view and update holidays."
          >
            <TabsNavigation tabItems={tabItems} tabValuePropsFromChild={handleTabValueProps} tabValuePropsFromParent={tabValue}></TabsNavigation>
          </PapperBlock>
          <Paper elevation={2} >
            <TabPanel tabIndex={0} tabValue={tabValue}>
              <Box p={2}>
                <MUIDataTable
                    data={data}
                    columns={columns}
                    options={TableOptionsSetup}
                />
                {/* <div>Total: &nbsp; {data.length}</div> */}
              </Box>
            </TabPanel>

            <TabPanel tabIndex={1} tabValue={tabValue} forceRender={forceRender}>
              <Box p={3}>
                <form onSubmit={handleHolidayFormSubmit}>
                  <CustomFormElement detailFormData={detailFormData} valueFormProps={handleFormValueProps} field={'name'} label={'Name'} isRequired formElementType='text' />
                  <DateRangePicker detailFormData={detailFormData} valueFormProps={handleFormValueProps} fromDateisRequired toDateisRequired />
                  <CustomFormElement detailFormData={detailFormData} valueFormProps={handleFormValueProps} field={'description'} label={'Description'} formElementType='textarea' />
                  <ButtonGroup detail={detailFormData} handleCancelEdit={handleCancelEdit}></ButtonGroup>
                </form>
              </Box>
            </TabPanel>
          </Paper>
        </div>
        <CustomNotification
            open={openNotification}
            close={() => { setOpenNotification(false) }}
            notificationMessage={notificationMessage}
            severity={notificationSeverity}
        />
      </>
  );
}

export default Holiday;
