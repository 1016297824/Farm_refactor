<template>
  <div>
    <!-- 导航栏 -->
    <restaurantStaffNavbar />

    <!-- 子路由 -->
    <router-view />

    <!-- 页脚 -->
    <footerNavbar />
  </div>
</template>

<script>
export default {
  name: "RestaurantStaff",
  components: {
    restaurantStaffNavbar: () =>
      import("@/components/Header/RestaurantStaffNavbar"),
    footerNavbar: () => import("@/components/FooterNavbar")
  }
};
</script>
