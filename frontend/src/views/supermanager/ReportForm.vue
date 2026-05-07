<template>
  <div class="farm-bg">
    <div class="farm-page-container">
      <div class="farm-card farm-card-static">
        <div class="report-form-header">
          <div class="header-left">
            <h2 class="report-form-title">报表管理</h2>
          </div>
        </div>

        <div class="report-form-content">
          <form class="farm-form">
            <!-- 考勤信息 -->
            <div class="farm-form-section">
              <h4 class="farm-section-title">考勤信息</h4>
              <div class="form-row">
                <div class="form-group">
                  <label class="farm-form-label">选择月份</label>
                  <el-date-picker
                    v-model="choosedDate"
                    type="month"
                    placeholder="选择月"
                    class="farm-form-control"
                  >
                  </el-date-picker>
                </div>
                <div class="form-group form-group-btn">
                  <label class="farm-form-label">&nbsp;</label>
                  <button type="button" class="farm-btn farm-btn-primary" @click="downloadAttendanceExcel">
                    导出
                  </button>
                </div>
              </div>
            </div>

            <hr class="farm-divider" />

            <!-- 农场收入 -->
            <div class="farm-form-section">
              <h4 class="farm-section-title">农场收入</h4>
              <div class="form-row">
                <div class="form-group">
                  <label class="farm-form-label">月度收入</label>
                  <el-date-picker
                    v-model="choosedDate1"
                    type="month"
                    placeholder="选择月"
                    class="farm-form-control"
                  >
                  </el-date-picker>
                </div>
                <div class="form-group form-group-btn">
                  <label class="farm-form-label">&nbsp;</label>
                  <button type="button" class="farm-btn farm-btn-primary" @click="downloadMonthSaleExcel">
                    月度导出
                  </button>
                </div>
              </div>
              <div class="form-row mt-2">
                <div class="form-group">
                  <label class="farm-form-label">日度收入</label>
                  <el-date-picker
                    v-model="choosedDate2"
                    type="date"
                    placeholder="选择日"
                    class="farm-form-control"
                  >
                  </el-date-picker>
                </div>
                <div class="form-group form-group-btn">
                  <label class="farm-form-label">&nbsp;</label>
                  <button type="button" class="farm-btn farm-btn-primary" @click="downloadDaySaleExcel">
                    日度导出
                  </button>
                </div>
              </div>
            </div>

            <hr class="farm-divider" />

            <!-- 农场支出 -->
            <div class="farm-form-section">
              <h4 class="farm-section-title">农场支出</h4>
              <div class="form-row">
                <div class="form-group">
                  <label class="farm-form-label">月度支出</label>
                  <el-date-picker
                    v-model="choosedDate3"
                    type="month"
                    placeholder="选择月"
                    class="farm-form-control"
                  >
                  </el-date-picker>
                </div>
                <div class="form-group form-group-btn">
                  <label class="farm-form-label">&nbsp;</label>
                  <button type="button" class="farm-btn farm-btn-primary" @click="downloadMonthPurchaseExcel">
                    月度导出
                  </button>
                </div>
              </div>
              <div class="form-row mt-2">
                <div class="form-group">
                  <label class="farm-form-label">日度支出</label>
                  <el-date-picker
                    v-model="choosedDate4"
                    type="date"
                    placeholder="选择日"
                    class="farm-form-control"
                  >
                  </el-date-picker>
                </div>
                <div class="form-group form-group-btn">
                  <label class="farm-form-label">&nbsp;</label>
                  <button type="button" class="farm-btn farm-btn-primary" @click="downloadDayPurchaseExcel">
                    日度导出
                  </button>
                </div>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {
  downloadAttendanceExcel,
  downloadMonthSaleExcel,
  downloadDaySaleExcel,
  downloadMonthPurchaseExcel,
  downloadDayPurchaseExcel
} from "@/api/supermanager.js";

export default {
  name: "ReportForm",
  data: () => ({
    choosedDate: new Date(),
    choosedDate1: new Date(),
    choosedDate2: new Date(),
    choosedDate3: new Date(),
    choosedDate4: new Date()
  }),
  methods: {
    downloadAttendanceExcel() {
      downloadAttendanceExcel(this.choosedDate);
    },
    downloadMonthSaleExcel() {
      downloadMonthSaleExcel(this.choosedDate1);
    },
    downloadDaySaleExcel() {
      downloadDaySaleExcel(this.choosedDate2);
    },
    downloadMonthPurchaseExcel() {
      downloadMonthPurchaseExcel(this.choosedDate3);
    },
    downloadDayPurchaseExcel() {
      downloadDayPurchaseExcel(this.choosedDate4);
    }
  }
};
</script>

<style scoped>
.report-form-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid var(--farm-border);
}

.header-left {
  display: flex;
  align-items: center;
}

.report-form-title {
  font-size: 20px;
  font-weight: 600;
  color: var(--farm-text-primary);
  margin: 0;
}

.report-form-content {
  padding: 24px;
}

.farm-form {
  max-width: 600px;
  margin: 0 auto;
}

.farm-form-section {
  margin-bottom: 24px;
}

.farm-section-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--farm-text-primary);
  margin: 0 0 16px 0;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.mt-2 {
  margin-top: 16px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group-btn {
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
}

.form-group-btn .farm-btn {
  width: 100%;
}

.farm-form-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: var(--farm-text-primary);
  margin-bottom: 8px;
}

.farm-form-control :deep(.el-input__inner) {
  width: 100%;
  padding: 12px 32px 12px 36px;
  font-size: 14px;
  border: 1px solid var(--farm-border);
  border-radius: var(--farm-radius);
  background: white;
  box-sizing: border-box;
  transition: all 0.2s;
}

.farm-form-control :deep(.el-input__inner:focus) {
  outline: none;
  border-color: var(--farm-primary);
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
}

.farm-form-control :deep(.el-input__icon) {
  color: var(--farm-text-muted);
}

.farm-form-control :deep(.el-input__icon:hover) {
  color: var(--farm-primary);
}

.farm-divider {
  border: none;
  height: 1px;
  background: var(--farm-border);
  margin: 24px 0;
}
</style>
