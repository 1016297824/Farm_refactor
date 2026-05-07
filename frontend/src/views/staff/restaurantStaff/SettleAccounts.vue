<template>
  <div class="farm-page-container">
    <div class="farm-card-static settle-page">
      <div class="settle-header">
        <div class="settle-title-section">
          <h1 class="farm-title-main">顾客订单管理</h1>
          <div class="farm-divider"></div>
        </div>
      </div>

      <div class="settle-table-wrapper">
        <table class="farm-table">
          <thead>
            <tr>
              <th>订单号</th>
              <th>桌位</th>
              <th>顾客姓名</th>
              <th>操作</th>
              <th>状态</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(reserve, index) in reserveList"
              :key="index"
            >
              <td>{{ reserve.no }}</td>
              <td>{{ reserve.diningTable.id }}</td>
              <td>{{ reserve.customer.name }}</td>
              <td>
                <button
                  type="button"
                  class="farm-btn farm-btn-sm farm-btn-primary"
                  @click="getOrdering(reserve.no)"
                >
                  查看
                </button>
              </td>
              <td>
                <button
                  type="button"
                  class="farm-btn farm-btn-sm farm-btn-outline"
                  @click="showDeleteConfirm(reserve.no)"
                >
                  取消订单
                </button>
              </td>
            </tr>
            <tr v-if="!reserveList || reserveList.length === 0">
              <td colspan="5" class="settle-empty">暂无订单</td>
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
      v-if="showOrderModal"
      @click="showOrderModal = false"
    >
      <div class="farm-modal" @click.stop>
        <div class="farm-modal-header">
          <h3 class="farm-modal-title">订单详情</h3>
          <button class="farm-modal-close" @click="showOrderModal = false">
            &times;
          </button>
        </div>
        <div class="farm-modal-body">
          <div v-if="orderingList.length == 0" class="settle-empty-modal">
            暂无订单详情
          </div>
          <table class="farm-table" v-else>
            <thead>
              <tr>
                <th>菜名</th>
                <th>份数</th>
                <th>单价</th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="(ordering, index) in orderingList"
                :key="index"
              >
                <td>{{ ordering.menu.name }}</td>
                <td>{{ ordering.count }}</td>
                <td>
                  {{ ordering.menu.price | numFilter }}
                  {{ ordering.menu.unite }}/份
                </td>
              </tr>
              <tr class="settle-total-row">
                <td></td>
                <td class="settle-total-label">合计</td>
                <td class="settle-total-value">
                  {{ totalPrice | numFilter }} 元
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="farm-modal-footer">
          <button
            class="farm-btn farm-btn-outline"
            @click="showOrderModal = false"
          >
            关闭
          </button>
          <button
            class="farm-btn farm-btn-primary"
            :disabled="orderingList.length == 0"
            @click="showSettleConfirm"
          >
            结账
          </button>
        </div>
      </div>
    </div>

    <div
      class="farm-modal-overlay"
      v-if="showDeleteDialog"
      @click="cancelDeleteReserve"
    >
      <div class="confirm-dialog" @click.stop>
        <h3 class="confirm-title">确认取消</h3>
        <p class="confirm-message">是否取消订单：{{ deleteReserveNo }}？</p>
        <div class="confirm-buttons">
          <button class="farm-btn farm-btn-outline" @click="cancelDeleteReserve">
            返回
          </button>
          <button class="farm-btn farm-btn-danger" @click="executeDeleteReserve">
            确认取消
          </button>
        </div>
      </div>
    </div>

    <div
      class="farm-modal-overlay"
      v-if="showSettleDialog"
      @click="cancelSettle"
    >
      <div class="confirm-dialog" @click.stop>
        <h3 class="confirm-title">确认结账</h3>
        <p class="confirm-message">订单号：{{ reserveNo }}</p>
        <div class="confirm-buttons">
          <button class="farm-btn farm-btn-outline" @click="cancelSettle">
            返回
          </button>
          <button class="farm-btn farm-btn-primary" @click="executeSettle">
            确认结账
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import bus from "@/util/Bus";
import {
  getReserve,
  doPage,
  deleteReserve,
  getOrdering,
  settleAccounts
} from "@/api/restaurantStaff.js";

export default {
  name: "SettleAccounts",
  data: () => ({
    reserveList: null,
    orderingList: [],
    reserveNo: null,
    pageBody1: {
      page: null,
      pages: null,
      pageList: []
    },
    showOrderModal: false,
    showDeleteDialog: false,
    showSettleDialog: false,
    deleteReserveNo: null
  }),
  methods: {
    getOrdering(no) {
      this.reserveNo = no;
      getOrdering(no);
      this.showOrderModal = true;
    },
    doPage(page) {
      this.pageBody1.page = page;
      doPage(this.pageBody1);
    },
    showDeleteConfirm(no) {
      this.deleteReserveNo = no;
      this.showDeleteDialog = true;
    },
    cancelDeleteReserve() {
      this.showDeleteDialog = false;
      this.deleteReserveNo = null;
    },
    executeDeleteReserve() {
      this.showDeleteDialog = false;
      deleteReserve(this.deleteReserveNo);
      this.deleteReserveNo = null;
    },
    showSettleConfirm() {
      this.showSettleDialog = true;
    },
    cancelSettle() {
      this.showSettleDialog = false;
    },
    executeSettle() {
      this.showSettleDialog = false;
      this.showOrderModal = false;
      settleAccounts(this.reserveNo);
      bus.$emit("toast", { message: "结账成功！", type: "success" });
    },
    accMul(arg1, arg2) {
      var m = 0,
        s1 = arg1.toString(),
        s2 = arg2.toString();
      try {
        m += s1.split(".")[1].length;
      } catch (e) {
        // no decimal part
      }
      try {
        m += s2.split(".")[1].length;
      } catch (e) {
        // no decimal part
      }
      return (
        (Number(s1.replace(".", "")) * Number(s2.replace(".", ""))) /
        Math.pow(10, m)
      );
    },
    accAdd(arg1, arg2) {
      var r1, r2, m;
      try {
        r1 = arg1.toString().split(".")[1].length;
      } catch (e) {
        r1 = 0;
      }
      try {
        r2 = arg2.toString().split(".")[1].length;
      } catch (e) {
        r2 = 0;
      }
      m = Math.pow(10, Math.max(r1, r2));
      return (arg1 * m + arg2 * m) / m;
    }
  },
  created() {
    getReserve();
    bus.$on(bus.reserveList, data => {
      this.reserveList = data;
    });
    bus.$on(bus.orderingList, data => {
      this.orderingList = data;
    });
    bus.$on(bus.pageBody1, data => {
      this.pageBody1 = data;
    });
  },
  computed: {
    totalPrice: function() {
      let totalPrice = 0;
      if (this.orderingList.length != 0) {
        for (let i = 0; i < this.orderingList.length; i++) {
          let tp = this.accMul(
            this.orderingList[i].count,
            this.orderingList[i].menu.price
          );
          totalPrice = this.accAdd(totalPrice, tp);
        }
        return totalPrice.toFixed(2);
      } else {
        return null;
      }
    }
  },
  filters: {
    numFilter(value) {
      let realVal = "";
      if (!isNaN(value) && value !== "") {
        realVal = parseFloat(value).toFixed(2);
      } else {
        realVal = "--";
      }
      return realVal;
    }
  },
  beforeDestroy() {
    bus.$off(bus.reserveList);
    bus.$off(bus.orderingList);
    bus.$off(bus.pageBody1);
  }
};
</script>

<style scoped>
.settle-page {
  padding: 32px;
}

.settle-header {
  margin-bottom: 32px;
}

.settle-title-section {
  text-align: center;
  margin-bottom: 32px;
}

.settle-table-wrapper {
  overflow-x: auto;
  margin-bottom: 24px;
  border-radius: var(--farm-radius);
  background: white;
}

.farm-table {
  min-width: 600px;
}

.settle-empty {
  text-align: center;
  padding: 32px 0;
  color: var(--farm-text-muted);
  font-size: 14px;
}

.settle-empty-modal {
  text-align: center;
  padding: 32px 0;
  color: var(--farm-text-muted);
  font-size: 14px;
}

.settle-total-row {
  border-top: 2px solid var(--farm-border);
}

.settle-total-label {
  font-weight: 600;
  text-align: center;
}

.settle-total-value {
  font-weight: 600;
  text-align: center;
  color: var(--farm-primary);
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
  .settle-page {
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
