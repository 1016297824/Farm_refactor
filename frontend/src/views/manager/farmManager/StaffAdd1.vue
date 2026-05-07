<template>
  <div class="farm-bg">
    <div class="farm-page-container">
      <div class="farm-card farm-card-static">
        <div class="staff-add-header">
          <div class="header-left">
            <button class="farm-btn farm-btn-secondary farm-btn-sm" @click="goBack">
              返回
            </button>
            <h2 class="staff-add-title">添加员工</h2>
          </div>
        </div>

        <div class="staff-add-content">
          <form class="farm-form" @submit.prevent="addStaff">
            <div class="form-row">
              <div class="form-group">
                <label class="farm-form-label">姓名</label>
                <input
                  type="text"
                  class="farm-form-control"
                  v-model="staff.name"
                  @keyup="writeName"
                  placeholder="请输入姓名"
                />
                <p class="farm-form-error">{{ nameMessage }}</p>
              </div>

              <div class="form-group">
                <label class="farm-form-label">身份证号</label>
                <input
                  type="text"
                  class="farm-form-control"
                  v-model="staff.idCardNo"
                  @keyup="writeIdCardNo"
                  placeholder="请输入身份证号"
                />
                <p class="farm-form-error">{{ idCardNoMessage }}</p>
              </div>
            </div>

            <div class="form-row">
              <div class="form-group">
                <label class="farm-form-label">电话号码</label>
                <input
                  type="text"
                  class="farm-form-control"
                  v-model="staff.telNumber"
                  @keyup="writeTelNumber"
                  placeholder="请输入电话号码"
                />
                <p class="farm-form-error">{{ telNumberMessage }}</p>
              </div>

              <div class="form-group">
                <label class="farm-form-label">邮箱</label>
                <input
                  type="text"
                  class="farm-form-control"
                  v-model="staff.email"
                  @keyup="writeEmail"
                  placeholder="请输入邮箱"
                />
                <p class="farm-form-error">{{ emailMessage }}</p>
              </div>
            </div>

            <div class="form-group">
              <label class="farm-form-label">住址</label>
              <input
                type="text"
                class="farm-form-control"
                v-model="staff.address"
                @keyup="writeAddress"
                placeholder="请输入住址"
              />
              <p class="farm-form-error">{{ addressMessage }}</p>
            </div>

            <div class="form-actions">
              <button type="button" class="farm-btn farm-btn-secondary" @click="goBack">
                取消
              </button>
              <button type="submit" class="farm-btn farm-btn-primary">
                添加
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
import { addStaff } from "@/api/farmManager";

export default {
  name: "StaffAdd",
  data: () => ({
    staff: {
      name: null,
      idCardNo: null,
      telNumber: null,
      email: null,
      address: null
    },
    nameMessage: null,
    idCardNoMessage: null,
    telNumberMessage: null,
    emailMessage: null,
    addressMessage: null
  }),
  methods: {
    goBack() {
      this.$router.back();
    },
    addStaff() {
      this.clearMessages();

      if (
        this.staff.name == null ||
        this.staff.idCardNo == null ||
        this.staff.telNumber == null ||
        this.staff.email == null ||
        this.staff.address == null
      ) {
        if (this.staff.name == null) {
          this.nameMessage = "姓名不能为空！";
        }
        if (this.staff.idCardNo == null) {
          this.idCardNoMessage = "身份证号不能为空！";
        }
        if (this.staff.telNumber == null) {
          this.telNumberMessage = "手机号不能为空！";
        }
        if (this.staff.email == null) {
          this.emailMessage = "邮箱不能为空！";
        }
        if (this.staff.address == null) {
          this.addressMessage = "住址不能为空！";
        }
      } else {
        let result = true;
        var regEmail = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
        var regIdCardNo = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
        var regTelNulber = /^(\+86)?1[3456789]\d{9}$/;

        if (!regEmail.test(this.staff.email)) {
          result = false;
          this.emailMessage = "邮箱格式不正确！";
        }
        if (!regIdCardNo.test(this.staff.idCardNo)) {
          result = false;
          this.idCardNoMessage = "身份证号格式不正确！";
        }
        if (!regTelNulber.test(this.staff.telNumber)) {
          result = false;
          this.telNumberMessage = "手机号格式不正确！";
        }

        if (result) {
          addStaff(this.staff);
          bus.$emit("toast", { message: "添加成功！", type: "success" });
          this.$router.back();
        }
      }
    },
    clearMessages() {
      this.nameMessage = null;
      this.idCardNoMessage = null;
      this.telNumberMessage = null;
      this.emailMessage = null;
      this.addressMessage = null;
    },
    writeName() {
      this.nameMessage = null;
    },
    writeIdCardNo() {
      this.idCardNoMessage = null;
    },
    writeTelNumber() {
      this.telNumberMessage = null;
    },
    writeEmail() {
      this.emailMessage = null;
    },
    writeAddress() {
      this.addressMessage = null;
    }
  }
};
</script>

<style scoped>
.staff-add-header {
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

.staff-add-title {
  font-size: 20px;
  font-weight: 600;
  color: var(--farm-text-primary);
  margin: 0;
}

.staff-add-content {
  padding: 24px;
}

.farm-form {
  max-width: 600px;
  margin: 0 auto;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-row .form-group {
  margin-bottom: 0;
}

.farm-form-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: var(--farm-text-primary);
  margin-bottom: 8px;
}

.farm-form-control {
  width: 100%;
  padding: 12px 14px;
  font-size: 14px;
  border: 1px solid var(--farm-border);
  border-radius: var(--farm-radius);
  background: white;
  transition: border-color 0.2s, box-shadow 0.2s;
  box-sizing: border-box;
}

.farm-form-control:focus {
  outline: none;
  border-color: var(--farm-primary);
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
}

.farm-form-control::placeholder {
  color: var(--farm-text-muted);
}

.farm-form-error {
  margin: 6px 0 0 0;
  font-size: 13px;
  color: var(--farm-danger);
  min-height: 18px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid var(--farm-border);
}
</style>
