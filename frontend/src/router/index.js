import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

const routes = [
  {
    path: "",
    name: "",
    redirect: "/login"
  },
  {
    path: "/login",
    name: "login",
    meta: {
      title: "用户登录"
    },
    component: () => import("@/views/login/Login")
  },
  {
    path: "/register",
    name: "register",
    meta: {
      title: "用户注册"
    },
    component: () => import("@/views/login/Register")
  },
  {
    path: "/customer",
    name: "customer",
    meta: {
      title: "欢迎使用！"
    },
    component: () => import("@/views/customer/Customer"),
    children: [
      {
        path: "reserve",
        name: "reserve",
        meta: {
          title: "预定"
        },
        component: () => import("@/views/customer/Reserve")
      },
      {
        path: "reserveAdd",
        name: "reserveAdd",
        meta: {
          title: "预定"
        },
        component: () => import("@/views/customer/ReserveAdd")
      },
      {
        path: "order",
        name: "order",
        meta: {
          title: "点餐"
        },
        component: () => import("@/views/customer/Order")
      },
      {
        path: "orderAdd",
        name: "orderAdd",
        meta: {
          title: "点餐"
        },
        component: () => import("@/views/customer/OrderAdd")
      },
      {
        path: "purchase",
        name: "purchase",
        meta: {
          title: "采购"
        },
        component: () => import("@/views/customer/Purchase")
      },
      {
        path: "password",
        name: "password",
        meta: {
          title: "修改密码"
        },
        component: () => import("@/views/customer/Password")
      }
    ]
  },
  {
    path: "/supermanager",
    name: "supermanager",
    meta: {
      title: "欢迎使用！"
    },
    component: () => import("@/views/supermanager/SuperManager"),
    children: [
      {
        path: "staffShow",
        name: "staffShow",
        meta: {
          title: "员工管理"
        },
        component: () => import("@/views/supermanager/StaffShow")
      },
      {
        path: "staffAdd",
        name: "staffAdd",
        meta: {
          title: "添加员工"
        },
        component: () => import("@/views/supermanager/StaffAdd")
      },
      {
        path: "attendance",
        name: "attendance",
        meta: {
          title: "考勤管理"
        },
        component: () => import("@/views/supermanager/Attendance")
      },
      {
        path: "reportForm",
        name: "reportForm",
        meta: {
          title: "报表打印"
        },
        component: () => import("@/views/supermanager/ReportForm")
      },
      {
        path: "passwordSupermanager",
        name: "passwordSupermanager",
        meta: {
          title: "修改密码"
        },
        component: () => import("@/views/supermanager/PasswordSupermanager")
      }
    ]
  },
  {
    path: "/farmManager",
    name: "farmManager",
    meta: {
      title: "欢迎使用"
    },
    component: () => import("@/views/manager/farmManager/FarmManager"),
    children: [
      {
        path: "staffShow1",
        name: "staffShow1",
        meta: {
          title: "员工管理"
        },
        component: () => import("@/views/manager/farmManager/StaffShow1")
      },
      {
        path: "staffAdd1",
        name: "staffAdd1",
        meta: {
          title: "添加员工"
        },
        component: () => import("@/views/manager/farmManager/StaffAdd1")
      },
      {
        path: "attendance1",
        name: "attendance1",
        meta: {
          title: "考勤管理"
        },
        component: () => import("@/views/manager/farmManager/Attendance1")
      },
      {
        path: "reportForm1",
        name: "reportForm1",
        meta: {
          title: "报表打印"
        },
        component: () => import("@/views/manager/farmManager/ReportForm1")
      },
      {
        path: "passwordFarmManager",
        name: "passwordFarmManager",
        meta: {
          title: "修改密码"
        },
        component: () =>
          import("@/views/manager/farmManager/PasswordFarmManager")
      }
    ]
  },
  {
    path: "/restaurantManager",
    name: "restaurantManager",
    meta: {
      title: "欢迎使用"
    },
    component: () =>
      import("@/views/manager/restaurantManager/RestaurantManager"),
    children: [
      {
        path: "staffShow2",
        name: "staffShow2",
        meta: {
          title: "员工管理"
        },
        component: () => import("@/views/manager/restaurantManager/StaffShow2")
      },
      {
        path: "staffAdd2",
        name: "staffAdd2",
        meta: {
          title: "添加员工"
        },
        component: () => import("@/views/manager/restaurantManager/StaffAdd2")
      },
      {
        path: "attendance2",
        name: "attendance2",
        meta: {
          title: "考勤管理"
        },
        component: () => import("@/views/manager/restaurantManager/Attendance2")
      },
      {
        path: "reportForm2",
        name: "reportForm2",
        meta: {
          title: "报表打印"
        },
        component: () => import("@/views/manager/restaurantManager/ReportForm2")
      },
      {
        path: "passwordRestaurantManager",
        name: "passwordRestaurantManager",
        meta: {
          title: "修改密码"
        },
        component: () =>
          import("@/views/manager/restaurantManager/PasswordRestaurantManager")
      }
    ]
  },
  {
    path: "/restaurantStaff",
    name: "restaurantStaff",
    meta: {
      title: "欢迎使用"
    },
    component: () =>
      import("@/views/staff/restaurantStaff/RestaurantStaff.vue"),
    children: [
      {
        path: "settleAccounts",
        name: "settleAccounts",
        meta: {
          title: "结账"
        },
        component: () =>
          import("@/views/staff/restaurantStaff/SettleAccounts.vue")
      },
      {
        path: "restaurantMaterial",
        name: "RestaurantMaterial",
        meta: {
          title: "餐厅物资管理"
        },
        component: () =>
          import("@/views/staff/restaurantStaff/RestaurantMaterial.vue")
      },
      {
        path: "repair",
        name: "repair",
        meta: {
          title: "上报"
        },
        component: () => import("@/views/staff/restaurantStaff/Repair")
      },
      {
        path: "repairComplete",
        name: "repairComplete",
        meta: {
          title: "采购"
        },
        component: () => import("@/views/staff/restaurantStaff/RepairComplete")
      },
      {
        path: "passwordRestaurantStaff",
        name: "passwordRestaurantStaff",
        meta: {
          title: "修改密码"
        },
        component: () =>
          import("@/views/staff/restaurantStaff/PasswordRestaurantStaff")
      }
    ]
  },
  {
    path: "/farmStaff",
    name: "farmStaff",
    meta: {
      title: "欢迎使用"
    },
    component: () => import("@/views/staff/farmStaff/FarmStaff"),
    children: [
      {
        path: "productShow",
        name: "productShow",
        meta: {
          title: "产品管理"
        },
        component: () => import("@/views/staff/farmStaff/ProductShow.vue")
      },
      {
        path: "rawMaterial",
        name: "rawMaterial",
        meta: {
          title: "生产采购"
        },
        component: () => import("@/views/staff/farmStaff/RawMaterial.vue")
      },
      {
        path: "fertilizer",
        name: "fertilizer",
        meta: {
          title: "化肥饲料采购"
        },
        component: () => import("@/views/staff/farmStaff/Fertilizer.vue")
      },
      {
        path: "sale",
        name: "Sale",
        meta: {
          title: "产品销售"
        },
        component: () => import("@/views/staff/farmStaff/Sale")
      },
      {
        path: "passwordFarmStaff",
        name: "passwordFarmStaff",
        meta: {
          title: "修改密码"
        },
        component: () => import("@/views/staff/farmStaff/PasswordFarmStaff")
      }
    ]
  }
];

const router = new VueRouter({
  routes,
  mode: "history"
});

router.beforeEach((to, from, next) => {
  if (to.matched.length > 0 && to.matched[to.matched.length - 1].meta) {
    document.title = to.matched[to.matched.length - 1].meta.title;
  }
  next();
});

export default router;
