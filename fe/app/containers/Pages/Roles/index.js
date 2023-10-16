import React, { useState, useEffect } from 'react';
import { Helmet } from 'react-helmet';
import { PapperBlock, 
  TabsNavigation, 
  TabPanel, 
  TruncateTypography, 
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
import rolesService from './RolesService';
import Grid from '@material-ui/core/Grid';
import {useSelector} from "react-redux";

const Role = () => {
  const [reloadKey, setReloadKey] = useState(0);
  const [tabValue, setTabValue] = useState(0);
  const [formData, setFormData] = useState({
    name: '',
    description: '',
  })
  const [data, setData] = useState([]);
  const [detailFormData, setDetailFormData] = useState();
  const [forceRender, setForceRender] = useState(false);
  const [openNotification, setOpenNotification] = useState(false);
  const tabItems = rolesService.getTabItems(detailFormData);
  const baseApiUrl = useSelector((state) => state.env.BASE_API_URL);

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
    rolesService.handleEditProcessing(item, handleTabValueProps, setDetailFormData);
  }

  const handleFormSubmit = (e) => {
    rolesService.handleFormSubmit(e, setOpenNotification, setReloadKey, handleTabValueProps);
  }

  const handleCancelEdit = (e) => {
    rolesService.handleCancelEdit(e,
      setDetailFormData,
      setForceRender,
      setReloadKey,
      handleTabValueProps,
      detailFormData
    )
  }

  useEffect(() => {
  }, [formData, tabValue, detailFormData]);

  useEffect(() => {
    let isMounted = true;
    const fetchData = async () => {
      const detail = await rolesService.getRoles(baseApiUrl);

      if (isMounted) {
        setData(detail.data.content);
      }
    };
    fetchData();
    return () => {
      isMounted = false;
    }
  }, [detailFormData,baseApiUrl])

  //data table setup
  const columns = [
    {
      name: "name",
      label: "Role's name",
      options: {
        filter: true,
        ...TableOptionStyle(),
      }
    },
    {
        name: 'description',
        label: "Description",
        options: {
          filter: true,
          customBodyRender: (value) => {
              if (!value) {
                return "No description";
              }
              return (
                  <TruncateTypography text={value} component={'p'}/>
              )},
          ...TableOptionStyle(),
        }
    },
    // {
    //   name: 'Action',
    //   options: {
    //     download: false,
    //     csv: false,
    //     filter: false,
    //     customBodyRender: (_, rowValue) => {
    //       return (
    //         <DataTableDropdownMenu delete edit ItemValue={rowValue} editProcessing={handleEditProcessing}></DataTableDropdownMenu>
    //     )},
    //   }
    // },
  ];

  return (
    <>
      <div key={reloadKey}>
        <Helmet>
          <title>Roles management</title>
        </Helmet>
        <PapperBlock title="Roles"
          whiteBg
          icon="works"
          desc="This module allows admins view the roles."
        >
          {/*<TabsNavigation tabItems={tabItems} tabValuePropsFromChild={handleTabValueProps} tabValuePropsFromParent={tabValue}></TabsNavigation>*/}
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
              <form onSubmit={handleFormSubmit}>
                <Grid container>
                  <Grid item xs={12} sm={6} >
                    <CustomFormElement detailFormData={detailFormData} valueFormProps={handleFormValueProps} field={'roles'} label={'Roles'} isRequired formElementType='text' />
                  </Grid>
                  <Grid item xs={12} sm={10}>
                    <CustomFormElement detailFormData={detailFormData} valueFormProps={handleFormValueProps} field={'description'} label={'Description'} formElementType='textarea' />
                  </Grid>
                </Grid>
                <ButtonGroup detail={detailFormData} handleCancelEdit={handleCancelEdit}></ButtonGroup>
              </form>
            </Box>
          </TabPanel>
        </Paper>
      </div>
      <CustomNotification
          open={openNotification}
          close={() => {setOpenNotification(false)}}
          notificationMessage={'Great! a role was created successfully.'}
          severity={'success'}
      />
    </>
  );
}

export default Role;
