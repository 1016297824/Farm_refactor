<template>
  <div class="farm-bg">
    <div class="farm-page-container">
      <div class="farm-card farm-card-static password-card">
        <h2 class="password-title">修改密码</h2>

        <form class="password-form">
          <div class="form-group">
            <label class="farm-label">用户名</label>
            <input
              type="text"
              class="form-control farm-input"
              readonly
              v-model="userBodyChangePassword.username"
            />
          </div>

          <div class="form-group">
            <label class="farm-label">旧密码</label>
            <input
              type="password"
              class="form-control farm-input"
              v-model="userBodyChangePassword.password"
              @keyup="writePassword"
              placeholder="请输入旧密码"
            />
            <p class="error-message" v-if="passwordMessage">{{ passwordMessage }}</p>
          </div>

          <div class="form-group">
            <label class="farm-label">新密码</label>
            <input
              type="password"
              class="form-control farm-input"
              v-model="userBodyChangePassword.newPassword"
              @keyup="writeNewPassword"
              placeholder="请输入新密码"
            />
            <p class="error-message" v-if="newPasswordMessage">{{ newPasswordMessage }}</p>
          </div>

          <div class="form-group">
            <label class="farm-label">确认新密码</label>
            <input
              type="password"
              class="form-control farm-input"
              v-model="userBodyChangePassword.newPassword1"
              @keyup="writeNewPassword1"
              placeholder="请再次输入新密码"
            />
            <p class="error-message" v-if="newPassword1Message">{{ newPassword1Message }}</p>
          </div>

          <div class="form-actions">
            <button type="button" class="farm-btn farm-btn-primary" @click="changePassword">
              提交
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { changePassword } from "@/api/customer.js";

export default {
  name: "Password",
  data: () => ({
    userBodyChangePassword: {
      username: sessionStorage.getItem("username"),
      password: null,
      newPassword: null,
      newPassword1: null
    },
    passwordMessage: null,
    newPasswordMessage: null,
    newPassword1Message: null
  }),
  methods: {
    changePassword() {
      if (
        this.userBodyChangePassword.password == null ||
        this.userBodyChangePassword.newPassword == null ||
        this.userBodyChangePassword.newPassword1 == null
      ) {
        if (this.userBodyChangePassword.password == null) {
          this.passwordMessage = "请输入旧密码！";
        }
        if (this.userBodyChangePassword.newPassword == null) {
          this.newPasswordMessage = "请输入新密码！";
        }
        if (this.userBodyChangePassword.newPassword1 == null) {
          this.newPassword1Message = "请确认新密码！";
        }
      } else if (
        this.userBodyChangePassword.newPassword !=
        this.userBodyChangePassword.newPassword1
      ) {
        this.newPassword1Message = "两次输入密码不一致，请再次确认！";
      } else {
        changePassword(this.userBodyChangePassword);
      }
    },
    writePassword() {
      this.passwordMessage = null;
    },
    writeNewPassword() {
      this.newPasswordMessage = null;
    },
    writeNewPassword1() {
      this.newPassword1Message = null;
    }
  }
};
</script>

<style scoped>
.password-card {
  max-width: 450px;
  margin: 0 auto;
}

.password-title {
  font-size: 22px;
  font-weight: 600;
  color: var(--farm-text-primary);
  text-align: center;
  margin-bottom: 32px;
  padding-bottom: 20px;
  border-bottom: 1px solid var(--farm-border);
}

.password-form {
  padding: 0 20px 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-control {
  margin-top: 8px;
}

.error-message {
  color: var(--farm-danger);
  font-size: 13px;
  margin-top: 6px;
  margin-bottom: 0;
}

.form-actions {
  margin-top: 32px;
  text-align: center;
}

.form-actions .farm-btn {
  min-width: 120px;
}
</style>