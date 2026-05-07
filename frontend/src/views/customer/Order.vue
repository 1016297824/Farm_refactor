<template>
  <div class="farm-bg">
    <div class="farm-page-container">
      <div class="farm-card farm-card-static">
        <div class="order-header">
          <div class="header-left">
            <h2 class="order-title">我的订单</h2>
            <span class="order-hint">点击餐桌进入点餐</span>
          </div>
          <a href="/customer/reserveAdd" class="farm-btn farm-btn-primary">
            没有订单，去添加
          </a>
        </div>

        <div class="order-content">
          <div v-if="!reserveLists || reserveLists.length === 0" class="empty-state">
            <p>暂无订单</p>
          </div>
          <div
            class="table-grid"
            v-for="(reserveList, index) in reserveLists"
            :key="index"
          >
            <div
              class="table-item"
              v-for="(reserve, index) in reserveList"
              :key="index"
              @click="orderAdd(reserve.no)"
            >
              <img
                src="@/assets/imgs/diningtable.jpg"
                class="table-image"
                :alt="'订单号：' + reserve.no"
                :title="'订单号：' + reserve.no"
              />
              <span class="table-label">{{ reserve.diningTable.id }}号桌</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import bus from "@/util/Bus";
import { getReserve } from "@/api/customer";

export default {
  name: "Order",
  data: () => ({
    reserveLists: null
  }),
  methods: {
    addReserve: function() {
      this.$router.push("reserveAdd");
    },
    orderAdd(no) {
      this.$router.push({
        path: "orderAdd",
        query: {
          no: no
        }
      });
    }
  },
  created() {
    bus.$on(bus.reserveLists, data => {
      this.reserveLists = data;
    });
  },
  mounted() {
    getReserve();
  },
  beforeDestroy() {
    bus.$off(bus.reserveLists);
  }
};
</script>

<style scoped>
.order-header {
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

.order-title {
  font-size: 20px;
  font-weight: 600;
  color: var(--farm-text-primary);
  margin: 0;
}

.order-hint {
  font-size: 13px;
  color: var(--farm-text-muted);
}

.order-content {
  padding: 24px;
}

.empty-state {
  text-align: center;
  padding: 48px;
  color: var(--farm-text-muted);
}

.table-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 24px;
  margin-bottom: 24px;
}

.table-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  transition: transform 0.2s;
}

.table-item:hover {
  transform: scale(1.05);
}

.table-image {
  width: 120px;
  height: 120px;
  border-radius: var(--farm-radius-lg);
  box-shadow: var(--farm-shadow-md);
  object-fit: cover;
  margin-bottom: 8px;
}

.table-label {
  font-size: 14px;
  font-weight: 500;
  color: var(--farm-text-primary);
}
</style>