<template>
  <div class="farm-page-container">
    <div class="farm-card-static restaurant-material-page">
      <div class="restaurant-material-header">
        <div class="restaurant-material-title-section">
          <h1 class="farm-title-main">餐厅物资管理</h1>
          <div class="farm-divider"></div>
        </div>

        <div class="restaurant-material-toolbar">
          <div class="restaurant-material-filter">
            <label class="farm-label">餐厅物资</label>
          </div>
          <button class="farm-btn farm-btn-primary" @click="openAddModal">
            <svg
              width="18"
              height="18"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
            >
              <line x1="12" y1="5" x2="12" y2="19"></line>
              <line x1="5" y1="12" x2="19" y2="12"></line>
            </svg>
            新建采购信息
          </button>
        </div>
      </div>

      <div class="restaurant-material-table-wrapper">
        <table class="farm-table">
          <thead>
            <tr>
              <th>物资名</th>
              <th>库存</th>
              <th>安全库存</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(restaurantMaterial, index) in restaurantMaterialList"
              :key="index"
            >
              <td>{{ restaurantMaterial.name }}</td>
              <td>{{ restaurantMaterial.amount + restaurantMaterial.unit }}</td>
              <td>
                {{ restaurantMaterial.safeAmount + restaurantMaterial.unit }}
              </td>
              <td>
                <span
                  v-if="
                    restaurantMaterial.amount >= restaurantMaterial.safeAmount
                  "
                  class="farm-badge farm-badge-success"
                >
                  <span class="farm-status-dot farm-status-dot-success"></span>
                  正常
                </span>
                <span v-else class="farm-badge farm-badge-danger">
                  <span class="farm-status-dot farm-status-dot-danger"></span>
                  缺货
                </span>
              </td>
              <td>
                <div class="material-actions">
                  <button
                    class="farm-btn farm-btn-sm farm-btn-primary"
                    @click="openPurchaseModal(restaurantMaterial)"
                  >
                    采购
                  </button>
                  <button
                    class="farm-btn farm-btn-sm farm-btn-outline"
                    @click="openConsumptionModal(restaurantMaterial)"
                  >
                    消耗
                  </button>
                  <button
                    class="farm-btn farm-btn-sm farm-btn-secondary"
                    @click="openUseModal(restaurantMaterial)"
                  >
                    使用
                  </button>
                  <button
                    class="farm-btn farm-btn-sm farm-btn-danger"
                    @click="showDeleteConfirm(restaurantMaterial)"
                  >
                    删除
                  </button>
                </div>
              </td>
            </tr>
            <tr
              v-if="!restaurantMaterialList || restaurantMaterialList.length === 0"
            >
              <td colspan="5" class="material-empty">暂无餐厅物资信息</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="farm-pagination" v-if="pageBody1.pages > 1">
        <button class="farm-pagination-item" @click="doPage2(1)">
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
          @click="doPage2(page)"
        >
          {{ page }}
        </button>
        <button class="farm-pagination-item" @click="doPage2(pageBody1.pages)">
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
      v-if="showAddModal"
      @click="showAddModal = false"
    >
      <div class="farm-modal" @click.stop>
        <div class="farm-modal-header">
          <h3 class="farm-modal-title">新建采购信息</h3>
          <button class="farm-modal-close" @click="showAddModal = false">
            &times;
          </button>
        </div>
        <div class="farm-modal-body">
          <div class="farm-form-group">
            <label class="farm-label">物资名</label>
            <input
              type="text"
              class="farm-input"
              v-model="restaurantMaterial.name"
              @keyup="nameWrite"
              placeholder="请输入物资名"
            />
            <p v-if="nameMessage" class="farm-error">{{ nameMessage }}</p>
          </div>

          <div class="form-row">
            <div class="farm-form-group">
              <label class="farm-label">库存</label>
              <input
                v-model="restaurantMaterial.amount"
                type="text"
                class="farm-input"
                @keyup="amountWrite"
                placeholder="请输入库存"
              />
              <p v-if="amountMessage" class="farm-error">
                {{ amountMessage }}
              </p>
            </div>
            <div class="farm-form-group">
              <label class="farm-label">安全库存</label>
              <input
                v-model="restaurantMaterial.safeAmount"
                type="text"
                class="farm-input"
                @keyup="safeAmountWrite"
                placeholder="请输入安全库存"
              />
              <p v-if="safeAmountMessage" class="farm-error">
                {{ safeAmountMessage }}
              </p>
            </div>
          </div>

          <div class="farm-form-group">
            <label class="farm-label">单位</label>
            <input
              v-model="restaurantMaterial.unit"
              type="text"
              class="farm-input"
              @keyup="unitWrite"
              placeholder="请输入单位"
            />
            <p v-if="unitMessage" class="farm-error">{{ unitMessage }}</p>
          </div>
        </div>
        <div class="farm-modal-footer">
          <button
            class="farm-btn farm-btn-outline"
            @click="showAddModal = false"
          >
            取消
          </button>
          <button class="farm-btn farm-btn-primary" @click="addRestaurantMaterialModel">
            提交
          </button>
        </div>
      </div>
    </div>

    <div
      class="farm-modal-overlay"
      v-if="showPurchaseModal"
      @click="showPurchaseModal = false"
    >
      <div class="farm-modal" @click.stop>
        <div class="farm-modal-header">
          <h3 class="farm-modal-title">物资采购</h3>
          <button class="farm-modal-close" @click="showPurchaseModal = false">
            &times;
          </button>
        </div>
        <div class="farm-modal-body">
          <div class="farm-form-group">
            <label class="farm-label">物资名</label>
            <input
              v-model="restaurantMaterial.name"
              disabled
              type="text"
              class="farm-input"
              placeholder="物资名"
            />
          </div>

          <div class="farm-form-group">
            <label class="farm-label">采购数量</label>
            <input
              v-model="purchase.amount"
              type="text"
              class="farm-input"
              @keyup="amountWrite"
              placeholder="请输入采购数量"
            />
            <p v-if="amountMessage" class="farm-error">{{ amountMessage }}</p>
          </div>

          <div class="farm-form-group">
            <label class="farm-label">单位</label>
            <input
              v-model="restaurantMaterial.unit"
              disabled
              type="text"
              class="farm-input"
              placeholder="单位"
            />
          </div>

          <div class="farm-form-group">
            <label class="farm-label">单价</label>
            <input
              v-model="purchase.price"
              type="text"
              class="farm-input"
              @keyup="priceWrite"
              placeholder="请输入单价"
            />
            <p v-if="priceMessage" class="farm-error">{{ priceMessage }}</p>
          </div>
        </div>
        <div class="farm-modal-footer">
          <button
            class="farm-btn farm-btn-outline"
            @click="showPurchaseModal = false"
          >
            取消
          </button>
          <button
            class="farm-btn farm-btn-primary"
            @click="addRestaurantMaterialPurchaseModel"
          >
            提交
          </button>
        </div>
      </div>
    </div>

    <div
      class="farm-modal-overlay"
      v-if="showConsumptionModal"
      @click="showConsumptionModal = false"
    >
      <div class="farm-modal" @click.stop>
        <div class="farm-modal-header">
          <h3 class="farm-modal-title">物资消耗</h3>
          <button
            class="farm-modal-close"
            @click="showConsumptionModal = false"
          >
            &times;
          </button>
        </div>
        <div class="farm-modal-body">
          <div class="farm-form-group">
            <label class="farm-label">物资名</label>
            <input
              v-model="restaurantMaterial.name"
              disabled
              type="text"
              class="farm-input"
              placeholder="物资名"
            />
          </div>

          <div class="farm-form-group">
            <label class="farm-label">异常消耗数量</label>
            <input
              v-model="restaurantMaterial.amount"
              type="text"
              class="farm-input"
              @keyup="amountWrite"
              placeholder="请输入消耗数量"
            />
            <p v-if="amountMessage" class="farm-error">{{ amountMessage }}</p>
          </div>

          <div class="farm-form-group">
            <label class="farm-label">单位</label>
            <input
              v-model="restaurantMaterial.unit"
              disabled
              type="text"
              class="farm-input"
              placeholder="单位"
            />
          </div>
        </div>
        <div class="farm-modal-footer">
          <button
            class="farm-btn farm-btn-outline"
            @click="showConsumptionModal = false"
          >
            取消
          </button>
          <button
            class="farm-btn farm-btn-primary"
            @click="consumptionRestaurantMaterialModel"
          >
            提交
          </button>
        </div>
      </div>
    </div>

    <div
      class="farm-modal-overlay"
      v-if="showUseModal"
      @click="showUseModal = false"
    >
      <div class="farm-modal" @click.stop>
        <div class="farm-modal-header">
          <h3 class="farm-modal-title">物资使用</h3>
          <button class="farm-modal-close" @click="showUseModal = false">
            &times;
          </button>
        </div>
        <div class="farm-modal-body">
          <div class="farm-form-group">
            <label class="farm-label">物资名</label>
            <input
              v-model="restaurantMaterial.name"
              disabled
              type="text"
              class="farm-input"
              placeholder="物资名"
            />
          </div>

          <div class="farm-form-group">
            <label class="farm-label">使用数量</label>
            <input
              v-model="restaurantMaterial.amount"
              type="text"
              class="farm-input"
              @keyup="amountWrite"
              placeholder="请输入使用数量"
            />
            <p v-if="amountMessage" class="farm-error">{{ amountMessage }}</p>
          </div>

          <div class="farm-form-group">
            <label class="farm-label">单位</label>
            <input
              v-model="restaurantMaterial.unit"
              disabled
              type="text"
              class="farm-input"
              placeholder="单位"
            />
          </div>
        </div>
        <div class="farm-modal-footer">
          <button
            class="farm-btn farm-btn-outline"
            @click="showUseModal = false"
          >
            取消
          </button>
          <button
            class="farm-btn farm-btn-primary"
            @click="useRestaurantMaterialModel"
          >
            提交
          </button>
        </div>
      </div>
    </div>

    <div
      class="farm-modal-overlay"
      v-if="showDeleteDialog"
      @click="cancelDelete"
    >
      <div class="confirm-dialog" @click.stop>
        <h3 class="confirm-title">确认删除</h3>
        <p class="confirm-message">
          是否删除物资：{{ deleteMaterialName }}？
        </p>
        <div class="confirm-buttons">
          <button class="farm-btn farm-btn-outline" @click="cancelDelete">
            取消
          </button>
          <button class="farm-btn farm-btn-danger" @click="executeDelete">
            删除
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import bus from "@/util/Bus";
import {
  addRestaurantMaterial,
  initRestaurantMaterial,
  doPage2,
  addRestaurantMaterialPurchase,
  deleteRestaurantMaterial,
  consumptionRestaurantMaterial,
  useRestaurantMaterial
} from "@/api/restaurantStaff.js";

export default {
  name: "RestaurantMaterial",
  data: () => ({
    restaurantMaterial: {
      id: null,
      name: null,
      amount: null,
      safeAmount: null,
      unit: null
    },
    restaurantMaterialList: [],
    pageBody1: {
      page: null,
      pages: null,
      pageList: []
    },
    purchase: {
      amount: null,
      price: null,
      restaurantMaterial: null
    },
    nowAmount: null,
    nameMessage: null,
    amountMessage: null,
    safeAmountMessage: null,
    unitMessage: null,
    priceMessage: null,
    showAddModal: false,
    showPurchaseModal: false,
    showConsumptionModal: false,
    showUseModal: false,
    showDeleteDialog: false,
    deleteMaterialName: ""
  }),
  methods: {
    nameWrite() {
      this.nameMessage = null;
    },
    amountWrite() {
      this.amountMessage = null;
    },
    safeAmountWrite() {
      this.safeAmountMessage = null;
    },
    unitWrite() {
      this.unitMessage = null;
    },
    priceWrite() {
      this.priceMessage = null;
    },
    doPage2(page) {
      this.pageBody1.page = page;
      doPage2(this.pageBody1);
    },
    openAddModal() {
      this.restaurantMaterial = {
        id: null,
        name: null,
        amount: null,
        safeAmount: null,
        unit: null
      };
      this.nameMessage = null;
      this.amountMessage = null;
      this.safeAmountMessage = null;
      this.unitMessage = null;
      this.showAddModal = true;
    },
    addRestaurantMaterialModel() {
      if (
        this.restaurantMaterial.name == null ||
        this.restaurantMaterial.amount == null ||
        this.restaurantMaterial.safeAmount == null ||
        this.restaurantMaterial.unit == null
      ) {
        if (this.restaurantMaterial.name == null) {
          this.nameMessage = "请输入名称！";
        }
        if (this.restaurantMaterial.amount == null) {
          this.amountMessage = "请输入库存！";
        }
        if (this.restaurantMaterial.safeAmount == null) {
          this.safeAmountMessage = "请输入安全库存！";
        }
        if (this.restaurantMaterial.unit == null) {
          this.unitMessage = "请输入单位！";
        }
      } else {
        let cn = /^[\u4E00-\u9FA5]+$/;
        let re = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/;
        if (
          !cn.test(this.restaurantMaterial.name) ||
          !cn.test(this.restaurantMaterial.unit) ||
          !re.test(this.restaurantMaterial.amount) ||
          !re.test(this.restaurantMaterial.safeAmount)
        ) {
          if (!cn.test(this.restaurantMaterial.name)) {
            this.nameMessage = "请输入中文！";
          }
          if (!cn.test(this.restaurantMaterial.unit)) {
            this.unitMessage = "请输入中文！";
          }
          if (!re.test(this.restaurantMaterial.amount)) {
            this.amountMessage = "请输入正数（最高小数点后两位）！";
          }
          if (!re.test(this.restaurantMaterial.safeAmount)) {
            this.safeAmountMessage = "请输入正数（最高小数点后两位）！";
          }
        } else {
          addRestaurantMaterial(this.restaurantMaterial);
          this.showAddModal = false;
        }
      }
    },
    openPurchaseModal(restaurantMaterial) {
      this.amountMessage = null;
      this.priceMessage = null;
      this.restaurantMaterial = JSON.parse(JSON.stringify(restaurantMaterial));
      this.purchase = { amount: null, price: null, restaurantMaterial: null };
      this.showPurchaseModal = true;
    },
    addRestaurantMaterialPurchaseModel() {
      if ((this.purchase.amount == null) | (this.purchase.price == null)) {
        if (this.purchase.amount == null) {
          this.amountMessage = "请输入采购数量！";
        }
        if (this.purchase.price == null) {
          this.priceMessage = "请输入采购单价！";
        }
      } else {
        let p = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/;
        if (!p.test(this.purchase.amount) | !p.test(this.purchase.price)) {
          if (!p.test(this.purchase.amount)) {
            this.amountMessage = "请输入正数（最高小数点后两位）！";
          }
          if (!p.test(this.purchase.price)) {
            this.priceMessage = "请输入正数（最高小数点后两位）！";
          }
        } else {
          this.purchase.restaurantMaterial = this.restaurantMaterial;
          addRestaurantMaterialPurchase(this.purchase);
          this.showPurchaseModal = false;
        }
      }
    },
    showDeleteConfirm(restaurantMaterial) {
      this.deleteMaterialName = restaurantMaterial.name;
      this.restaurantMaterial = JSON.parse(JSON.stringify(restaurantMaterial));
      this.showDeleteDialog = true;
    },
    cancelDelete() {
      this.showDeleteDialog = false;
      this.deleteMaterialName = "";
    },
    executeDelete() {
      this.showDeleteDialog = false;
      deleteRestaurantMaterial(this.restaurantMaterial);
      this.deleteMaterialName = "";
    },
    openConsumptionModal(restaurantMaterial) {
      this.nowAmount = restaurantMaterial.amount;
      this.amountMessage = null;
      this.restaurantMaterial = JSON.parse(JSON.stringify(restaurantMaterial));
      this.restaurantMaterial.amount = null;
      this.showConsumptionModal = true;
    },
    consumptionRestaurantMaterialModel() {
      if (this.restaurantMaterial.amount == null) {
        this.amountMessage = "请输入消耗数量！";
      } else {
        let p = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/;
        if (!p.test(this.restaurantMaterial.amount)) {
          this.amountMessage = "请输入正数（最高小数点后两位）！";
        } else if (this.restaurantMaterial.amount > this.nowAmount) {
          this.amountMessage = "库存不足";
        } else {
          consumptionRestaurantMaterial(this.restaurantMaterial);
          this.showConsumptionModal = false;
        }
      }
    },
    openUseModal(restaurantMaterial) {
      this.nowAmount = restaurantMaterial.amount;
      this.amountMessage = null;
      this.restaurantMaterial = JSON.parse(JSON.stringify(restaurantMaterial));
      this.restaurantMaterial.amount = null;
      this.showUseModal = true;
    },
    useRestaurantMaterialModel() {
      if (this.restaurantMaterial.amount == null) {
        this.amountMessage = "请输入消耗数量！";
      } else {
        let p = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/;
        if (!p.test(this.restaurantMaterial.amount)) {
          this.amountMessage = "请输入正数（最高小数点后两位）！";
        } else if (this.restaurantMaterial.amount > this.nowAmount) {
          this.amountMessage = "库存不足";
        } else {
          useRestaurantMaterial(this.restaurantMaterial);
          this.showUseModal = false;
        }
      }
    }
  },
  created() {
    initRestaurantMaterial();
    bus.$on(bus.restaurantMaterialList, data => {
      this.restaurantMaterialList = data;
    });
    bus.$on(bus.pageBody1, data => {
      this.pageBody1 = data;
    });
  },
  beforeDestroy() {
    bus.$off(bus.restaurantMaterialList);
    bus.$off(bus.pageBody1);
  }
};
</script>

<style scoped>
.restaurant-material-page {
  padding: 32px;
}

.restaurant-material-header {
  margin-bottom: 32px;
}

.restaurant-material-title-section {
  text-align: center;
  margin-bottom: 32px;
}

.restaurant-material-toolbar {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 20px;
}

.restaurant-material-filter {
  display: flex;
  flex-direction: column;
  gap: 8px;
  min-width: 200px;
}

.restaurant-material-table-wrapper {
  overflow-x: auto;
  margin-bottom: 24px;
  border-radius: var(--farm-radius);
  background: white;
}

.farm-table {
  min-width: 600px;
}

.material-actions {
  display: flex;
  gap: 8px;
  justify-content: center;
  flex-wrap: wrap;
}

.material-empty {
  text-align: center;
  padding: 32px 0;
  color: var(--farm-text-muted);
  font-size: 14px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.farm-error {
  color: var(--farm-danger);
  font-size: 12px;
  margin-top: 6px;
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
  .restaurant-material-page {
    padding: 20px;
  }

  .restaurant-material-toolbar {
    flex-direction: column;
    align-items: stretch;
  }

  .restaurant-material-filter {
    width: 100%;
  }

  .form-row {
    grid-template-columns: 1fr;
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
