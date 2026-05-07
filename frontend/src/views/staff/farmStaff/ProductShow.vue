<template>
  <div class="farm-page-container">
    <div class="farm-card-static product-show">
      <div class="product-header">
        <div class="product-title-section">
          <h1 class="farm-title-main">农产品管理</h1>
          <div class="farm-divider"></div>
        </div>

        <div class="product-toolbar">
          <div class="product-filter">
            <label class="farm-label">选择类型</label>
            <select
              class="farm-select"
              v-model="productType"
              @change="changeProductType"
            >
              <option
                v-for="(productType, index) in productTypeList"
                :key="index"
                :value="productType"
              >
                {{ productType }}
              </option>
            </select>
          </div>
          <button class="farm-btn farm-btn-primary" @click="addProduct">
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
            添加农产品
          </button>
        </div>
      </div>

      <div class="product-table-wrapper">
        <table class="farm-table">
          <thead>
            <tr>
              <th>农产品名</th>
              <th>库存</th>
              <th>安全库存</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(product, index) in productList" :key="index">
              <td>{{ product.name }}</td>
              <td>{{ product.amount }}{{ product.unit }}</td>
              <td>{{ product.safeAmount }}{{ product.unit }}</td>
              <td>
                <span
                  v-if="product.amount >= product.safeAmount"
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
                <div class="product-actions">
                  <button
                    class="farm-btn farm-btn-sm farm-btn-primary"
                    @click="produce(product)"
                  >
                    生产
                  </button>
                  <button
                    class="farm-btn farm-btn-sm farm-btn-outline"
                    @click="abnormalConsumption(product)"
                  >
                    消耗
                  </button>
                  <button
                    class="farm-btn farm-btn-sm farm-btn-danger"
                    @click="showDeleteConfirm(product)"
                  >
                    删除
                  </button>
                </div>
              </td>
            </tr>
            <tr v-if="!productList || productList.length === 0">
              <td colspan="5" class="product-empty">暂无农产品信息</td>
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

    <div class="farm-modal-overlay" v-if="showProductModal" @click="closeProductModal">
      <div class="farm-modal" @click.stop>
        <div class="farm-modal-header">
          <h3 class="farm-modal-title">
            {{ modelType === "添加" ? "添加农产品" : "修改农产品" }}
          </h3>
          <button class="farm-modal-close" @click="closeProductModal">
            &times;
          </button>
        </div>
        <div class="farm-modal-body">
          <div class="farm-form-group">
            <label class="farm-label">农产品类型</label>
            <select
              class="farm-select"
              v-model="product.productType"
              :disabled="modelType == '修改'"
            >
              <option
                v-for="(productType, index) in productTypeList"
                :key="index"
                :value="productType"
              >
                {{ productType }}
              </option>
            </select>
            <p v-if="typeMessage" class="farm-error">{{ typeMessage }}</p>
          </div>

          <div class="farm-form-group">
            <label class="farm-label">农产品名称</label>
            <input
              v-model="product.name"
              :disabled="modelType == '修改'"
              type="text"
              class="farm-input"
              placeholder="请输入农产品名称"
              @keyup="nameWrite"
            />
            <p v-if="nameMessage" class="farm-error">{{ nameMessage }}</p>
          </div>

          <div class="form-row">
            <div class="farm-form-group">
              <label class="farm-label">库存</label>
              <input
                v-model="product.amount"
                :disabled="modelType == '修改'"
                type="text"
                class="farm-input"
                placeholder="库存数量"
                @keyup="amountWrite"
              />
            </div>
            <div class="farm-form-group">
              <label class="farm-label">安全库存</label>
              <input
                v-model="product.safeAmount"
                type="text"
                class="farm-input"
                placeholder="安全库存"
                @keyup="amountWrite"
              />
            </div>
          </div>
          <p v-if="amountMessage" class="farm-error">{{ amountMessage }}</p>

          <div class="form-row">
            <div class="farm-form-group">
              <label class="farm-label">单位</label>
              <input
                v-model="product.unit"
                type="text"
                class="farm-input"
                placeholder="如：只、斤、箱"
                @keyup="unitWrite"
              />
            </div>
            <div class="farm-form-group">
              <label class="farm-label">原料单位</label>
              <input
                v-model="product.baseUnit"
                type="text"
                class="farm-input"
                placeholder="原料单位"
                @keyup="unitWrite"
              />
            </div>
          </div>
          <p v-if="unitMessage" class="farm-error">{{ unitMessage }}</p>
        </div>
        <div class="farm-modal-footer">
          <button class="farm-btn farm-btn-outline" @click="closeProductModal">
            取消
          </button>
          <button class="farm-btn farm-btn-primary" @click="doModel">
            {{ modelType }}
          </button>
        </div>
      </div>
    </div>

    <div class="farm-modal-overlay" v-if="showConsumptionModal" @click="closeConsumptionModal">
      <div class="farm-modal" @click.stop>
        <div class="farm-modal-header">
          <h3 class="farm-modal-title">农产品消耗</h3>
          <button class="farm-modal-close" @click="closeConsumptionModal">
            &times;
          </button>
        </div>
        <div class="farm-modal-body">
          <div class="farm-form-group">
            <label class="farm-label">农产品类型</label>
            <input
              v-model="product.productType"
              type="text"
              class="farm-input"
              disabled
            />
          </div>

          <div class="farm-form-group">
            <label class="farm-label">农产品名称</label>
            <input
              v-model="product.name"
              type="text"
              class="farm-input"
              disabled
            />
          </div>

          <div class="farm-form-group">
            <label class="farm-label">异常消耗数量</label>
            <input
              v-model="product.amount"
              type="text"
              class="farm-input"
              placeholder="请输入消耗数量"
              @keyup="amountWrite1"
            />
            <p v-if="amountMessage1" class="farm-error">
              {{ amountMessage1 }}
            </p>
          </div>

          <div class="farm-form-group">
            <label class="farm-label">单位</label>
            <input
              v-model="product.unit"
              type="text"
              class="farm-input"
              disabled
            />
          </div>
        </div>
        <div class="farm-modal-footer">
          <button class="farm-btn farm-btn-outline" @click="closeConsumptionModal">
            取消
          </button>
          <button class="farm-btn farm-btn-primary" @click="doModel1">
            提交
          </button>
        </div>
      </div>
    </div>

    <div class="farm-modal-overlay" v-if="showProduceModal" @click="closeProduceModal">
      <div class="farm-modal" @click.stop>
        <div class="farm-modal-header">
          <h3 class="farm-modal-title">农产品生产</h3>
          <button class="farm-modal-close" @click="closeProduceModal">
            &times;
          </button>
        </div>
        <div class="farm-modal-body">
          <div class="farm-form-group">
            <label class="farm-label">农产品类型</label>
            <input
              v-model="product.productType"
              type="text"
              class="farm-input"
              disabled
            />
          </div>

          <div class="farm-form-group">
            <label class="farm-label">农产品名称</label>
            <input
              v-model="product.name"
              type="text"
              class="farm-input"
              disabled
            />
          </div>

          <div class="farm-form-group">
            <label class="farm-label">生产数量</label>
            <input
              v-model="product.amount"
              type="text"
              class="farm-input"
              placeholder="请输入生产数量"
              @keyup="amountWrite2"
            />
            <p v-if="amountMessage2" class="farm-error">
              {{ amountMessage2 }}
            </p>
          </div>

          <div class="farm-form-group">
            <label class="farm-label">单位</label>
            <input
              v-model="product.unit"
              type="text"
              class="farm-input"
              disabled
            />
          </div>
        </div>
        <div class="farm-modal-footer">
          <button class="farm-btn farm-btn-outline" @click="closeProduceModal">
            取消
          </button>
          <button class="farm-btn farm-btn-primary" @click="doModel2">
            提交
          </button>
        </div>
      </div>
    </div>

    <div class="farm-modal-overlay" v-if="showDeleteDialog" @click="cancelDelete">
      <div class="confirm-dialog" @click.stop>
        <h3 class="confirm-title">确认删除</h3>
        <p class="confirm-message">是否删除农产品：{{ deleteProductName }}？</p>
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
  initProduct,
  doPage,
  addProduct,
  deleteProduct,
  modifyProduct,
  abnormalConsumption,
  produce
} from "@/api/farmStaff.js";

export default {
  name: "ProductShow",
  data: () => ({
    productType: "家禽",
    productTypeList: ["家禽", "鱼类", "果蔬"],
    productList: [],
    pageBody1: {
      page: null,
      pages: null,
      pageList: []
    },
    modelType: null,
    product: {
      id: null,
      productType: null,
      name: null,
      amount: null,
      safeAmount: null,
      unit: null,
      baseUnit: null
    },
    typeMessage: null,
    nameMessage: null,
    amountMessage: null,
    amountMessage1: null,
    amountMessage2: null,
    unitMessage: null,
    nowAmount: null,
    showProductModal: false,
    showConsumptionModal: false,
    showProduceModal: false,
    showDeleteDialog: false,
    deleteProductName: ""
  }),
  methods: {
    changeProductType() {
      this.typeMessage = null;
      initProduct(this.productType);
    },
    doPage(page) {
      this.pageBody1.page = page;
      doPage(this.pageBody1, this.productType);
    },
    clearMessages() {
      this.typeMessage = null;
      this.nameMessage = null;
      this.amountMessage = null;
      this.amountMessage1 = null;
      this.amountMessage2 = null;
      this.unitMessage = null;
    },
    addProduct() {
      this.modelType = "添加";
      this.product = {
        id: null,
        productType: null,
        name: null,
        amount: null,
        safeAmount: null,
        unit: null,
        baseUnit: null
      };
      this.clearMessages();
      this.showProductModal = true;
    },
    closeProductModal() {
      this.showProductModal = false;
    },
    modifyProduct(product) {
      this.clearMessages();
      this.product = JSON.parse(JSON.stringify(product));
      this.modelType = "修改";
      this.showProductModal = true;
    },
    doModel() {
      if (
        (this.product.productType == null) |
        (this.product.name == null) |
        (this.product.amount == null) |
        (this.product.safeAmount == null) |
        (this.product.unit == null) |
        (this.product.baseUnit == null)
      ) {
        if (this.product.productType == null) {
          this.typeMessage = "请选择农产品类型！";
        }
        if (this.product.name == null) {
          this.nameMessage = "请输入农产品名！";
        }
        if (this.product.amount == null || this.product.safeAmount == null) {
          this.amountMessage = "请输入库存或安全库存！";
        }
        if (this.product.unit == null || this.product.baseUnit == null) {
          this.unitMessage = "请输入单位！";
        }
      } else {
        let cn = /^[\u4E00-\u9FA5]+$/;
        let re = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/;
        if (this.product.productType == "家禽") {
          re = /^(0|\+?[1-9][0-9]*)$/;
        }
        if (
          !re.test(this.product.amount) ||
          !re.test(this.product.safeAmount)
        ) {
          if (this.product.productType == "家禽") {
            this.amountMessage = "请输入正整数！";
          } else {
            this.amountMessage = "请输入正数（最高小数点后两位）！";
          }
        } else if (
          !cn.test(this.product.unit) ||
          !cn.test(this.product.baseUnit)
        ) {
          this.unitMessage = "请输入中文！";
        } else if (!cn.test(this.product.name)) {
          this.nameMessage = "请输入中文！";
        } else {
          if (this.modelType == "添加") {
            addProduct(this.product);
            this.showProductModal = false;
            this.productType = this.product.productType;
          } else if (this.modelType == "修改") {
            modifyProduct(this.product);
            this.showProductModal = false;
            this.productType = this.product.productType;
          }
        }
      }
    },
    nameWrite() {
      this.nameMessage = null;
    },
    amountWrite() {
      this.amountMessage = null;
    },
    amountWrite1() {
      this.amountMessage1 = null;
    },
    amountWrite2() {
      this.amountMessage2 = null;
    },
    unitWrite() {
      this.unitMessage = null;
    },
    showDeleteConfirm(product) {
      this.deleteProductName = product.name;
      this.product = JSON.parse(JSON.stringify(product));
      this.showDeleteDialog = true;
    },
    cancelDelete() {
      this.showDeleteDialog = false;
      this.deleteProductName = "";
    },
    executeDelete() {
      this.showDeleteDialog = false;
      deleteProduct(this.product);
      this.deleteProductName = "";
    },
    abnormalConsumption(product) {
      this.nowAmount = product.amount;
      this.clearMessages();
      this.product = JSON.parse(JSON.stringify(product));
      this.product.amount = null;
      this.showConsumptionModal = true;
    },
    closeConsumptionModal() {
      this.showConsumptionModal = false;
    },
    doModel1() {
      if (this.product.amount == null) {
        this.amountMessage1 = "请输入消耗数量！";
      } else {
        let re = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/;
        if (this.product.productType == "家禽") {
          re = /^(0|\+?[1-9][0-9]*)$/;
        }
        if (
          !re.test(this.product.amount) ||
          !re.test(this.product.safeAmount)
        ) {
          if (this.product.productType == "家禽") {
            this.amountMessage1 = "请输入正整数！";
          } else {
            this.amountMessage1 = "请输入正数（最高小数点后两位）！";
          }
        } else if (this.product.amount > this.nowAmount) {
          this.amountMessage1 = "库存不足！";
        } else {
          abnormalConsumption(this.product);
          this.showConsumptionModal = false;
          this.productType = this.product.productType;
        }
      }
    },
    produce(product) {
      this.clearMessages();
      this.product = JSON.parse(JSON.stringify(product));
      this.product.amount = null;
      this.showProduceModal = true;
    },
    closeProduceModal() {
      this.showProduceModal = false;
    },
    doModel2() {
      if (this.product.amount == null) {
        this.amountMessage2 = "请输入生产数量！";
      } else {
        let re = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/;
        if (this.product.productType == "家禽") {
          re = /^(0|\+?[1-9][0-9]*)$/;
        }
        if (!re.test(this.product.amount)) {
          if (this.product.productType == "家禽") {
            this.amountMessage2 = "请输入正整数！";
          } else {
            this.amountMessage2 = "请输入正数（最高小数点后两位）！";
          }
        } else {
          produce(this.product);
          this.showProduceModal = false;
          this.productType = this.product.productType;
        }
      }
    }
  },
  created() {
    initProduct(this.productType);
    bus.$on(bus.productList, data => {
      this.productList = data;
    });
    bus.$on(bus.pageBody1, data => {
      this.pageBody1 = data;
    });
  },
  beforeDestroy() {
    bus.$off(bus.productList);
    bus.$off(bus.pageBody1);
  }
};
</script>

<style scoped>
.product-show {
  padding: 32px;
}

.product-header {
  margin-bottom: 32px;
}

.product-title-section {
  text-align: center;
  margin-bottom: 32px;
}

.product-toolbar {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 20px;
}

.product-filter {
  display: flex;
  flex-direction: column;
  gap: 8px;
  min-width: 200px;
}

.product-filter .farm-select {
  width: 200px;
}

.product-table-wrapper {
  overflow-x: auto;
  margin-bottom: 24px;
  border-radius: var(--farm-radius);
  background: white;
}

.farm-table {
  min-width: 600px;
}

.product-actions {
  display: flex;
  gap: 8px;
  justify-content: center;
  flex-wrap: wrap;
}

.product-empty {
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
  .product-show {
    padding: 20px;
  }

  .product-toolbar {
    flex-direction: column;
    align-items: stretch;
  }

  .product-filter {
    width: 100%;
  }

  .product-filter .farm-select {
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
