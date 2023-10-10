module.exports = [
  {
    key: "home_page",
    name: "Home",
    icon: "home",
    link: "/app/pages/home",
  },
  {
    key: "mange_user",
    name: "Manage Users",
    icon: "account_box",
    child: [
      {
        key: "user_list",
        name: "User List",
        icon: "view_list",
        link: "/app/pages/user",
      },
      {
        key: "user_add",
        name: "Add User",
        icon: "add_box",
        link: "/app/pages/add/user",
      },
      {
        key: "user_import",
        name: "Import User",
        icon: "file_upload",
        link: "/app/pages/import/user",
      },
    ],
  },
  {
    key: "manage_leave",
    name: "Manage Leaves",
    icon: "view_stream",
    link: "/app/pages/leave/manage",
  },
  {
    key: "apply_leave",
    name: "Apply Leave",
    icon: "view_list",
    link: "/app/pages/leave/apply",
  },
  {
    key: "my_leave",
    name: "My Leaves",
    icon: "view_list",
    link: "/app/pages/leave/my",
  },
  {
    key: "system_information",
    name: "System information",
    icon: "important_devices",
    child: [
      {
        key: "holiday_list",
        name: "Holidays",
        icon: "insert_chart",
        link: "/app/pages/holidays",
      },
    ],
  },

  // {
  //   key: "pages",
  //   name: "Pages",
  //   icon: "important_devices",
  //   child: [
  //     {
  //       key: "blank",
  //       name: "Blank Page",
  //       icon: "video_label",
  //       link: "/app",
  //     },

  //     // {
  //     //   key: 'dashboard',
  //     //   name: 'Dashboard',
  //     //   icon: 'settings_brightness',
  //     //   link: '/app/pages/dashboard'
  //     // },
  //     {
  //       key: "forms",
  //       name: "Form",
  //       link: "/app/pages/form",
  //       icon: "ballot",
  //     },
  //     {
  //       key: "tables",
  //       name: "Table",
  //       icon: "grid_on",
  //       link: "/app/pages/table",
  //     },
  //   ],
  // },
];
