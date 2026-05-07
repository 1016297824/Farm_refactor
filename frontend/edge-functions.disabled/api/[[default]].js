const fs = require('fs');
const path = require('path');

class CopyEdgeFunctionsPlugin {
  apply(compiler) {
    compiler.hooks.done.tap('CopyEdgeFunctionsPlugin', () => {
      const src = path.join(__dirname, 'edge-functions');
      const dest = path.join(__dirname, 'dist', 'edge-functions');
      if (fs.existsSync(src)) {
        copyDirSync(src, dest);
        console.log('\n✅ edge-functions copied to dist/edge-functions');
      }
    });
  }
}

function copyDirSync(src, dest) {
  fs.mkdirSync(dest, { recursive: true });
  const entries = fs.readdirSync(src, { withFileTypes: true });
  for (const entry of entries) {
    const srcPath = path.join(src, entry.name);
    const destPath = path.join(dest, entry.name);
    if (entry.isDirectory()) {
      copyDirSync(srcPath, destPath);
    } else {
      fs.copyFileSync(srcPath, destPath);
    }
  }
}

module.exports = {
  publicPath: "/",
  devServer: {
    port: 8081,
    host: "localhost",
    proxy: {
      "/api": {
        target: "http://服务器地址:39080/",
        changeOrigin: true
      }
    }
  },
  productionSourceMap: false,
  configureWebpack: {
    plugins: [new CopyEdgeFunctionsPlugin()]
  }
};
