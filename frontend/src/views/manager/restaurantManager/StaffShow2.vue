<template>
  <div class="farm-bg">
    <div class="farm-page-container">
      <div class="farm-card farm-card-static">
        <div class="staff-show-header">
          <div class="header-left">
            <h2 class="staff-show-title">员工信息管理</h2>
          </div>
          <button class="farm-btn farm-btn-primary" @click="addStaff">
            添加员工
          </button>
        </div>

        <div class="staff-show-content">
          <table class="farm-table">
            <thead>
              <tr>
                <th class="text-center">工号</th>
                <th class="text-center">姓名</th>
                <th class="text-center">职务</th>
                <th class="text-center">操作</th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="(staff, index) in staffList1"
                :key="index"
                class="farm-table-row"
              >
                <td class="text-center">{{ staff.username }}</td>
                <td class="text-center">{{ staff.name }}</td>
                <td class="text-center">
                  {{ staff.position == null ? "未分配" : staff.position.name }}
                </td>
                <td class="text-center">
                  <button
                    class="farm-btn farm-btn-primary farm-btn-sm mr-2"
                    @click="chooseStaff(staff)"
                  >
                    {{ staff.position == null ? "任职" : "调任" }}
                  </button>
                  <button
                    class="farm-btn farm-btn-danger farm-btn-sm"
                    @click="showDeleteConfirm(staff.name, staff.username)"
                  >
                    解雇
                  </button>
                </td>
              </tr>
              <tr v-if="!staffList1 || staffList1.length === 0">
                <td colspan="4" class="text-center py-4 text-muted">
                  暂无员工信息
                </td>
              </tr>
            </tbody>
          </table>

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

    <div class="position-modal" v-if="showPositionModal">
      <div class="position-modal-overlay" @click="closePositionModal"></div>
      <div class="position-modal-content">
        <div class="position-modal-header">
          <h3 class="position-modal-title">
            {{ staff && staff.position ? "调任" : "任职" }}
          </h3>
          <button class="position-modal-close" @click="closePositionModal">
            &times;
          </button>
        </div>
        <div class="position-modal-body">
          <div class="form-group">
            <label class="farm-form-label">选择职位</label>
            <select class="farm-form-control" v-model="position">
              <option
                v-for="(pos, index) in positionList"
                :key="index"
                :value="pos"
              >
                {{ pos.name }}
              </option>
            </select>
          </div>
        </div>
        <div class="position-modal-footer">
          <button class="farm-btn farm-btn-primary" @click="takeOffice">
            {{ staff && staff.position ? "调任" : "任职" }}
          </button>
        </div>
      </div>
    </div>

    <div class="confirm-overlay" v-if="showConfirmDialog" @click="cancelConfirm">
      <div class="confirm-dialog" @click.stop>
        <h3 class="confirm-title">确认解雇</h3>
        <p class="confirm-message">{{ confirmMessage }}</p>
        <div class="confirm-buttons">
          <button class="farm-btn farm-btn-secondary" @click="cancelConfirm">
            取消
          </button>
          <button class="farm-btn farm-btn-danger" @click="executeDelete">
            确认
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import bus from "@/util/Bus";
import { getStaff, deleteStaff, takeOffice } from "@/api/restaurantManager";

export default {
  name: "StaffShow2",
  data: () => ({
    staffList: [],
    staffList1: [],
    pageBody1: {
      page: null,
      pages: null,
      pageList: []
    },
    positionList: [],
    staff: null,
    position: null,
    showPositionModal: false,
    showConfirmDialog: false,
    confirmMessage: "",
    deleteUsername: null
  }),
  methods: {
    showDeleteConfirm(name, username) {
      this.deleteUsername = username;
      this.confirmMessage = `是否解雇员工：${name}\n工号：${username}`;
      this.showConfirmDialog = true;
    },
    cancelConfirm() {
      this.showConfirmDialog = false;
      this.deleteUsername = null;
    },
    executeDelete() {
      this.showConfirmDialog = false;
      if (this.deleteUsername) {
        deleteStaff(this.deleteUsername);
      }
      this.deleteUsername = null;
    },
    addStaff() {
      this.$router.push("staffAdd2");
    },
    closePositionModal() {
      this.showPositionModal = false;
      this.position = null;
    },
    chooseStaff(staff) {
      this.staff = staff;
      this.showPositionModal = true;
    },
    takeOffice() {
      if (
        this.staff.position != null &&
        this.staff.position.name == this.position.name
      ) {
        bus.$emit("toast", { message: "已是此职位！", type: "warning" });
      } else {
        takeOffice(this.staff, this.position.id);
        this.closePositionModal();
      }
    },
    doPage(page) {
      this.staffList1 = [];
      if (this.pageBody1.pages <= 5) {
        if (this.pageBody1.pages == page) {
          for (let i = (page - 1) * 5; i < this.staffList.length; i++) {
            this.staffList1.push(this.staffList[i]);
          }
        } else {
          for (let i = (page - 1) * 5; i < page * 5; i++) {
            this.staffList1.push(this.staffList[i]);
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
            for (let i = (page - 1) * 5; i < this.staffList.length; i++) {
              this.staffList1.push(this.staffList[i]);
            }
          } else {
            for (let i = (page - 1) * 5; i < page * 5; i++) {
              this.staffList1.push(this.staffList[i]);
            }
          }
        } else if (page <= 2) {
          for (let i = 0; i < 5; i++) {
            this.pageBody1.pageList.push(i + 1);
          }
          for (let i = (page - 1) * 5; i < page * 5; i++) {
            this.staffList1.push(this.staffList[i]);
          }
        } else {
          for (let i = page - 2; i <= page + 2; i++) {
            this.pageBody1.pageList.push(i);
          }
          for (let i = (page - 1) * 5; i < page * 5; i++) {
            this.staffList1.push(this.staffList[i]);
          }
        }
      }
      this.pageBody1.page = page;
    }
  },
  created() {
    getStaff();
    bus.$on(bus.staffList, data => {
      this.staffList = data;
    });
    bus.$on(bus.staffList1, data => {
      this.staffList1 = data;
    });
    bus.$on(bus.pageBody1, data => {
      this.pageBody1 = data;
    });
    bus.$on(bus.positionList, data => {
      this.positionList = data;
    });
  },
  beforeDestroy() {
    bus.$off(bus.staffList);
    bus.$off(bus.staffList1);
    bus.$off(bus.pageBody1);
    bus.$off(bus.positionList);
  }
};
</script>

<style scoped>
.staff-show-header {
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

.staff-show-title {
  font-size: 20px;
  font-weight: 600;
  color: var(--farm-text-primary);
  margin: 0;
}

.staff-show-content {
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

.mr-2 {
  margin-right: 8px;
}

.position-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
}

.position-modal-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
}

.position-modal-content {
  position: relative;
  background: white;
  border-radius: var(--farm-radius-lg);
  width: 90%;
  max-width: 450px;
  box-shadow: var(--farm-shadow-xl);
}

.position-modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid var(--farm-border);
}

.position-modal-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
}

.position-modal-close {
  background: none;
  border: none;
  font-size: 28px;
  line-height: 1;
  cursor: pointer;
  color: var(--farm-text-muted);
}

.position-modal-close:hover {
  color: var(--farm-text-primary);
}

.position-modal-body {
  padding: 20px;
}

.form-group {
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
  padding: 10px 12px;
  font-size: 14px;
  border: 1px solid var(--farm-border);
  border-radius: var(--farm-radius);
  background: white;
  transition: border-color 0.2s;
}

.farm-form-control:focus {
  outline: none;
  border-color: var(--farm-primary);
}

.position-modal-footer {
  padding: 16px 20px;
  border-top: 1px solid var(--farm-border);
  display: flex;
  justify-content: flex-end;
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
  white-space: pre-line;
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
