<template>
  <div class="farm-page-container">
    <div class="farm-card-static raw-material-page">
      <div class="raw-material-header">
        <div class="raw-material-title-section">
          <h1 class="farm-title-main">原料管理</h1>
          <div class="farm-divider"></div>
        </div>

        <div class="raw-material-toolbar">
          <div class="raw-material-filter">
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
        </div>
      </div>

      <div class="raw-material-table-wrapper">
        <table class="farm-table">
          <thead>
            <tr>
              <th>
                {{
                  productType == "家禽"
                    ? "幼崽"
                    : productType == "鱼类"
                    ? "鱼苗"
                    : "植株/种子"
                }}
              </th>
              <th>库存</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(product, index) in productList" :key="index">
              <td>{{ product.name }}</td>
              <td>
                {{ productType == "鱼类" ? product.amount : product.baseAmount
                }}{{ product.baseUnit }}
              </td>
              <td>
                <div class="material-actions">
                  <button
                    class="farm-btn farm-btn-sm farm-btn-primary"
                    @click="openPurchaseModal(product)"
                  >
                    采购
                  </button>
                  <button
                    class="farm-btn farm-btn-sm farm-btn-outline"
                    @click="openConsumptionModal(product)"
                  >
                    消耗
                  </button>
                  <button
                    class="farm-btn farm-btn-sm farm-btn-success"
                    @click="openProduceModal(product)"
                  >
                    成熟
                  </button>
                </div>
              </td>
            </tr>
            <tr v-if="!productList || productList.length === 0">
              <td colspan="3" class="material-empty">暂无原料信息</td>
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

    <div class="farm-modal-overlay" v-if="showConsumptionModal" @click="showConsumptionModal = false">
      <div class="farm-modal" @click.stop>
        <div class="farm-modal-header">
          <h3 class="farm-modal-title">原料消耗</h3>
          <button class="farm-modal-close" @click="showConsumptionModal = false">&times;</button>
        </div>
        <div class="farm-modal-body">
          <div class="farm-form-group">
            <label class="farm-label">农产品类型</label>
            <select class="farm-select" v-model="product.productType" disabled>
              <option
                v-for="(productType, index) in productTypeList"
                :key="index"
                :value="productType"
              >
                {{ productType }}
              </option>
            </select>
          </div>

          <div class="farm-form-group">
            <label class="farm-label">农产品名</label>
            <input
              v-model="product.name"
              disabled
              type="text"
              class="farm-input"
              placeholder="农产品名"
            />
          </div>

          <div class="farm-form-group">
            <label class="farm-label">异常消耗数量</label>
            <input
              v-model="product.amount"
              type="text"
              class="farm-input"
              @keyup="amountWrite1"
              placeholder="请输入消耗数量"
            />
            <p v-if="amountMessage1" class="farm-error">{{ amountMessage1 }}</p>
          </div>

          <div class="farm-form-group">
            <label class="farm-label">单位</label>
            <input
              v-model="product.unit"
              disabled
              type="text"
              class="farm-input"
              placeholder="单位"
            />
          </div>
        </div>
        <div class="farm-modal-footer">
          <button class="farm-btn farm-btn-outline" @click="showConsumptionModal = false">取消</button>
          <button class="farm-btn farm-btn-primary" @click="doModel1">提交</button>
        </div>
      </div>
    </div>

    <div class="farm-modal-overlay" v-if="showPurchaseModal" @click="showPurchaseModal = false">
      <div class="farm-modal" @click.stop>
        <div class="farm-modal-header">
          <h3 class="farm-modal-title">原料采购</h3>
          <button class="farm-modal-close" @click="showPurchaseModal = false">&times;</button>
        </div>
        <div class="farm-modal-body">
          <div class="farm-form-group">
            <label class="farm-label">原料类型</label>
            <select class="farm-select" v-model="product.productType" disabled>
              <option
                v-for="(productType, index) in productTypeList"
                :key="index"
                :value="productType"
              >
                {{ productType }}
              </option>
            </select>
          </div>

          <div class="farm-form-group">
            <label class="farm-label">农产品名</label>
            <input
              v-model="product.name"
              disabled
              type="text"
              class="farm-input"
              placeholder="农产品名"
            />
          </div>

          <div class="farm-form-group">
            <label class="farm-label">采购数量</label>
            <input
              v-model="purchase.amount"
              type="text"
              class="farm-input"
              @keyup="amountWrite2"
              placeholder="请输入采购数量"
            />
            <p v-if="amountMessage2" class="farm-error">{{ amountMessage2 }}</p>
          </div>

          <div class="farm-form-group">
            <label class="farm-label">单位</label>
            <input
              v-model="product.unit"
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
          <button class="farm-btn farm-btn-outline" @click="showPurchaseModal = false">取消</button>
          <button class="farm-btn farm-btn-primary" @click="doModel2">提交</button>
        </div>
      </div>
    </div>

    <div class="farm-modal-overlay" v-if="showProduceModal" @click="showProduceModal = false">
      <div class="farm-modal" @click.stop>
        <div class="farm-modal-header">
          <h3 class="farm-modal-title">农产品生产</h3>
          <button class="farm-modal-close" @click="showProduceModal = false">&times;</button>
        </div>
        <div class="farm-modal-body">
          <div class="farm-form-group">
            <label class="farm-label">农产品类型</label>
            <select class="farm-select" v-model="product.productType" disabled>
              <option
                v-for="(productType, index) in productTypeList"
                :key="index"
                :value="productType"
              >
                {{ productType }}
              </option>
            </select>
          </div>

          <div class="farm-form-group">
            <label class="farm-label">农产品名</label>
            <input
              v-model="product.name"
              disabled
              type="text"
              class="farm-input"
              placeholder="农产品名"
            />
          </div>

          <div class="farm-form-group">
            <label class="farm-label">生产数量</label>
            <input
              v-model="product.amount"
              type="text"
              class="farm-input"
              @keyup="amountWrite2"
              placeholder="请输入生产数量"
            />
            <p v-if="amountMessage2" class="farm-error">{{ amountMessage2 }}</p>
          </div>

          <div class="farm-form-group">
            <label class="farm-label">单位</label>
            <input
              v-model="product.unit"
              disabled
              type="text"
              class="farm-input"
              placeholder="单位"
            />
          </div>
        </div>
        <div class="farm-modal-footer">
          <button class="farm-btn farm-btn-outline" @click="showProduceModal = false">取消</button>
          <button class="farm-btn farm-btn-primary" @click="doModel3">提交</button>
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
  abnormalConsumption1,
  addPurchase,
  produce1
} from "@/api/farmStaff.js";

export default {
  name: "RawMeterial",
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
      productType: null,
      name: null,
      amount: null,
      safeAmount: null,
      unit: null,
      baseAmount: null,
      baseUnit: null
    },
    purchase: {
      amount: null,
      price: null,
      product: null
    },
    typeMessage: null,
    nameMessage: null,
    amountMessage: null,
    amountMessage1: null,
    amountMessage2: null,
    unitMessage: null,
    nowAmount: null,
    priceMessage: null,
    showConsumptionModal: false,
    showPurchaseModal: false,
    showProduceModal: false
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
    amountWrite1() {
      this.amountMessage1 = null;
    },
    amountWrite2() {
      this.amountMessage2 = null;
    },
    priceWrite() {
      this.priceMessage = null;
    },
    openConsumptionModal(product) {
      if (this.productType == "鱼类") {
        this.nowAmount = product.amount;
      } else {
        this.nowAmount = product.baseAmount;
      }
      this.amountMessage1 = null;
      this.product = JSON.parse(JSON.stringify(product));
      this.product.amount = null;
      this.showConsumptionModal = true;
    },
    doModel1() {
      if (this.product.amount == null) {
        this.amountMessage1 = "请输入消耗数量！";
      } else {
        var re = /^(0|\+?[1-9][0-9]*)$/;
        if (this.productType == "鱼类") {
          re = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/;
        }
        if (!re.test(this.product.amount)) {
          if (this.productType == "鱼类") {
            this.amountMessage1 = "请输入正数（最高小数点后两位）！";
          } else {
            this.amountMessage1 = "请输入正整数！";
          }
        } else if (this.product.amount > this.nowAmount) {
          this.amountMessage1 = "库存不足！";
        } else {
          abnormalConsumption1(this.product);
          this.showConsumptionModal = false;
          this.productType = this.product.productType;
        }
      }
    },
    openPurchaseModal(product) {
      this.amountMessage2 = null;
      this.priceMessage = null;
      this.product = JSON.parse(JSON.stringify(product));
      this.purchase = { amount: null, price: null, product: null };
      this.showPurchaseModal = true;
    },
    doModel2() {
      if ((this.purchase.amount == null) | (this.purchase.price == null)) {
        if (this.purchase.amount == null) {
          this.amountMessage2 = "请输入采购数量！";
        }
        if (this.purchase.price == null) {
          this.priceMessage = "请输入采购单价！";
        }
      } else {
        let p = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/;
        let re = /^(0|\+?[1-9][0-9]*)$/;
        if (this.productType == "鱼类") {
          re = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/;
        }
        if (!re.test(this.purchase.amount) | !p.test(this.purchase.price)) {
          if (!re.test(this.purchase.amount)) {
            if (this.productType == "鱼类") {
              this.amountMessage2 = "请输入正数（最高小数点后两位）！";
            } else {
              this.amountMessage2 = "请输入正整数！";
            }
          }
          if (!p.test(this.purchase.price)) {
            this.priceMessage = "请输入正数（最高小数点后两位）！";
          }
        } else {
          this.purchase.product = this.product;
          addPurchase(this.purchase);
          this.showPurchaseModal = false;
          this.productType = this.product.productType;
        }
      }
    },
    openProduceModal(product) {
      this.nowAmount = product.baseAmount;
      this.amountMessage2 = null;
      this.product = JSON.parse(JSON.stringify(product));
      this.product.amount = null;
      this.showProduceModal = true;
    },
    doModel3() {
      if (this.product.amount == null) {
        this.amountMessage2 = "请输入生产数量！";
      } else {
        let re = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/;
        if (this.productType == "家禽") {
          re = /^(0|\+?[1-9][0-9]*)$/;
        }
        if (!re.test(this.product.amount)) {
          if (this.productType == "家禽") {
            this.amountMessage2 = "请输入正整数！";
          } else {
            this.amountMessage2 = "请输入正数（最高小数点后两位）！";
          }
        } else if (this.product.amount > this.nowAmount) {
          this.amountMessage2 = "库存不足！";
        } else {
          produce1(this.product);
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
.raw-material-page {
  padding: 32px;
}

.raw-material-header {
  margin-bottom: 32px;
}

.raw-material-title-section {
  text-align: center;
  margin-bottom: 32px;
}

.raw-material-toolbar {
  display: flex;
  align-items: flex-end;
  justify-content: flex-start;
  gap: 20px;
}

.raw-material-filter {
  display: flex;
  flex-direction: column;
  gap: 8px;
  min-width: 200px;
}

.raw-material-filter .farm-select {
  width: 200px;
}

.raw-material-table-wrapper {
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

.farm-error {
  color: var(--farm-danger);
  font-size: 12px;
  margin-top: 6px;
}

@media (max-width: 768px) {
  .raw-material-page {
    padding: 20px;
  }

  .raw-material-toolbar {
    flex-direction: column;
    align-items: stretch;
  }

  .raw-material-filter {
    width: 100%;
  }

  .raw-material-filter .farm-select {
    width: 100%;
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
