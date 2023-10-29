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
import {injectIntl} from 'react-intl';
import messages from "enl-api/leaveType/leaveTypeMessages";

const Leave = ({intl}) => {
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
  const [data, setData] = useState([]);
  const baseApiUrl = useSelector((state) => state.env.BASE_API_URL);
  const [notificationMessage, setNotificationMessage] = useState('');
  const [notificationSeverity, setNotificationSeverity] = useState('');

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
    await putLeave(baseApiUrl, id, body, setOpenNotification, handleTabValueProps, setNotificationMessage, setNotificationSeverity, intl);
  };

  const handlePostLeave = async () => {
    const body = {
      name: formData.name,
      affectsDaysOff: Service.checkAffectsDaysOff(formData.affectsDaysOff),
      description: formData.description,
    };
    await postLeave(baseApiUrl, body, setOpenNotification, handleTabValueProps, setNotificationMessage, setNotificationSeverity, intl);
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

  const handleFormSubmit = async (e) => {
    e.preventDefault();
    const { id } = formData;
    if (id === undefined) {
      await handlePostLeave();
    }
    if (id !== undefined) {
      await handlePutLeave();
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
    await deleteLeave(baseApiUrl, id, setOpenNotification, setNotificationMessage, setNotificationSeverity, setReloadKey, intl)
  }

  useEffect(async () => {
    setData(await fetchVerify(baseApiUrl));
  }, [ tabValue, reloadKey]);

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
        label: intl.formatMessage(messages.name),
      },
    },
    {
      name: "affectsDaysOff",
      options: {
        filter: true,
        customBodyRender: (value) => {
          return Service.formatAffectsDaysOff(value);
        },
        ...TableOptionStyle(),
        label: intl.formatMessage(messages.affect),
      },
    },
    {
      name: "description",
      options: {
        filter: true,
        customBodyRender: (value) => (
            <TruncateTypography text={value === null ? '' : value} component={"p"} />
        ),
        ...TableOptionStyle(),
        label: intl.formatMessage(messages.desc),
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
        label: intl.formatMessage(messages.action),
      },
    },
  ];


  return (
      <>
        <div key={reloadKey}>
          <Helmet>
            <title>Leave type management</title>
          </Helmet>
          <PapperBlock
              title="Leave type"
              whiteBg
              icon="insert_chart"
              desc={intl.formatMessage(messages.title)}
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
                 <div>Total: &nbsp; {data.length}</div>
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
                          label={intl.formatMessage(messages.name)}
                          isRequired
                          formElementType="text"
                      />
                    </Grid>
                    <Grid item xs={12} sm={6}>
                      <CustomFormElement
                        detailFormData={detailFormData}
                        valueFormProps={handleFormValueProps}
                        field={"affectsDaysOff"}
                        label={intl.formatMessage(messages.affect)}
                        isRequired
                        formElementType="selectAffectsDaysOff"
                      />
                    </Grid>
                    <Grid item xs={12} sm={10}>
                      <CustomFormElement
                          detailFormData={detailFormData}
                          valueFormProps={handleFormValueProps}
                          field={"description"}
                          label={intl.formatMessage(messages.desc)}
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
            notificationMessage={notificationMessage}
            severity={notificationSeverity}
        />
      </>
  );
};

export default injectIntl(Leave);
