<template>
  <div class="farm-bg">
    <div class="farm-page-container">
      <div class="farm-card farm-card-static">
        <div class="reserve-add-header">
          <div class="header-left">
            <h2 class="reserve-add-title">选择时间</h2>
            <span class="time-hint">9:00 ~ 21:00</span>
          </div>
          <button class="farm-btn farm-btn-secondary" @click="back">
            返回
          </button>
        </div>

        <div class="reserve-add-body">
          <div class="time-picker-row">
            <div class="time-picker-item">
              <label class="farm-label">开始时间</label>
              <el-date-picker
                v-model="pageBody.startTime"
                type="datetime"
                format="yyyy-MM-dd HH:mm"
                placeholder="选择日期时间"
                :picker-options="pickerOptionsStartTime"
                @change="startTimeChange"
              >
              </el-date-picker>
            </div>
            <div class="time-picker-item">
              <label class="farm-label">结束时间</label>
              <el-date-picker
                v-model="pageBody.endTime"
                type="datetime"
                format="yyyy-MM-dd HH:00"
                placeholder="选择日期时间"
                :picker-options="pickerOptionsEndTime"
                @change="endTimeChange"
              >
              </el-date-picker>
            </div>
          </div>

          <div class="table-list-wrapper">
            <table class="farm-table">
              <thead>
                <tr>
                  <th>餐桌号</th>
                  <th>类型</th>
                  <th>座位</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(dt, index) in diningTableList" :key="index">
                  <td>{{ dt.id }}</td>
                  <td>{{ dt.type }}</td>
                  <td>{{ dt.seat }}人</td>
                  <td>
                    <input
                      type="button"
                      class="farm-btn farm-btn-primary farm-btn-sm"
                      value="预定"
                      @click="showConfirm(dt)"
                    />
                  </td>
                </tr>
                <tr v-if="!diningTableList || diningTableList.length === 0">
                  <td colspan="4" class="text-center">暂无餐桌</td>
                </tr>
              </tbody>
            </table>
          </div>

          <div class="farm-pagination" v-if="pageBody.pages > 1">
            <button
              type="button"
              class="farm-pagination-item"
              @click="doPage(1)"
              :class="{ disabled: pageBody.page == 1 }"
            >
              首页
            </button>
            <button
              type="button"
              v-for="(page, index) in pageBody.pageList"
              :key="index"
              class="farm-pagination-item"
              :class="{ active: pageBody.page == page }"
              @click="doPage(page)"
            >
              {{ page }}
            </button>
            <button
              type="button"
              class="farm-pagination-item"
              @click="doPage(pageBody.pages)"
              :class="{ disabled: pageBody.page == pageBody.pages }"
            >
              末页
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="confirm-overlay" v-if="showConfirmDialog" @click="cancelConfirm">
      <div class="confirm-dialog" @click.stop>
        <h3 class="confirm-title">确认预定</h3>
        <p class="confirm-message">{{ confirmMessage }}</p>
        <div class="confirm-buttons">
          <button class="farm-btn farm-btn-secondary" @click="cancelConfirm">取消</button>
          <button class="farm-btn farm-btn-primary" @click="executeReserve">确认</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import bus from "@/util/Bus";
import moment from "moment";
import { formatDate } from "@/assets/js/date";
import { initDiningTable, doPage, reserveAddApi } from "@/api/customer";

export default {
  name: "ReserveAdd",
  data: () => ({
    diningTableList: null,
    pageBody: {
      page: null,
      pages: null,
      pageList: null,
      startTime: null,
      endTime: null
    },
    showConfirmDialog: false,
    confirmMessage: "",
    selectedDt: null,
    pickerOptionsStartTime: {
      disabledDate: time => {
        return (
          time.getTime() < Date.now() - 8.64e7 ||
          time.getTime() > Date.now() + 6 * 8.64e7
        );
      }
    },
    pickerOptionsEndTime: {
      disabledDate: time => {
        return (
          time.getTime() < Date.now() - 8.64e7 ||
          time.getTime() > Date.now() + 6 * 8.64e7
        );
      }
    }
  }),
  methods: {
    doPage(page) {
      this.pageBody.page = page;
      doPage(this.pageBody);
    },
    showConfirm(dt) {
      this.selectedDt = dt;
      this.confirmMessage = `餐桌号：${dt.id}（${dt.type}）\n时间：${this.pageBody.startTime}至${this.pageBody.endTime}\n注意：预定后取消需联系工作人员`;
      this.showConfirmDialog = true;
    },
    cancelConfirm() {
      this.showConfirmDialog = false;
      this.selectedDt = null;
    },
    executeReserve() {
      this.showConfirmDialog = false;
      if (!this.selectedDt) return;
      const dt = this.selectedDt;
      this.selectedDt = null;
      this.pageBody.page = 1;
      reserveAddApi(dt, this.pageBody, (message) => {
        bus.$emit("toast", { message: message, type: "success" });
        doPage(this.pageBody);
      });
    },
    back() {
      this.$router.push("reserve");
    },
    startTimeChange() {
      this.pageBody.startTime = new Date(this.pageBody.startTime);
      this.pageBody.endTime = new Date(this.pageBody.endTime);
      if (
        this.pageBody.startTime.getTime() >= this.pageBody.endTime.getTime()
      ) {
        let newEndTime = new Date(this.pageBody.startTime);
        this.pageBody.endTime = new Date(
          newEndTime.getTime() + 1 * 60 * 60 * 1000
        );
      }
      let newDate1 = new Date(this.pageBody.startTime);
      let newDate2 = new Date(this.pageBody.endTime);
      newDate1.setHours(9);
      newDate1.setMinutes(0);
      newDate1.setSeconds(0);
      newDate1.setMilliseconds(0);
      newDate2.setHours(21);
      newDate2.setMinutes(0);
      newDate2.setSeconds(0);
      newDate2.setMilliseconds(0);
      if (newDate1.getTime() > this.pageBody.startTime.getTime()) {
        this.pageBody.startTime.setHours(9);
        this.pageBody.endTime.setHours(10);
      }
      if (newDate2.getTime() <= this.pageBody.startTime.getTime()) {
        this.pageBody.startTime.setHours(20);
        this.pageBody.endTime.setHours(21);
      }
      this.pageBody.startTime.setMinutes(0);
      this.pageBody.startTime.setSeconds(0);
      this.pageBody.startTime.setMilliseconds(0);
      this.pageBody.endTime.setMinutes(0);
      this.pageBody.endTime.setSeconds(0);
      this.pageBody.endTime.setMilliseconds(0);
      this.pageBody.startTime = moment(this.pageBody.startTime)
        .utcOffset(480)
        .format("YYYY-MM-DD HH:mm:ss");
      this.pageBody.endTime = moment(this.pageBody.endTime)
        .utcOffset(480)
        .format("YYYY-MM-DD HH:mm:ss");
      this.doPage(1);
    },
    endTimeChange() {
      this.pageBody.startTime = new Date(this.pageBody.startTime);
      this.pageBody.endTime = new Date(this.pageBody.endTime);
      if (
        this.pageBody.startTime.getTime() >= this.pageBody.endTime.getTime()
      ) {
        let newStartTime = new Date(this.pageBody.endTime);
        this.pageBody.startTime = new Date(
          newStartTime.getTime() - 1 * 60 * 60 * 1000
        );
      }
      let newDate1 = new Date(this.pageBody.startTime);
      let newDate2 = new Date(this.pageBody.endTime);
      newDate1.setHours(9);
      newDate1.setMinutes(0);
      newDate1.setSeconds(0);
      newDate1.setMilliseconds(0);
      newDate2.setHours(21);
      newDate2.setMinutes(0);
      newDate2.setSeconds(0);
      newDate2.setMilliseconds(0);
      if (newDate1.getTime() >= this.pageBody.endTime.getTime()) {
        this.pageBody.startTime.setHours(9);
        this.pageBody.endTime.setHours(10);
      }
      if (newDate2.getTime() < this.pageBody.endTime.getTime()) {
        this.pageBody.startTime.setHours(20);
        this.pageBody.endTime.setHours(21);
      }
      this.pageBody.startTime.setMinutes(0);
      this.pageBody.startTime.setSeconds(0);
      this.pageBody.startTime.setMilliseconds(0);
      this.pageBody.endTime.setMinutes(0);
      this.pageBody.endTime.setSeconds(0);
      this.pageBody.endTime.setMilliseconds(0);
      this.pageBody.startTime = moment(this.pageBody.startTime)
        .utcOffset(480)
        .format("YYYY-MM-DD HH:mm:ss");
      this.pageBody.endTime = moment(this.pageBody.endTime)
        .utcOffset(480)
        .format("YYYY-MM-DD HH:mm:ss");
      this.doPage(1);
    }
  },
  filters: {
    formatDate(time) {
      var date = new Date(time);
      return formatDate(date, "yyyy-MM-dd hh : 00 : 00");
    }
  },
  created() {
    initDiningTable();
    bus.$on(bus.diningTableList, data => {
      this.diningTableList = data;
    });
    bus.$on(bus.pageBody, data => {
      this.pageBody = data;
    });
  },
  mounted() {},
  beforeDestroy() {
    bus.$off(bus.diningTableList);
    bus.$off(bus.pageBody);
  }
};
</script>

<style scoped>
.reserve-add-header {
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

.reserve-add-title {
  font-size: 20px;
  font-weight: 600;
  color: var(--farm-text-primary);
  margin: 0;
}

.time-hint {
  font-size: 13px;
  color: var(--farm-text-muted);
  background: var(--farm-bg-light);
  padding: 4px 12px;
  border-radius: var(--farm-radius);
}

.reserve-add-body {
  padding: 24px;
}

.time-picker-row {
  display: flex;
  gap: 24px;
  margin-bottom: 24px;
}

.time-picker-item {
  flex: 1;
}

.table-list-wrapper {
  margin-bottom: 24px;
}

.farm-pagination-item.disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.text-center {
  text-align: center;
}

.confirm-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.confirm-dialog {
  background: white;
  border-radius: var(--farm-radius-lg);
  padding: 24px;
  min-width: 320px;
  max-width: 400px;
  box-shadow: var(--farm-shadow-xl);
}

.confirm-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--farm-text-primary);
  margin: 0 0 16px 0;
}

.confirm-message {
  font-size: 14px;
  color: var(--farm-text-secondary);
  line-height: 1.6;
  margin: 0 0 24px 0;
  white-space: pre-line;
}

.confirm-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>