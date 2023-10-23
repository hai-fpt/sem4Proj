import React, { useState, useEffect } from 'react';
import { Helmet } from 'react-helmet';
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
} from 'enl-components';
import Paper from '@material-ui/core/Paper';
import Box from '@material-ui/core/Box';
import MUIDataTable from 'mui-datatables';
import departmentService from './departmentService'
import {useDispatch, useSelector} from 'react-redux';
import {
  putDepartment,
  postDepartment,
  deleteDepartment,
  fetchManager,
  fetchDepartment, fetchTeam
} from "../../../api/department/department";
import {setManager, setTeam} from "../../../redux/actions/departmentActions";
import {injectIntl} from 'react-intl';
import messages from "enl-api/department/departmentMessages";


const Department = ({intl}) => {
  const [reloadKey, setReloadKey] = useState(0);
  const [tabValue, setTabValue] = useState(0);
  const [formData, setFormData] = useState({
    name: '',
    managerId: 0,
    teamList: [],
  })
  const [detailFormData, setDetailFormData] = useState();
  const [forceRender, setForceRender] = useState(false);
  const [openNotification, setOpenNotification] = useState(false);
  const tabItems = departmentService.getTabItems(detailFormData);
  const [data, setData] = useState([]);
  const dispatch = useDispatch();
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
  };

  const handleEditProcessing = (item) => {
    departmentService.handleEditProcessing(item, handleTabValueProps, setDetailFormData, setFormData);
  }

  const handleDepartmentFormSubmit = async (e) => {
    e.preventDefault();
    const { id } = formData;
    if (id === undefined) {
      await handlePostDepartment();
    }
    if (id !== undefined) {
      await handlePutDepartment();
    }
  }

  const handlePutDepartment = async () => {
    const { id } = formData;
    const body = {
      name: formData.name,
      managerId: formData.manager,
      teamList: formData.team,
    };
    await putDepartment(baseApiUrl, id, body, setOpenNotification, setNotificationMessage, setNotificationSeverity, handleTabValueProps, intl);
  };

  const handlePostDepartment = async () => {
    console.log('formData', formData)
    const body = {
      name: formData.name,
      managerId: formData.manager,
      teamList: formData.team,
    };
    await postDepartment(baseApiUrl, body, setOpenNotification, setNotificationMessage, setNotificationSeverity, handleTabValueProps, intl);
  };
  const handleCancelEdit = (e) => {
    departmentService.handleCancelEdit(e,
        setDetailFormData,
        setForceRender,
        setReloadKey,
        handleTabValueProps,
        detailFormData
    )
  }

  const handleDeleteProcessing = async (value) => {
    const id = value[0]
    await deleteDepartment(baseApiUrl, id, setOpenNotification, setNotificationMessage, setNotificationSeverity, setReloadKey, intl)
  }

  const baseApiUrl = useSelector(state => state.env.BASE_API_URL);

  useEffect(async () => { setData(await fetchDepartment(baseApiUrl))}, [tabValue, reloadKey]);

  useEffect(async () => { dispatch(setManager(await fetchManager(baseApiUrl))); dispatch(setTeam(await fetchTeam(baseApiUrl)))}, [])

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
      label: intl.formatMessage(messages.name),
      options: {
        filter: true,
        ...TableOptionStyle(),
      }
    },
    {
      name: 'teams',
      label: intl.formatMessage(messages.team),
      options: {
        filter: true,
        ...TableOptionStyle(),
        customBodyRender: (value) => {
          if (Array.isArray(value)) {
            return value.map((item, index) => (
                <React.Fragment key={index}>
                  {item.teamName}
                  {index !== value.length - 1 && ', '}
                </React.Fragment>
            ));
          }
          return value;
        },
      }
    },
    {
      name: 'manager',
      label: intl.formatMessage(messages.manager),
      options: {
        filter: true,
        ...TableOptionStyle(),
        customBodyRender: value => {
          return value.name;
        }
      }
    },
    {
      name: 'createdDate',
      label: intl.formatMessage(messages.created),
      options: {
        filter: true,
        ...TableOptionStyle(),
        customBodyRender: (value) => {
          return departmentService.formatDate(value);
        }
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
              <DataTableDropdownMenu delete edit ItemValue={rowValue} editProcessing={handleEditProcessing} deleteProcessing={handleDeleteProcessing}></DataTableDropdownMenu>
          )
        }
      }
    },
  ];

  return (
      <>
        <div key={reloadKey}>
          <Helmet>
            <title>Department management</title>
          </Helmet>
          <PapperBlock title="Department"
                       whiteBg
                       icon="account_balance"
                       desc={intl.formatMessage(messages.title)}
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
                 <div>Total: &nbsp; {data.length}</div>
              </Box>
            </TabPanel>

            <TabPanel tabIndex={1} tabValue={tabValue} forceRender={forceRender}>
              <Box p={3}>
                <form onSubmit={handleDepartmentFormSubmit}>
                  <CustomFormElement detailFormData={detailFormData} valueFormProps={handleFormValueProps} field={'name'} label={intl.formatMessage(messages.name)} isRequired formElementType='text' />
                  <CustomFormElement detailFormData={detailFormData} valueFormProps={handleFormValueProps} field={'manager'} label={intl.formatMessage(messages.manager)} isRequired formElementType={'department_manager_select'} />
                  <CustomFormElement detailFormData={detailFormData} valueFormProps={handleFormValueProps} field={'team'} label={intl.formatMessage(messages.team)} isRequired formElementType={'department_multi_select_team'} />
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

export default injectIntl(Department);
