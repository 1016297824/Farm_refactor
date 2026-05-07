<template>
  <div id="app" class="farm-bg">
    <router-view />
    <alert />
  </div>
</template>

<script>
import "@/assets/css/myCSS.css";

export default {
  components: { alert: () => import("@/components/AlertDialog") }
};
</script>
