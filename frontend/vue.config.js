module.exports = {
  publicPath: "/",
  devServer: {
    port: 8081,
    host: "localhost",
    proxy: {
      "/api": {
        target: process.env.VUE_APP_PROXY_TARGET || "http://localhost:8080/",
        changeOrigin: true
      }
    }
  },
  productionSourceMap: false
};
