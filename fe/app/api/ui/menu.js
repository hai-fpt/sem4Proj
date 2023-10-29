module.exports = [
  {
    key: "mange_user",
    name: "Manage Users",
    icon: "account_box",
    link: "/app/pages/users",
    roles: ['MANAGER']
  },
  {
    key: "manage_leave",
    name: "Manage Leaves",
    icon: "view_stream",
    link: "/app/pages/leave/leave management",
    roles: ['MANAGER']
  },
  {
    key: "apply_leave",
    name: "Apply Leave",
    icon: "view_list",
    link: "/app/pages/leave/apply leave",
    roles: ['USER']
  },
  {
    key: "my_leave",
    name: "My Leaves",
    icon: "flight_takeoff",
    link: "/app/pages/my leave",
    roles: ['USER']
  },
  {
    key: "my_profile",
    name: "My profile",
    icon: "person",
    link: "/app/pages/user/profile",
    roles: ['USER']
  },
  {
    key: "system_information",
    name: "System information",
    icon: "important_devices",
    roles: ['ADMIN'],
    child: [
      {
        key: "holiday_list",
        name: "Holidays",
        icon: "flight",
        link: "/app/pages/holidays",
        roles: ['ADMIN']
      },
      {
        key: "team",
        name: "Group/Team",
        icon: "supervisor_account",
        link: "/app/pages/team",
        roles: ['ADMIN']
      },
      {
        key: "roles",
        name: "Roles",
        icon: "work",
        link: "/app/pages/roles",
        roles: ['ADMIN']
      },
      {
        key: "leave_type",
        name: "Leave type",
        icon: "insert_chart",
        link: "/app/pages/leave type",
        roles: ['ADMIN']
      },
      {
        key: "department",
        name: "Department",
        icon: "account_balance",
        link: "/app/pages/department",
        roles: ['ADMIN']
      },
    ],
  },
];
