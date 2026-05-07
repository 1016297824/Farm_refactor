<template>
  <div class="farm-bg">
    <div class="farm-page-container">
      <div class="farm-card farm-card-static">
        <div class="attendance-header">
          <div class="header-left">
            <h2 class="attendance-title">考勤信息管理</h2>
          </div>
          <div class="header-right">
            <el-date-picker
              v-model="choosedDate"
              type="date"
              placeholder="选择日期"
              format="yyyy 年 MM 月 dd 日"
              @change="chooseDate"
              class="farm-date-picker"
            >
            </el-date-picker>
          </div>
        </div>

        <div class="attendance-content">
          <table class="farm-table">
            <thead>
              <tr>
                <th class="text-center">工号</th>
                <th class="text-center">姓名</th>
                <th class="text-center">工时</th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="(attendance, index) in attendanceList1"
                :key="index"
                class="farm-table-row"
              >
                <td class="text-center">{{ attendance.staff.username }}</td>
                <td class="text-center">{{ attendance.staff.name }}</td>
                <td class="text-center">
                  <select
                    v-model="attendance.workingHours"
                    @change="changeWorkingHours(attendance)"
                    class="farm-form-control farm-form-control-sm"
                  >
                    <option
                      v-for="(workingHours, index) in workingHoursList"
                      :key="index"
                      :value="workingHours"
                    >
                      {{ workingHours }}
                    </option>
                  </select>
                  <span class="ml-2">小时</span>
                </td>
              </tr>
              <tr v-if="!attendanceList1 || attendanceList1.length === 0">
                <td colspan="3" class="text-center py-4 text-muted">
                  暂无考勤信息
                </td>
              </tr>
            </tbody>
          </table>

          <div class="attendance-actions">
            <button class="farm-btn farm-btn-primary" @click="showConfirm">
              提交更改
            </button>
          </div>

          <div class="farm-pagination" v-if="pageBody1.pages > 1">
            <button
              class="farm-pagination-item"
              @click="doPage(1)"
              :class="{ disabled: pageBody1.page == 1 }"
            >
              首页
            </button>
            <button
              v-for="(page, index) in pageBody1.pageList"
              :key="index"
              class="farm-pagination-item"
              :class="{ active: pageBody1.page == page }"
              @click="doPage(page)"
            >
              {{ page }}
            </button>
            <button
              class="farm-pagination-item"
              @click="doPage(pageBody1.pages)"
              :class="{ disabled: pageBody1.page == pageBody1.pages }"
            >
              末页
            </button>
          </div>
        </div>
      </div>
    </div>

    <div
      class="confirm-overlay"
      v-if="showConfirmDialog"
      @click="cancelConfirm"
    >
      <div class="confirm-dialog" @click.stop>
        <h3 class="confirm-title">确认提交</h3>
        <p class="confirm-message">是否提交更改？</p>
        <div class="confirm-buttons">
          <button class="farm-btn farm-btn-secondary" @click="cancelConfirm">
            取消
          </button>
          <button class="farm-btn farm-btn-primary" @click="executeSubmit">
            确认
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import bus from "@/util/Bus";
import {
  initAttendance,
  updateAttendance,
  chooseDate
} from "@/api/restaurantManager.js";

export default {
  name: "Attendance2",
  data: () => ({
    choosedDate: new Date(),
    attendanceList: [],
    attendanceList1: [],
    pageBody1: {
      page: null,
      pages: null,
      pageList: []
    },
    workingHoursList: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
    showConfirmDialog: false
  }),
  methods: {
    showConfirm() {
      this.showConfirmDialog = true;
    },
    cancelConfirm() {
      this.showConfirmDialog = false;
    },
    executeSubmit() {
      this.showConfirmDialog = false;
      updateAttendance(this.attendanceList, this.choosedDate);
      bus.$emit("toast", { message: "提交成功！", type: "success" });
    },
    changeWorkingHours(attendance) {
      for (let i = 0; i < this.attendanceList.length; i++) {
        if (this.attendanceList[i].id == attendance.id) {
          this.attendanceList[i].workingHours = attendance.workingHours;
        }
      }
    },
    chooseDate() {
      chooseDate(this.choosedDate);
    },
    doPage(page) {
      this.attendanceList1 = [];
      if (this.pageBody1.pages <= 5) {
        if (this.pageBody1.pages == page) {
          for (let i = (page - 1) * 5; i < this.attendanceList.length; i++) {
            this.attendanceList1.push(this.attendanceList[i]);
          }
        } else {
          for (let i = (page - 1) * 5; i < page * 5; i++) {
            this.attendanceList1.push(this.attendanceList[i]);
          }
        }
      } else {
        this.pageBody1.pageList = [];
        if (page > this.pageBody1.pages - 2) {
          for (
            let i = this.pageBody1.pages - 5;
            i < this.pageBody1.pages;
            i++
          ) {
            this.pageBody1.pageList.push(i + 1);
          }
          if (this.pageBody1.pages == page) {
            for (let i = (page - 1) * 5; i < this.attendanceList.length; i++) {
              this.attendanceList1.push(this.attendanceList[i]);
            }
          } else {
            for (let i = (page - 1) * 5; i < page * 5; i++) {
              this.attendanceList1.push(this.attendanceList[i]);
            }
          }
        } else if (page <= 2) {
          for (let i = 0; i < 5; i++) {
            this.pageBody1.pageList.push(i + 1);
          }
          for (let i = (page - 1) * 5; i < page * 5; i++) {
            this.attendanceList1.push(this.attendanceList[i]);
          }
        } else {
          for (let i = page - 2; i <= page + 2; i++) {
            this.pageBody1.pageList.push(i);
          }
          for (let i = (page - 1) * 5; i < page * 5; i++) {
            this.attendanceList1.push(this.attendanceList[i]);
          }
        }
      }
      this.pageBody1.page = page;
    }
  },
  created() {
    initAttendance();
    bus.$on(bus.attendanceList, data => {
      this.attendanceList = data;
    });
    bus.$on(bus.attendanceList1, data => {
      this.attendanceList1 = data;
    });
    bus.$on(bus.pageBody1, data => {
      this.pageBody1 = data;
    });
  },
  beforeDestroy() {
    bus.$off(bus.attendanceList);
    bus.$off(bus.attendanceList1);
    bus.$off(bus.pageBody1);
  }
};
</script>

<style scoped>
.attendance-header {
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

.header-right {
  display: flex;
  align-items: center;
}

.attendance-title {
  font-size: 20px;
  font-weight: 600;
  color: var(--farm-text-primary);
  margin: 0;
}

.attendance-content {
  padding: 24px;
}

.text-center {
  text-align: center;
}

.py-4 {
  padding-top: 1.5rem;
  padding-bottom: 1.5rem;
}

.text-muted {
  color: var(--farm-text-muted);
}

.ml-2 {
  margin-left: 8px;
}

.farm-date-picker {
  width: 200px;
}

.farm-date-picker :deep(.el-input__inner) {
  padding: 10px 32px 10px 36px;
  border: 1px solid var(--farm-border);
  border-radius: var(--farm-radius);
  font-size: 14px;
  background: white;
  transition: all 0.2s;
}

.farm-date-picker :deep(.el-input__inner:focus) {
  outline: none;
  border-color: var(--farm-primary);
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
}

.farm-date-picker :deep(.el-input__icon) {
  color: var(--farm-text-muted);
}

.farm-date-picker :deep(.el-input__icon:hover) {
  color: var(--farm-primary);
}

.farm-form-control-sm {
  width: auto;
  display: inline-block;
  padding: 6px 10px;
  font-size: 13px;
  border: 1px solid var(--farm-border);
  border-radius: var(--farm-radius);
}

.attendance-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
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
  z-index: 1001;
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
}

.confirm-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.farm-pagination {
  display: flex;
  justify-content: center;
  gap: 4px;
  margin-top: 20px;
}

.farm-pagination-item {
  padding: 6px 12px;
  border: 1px solid var(--farm-border);
  border-radius: var(--farm-radius);
  background: white;
  cursor: pointer;
  font-size: 13px;
  transition: all 0.2s;
}

.farm-pagination-item:hover {
  background: var(--farm-bg-light);
}

.farm-pagination-item.active {
  background: var(--farm-primary);
  border-color: var(--farm-primary);
  color: white;
}

.farm-pagination-item.disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
