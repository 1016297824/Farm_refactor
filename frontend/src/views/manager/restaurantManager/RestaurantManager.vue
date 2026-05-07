<template>
  <div>
    <!-- 导航栏 -->
    <restaurantManagerNavbar />

    <!-- 子路由 -->
    <router-view />

    <!-- 页脚 -->
    <footerNavbar />
  </div>
</template>

<script>
export default {
  name: "RestaurantManager",
  components: {
    restaurantManagerNavbar: () =>
      import("@/components/Header/RestaurantManagerNavbar"),
    footerNavbar: () => import("@/components/FooterNavbar")
  }
};
</script>
