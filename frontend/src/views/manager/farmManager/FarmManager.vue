<template>
  <div>
    <!-- 导航栏 -->
    <farmManagerNavbar />

    <!-- 子路由 -->
    <router-view />

    <!-- 页脚 -->
    <footerNavbar />
  </div>
</template>

<script>
export default {
  name: "FarmManager",
  components: {
    farmManagerNavbar: () => import("@/components/Header/FarmManagerNavbar"),
    footerNavbar: () => import("@/components/FooterNavbar")
  }
};
</script>
