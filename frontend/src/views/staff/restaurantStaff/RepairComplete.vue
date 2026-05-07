<template>
  <div class="farm-page-container">
    <div class="farm-card-static repair-complete-page">
      <div class="repair-complete-header">
        <div class="repair-complete-title-section">
          <h1 class="farm-title-main">报修报损管理</h1>
          <div class="farm-divider"></div>
        </div>
      </div>

      <div class="repair-complete-table-wrapper">
        <table class="farm-table">
          <thead>
            <tr>
              <th>编号</th>
              <th>日期</th>
              <th>类型</th>
              <th>操作</th>
              <th>状态</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(repair, index) in repairList"
              :key="index"
            >
              <td>{{ repair.id }}</td>
              <td>{{ repair.insertTime | formatDate }}</td>
              <td>{{ repair.repairType }}</td>
              <td>
                <button
                  type="button"
                  class="farm-btn farm-btn-sm farm-btn-primary"
                  @click="checkRepair(repair)"
                >
                  查看
                </button>
              </td>
              <td>
                <button
                  type="button"
                  class="farm-btn farm-btn-sm farm-btn-outline"
                  @click="showDeleteConfirm(repair)"
                >
                  取消
                </button>
              </td>
            </tr>
            <tr v-if="!repairList || repairList.length === 0">
              <td colspan="5" class="repair-empty">暂无报修报损记录</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="farm-pagination" v-if="pageBody1.pages > 1">
        <button class="farm-pagination-item" @click="doPage(1)">
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
          >
            <polyline points="11 17 6 12 11 7"></polyline>
            <polyline points="18 17 13 12 18 7"></polyline>
          </svg>
        </button>
        <button
          v-for="(page, index) in pageBody1.pageList"
          :key="index"
          :class="[
            'farm-pagination-item',
            pageBody1.page == page ? 'active' : ''
          ]"
          @click="doPage(page)"
        >
          {{ page }}
        </button>
        <button class="farm-pagination-item" @click="doPage(pageBody1.pages)">
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
          >
            <polyline points="13 17 18 12 13 7"></polyline>
            <polyline points="6 17 11 12 6 7"></polyline>
          </svg>
        </button>
      </div>
    </div>

    <div
      class="farm-modal-overlay"
      v-if="showDetailModal"
      @click="showDetailModal = false"
    >
      <div class="farm-modal" @click.stop>
        <div class="farm-modal-header">
          <h3 class="farm-modal-title">详细信息</h3>
          <button class="farm-modal-close" @click="showDetailModal = false">
            &times;
          </button>
        </div>
        <div class="farm-modal-body">
          <div class="farm-form-group">
            <label class="farm-label">{{ repair.repairType }}内容</label>
            <textarea
              readonly
              class="farm-input farm-textarea"
              rows="3"
              v-model="repair.content"
            ></textarea>
          </div>
          <div class="farm-form-group">
            <label class="farm-label">{{ repair.repairType }}原因</label>
            <textarea
              readonly
              class="farm-input farm-textarea"
              rows="5"
              v-model="repair.cause"
            ></textarea>
          </div>
        </div>
        <div class="farm-modal-footer">
          <div class="price-input-row">
            <label class="farm-label">花费</label>
            <div class="price-input-wrapper">
              <input
                type="text"
                class="farm-input"
                v-model="repair.price"
                @keyup="writePrice"
                placeholder="请输入花费金额"
              />
              <span class="price-unit">元</span>
            </div>
          </div>
          <p v-if="priceMessage" class="farm-error">{{ priceMessage }}</p>
          <div class="modal-footer-actions">
            <button
              class="farm-btn farm-btn-outline"
              @click="showDetailModal = false"
            >
              取消
            </button>
            <button class="farm-btn farm-btn-primary" @click="completeRepair">
              完成
            </button>
          </div>
        </div>
      </div>
    </div>

    <div
      class="farm-modal-overlay"
      v-if="showDeleteDialog"
      @click="cancelDelete"
    >
      <div class="confirm-dialog" @click.stop>
        <h3 class="confirm-title">确认取消</h3>
        <p class="confirm-message">是否取消编号：{{ deleteRepairId }}？</p>
        <div class="confirm-buttons">
          <button class="farm-btn farm-btn-outline" @click="cancelDelete">
            返回
          </button>
          <button class="farm-btn farm-btn-danger" @click="executeDelete">
            确认取消
          </button>
        </div>
      </div>
    </div>

    <div
      class="farm-modal-overlay"
      v-if="showCompleteDialog"
      @click="cancelComplete"
    >
      <div class="confirm-dialog" @click.stop>
        <h3 class="confirm-title">确认完成</h3>
        <p class="confirm-message">
          编号：{{ repair.id }}<br />花费：{{ repair.price }} 元
        </p>
        <div class="confirm-buttons">
          <button class="farm-btn farm-btn-outline" @click="cancelComplete">
            返回
          </button>
          <button class="farm-btn farm-btn-primary" @click="executeComplete">
            确认完成
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import bus from "@/util/Bus";
import { formatDate } from "@/assets/js/date";
import {
  getRepair,
  doPage1,
  deleteRepair,
  completeRepair
} from "@/api/restaurantStaff.js";

export default {
  name: "RepairComplete",
  data: () => ({
    repairList: null,
    pageBody1: {
      page: null,
      pages: null,
      pageList: []
    },
    repair: {
      id: null,
      insertTime: null,
      repairType: null,
      content: null,
      cause: null,
      state: null,
      price: null
    },
    priceMessage: null,
    showDetailModal: false,
    showDeleteDialog: false,
    showCompleteDialog: false,
    deleteRepairId: null
  }),
  methods: {
    writePrice() {
      this.priceMessage = null;
    },
    doPage(page) {
      this.pageBody1.page = page;
      doPage1(this.pageBody1);
    },
    showDeleteConfirm(repair) {
      this.deleteRepairId = repair.id;
      this.repair = JSON.parse(JSON.stringify(repair));
      this.showDeleteDialog = true;
    },
    cancelDelete() {
      this.showDeleteDialog = false;
      this.deleteRepairId = null;
    },
    executeDelete() {
      this.showDeleteDialog = false;
      deleteRepair(this.repair);
      this.deleteRepairId = null;
    },
    checkRepair(repair) {
      this.priceMessage = null;
      this.repair = JSON.parse(JSON.stringify(repair));
      this.showDetailModal = true;
    },
    completeRepair() {
      this.priceMessage = null;
      let re = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/;
      if (!re.test(this.repair.price)) {
        this.priceMessage = "请输入正数（小数点后最多两位）！";
        return;
      }
      if (this.repair.price == 0) {
        this.priceMessage = "输入数字不能为0！";
        return;
      }
      this.showCompleteDialog = true;
    },
    cancelComplete() {
      this.showCompleteDialog = false;
    },
    executeComplete() {
      this.showCompleteDialog = false;
      this.showDetailModal = false;
      completeRepair(this.repair);
      bus.$emit("toast", { message: "完成确认成功！", type: "success" });
    }
  },
  filters: {
    formatDate(time) {
      var date = new Date(time);
      return formatDate(date, "yyyy-MM-dd");
    }
  },
  created() {
    getRepair();
    bus.$on(bus.repairList, data => {
      this.repairList = data;
    });
    bus.$on(bus.pageBody1, data => {
      this.pageBody1 = data;
    });
  },
  beforeDestroy() {
    bus.$off(bus.repairList);
    bus.$off(bus.pageBody1);
  }
};
</script>

<style scoped>
.repair-complete-page {
  padding: 32px;
}

.repair-complete-header {
  margin-bottom: 32px;
}

.repair-complete-title-section {
  text-align: center;
  margin-bottom: 32px;
}

.repair-complete-table-wrapper {
  overflow-x: auto;
  margin-bottom: 24px;
  border-radius: var(--farm-radius);
  background: white;
}

.farm-table {
  min-width: 600px;
}

.repair-empty {
  text-align: center;
  padding: 32px 0;
  color: var(--farm-text-muted);
  font-size: 14px;
}

.farm-textarea {
  resize: vertical;
  min-height: 80px;
}

.price-input-row {
  margin-bottom: 16px;
}

.price-input-wrapper {
  display: flex;
  align-items: center;
  gap: 8px;
}

.price-input-wrapper .farm-input {
  flex: 1;
}

.price-unit {
  font-size: 14px;
  color: var(--farm-text-secondary);
  white-space: nowrap;
}

.modal-footer-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 16px;
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

@media (max-width: 768px) {
  .repair-complete-page {
    padding: 20px;
  }

  .farm-table {
    font-size: 13px;
  }

  .farm-btn-sm {
    padding: 4px 10px;
    font-size: 12px;
  }
}
</style>
