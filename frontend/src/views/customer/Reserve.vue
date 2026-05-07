<template>
  <div class="farm-bg">
    <div class="farm-page-container">
      <div class="farm-card farm-card-static">
        <div class="reserve-header">
          <h2 class="reserve-title">我的订单</h2>
          <button class="farm-btn farm-btn-primary" @click="addReserve">
            添加预定
          </button>
        </div>

        <div class="reserve-table-wrapper">
          <table class="farm-table">
            <thead>
              <tr>
                <th>订单号</th>
                <th>桌位</th>
                <th>开始时间</th>
                <th>结束时间</th>
                <th>下单时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(reserve, index) in reserveList" :key="index">
                <td>{{ reserve.no }}</td>
                <td>{{ reserve.diningTable.id }}</td>
                <td>{{ reserve.startTime | formatDate }}</td>
                <td>{{ reserve.endTime | formatDate }}</td>
                <td>{{ reserve.insertTime | formatDate }}</td>
                <td>
                  <button class="farm-btn farm-btn-danger farm-btn-sm" @click="showDeleteConfirm(reserve.no)">
                    取消
                  </button>
                </td>
              </tr>
              <tr v-if="!reserveList || reserveList.length === 0">
                <td colspan="6" class="text-center">暂无预定记录</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <div class="confirm-overlay" v-if="showConfirmDialog" @click="cancelConfirm">
      <div class="confirm-dialog" @click.stop>
        <h3 class="confirm-title">确认取消</h3>
        <p class="confirm-message">{{ confirmMessage }}</p>
        <div class="confirm-buttons">
          <button class="farm-btn farm-btn-secondary" @click="cancelConfirm">取消</button>
          <button class="farm-btn farm-btn-primary" @click="executeDelete">确认</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import bus from "@/util/Bus";
import { formatDate } from "@/assets/js/date";
import { getReserve, deleteReserve } from "@/api/customer";

export default {
  name: "Reserve",
  data: () => ({
    reserveList: null,
    showConfirmDialog: false,
    confirmMessage: "",
    deleteNo: null
  }),
  methods: {
    addReserve: function() {
      this.$router.push("reserveAdd");
    },
    showDeleteConfirm(no) {
      this.deleteNo = no;
      this.confirmMessage = `取消订单：订单号：${no}`;
      this.showConfirmDialog = true;
    },
    cancelConfirm() {
      this.showConfirmDialog = false;
      this.deleteNo = null;
    },
    executeDelete() {
      this.showConfirmDialog = false;
      if (this.deleteNo) {
        deleteReserve(this.deleteNo);
      }
      this.deleteNo = null;
    }
  },
  filters: {
    formatDate(time) {
      var date = new Date(time);
      return formatDate(date, "yyyy-MM-dd hh : mm");
    }
  },
  created() {
    bus.$on(bus.reserveList, data => {
      this.reserveList = data;
    });
  },
  mounted() {
    getReserve();
  },
  beforeDestroy() {
    bus.$off(bus.reserveList);
  }
};
</script>

<style scoped>
.reserve-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid var(--farm-border);
}

.reserve-title {
  font-size: 20px;
  font-weight: 600;
  color: var(--farm-text-primary);
  margin: 0;
}

.reserve-table-wrapper {
  padding: 20px 24px;
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
</style>