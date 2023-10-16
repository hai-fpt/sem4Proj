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
  fetchDepartment
} from "../../../api/department/department";
import {setManager} from "../../../redux/actions/departmentActions";


const Department = () => {
  const [reloadKey, setReloadKey] = useState(0);
  const [tabValue, setTabValue] = useState(0);
  const [formData, setFormData] = useState({
    name: '',
    managerId: 0,
  })
  const [detailFormData, setDetailFormData] = useState();
  const [forceRender, setForceRender] = useState(false);
  const [openNotification, setOpenNotification] = useState(false);
  const tabItems = departmentService.getTabItems(detailFormData);
  const [data, setData] = useState([]);
  const dispatch = useDispatch();

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

  const handleDepartmentFormSubmit = (e) => {
    e.preventDefault();
    const { id } = formData;
    if (id === undefined) {
      handlePostDepartment();
    }
    if (id !== undefined) {
      handlePutDepartment();
    }
  }

  const handlePutDepartment = async () => {
    const { id } = formData;
    const body = {
      name: formData.name,
      managerId: formData.manager,
    };
    await putDepartment(baseApiUrl, id, body);
    setOpenNotification(true);
    setReloadKey((prevCount) => prevCount + 1);
    handleTabValueProps(0);
  };

  const handlePostDepartment = async () => {
    const body = {
      name: formData.name,
      managerId: formData.manager,
    };
    await postDepartment(baseApiUrl, body);
    setOpenNotification(true);
    setReloadKey((prevCount) => prevCount + 1);
    handleTabValueProps(0);
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
    await deleteDepartment(baseApiUrl, id)
  }

  const baseApiUrl = useSelector(state => state.env.BASE_API_URL);

  useEffect(async () => { setData(await fetchDepartment(baseApiUrl))}, [formData, tabValue, detailFormData]);

  useEffect(async () => { dispatch(setManager(await fetchManager(baseApiUrl)))}, [] )

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
      name: 'manager',
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
  // const data = [
  //   ['Gio to Hung Vuong', 'Wed Jun 4 2023 14:18:15 GMT+0700 (Indochina Time)', 'Wed Jun 5 2023 14:18:15 GMT+0700 (Indochina Time)', 'The Hùng Kings Temple Festival (Vietnamese: Giỗ Tổ Hùng Vương or Lễ hội đền Hùng) is a Vietnamese festival held annually from the 8th to the 11th day of the third lunar month in honour of the'],
  //   ['Giang Sinh', 'Wed Jun 4 2023 14:18:15 GMT+0700 (Indochina Time)' , 'Wed Jun 5 2023 14:18:15 GMT+0700 (Indochina Time)', 'Christmas is an annual festival commemorating the birth of Jesus Christ, observed primarily on December 25[a] as a religious and cultural celebration among billions of people around the world'],
  //   ['Tet co truyen', 'Wed Jun 4 2023 14:18:15 GMT+0700 (Indochina Time)' , 'Wed Jun 5 2023 14:18:15 GMT+0700 (Indochina Time)', ''],
  //   ['Linh tinh', 'Wed Jun 4 2023 14:18:15 GMT+0700 (Indochina Time)' , 'Wed Jun 5 2023 14:18:15 GMT+0700 (Indochina Time)', ''],
  //   ['Gio to Hung Vuong', 'Wed Jun 4 2023 14:18:15 GMT+0700 (Indochina Time)', 'Wed Jun 5 2023 14:18:15 GMT+0700 (Indochina Time)', 'The Hùng Kings Temple Festival (Vietnamese: Giỗ Tổ Hùng Vương or Lễ hội đền Hùng) is a Vietnamese festival held annually from the 8th to the 11th day of the third lunar month in honour of the'],
  //   ['Giang Sinh', 'Wed Jun 4 2023 14:18:15 GMT+0700 (Indochina Time)' , 'Wed Jun 5 2023 14:18:15 GMT+0700 (Indochina Time)', 'Christmas is an annual festival commemorating the birth of Jesus Christ, observed primarily on December 25[a] as a religious and cultural celebration among billions of people around the world'],
  //   ['Gio to Hung Vuong', 'Wed Jun 4 2023 14:18:15 GMT+0700 (Indochina Time)', 'Wed Jun 5 2023 14:18:15 GMT+0700 (Indochina Time)', 'The Hùng Kings Temple Festival (Vietnamese: Giỗ Tổ Hùng Vương or Lễ hội đền Hùng) is a Vietnamese festival held annually from the 8th to the 11th day of the third lunar month in honour of the'],
  //   ['Giang Sinh', 'Wed Jun 4 2023 14:18:15 GMT+0700 (Indochina Time)' , 'Wed Jun 5 2023 14:18:15 GMT+0700 (Indochina Time)', 'Christmas is an annual festival commemorating the birth of Jesus Christ, observed primarily on December 25[a] as a religious and cultural celebration among billions of people around the world'],
  //   ['Gio to Hung Vuong', 'Wed Jun 4 2023 14:18:15 GMT+0700 (Indochina Time)', 'Wed Jun 4 2023 14:18:15 GMT+0700 (Indochina Time)', 'The Hùng Kings Temple Festival (Vietnamese: Giỗ Tổ Hùng Vương or Lễ hội đền Hùng) is a Vietnamese festival held annually from the 8th to the 11th day of the third lunar month in honour of the'],
  //   ['Giang Sinh', 'Wed Jun 4 2023 14:18:15 GMT+0700 (Indochina Time)' , 'Wed Jun 5 2023 14:18:15 GMT+0700 (Indochina Time)', 'Christmas is an annual festival commemorating the birth of Jesus Christ, observed primarily on December 25[a] as a religious and cultural celebration among billions of people around the world'],
  // ];

  return (
      <>
        <div key={reloadKey}>
          <Helmet>
            <title>Department management</title>
          </Helmet>
          <PapperBlock title="Department"
                       whiteBg
                       icon="local_airport"
                       desc="This module allows admins to view and update department."
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
                 {/*<div>Total: &nbsp; {data.length}</div> */}
              </Box>
            </TabPanel>

            <TabPanel tabIndex={1} tabValue={tabValue} forceRender={forceRender}>
              <Box p={3}>
                <form onSubmit={handleDepartmentFormSubmit}>
                  <CustomFormElement detailFormData={detailFormData} valueFormProps={handleFormValueProps} field={'name'} label={'Name'} isRequired formElementType='text' />
                  <CustomFormElement detailFormData={detailFormData} valueFormProps={handleFormValueProps} field={'manager'} label={'Manager'} isRequired formElementType={'department_manager_select'} />
                  <ButtonGroup detail={detailFormData} handleCancelEdit={handleCancelEdit}></ButtonGroup>
                </form>
              </Box>
            </TabPanel>
          </Paper>
        </div>
        <CustomNotification
            open={openNotification}
            close={() => { setOpenNotification(false) }}
            notificationMessage={'Great! a department was created successfully.'}
            severity={'success'}
        />
      </>
  );
}

export default Department;
