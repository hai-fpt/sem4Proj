import React, { useState, useEffect } from "react";
import { Helmet } from "react-helmet";
import {
  PapperBlock,
  TabsNavigation,
  TabPanel,
  TruncateTypography,
  DataTableDropdownMenu,
  CustomFormElement,
  CustomNotification,
  TableOptionStyle,
  ButtonGroup,
  TableOptionsSetup,
} from "enl-components";
import Paper from "@material-ui/core/Paper";
import Box from "@material-ui/core/Box";
import MUIDataTable from "mui-datatables";
import Service from "./Service";
import Grid from "@material-ui/core/Grid";
import {putLeave, postLeave, fetchVerify, deleteLeave} from "../../../api/leaveType/leaveType";
import { useSelector } from "react-redux";

const Leave = () => {
  const [reloadKey, setReloadKey] = useState(0);
  const [tabValue, setTabValue] = useState(0);
  const [formData, setFormData] = useState({
    name: "",
    affectsDaysOff: true,
    description: "",
  });
  const [detailFormData, setDetailFormData] = useState();
  const [forceRender, setForceRender] = useState(false);
  const [openNotification, setOpenNotification] = useState(false);
  const tabItems = Service.getTabItems(detailFormData);
  const [data, setData] = useState();
  const baseApiUrl = useSelector((state) => state.env.BASE_API_URL);

  const handleTabValueProps = (propsFromChild) => {
    setTabValue(propsFromChild);
  };

  const handlePutLeave = async () => {
    const { id } = formData;
    const body = {
      name: formData.name,
      affectsDaysOff: Service.checkAffectsDaysOff(formData.affectsDaysOff),
      description: formData.description,
    };
    await putLeave(baseApiUrl, id, body);
    setOpenNotification(true);
    setReloadKey((prevCount) => prevCount + 1);
    handleTabValueProps(0);
  };

  const handlePostLeave = async () => {
    const body = {
      name: formData.name,
      affectsDaysOff: Service.checkAffectsDaysOff(formData.affectsDaysOff),
      description: formData.description,
    };
    await postLeave(baseApiUrl, body);
    setOpenNotification(true);
    setReloadKey((prevCount) => prevCount + 1);
    handleTabValueProps(0);
  };

  const handleFormValueProps = (data, field) => {
    setFormData((prevFormData) => ({
      ...prevFormData,
      [field]: data,
    }));
  };

  const handleEditProcessing = (item) => {
    Service.handleEditProcessing(
        item,
        handleTabValueProps,
        setDetailFormData,
        setFormData
    );
  };

  const handleFormSubmit = (e) => {
    e.preventDefault();
    const { id } = formData;
    if (id === undefined) {
      handlePostLeave();
    }
    if (id !== undefined) {
      handlePutLeave();
    }
  };

  const handleCancelEdit = (e) => {
    Service.handleCancelEdit(
        e,
        setDetailFormData,
        setForceRender,
        setReloadKey,
        handleTabValueProps,
        detailFormData
    );
  };

  const handleDeleteProcessing = async (value) => {
    const id = value[0]
    await deleteLeave(baseApiUrl, id)
  }

  useEffect(async () => {
    setData(await fetchVerify(baseApiUrl));
  }, [formData, tabValue, detailFormData]);

  //data table setup
  const columns = [
    {
      name: "id",
      options: {
        filter: true,
        ...TableOptionStyle(),
        display: false,
      },
    },
    {
      name: "name",
      options: {
        filter: true,
        ...TableOptionStyle(),
      },
    },
    {
      name: "affectsDaysOff",
      options: {
        filter: true,
        customBodyRender: value => {
          return Service.formatAffectsDaysOff(value)
        },
        ...TableOptionStyle(),
      },
    },
    {
      name: "description",
      options: {
        filter: true,
        customBodyRender: (value) => (
            <TruncateTypography text={value} component={"p"} />
        ),
        ...TableOptionStyle(),
      },
    },
    {
      name: "Action",
      options: {
        download: false,
        csv: false,
        filter: false,
        customBodyRender: (_, rowValue) => {
          return (
              <DataTableDropdownMenu
                  delete
                  edit
                  ItemValue={rowValue}
                  editProcessing={handleEditProcessing}
                  deleteProcessing={handleDeleteProcessing}
              ></DataTableDropdownMenu>
          );
        },
      },
    },
  ];
  // const data = [
  //   ["just a leave type", 'just a description'],
  //   ["just a leave type", 'just a description'],
  //   ["just a leave type", 'just a description'],
  //   ["just a leave type", 'just a description'],
  //   ["just a leave type", 'just a description'],
  //   ["just a leave type", 'just a description']
  // ];

  return (
      <>
        <div key={reloadKey}>
          <Helmet>
            <title>Leave type management</title>
          </Helmet>
          <PapperBlock
              title="Leave type"
              whiteBg
              icon="works"
              desc="This module allows admins to create, view and update leave type."
          >
            <TabsNavigation
                tabItems={tabItems}
                tabValuePropsFromChild={handleTabValueProps}
                tabValuePropsFromParent={tabValue}
            ></TabsNavigation>
          </PapperBlock>
          <Paper elevation={2}>
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
                <form onSubmit={handleFormSubmit}>
                  <Grid>
                    <Grid item xs={12} sm={6}>
                      <CustomFormElement
                          detailFormData={detailFormData}
                          valueFormProps={handleFormValueProps}
                          field={"name"}
                          label={"Name"}
                          isRequired
                          formElementType="text"
                      />
                    </Grid>
                    <Grid item xs={12} sm={6}>
                      <CustomFormElement
                        detailFormData={detailFormData}
                        valueFormProps={handleFormValueProps}
                        field={"affectsDaysOff"}
                        label={"Affects Days Off"}
                        isRequired
                        formElementType="selectAffectsDaysOff"
                      />
                    </Grid>
                    <Grid item xs={12} sm={10}>
                      <CustomFormElement
                          detailFormData={detailFormData}
                          valueFormProps={handleFormValueProps}
                          field={"description"}
                          label={"Description"}
                          formElementType="textarea"
                      />
                    </Grid>
                  </Grid>
                  <ButtonGroup
                      detail={detailFormData}
                      handleCancelEdit={handleCancelEdit}
                  ></ButtonGroup>
                </form>
              </Box>
            </TabPanel>
          </Paper>
        </div>
        <CustomNotification
            open={openNotification}
            close={() => {
              setOpenNotification(false);
            }}
            notificationMessage={"Great! a leave type was created successfully."}
            severity={"success"}
        />
      </>
  );
};

export default Leave;
