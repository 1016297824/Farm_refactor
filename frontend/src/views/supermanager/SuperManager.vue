<template>
  <div>
    <!-- 导航栏 -->
    <supermanagerNavbar />

    <!-- 子路由 -->
    <router-view />

    <!-- 页脚 -->
    <footerNavbar />
  </div>
</template>

<script>
export default {
  name: "SuperManager",
  components: {
    supermanagerNavbar: () => import("@/components/Header/SuperManagerNavbar"),
    footerNavbar: () => import("@/components/FooterNavbar")
  }
};
</script>
