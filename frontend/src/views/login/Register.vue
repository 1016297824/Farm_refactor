<template>
  <div>
    <loginNavbar />

    <div class="register-page">
      <div class="register-container">
        <div class="register-card farm-card">
          <div class="register-header">
            <div class="register-logo">
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
            <h1 class="register-title">创建账号</h1>
            <p class="register-subtitle">加入西塘草舍农场大家庭</p>
            <div class="farm-divider"></div>
          </div>

          <form class="register-form" @submit.prevent="register">
            <div class="farm-form-group">
              <label class="farm-label">手机号</label>
              <input
                type="text"
                class="farm-input"
                v-model="customer.username"
                @keyup="writeUsername"
                placeholder="请输入手机号"
              />
              <p v-if="phoneMessage" class="farm-error">{{ phoneMessage }}</p>
            </div>

            <div class="farm-form-group">
              <label class="farm-label">密码</label>
              <input
                type="password"
                class="farm-input"
                v-model="customer.password"
                @keyup="writePassword"
                placeholder="请输入密码"
              />
              <p v-if="passwordMessage" class="farm-error">
                {{ passwordMessage }}
              </p>
            </div>

            <div class="farm-form-group">
              <label class="farm-label">确认密码</label>
              <input
                type="password"
                class="farm-input"
                v-model="password1"
                @keyup="writePassword1"
                placeholder="请再次输入密码"
              />
              <p v-if="password1Message" class="farm-error">
                {{ password1Message }}
              </p>
            </div>

            <div class="farm-form-group">
              <label class="farm-label">姓名</label>
              <input
                type="text"
                class="farm-input"
                v-model="customer.name"
                @keyup="writeName"
                placeholder="请输入您的姓名"
              />
              <p v-if="nameMessage" class="farm-error">{{ nameMessage }}</p>
            </div>

            <button
              type="submit"
              class="farm-btn farm-btn-primary farm-btn-lg register-submit"
            >
              注册
            </button>

            <div class="register-footer">
              <span class="register-link-text">已有账号？</span>
              <a href="/login" class="register-link">立即登录</a>
            </div>
          </form>
        </div>
      </div>
    </div>

    <footerNavbar />
  </div>
</template>

<script>
import { register } from "@/api/login.js";

export default {
  name: "Register",
  data: () => ({
    customer: {
      username: null,
      password: null,
      name: null
    },
    password1: null,
    phoneMessage: null,
    passwordMessage: null,
    password1Message: null,
    nameMessage: null
  }),
  methods: {
    register: function() {
      if (
        this.customer.username == null ||
        this.customer.password == null ||
        this.customer.name == null ||
        this.password1 == null
      ) {
        if (this.customer.username == null) {
          this.phoneMessage = "请输入手机号！";
        }
        if (this.customer.password == null) {
          this.passwordMessage = "密码不能为空！";
        }
        if (this.customer.name == null) {
          this.nameMessage = "请输入姓名！";
        }
        if (this.password1 == null) {
          this.password1Message = "请再次确认密码！";
        }
      } else if (this.customer.password != this.password1) {
        this.password1Message = "两次输入密码不一致，请再次确认！";
      } else {
        let re = /^1(3|4|5|6|7|8|9)\d{9}$/;
        if (!re.test(this.customer.username)) {
          this.phoneMessage = "请输入正确的手机号！";
        } else {
          register(this.customer);
        }
      }
    },
    writeUsername: function() {
      this.phoneMessage = null;
    },
    writePassword: function() {
      this.passwordMessage = null;
    },
    writePassword1: function() {
      this.password1Message = null;
    },
    writeName: function() {
      this.nameMessage = null;
    }
  },
  components: {
    loginNavbar: () => import("@/components/Header/LoginNavbar"),
    footerNavbar: () => import("@/components/FooterNavbar")
  }
};
</script>

<style scoped>
.register-page {
  min-height: calc(100vh - 120px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
}

.register-container {
  width: 100%;
  max-width: 440px;
}

.register-card {
  padding: 0;
  overflow: hidden;
}

.register-header {
  background: linear-gradient(
    135deg,
    var(--farm-primary) 0%,
    var(--farm-primary-light) 100%
  );
  padding: 40px 32px 32px;
  text-align: center;
  color: white;
}

.register-logo {
  width: 80px;
  height: 80px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
}

.register-title {
  font-size: 26px;
  font-weight: 700;
  margin: 0 0 8px;
  color: white;
}

.register-subtitle {
  font-size: 14px;
  opacity: 0.9;
  margin: 0;
  color: rgba(255, 255, 255, 0.9);
}

.register-header .farm-divider {
  width: 40px;
  height: 3px;
  background: rgba(255, 255, 255, 0.8);
  margin: 20px auto 0;
  border-radius: 2px;
}

.register-form {
  padding: 32px;
}

.register-submit {
  width: 100%;
  margin-top: 8px;
}

.register-footer {
  text-align: center;
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid var(--farm-border);
}

.register-link-text {
  color: var(--farm-text-muted);
  font-size: 14px;
}

.register-link {
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
