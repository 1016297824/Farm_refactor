<template>
  <div class="farm-bg">
    <div class="farm-page-container">
      <div class="farm-card farm-card-static">
        <div class="order-add-header">
          <div class="header-left">
            <button class="farm-btn farm-btn-secondary farm-btn-sm" @click="goBack">
              返回
            </button>
            <h2 class="order-add-title">本桌菜单</h2>
          </div>
          <button
            class="farm-btn farm-btn-primary"
            :disabled="isOrdered"
            @click="initModel"
          >
            添加
          </button>
        </div>

        <div class="order-add-content">
          <table class="farm-table">
            <thead>
              <tr>
                <th>菜名</th>
                <th>份数</th>
                <th>单价</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(ordering, index) in orderingList" :key="index">
                <td>{{ ordering.menu.name }}</td>
                <td>
                  <span v-show="!isOrdered" class="count-btn" @click="decreaseCount1(index)">-</span>
                  {{ ordering.count }}
                  <span v-show="!isOrdered" class="count-btn" @click="addCount1(index)">+</span>
                </td>
                <td>{{ ordering.menu.price | numFilter }} {{ ordering.menu.unite + "/份" }}</td>
              </tr>
              <tr v-if="orderingList.length === 0">
                <td colspan="3" class="text-center">暂无菜品，请添加</td>
              </tr>
              <tr v-if="orderingList.length != 0">
                <td colspan="2" class="text-right font-weight-bold">合计</td>
                <td class="font-weight-bold">{{ totalPrice | numFilter }} 元</td>
              </tr>
            </tbody>
          </table>

          <div class="text-center" v-if="!isOrdered && orderingList.length > 0">
            <button class="farm-btn farm-btn-primary" @click="showConfirm">
              提交
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="menu-modal" v-if="showMenuModal">
      <div class="menu-modal-overlay" @click="closeMenuModal"></div>
      <div class="menu-modal-content">
        <div class="menu-modal-header">
          <h3 class="menu-modal-title">菜单</h3>
          <button class="menu-modal-close" @click="closeMenuModal">&times;</button>
        </div>
        <div class="menu-modal-body">
          <table class="farm-table">
            <thead>
              <tr>
                <th>选项</th>
                <th width="60">图片</th>
                <th>菜名</th>
                <th>单价</th>
                <th>类型</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(menu, index) in menuList1" :key="index">
                <td>
                  <span class="select-icon" :class="{ selected: selectedMenu && selectedMenu.id === menu.id }" @click="selectMenu(menu)">
                    <span class="iconfont icon-xuanzhongduigou"></span>
                  </span>
                </td>
                <td>
                  <img :src="require('@/assets/imgs/menu/' + menu.id + '.jpg')" class="menu-img" />
                </td>
                <td>{{ menu.name }}</td>
                <td>{{ menu.price | numFilter }}{{ menu.unite + "/份" }}</td>
                <td>{{ menu.type }}</td>
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
        <div class="menu-modal-footer">
          <div class="selected-info">
            <span class="farm-label">已选：</span>
            <span class="selected-name">{{ selectedMenu ? selectedMenu.name : '请选择菜品' }}</span>
          </div>
          <div class="count-control">
            <span class="count-btn" @click="decreaseCount">-</span>
            <span class="count-number">{{ count }}</span>
            <span class="count-btn" @click="addCount">+</span>
          </div>
          <button class="farm-btn farm-btn-primary farm-btn-sm" @click="confirmAddMenu">
            确认
          </button>
        </div>
      </div>
    </div>

    <div class="confirm-overlay" v-if="showConfirmDialog" @click="cancelConfirm">
      <div class="confirm-dialog" @click.stop>
        <h3 class="confirm-title">确认提交</h3>
        <p class="confirm-message">是否提交？ 注意：提交后不可更改！</p>
        <div class="confirm-buttons">
          <button class="farm-btn farm-btn-secondary" @click="cancelConfirm">取消</button>
          <button class="farm-btn farm-btn-primary" @click="executeSubmit">确认</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import bus from "@/util/Bus";
import { getOrdering, addOrderingApi } from "@/api/customer";

export default {
  name: "OrderAdd",
  data: () => ({
    no: null,
    count: 1,
    pageBody1: {
      page: null,
      pages: null,
      pageList: []
    },
    isOrdered: null,
    menu: {
      id: null,
      name: null,
      price: null,
      unite: null,
      type: null
    },
    menuList1: [],
    menuList: [],
    ordering: {
      id: null,
      count: null,
      menu: null,
      reserve: null
    },
    orderingList: [],
    showConfirmDialog: false,
    showMenuModal: false,
    selectedMenu: null
  }),
  methods: {
    initModel() {
      this.count = 1;
      this.selectedMenu = null;
      this.menu.name = "";
      this.doPage(1);
      this.showMenuModal = true;
    },
    closeMenuModal() {
      this.showMenuModal = false;
    },
    selectMenu(menu) {
      this.selectedMenu = menu;
      this.count = 1;
    },
    confirmAddMenu() {
      if (!this.selectedMenu) {
        bus.$emit("toast", { message: "请选菜！", type: "warning" });
        return;
      }
      let result = false;
      for (let i = 0; i < this.orderingList.length; i++) {
        if (this.selectedMenu.name == this.orderingList[i].menu.name) {
          result = true;
        }
      }
      if (result == true) {
        bus.$emit("toast", { message: "已添加这个菜品！", type: "warning" });
      } else {
        const menuCopy = JSON.parse(JSON.stringify(this.selectedMenu));
        const orderingItem = {
          id: null,
          count: this.count,
          menu: menuCopy,
          reserve: this.ordering.reserve
        };
        this.orderingList.push(orderingItem);
        this.showMenuModal = false;
      }
    },
    getMenu(menu) {
      this.menu = JSON.parse(JSON.stringify(menu));
    },
    addCount() {
      this.count++;
    },
    decreaseCount() {
      if (this.count > 1) {
        this.count--;
      }
    },
    addCount1(index) {
      this.orderingList[index].count++;
    },
    decreaseCount1(index) {
      this.orderingList[index].count--;
      if (this.orderingList[index].count == 0) {
        this.$delete(this.orderingList, index);
      }
    },
    showConfirm() {
      this.showConfirmDialog = true;
    },
    cancelConfirm() {
      this.showConfirmDialog = false;
    },
    executeSubmit() {
      this.showConfirmDialog = false;
      addOrderingApi(this.no, this.orderingList, (message) => {
        bus.$emit("toast", { message: message, type: "success" });
      });
    },
    goBack() {
      this.$router.back();
    },
    doPage(page) {
      this.menuList1 = [];
      if (this.pageBody1.pages <= 5) {
        if (this.pageBody1.pages == page) {
          for (let i = (page - 1) * 5; i < this.menuList.length; i++) {
            this.menuList1.push(this.menuList[i]);
          }
        } else {
          for (let i = (page - 1) * 5; i < page * 5; i++) {
            this.menuList1.push(this.menuList[i]);
          }
        }
      } else {
        this.pageBody1.pageList = [];
        if (page > this.pageBody1.pages - 2) {
          for (let i = this.pageBody1.pages - 5; i < this.pageBody1.pages; i++) {
            this.pageBody1.pageList.push(i + 1);
          }
          if (this.pageBody1.pages == page) {
            for (let i = (page - 1) * 5; i < this.menuList.length; i++) {
              this.menuList1.push(this.menuList[i]);
            }
          } else {
            for (let i = (page - 1) * 5; i < page * 5; i++) {
              this.menuList1.push(this.menuList[i]);
            }
          }
        } else if (page <= 2) {
          for (let i = 0; i < 5; i++) {
            this.pageBody1.pageList.push(i + 1);
          }
          for (let i = (page - 1) * 5; i < page * 5; i++) {
            this.menuList1.push(this.menuList[i]);
          }
        } else {
          for (let i = page - 2; i <= page + 2; i++) {
            this.pageBody1.pageList.push(i);
          }
          for (let i = (page - 1) * 5; i < page * 5; i++) {
            this.menuList1.push(this.menuList[i]);
          }
        }
      }
      this.pageBody1.page = page;
    }
  },
  computed: {
    totalPrice: function() {
      let totalPrice = 0;
      if (this.orderingList.length != 0) {
        for (let i = 0; i < this.orderingList.length; i++) {
          totalPrice =
            (totalPrice * 10000 +
              this.orderingList[i].count * 100 * (this.orderingList[i].menu.price * 100)) /
            10000;
        }
        return totalPrice;
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
  created() {
    this.no = this.$route.query.no;
    getOrdering(this.no);
    bus.$on(bus.isOrdered, data => {
      this.isOrdered = data;
    });
    bus.$on(bus.pageBody1, data => {
      this.pageBody1 = data;
    });
    bus.$on(bus.orderingList, data => {
      this.orderingList = data;
    });
    bus.$on(bus.menuList1, data => {
      this.menuList1 = data;
    });
    bus.$on(bus.menuList, data => {
      this.menuList = data;
    });
    bus.$on(bus.reserve, data => {
      this.ordering.reserve = data;
    });
  },
  beforeDestroy() {
    bus.$off(bus.isOrdered);
    bus.$off(bus.pageBody1);
    bus.$off(bus.orderingList);
    bus.$off(bus.menuList1);
    bus.$off(bus.menuList);
    bus.$off(bus.reserve);
  }
};
</script>

<style scoped>
.order-add-header {
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

.order-add-title {
  font-size: 20px;
  font-weight: 600;
  color: var(--farm-text-primary);
  margin: 0;
}

.order-add-content {
  padding: 24px;
}

.text-center {
  text-align: center;
}

.text-right {
  text-align: right;
}

.font-weight-bold {
  font-weight: 600;
}

.count-btn {
  display: inline-block;
  width: 24px;
  height: 24px;
  line-height: 24px;
  text-align: center;
  background: var(--farm-bg-light);
  border-radius: var(--farm-radius);
  cursor: pointer;
  margin: 0 8px;
  font-weight: bold;
}

.count-btn:hover {
  background: var(--farm-border);
}

.menu-modal {
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

.menu-modal-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
}

.menu-modal-content {
  position: relative;
  background: white;
  border-radius: var(--farm-radius-lg);
  width: 90%;
  max-width: 700px;
  max-height: 80vh;
  display: flex;
  flex-direction: column;
  box-shadow: var(--farm-shadow-xl);
}

.menu-modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid var(--farm-border);
}

.menu-modal-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
}

.menu-modal-close {
  background: none;
  border: none;
  font-size: 28px;
  line-height: 1;
  cursor: pointer;
  color: var(--farm-text-muted);
}

.menu-modal-close:hover {
  color: var(--farm-text-primary);
}

.menu-modal-body {
  padding: 20px;
  overflow-y: auto;
  flex: 1;
}

.menu-img {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: var(--farm-radius);
}

.select-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  border: 2px solid var(--farm-border);
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.3s ease;
  background: white;
  font-size: 14px;
}

.select-icon:hover {
  border-color: var(--farm-primary);
  transform: scale(1.05);
}

.select-icon.selected {
  background: var(--farm-primary);
  border-color: var(--farm-primary);
  color: white;
  transform: scale(1.1);
  box-shadow: 0 2px 8px rgba(0, 123, 255, 0.3);
}

.select-icon .iconfont {
  opacity: 0;
  transition: opacity 0.2s ease;
}

.select-icon.selected .iconfont {
  opacity: 1;
}

.menu-modal-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  border-top: 1px solid var(--farm-border);
  gap: 16px;
}

.selected-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.selected-name {
  font-weight: 500;
  color: var(--farm-text-primary);
}

.count-control {
  display: flex;
  align-items: center;
}

.count-number {
  min-width: 40px;
  text-align: center;
  font-weight: 500;
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
  margin-top: 16px;
}

.farm-pagination-item {
  padding: 6px 12px;
  border: 1px solid var(--farm-border);
  border-radius: var(--farm-radius);
  background: white;
  cursor: pointer;
  font-size: 13px;
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