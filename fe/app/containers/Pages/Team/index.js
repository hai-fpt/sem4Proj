import React, {useEffect, useState} from 'react';
import {Helmet} from 'react-helmet';
import {
  ButtonGroup,
  CustomFormElement,
  CustomNotification,
  DataTableDropdownMenu,
  PapperBlock,
  TableOptionsSetup,
  TableOptionStyle,
  TabPanel,
  TabsNavigation,
  TruncateTypography,
} from 'enl-components';
import Paper from '@material-ui/core/Paper';
import Box from '@material-ui/core/Box';
import MUIDataTable from 'mui-datatables';
import TeamService from './TeamService'
import {useDispatch, useSelector} from 'react-redux';
import {setDepartments, setManager, setUsers} from "../../../redux/actions/teamActions";
import {injectIntl} from 'react-intl';
import messages from "enl-api/team/teamMessages";


const Team = ({intl}) => {
  const dispatch = useDispatch();
  const formDataSetup = TeamService.formDataSetup();
  const [reloadKey, setReloadKey] = useState(0);
  const [tabValue, setTabValue] = useState(0);
  const [formData, setFormData] = useState({
    teamName: '',
    manager: 0,
    createdDate: '', 
    description: '', 
    userList: [],
    updatedDate: '',
    updatedBy: '',
    department: ''
  })
  const [detailFormData, setDetailFormData] = useState();
  const [forceRender, setForceRender] = useState(false);
  const [openNotification, setOpenNotification] = useState(false);
  const tabItems = TeamService.getTabItems(detailFormData);
  const baseApiUrl = useSelector((state) => state.env.BASE_API_URL);
  const [data, setData] = useState([]);
  const [userListData, setUserListData] = useState([]);
  const userDetail = JSON.parse(localStorage.getItem('userDetail'));
  const manager = useSelector((state) => state.manager);
  const users = useSelector((state) => state.users);
  const departments = useSelector((state) => state.department);
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

  const handleEditProcessing = async (item) => {
    await teamUserList(item[0])
        .then(res => item[5] = res.data.content);
    await  TeamService.handleEditProcessing(item, handleTabValueProps, setDetailFormData, setFormData, userListData);

  };

  const teamUserList = async (id) => {
    return await TeamService.getUserList(id, baseApiUrl);
  };

  const handleDeleteProcessing = async (item) => {
    await deleteData(item[0]);
  }

  const deleteData = async (id) => {
    await TeamService.deleteTeam(id, baseApiUrl, setNotificationSeverity, setNotificationMessage, setOpenNotification, setReloadKey);
  };

  const postData = async () => {
    await TeamService.postTeam(formData, baseApiUrl, setNotificationSeverity, setNotificationMessage, setOpenNotification, handleTabValueProps);
  }

  const putData = async() => {
    await TeamService.putTeam(formData.id, formData, baseApiUrl, setNotificationSeverity, setNotificationMessage, setOpenNotification, handleTabValueProps);
  }

  const handleTeamFormSubmit = async (e) => {
    e.preventDefault();
    formData.updatedBy = userDetail.email;
    if (formData.id === undefined) {
     await postData();
    } else {
     await putData();
    }
    // await TeamService.handleTeamFormSubmit(e, setOpenNotification, setReloadKey, handleTabValueProps);
  };

  const handleCancelEdit = (e) => {
    TeamService.handleCancelEdit(e, 
      setDetailFormData, 
      setForceRender, 
      setReloadKey, 
      handleTabValueProps,
      detailFormData
    )
  }

  useEffect(() => {


    const fetchUserData = async () => {
      try {
        const response = await TeamService.getUsers(baseApiUrl);
        dispatch(setUsers(response));
      } catch (error) {
        throw new Error(error);
      }
    }

    const fetchDepartmentData = async () => {
      try {
        const res = await TeamService.getDepartments(baseApiUrl);
        dispatch(setDepartments(res))
      } catch (error) {
        throw new Error(error);
      }
    }

    let isMounted = true;
    const fetchData = async () => {
      const detail = await TeamService.getTeams(baseApiUrl);
      if (isMounted) {
        setData(detail.data.content);
      }
    };
    fetchUserData();
    fetchData();
    fetchDepartmentData();
  }, [formData, tabValue, detailFormData, reloadKey]);

  useEffect(() => {
    const fetchManagerData = async () => {
      try {
        const response = await TeamService.getManagers(baseApiUrl);
        dispatch(setManager(response));
      } catch (error) {
        console.error('Failed to fetch managers:', error);
      }
    };
    fetchManagerData();
  },[]);

  const columns = [
    {
      name: "id",
      options: {
        filter: true,
        ...TableOptionStyle(),
        display: false
      }
    },
    {
      name: "teamName",
      label: intl.formatMessage(messages.name),
      options: {
        filter: true,
        ...TableOptionStyle(),
      },
    },
    {
      name: "manager",
      label: intl.formatMessage(messages.manager),
      options: {
        filter: true,
        ...TableOptionStyle(),
        customBodyRender: (value) => value.name,
      },
    },
    {
      name: "createdDate",
      label: intl.formatMessage(messages.created),
      options: {
        filter: true,
        ...TableOptionStyle(),
        customBodyRender: (value) => {
          return TeamService.formatDate(value);
        },
      },
    },
    {
      name: "description",
      label: intl.formatMessage(messages.desc),
      options: {
        filter: true,
        customBodyRender: (value) => {
          return value === "" ? <p>{intl.formatMessage(messages.noDescription)}</p> : <TruncateTypography text={value} component="p" />;
        },
        ...TableOptionStyle(),
      },
    },
    {
      name: "updatedDate",
      options: {
        filter: false,
        display: "excluded",
      },
    },
    {
      name: "updatedBy",
      options: {
        filter: false,
        display: "excluded",
      },
    },
    {
      name: "department",
      label: "Department",
      options: {
        customBodyRender: (value) => {
          return value.name;
        }
      }
    },
    {
      name: "action",
      label: intl.formatMessage(messages.action),
      options: {
        download: false,
        csv: false,
        filter: false,
        customBodyRender: (_, rowValue) => {
          return <DataTableDropdownMenu delete edit ItemValue={rowValue} editProcessing={handleEditProcessing} deleteProcessing={handleDeleteProcessing}></DataTableDropdownMenu>;
        },
      },
    },
  ];

  return (
    <>
      <div key={reloadKey}>
        <Helmet>
          <title>Team management</title>
        </Helmet>
        <PapperBlock
          whiteBg
          icon="supervisor_account"
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
              <form onSubmit={handleTeamFormSubmit}>
                <Box p={2} display="grid"
                    gridTemplateColumns={['1fr', 'repeat(2, 1fr)']} // Responsive grid columns
                    gridRowGap={6} // Gap between rows
                    gridColumnGap={16} // Gap between columns
                >
                  <CustomFormElement
                      detailFormData={detailFormData}
                      valueFormProps={handleFormValueProps}
                      field={'teamName'}
                      label={intl.formatMessage(messages.name)}
                      isRequired
                      formElementType='text'
                  />

                  <CustomFormElement
                      detailFormData={detailFormData}
                      valueFormProps={handleFormValueProps}
                      field={'manager'}
                      label={intl.formatMessage(messages.manager)}
                      isRequired
                      formElementType='manager_select'
                  />

                  <CustomFormElement
                    detailFormData={detailFormData}
                    valueFormProps={handleFormValueProps}
                    field={'department'}
                    label={"Department"}
                    isRequired
                    formElementType={"select_department"}
                    />

                  <CustomFormElement
                      detailFormData={detailFormData}
                      valueFormProps={handleFormValueProps}
                      field={'createdDate'}
                      label={intl.formatMessage(messages.create)}
                      formElementType='today_date'
                      disabled
                  />

                  <CustomFormElement
                      detailFormData={detailFormData}
                      valueFormProps={handleFormValueProps}
                      field={'updatedDate'}
                      label={intl.formatMessage(messages.update)}
                      formElementType='today_date'
                      disabled
                  />

                  <CustomFormElement
                      detailFormData={detailFormData}
                      valueFormProps={handleFormValueProps}
                      field={'members'}
                      label={intl.formatMessage(messages.members)}
                      formElementType='multi_select_users'
                  />

                  {/*<CustomFormElement*/}
                  {/*    detailFormData={detailFormData}*/}
                  {/*    valueFormProps={handleFormValueProps}*/}
                  {/*    field={'updatedBy'}*/}
                  {/*    label={intl.formatMessage(messages.updatedBy)}*/}
                  {/*    formElementType='text'*/}
                  {/*    disabled*/}
                  {/*/>*/}

                  <CustomFormElement
                      detailFormData={detailFormData}
                      valueFormProps={handleFormValueProps}
                      field={'description'}
                      label={intl.formatMessage(messages.desc)}
                      formElementType='textarea'
                  /></Box>
                <ButtonGroup detail={detailFormData} handleCancelEdit={handleCancelEdit}></ButtonGroup>
              </form>
            </Box>
          </TabPanel>
        </Paper>

      </div>
      <CustomNotification
          open={openNotification}
          close={() => {setOpenNotification(false)}}
          notificationMessage={notificationMessage}
          severity={notificationSeverity}
      />
    </>
  );
}

export default injectIntl(Team);
