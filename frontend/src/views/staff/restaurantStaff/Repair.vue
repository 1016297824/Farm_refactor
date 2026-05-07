<template>
  <div class="farm-page-container">
    <div class="farm-card-static repair-page">
      <div class="repair-header">
        <div class="repair-title-section">
          <h1 class="farm-title-main">设备{{ repair.repairType }}</h1>
          <div class="farm-divider"></div>
        </div>
      </div>

      <div class="repair-form-wrapper">
        <form class="farm-form">
          <div class="farm-form-group">
            <label class="farm-label">选择类型</label>
            <select class="farm-input" v-model="repair.repairType">
              <option
                v-for="(rt, index) in repairType"
                :key="index"
                :value="rt"
              >
                {{ rt }}
              </option>
            </select>
          </div>

          <div class="farm-divider"></div>

          <div class="farm-form-group">
            <label class="farm-label">{{ repair.repairType }}内容</label>
            <p v-if="repair1Message" class="farm-error">{{ repair1Message }}</p>
            <textarea
              class="farm-input farm-textarea"
              rows="3"
              v-model="repair.content"
              @keyup="contentWrite"
              :placeholder="'请详细描述' + repair.repairType + '内容'"
            ></textarea>
          </div>

          <div class="farm-form-group">
            <label class="farm-label">{{ repair.repairType }}原因</label>
            <p v-if="repair2Message" class="farm-error">{{ repair2Message }}</p>
            <textarea
              class="farm-input farm-textarea"
              rows="5"
              v-model="repair.cause"
              @keyup="causeWrite"
              :placeholder="'请详细描述' + repair.repairType + '原因'"
            ></textarea>
          </div>

          <div class="repair-actions">
            <button type="button" class="farm-btn farm-btn-primary" @click="submitRepair">
              提交
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import bus from "@/util/Bus";
import { submitRepair } from "@/api/restaurantStaff.js";

export default {
  name: "Repair",
  data: () => ({
    repairType: ["报修", "报损"],
    repair: {
      repairType: "报修",
      content: null,
      cause: null
    },
    repair1Message: null,
    repair2Message: null
  }),
  methods: {
    submitRepair() {
      this.repair1Message = null;
      this.repair2Message = null;

      if (!this.repair.content || this.repair.content.trim() === "") {
        this.repair1Message = "请输入" + this.repair.repairType + "内容！";
        return;
      }
      if (!this.repair.cause || this.repair.cause.trim() === "") {
        this.repair2Message = "请输入" + this.repair.repairType + "原因！";
        return;
      }
      submitRepair(this.repair);
    },
    contentWrite() {
      this.repair1Message = null;
    },
    causeWrite() {
      this.repair2Message = null;
    }
  },
  created() {
    bus.$on(bus.repair, data => {
      this.repair = data;
    });
  },
  beforeDestroy() {
    bus.$off(bus.repair);
  }
};
</script>

<style scoped>
.repair-page {
  padding: 32px;
}

.repair-header {
  margin-bottom: 32px;
}

.repair-title-section {
  text-align: center;
  margin-bottom: 32px;
}

.repair-form-wrapper {
  max-width: 600px;
  margin: 0 auto;
}

.farm-form {
  padding: 32px;
  background: #fafafa;
  border-radius: var(--farm-radius-lg);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.farm-textarea {
  resize: vertical;
  min-height: 80px;
}

.repair-actions {
  display: flex;
  justify-content: center;
  margin-top: 32px;
}

.repair-actions .farm-btn {
  min-width: 160px;
  padding: 14px 32px;
  font-size: 15px;
  font-weight: 500;
}
</style>
