<template>
  <div class="farm-bg">
    <div class="farm-page-container">
      <div class="farm-card farm-card-static">
        <div class="password-header">
          <div class="header-left">
            <button class="farm-btn farm-btn-secondary farm-btn-sm" @click="goBack">
              ← 返回
            </button>
            <h2 class="password-title">修改密码</h2>
          </div>
        </div>

        <div class="password-content">
          <form class="farm-form">
            <div class="form-group">
              <label class="farm-form-label">工号</label>
              <input
                type="text"
                class="farm-form-control"
                readonly
                v-model="userBodyChangePassword.username"
              />
            </div>

            <div class="form-group">
              <label class="farm-form-label">旧密码</label>
              <input
                type="password"
                class="farm-form-control"
                v-model="userBodyChangePassword.password"
                @keyup="writePassword"
                placeholder="请输入旧密码"
              />
              <p class="farm-form-error" v-if="passwordMessage">{{ passwordMessage }}</p>
            </div>

            <div class="form-group">
              <label class="farm-form-label">新密码</label>
              <input
                type="password"
                class="farm-form-control"
                v-model="userBodyChangePassword.newPassword"
                @keyup="writeNewPassword"
                placeholder="请输入新密码"
              />
              <p class="farm-form-error" v-if="newPasswordMessage">{{ newPasswordMessage }}</p>
            </div>

            <div class="form-group">
              <label class="farm-form-label">确认新密码</label>
              <input
                type="password"
                class="farm-form-control"
                v-model="userBodyChangePassword.newPassword1"
                @keyup="writeNewPassword1"
                placeholder="请再次输入新密码"
              />
              <p class="farm-form-error" v-if="newPassword1Message">{{ newPassword1Message }}</p>
            </div>

            <div class="form-actions">
              <button type="button" class="farm-btn farm-btn-primary" @click="changePassword">
                提交
              </button>
              <button type="button" class="farm-btn farm-btn-secondary" @click="goBack">
                取消
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import bus from "@/util/Bus";
import { changePassword } from "@/api/restaurantStaff.js";

export default {
  name: "PasswordRestaurantStaff",
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
    goBack() {
      this.$router.back();
    },
    changePassword() {
      this.passwordMessage = null;
      this.newPasswordMessage = null;
      this.newPassword1Message = null;

      if (
        this.userBodyChangePassword.password == null ||
        this.userBodyChangePassword.password.trim() === ""
      ) {
        this.passwordMessage = "请输入旧密码！";
        return;
      }

      if (
        this.userBodyChangePassword.newPassword == null ||
        this.userBodyChangePassword.newPassword.trim() === ""
      ) {
        this.newPasswordMessage = "请输入新密码！";
        return;
      }

      if (
        this.userBodyChangePassword.newPassword1 == null ||
        this.userBodyChangePassword.newPassword1.trim() === ""
      ) {
        this.newPassword1Message = "请确认新密码！";
        return;
      }

      if (
        this.userBodyChangePassword.newPassword !==
        this.userBodyChangePassword.newPassword1
      ) {
        this.newPassword1Message = "两次输入密码不一致，请再次确认！";
        return;
      }

      changePassword(this.userBodyChangePassword);
      bus.$emit("toast", { message: "密码修改成功！", type: "success" });
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
.password-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid var(--farm-border);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.password-title {
  font-size: 20px;
  font-weight: 600;
  color: var(--farm-text-primary);
  margin: 0;
}

.password-content {
  padding: 40px 24px;
}

.farm-form {
  max-width: 420px;
  margin: 0 auto;
  padding: 32px;
  background: #fafafa;
  border-radius: var(--farm-radius-lg);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.form-group {
  margin-bottom: 24px;
}

.farm-form-label {
  display: block;
  font-size: 14px;
  font-weight: 600;
  color: var(--farm-text-primary);
  margin-bottom: 10px;
  letter-spacing: 0.3px;
}

.farm-form-control {
  width: 100%;
  padding: 14px 16px;
  font-size: 15px;
  border: 1.5px solid var(--farm-border);
  border-radius: var(--farm-radius-lg);
  background: white;
  transition: all 0.2s ease;
  box-sizing: border-box;
}

.farm-form-control:focus {
  outline: none;
  border-color: var(--farm-primary);
  box-shadow: 0 0 0 4px rgba(0, 123, 255, 0.12);
}

.farm-form-control:read-only {
  background: var(--farm-bg-light);
  color: var(--farm-text-muted);
  cursor: default;
}

.farm-form-control::placeholder {
  color: var(--farm-text-muted);
  opacity: 0.7;
}

.farm-form-error {
  margin: 10px 0 0 4px;
  font-size: 13px;
  color: #dc3545;
  display: flex;
  align-items: center;
  gap: 4px;
}

.farm-form-error::before {
  content: "!";
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 16px;
  height: 16px;
  background: #dc3545;
  color: white;
  border-radius: 50%;
  font-size: 11px;
  font-weight: 600;
}

.form-actions {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-top: 32px;
}

.form-actions .farm-btn {
  width: 100%;
  padding: 14px 24px;
  font-size: 15px;
  font-weight: 500;
}

.form-actions .farm-btn-secondary {
  background: white;
  border: 1.5px solid var(--farm-border);
  color: var(--farm-text-secondary);
}

.form-actions .farm-btn-secondary:hover {
  background: var(--farm-bg-light);
}
</style>
