<template>
  <div>
    <loginNavbar />

    <div class="login-page">
      <div class="login-container">
        <div class="login-card farm-card">
          <div class="login-header">
            <div class="login-logo">
              <svg
                width="48"
                height="48"
                viewBox="0 0 24 24"
                fill="none"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  d="M12 2C13.5 2 15 3 15 5C15 6 14.5 7 14 8C16 9 17 10 17 12C17 14 15 15 15 17C15 19 13.5 20 12 22C10.5 20 9 19 9 17C9 15 10 14 10 12C10 10 11 9 13 8C12.5 7 12 6 12 5C12 3 10.5 2 12 2Z"
                  fill="#22C55E"
                />
                <path
                  d="M3 12C4 10 6 9 8 9C9 9 10 9.5 11 10C10 11 9 13 9 15C9 17 10 19 12 20V22H10C8 22 6 21 5 19C4 17 4 15 5 13C5 12 5 11 6 10C5 11 4 11 3 12Z"
                  fill="#15803D"
                />
                <path
                  d="M21 12C20 10 18 9 16 9C15 9 14 9.5 13 10C14 11 15 13 15 15C15 17 14 19 12 20V22H14C16 22 18 21 19 19C20 17 20 15 19 13C19 12 19 11 18 10C19 11 20 11 21 12Z"
                  fill="#15803D"
                />
              </svg>
            </div>
            <h1 class="login-title">西塘草舍农场</h1>
            <p class="login-subtitle">欢迎回到绿色家园</p>
            <div class="farm-divider"></div>
          </div>

          <form class="login-form" @submit.prevent="login">
            <div class="farm-form-group">
              <label class="farm-label">用户名</label>
              <input
                type="text"
                class="farm-input"
                v-model="userBody.username"
                @keyup="writeUsername"
                placeholder="请输入用户名"
              />
              <p v-if="usernameMessage" class="farm-error">
                {{ usernameMessage }}
              </p>
            </div>

            <div class="farm-form-group">
              <label class="farm-label">密码</label>
              <input
                type="password"
                class="farm-input"
                v-model="userBody.password"
                @keyup="writePassword"
                placeholder="请输入密码"
              />
              <p v-if="passwordMessage" class="farm-error">
                {{ passwordMessage }}
              </p>
            </div>

            <button
              type="submit"
              class="farm-btn farm-btn-primary farm-btn-lg login-submit"
            >
              登录
            </button>

            <div class="login-footer">
              <span class="login-link-text">还没有账号？</span>
              <a href="/register" class="login-link">立即注册</a>
            </div>
          </form>
        </div>
      </div>
    </div>

    <footerNavbar />
  </div>
</template>

<script>
import { login } from "@/api/login";

export default {
  name: "Login",
  data: () => ({
    userBody: {
      username: null,
      password: null
    },
    usernameMessage: null,
    passwordMessage: null
  }),
  methods: {
    login: function() {
      if (this.userBody.username == "" || this.userBody.password == "") {
        if (this.userBody.username == "") {
          this.usernameMessage = "请输入用户名！";
        }
        if (this.userBody.password == "") {
          this.passwordMessage = "请输入密码！";
        }
      } else {
        this.usernameMessage = null;
        this.passwordMessage = null;
        login(this.userBody);
      }
    },
    writeUsername: function() {
      this.usernameMessage = null;
    },
    writePassword: function() {
      this.passwordMessage = null;
    }
  },
  components: {
    loginNavbar: () => import("@/components/Header/LoginNavbar"),
    footerNavbar: () => import("@/components/FooterNavbar")
  }
};
</script>

<style scoped>
.login-page {
  min-height: calc(100vh - 120px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
}

.login-container {
  width: 100%;
  max-width: 440px;
}

.login-card {
  padding: 0;
  overflow: hidden;
}

.login-header {
  background: linear-gradient(
    135deg,
    var(--farm-primary) 0%,
    var(--farm-primary-light) 100%
  );
  padding: 40px 32px 32px;
  text-align: center;
  color: white;
}

.login-logo {
  width: 80px;
  height: 80px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
}

.login-title {
  font-size: 26px;
  font-weight: 700;
  margin: 0 0 8px;
  color: white;
}

.login-subtitle {
  font-size: 14px;
  opacity: 0.9;
  margin: 0;
  color: rgba(255, 255, 255, 0.9);
}

.login-header .farm-divider {
  width: 40px;
  height: 3px;
  background: rgba(255, 255, 255, 0.8);
  margin: 20px auto 0;
  border-radius: 2px;
}

.login-form {
  padding: 32px;
}

.login-submit {
  width: 100%;
  margin-top: 8px;
}

.login-footer {
  text-align: center;
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid var(--farm-border);
}

.login-link-text {
  color: var(--farm-text-muted);
  font-size: 14px;
}

.login-link {
  color: var(--farm-primary);
  font-weight: 600;
  margin-left: 6px;
  font-size: 14px;
}

.farm-error {
  color: var(--farm-danger);
  font-size: 12px;
  margin-top: 6px;
}
</style>
