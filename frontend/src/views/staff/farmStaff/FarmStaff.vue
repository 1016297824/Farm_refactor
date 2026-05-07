<template>
  <div>
    <!-- 导航栏 -->
    <farmStaffNavbar />

    <!-- 子路由 -->
    <router-view />

    <!-- 页脚 -->
    <footerNavbar />
  </div>
</template>

<script>
export default {
  name: "FarmStaff",
  components: {
    farmStaffNavbar: () => import("@/components/Header/FarmStaffNavbar"),
    footerNavbar: () => import("@/components/FooterNavbar")
  }
};
</script>
