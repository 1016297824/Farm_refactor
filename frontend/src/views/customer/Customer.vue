// 顾客页面
<template>
  <div>
    <!-- 导航栏 -->
    <customerNavbar />

    <!-- 子路由 -->
    <router-view />

    <!-- 页脚 -->
    <footerNavbar />
  </div>
</template>

<script>
export default {
  name: "Customer",
  components: {
    customerNavbar: () => import("@/components/Header/CustomerNavbar"),
    footerNavbar: () => import("@/components/FooterNavbar")
  }
};
</script>
