<template>
  <div class="farm-page-container">
    <div class="farm-card-static sale-page">
      <div class="sale-header">
        <div class="sale-title-section">
          <h1 class="farm-title-main">产品销售</h1>
          <div class="farm-divider"></div>
        </div>

        <div class="sale-toolbar">
          <button class="farm-btn farm-btn-primary" @click="openProductModal">
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
            添加产品
          </button>
        </div>
      </div>

      <div class="sale-table-wrapper">
        <table class="farm-table">
          <thead>
            <tr>
              <th>产品名</th>
              <th>数量</th>
              <th>单价</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(sale, index) in saleList" :key="index">
              <td>{{ sale.product.name }}</td>
              <td>{{ sale.amount + sale.product.unit }}</td>
              <td>{{ sale.price + "元" }}</td>
              <td>
                <div class="sale-actions">
                  <button
                    class="farm-btn farm-btn-sm farm-btn-danger"
                    @click="deleteSale(index)"
                  >
                    删除
                  </button>
                </div>
              </td>
            </tr>
            <tr v-if="saleList.length > 0" class="sale-total-row">
              <td></td>
              <td class="sale-total-label">合计</td>
              <td class="sale-total-value">{{ totalPrice + "元" }}</td>
              <td></td>
            </tr>
            <tr v-if="!saleList || saleList.length === 0">
              <td colspan="4" class="sale-empty">暂无销售产品，请添加</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="sale-submit" v-if="saleList.length > 0">
        <button
          class="farm-btn farm-btn-success"
          :disabled="saleList.length == 0"
          @click="showSubmitConfirm"
        >
          提交销售
        </button>
      </div>
    </div>

    <div
      class="farm-modal-overlay"
      v-if="showProductModal"
      @click="closeProductModal"
    >
      <div class="farm-modal farm-modal-lg" @click.stop>
        <div class="farm-modal-header">
          <h3 class="farm-modal-title">产品列表</h3>
          <button class="farm-modal-close" @click="closeProductModal">
            &times;
          </button>
        </div>
        <div class="farm-modal-body">
          <div class="farm-form-group">
            <label class="farm-label">产品类型</label>
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

          <div class="sale-product-table-wrapper">
            <table class="farm-table">
              <thead>
                <tr>
                  <th>选项</th>
                  <th>产品名</th>
                  <th>库存</th>
                  <th>安全库存</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(product, index) in productList" :key="index">
                  <td>
                    <button
                      class="farm-btn farm-btn-sm farm-btn-outline"
                      @click="choosedProduct(product)"
                    >
                      选择
                    </button>
                  </td>
                  <td>{{ product.name }}</td>
                  <td>{{ product.amount + product.unit }}</td>
                  <td>{{ product.safeAmount + product.unit }}</td>
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
            <button
              class="farm-pagination-item"
              @click="doPage(pageBody1.pages)"
            >
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
        <div class="farm-modal-footer">
          <div class="sale-form-area">
            <div class="farm-form-group">
              <label class="farm-label">产品名</label>
              <input
                v-model="product.name"
                disabled
                type="text"
                class="farm-input"
                placeholder="请从上方选择产品"
              />
            </div>

            <div class="form-row">
              <div class="farm-form-group">
                <label class="farm-label">数量</label>
                <input
                  v-model="sale.amount"
                  type="text"
                  class="farm-input"
                  placeholder="请输入数量"
                  @keyup="writeAmount"
                />
                <p v-if="amountMessage" class="farm-error">
                  {{ amountMessage }}
                </p>
              </div>
              <div class="farm-form-group">
                <label class="farm-label">单价（元）</label>
                <input
                  v-model="sale.price"
                  type="text"
                  class="farm-input"
                  placeholder="请输入单价"
                  @keyup="writePrice"
                />
                <p v-if="priceMessage" class="farm-error">{{ priceMessage }}</p>
              </div>
            </div>

            <div class="sale-form-submit">
              <button class="farm-btn farm-btn-primary" @click="addSale">
                确认添加
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div
      class="farm-modal-overlay"
      v-if="showSubmitDialog"
      @click="cancelSubmit"
    >
      <div class="confirm-dialog" @click.stop>
        <h3 class="confirm-title">确认提交</h3>
        <p class="confirm-message">
          是否提交当前销售清单？共 {{ saleList.length }} 项产品，合计
          {{ totalPrice }}元。
        </p>
        <div class="confirm-buttons">
          <button class="farm-btn farm-btn-outline" @click="cancelSubmit">
            取消
          </button>
          <button class="farm-btn farm-btn-success" @click="executeSubmit">
            提交
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import bus from "@/util/Bus";
import { initProduct, doPage, addSaleList } from "@/api/farmStaff.js";

export default {
  name: "Sale",
  data: () => ({
    productType: "家禽",
    productTypeList: ["家禽", "鱼类", "果蔬"],
    saleList: [],
    productList: [],
    product: {
      id: null,
      name: null,
      amount: null,
      safeAmount: null,
      unit: null,
      productType: null,
      baseAmount: null,
      baseUnit: null
    },
    sale: {
      id: null,
      amount: null,
      price: null,
      saleNo: null,
      product: null
    },
    pageBody1: {
      page: null,
      pages: null,
      pageList: []
    },
    nowAmount: null,
    amountMessage: null,
    priceMessage: null,
    showProductModal: false,
    showSubmitDialog: false
  }),
  methods: {
    writePrice() {
      this.priceMessage = null;
    },
    writeAmount() {
      this.amountMessage = null;
    },
    changeProductType() {
      initProduct(this.productType);
    },
    doPage(page) {
      this.pageBody1.page = page;
      doPage(this.pageBody1, this.productType);
    },
    openProductModal() {
      this.product.id = null;
      this.product.name = null;
      this.product.amount = null;
      this.product.safeAmount = null;
      this.product.unit = null;
      this.product.productType = null;
      this.product.baseAmount = null;
      this.product.baseUnit = null;
      this.sale.amount = null;
      this.sale.price = null;
      this.amountMessage = null;
      this.priceMessage = null;
      initProduct(this.productType);
      this.showProductModal = true;
    },
    closeProductModal() {
      this.showProductModal = false;
    },
    choosedProduct(product) {
      this.nowAmount = product.amount;
      this.product = JSON.parse(JSON.stringify(product));
    },
    addSale() {
      let result = true;
      if (this.sale.amount == null || this.sale.price == null) {
        if (this.sale.amount == null) {
          this.amountMessage = "请输入数量！";
          result = false;
        }
        if (this.sale.price == null) {
          this.priceMessage = "请输入单价！";
          result = false;
        }
      }
      if (result == true) {
        let re = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/;
        if (!re.test(this.sale.price)) {
          this.priceMessage = "请输入正数（最高小数点后两位）！";
          result = false;
        }
        if (this.productType == "家禽") {
          re = /^(0|\+?[1-9][0-9]*)$/;
        }
        if (!re.test(this.sale.amount)) {
          if (this.productType == "家禽") {
            this.amountMessage = "请输入正整数！";
            result = false;
          } else {
            this.amountMessage = "请输入正数（最高小数点后两位）！";
            result = false;
          }
        }
      }
      if (this.sale.amount > this.nowAmount) {
        this.amountMessage = "库存不足！";
        result = false;
      }
      if (result == true) {
        if (this.product.name == null) {
          bus.$emit("toast", { message: "请选择产品！", type: "warning" });
        } else {
          let duplicate = false;
          for (let i = 0; i < this.saleList.length; i++) {
            if (this.product.name == this.saleList[i].product.name) {
              duplicate = true;
            }
          }
          if (duplicate) {
            bus.$emit("toast", {
              message: "已添加这个产品！",
              type: "warning"
            });
          } else {
            this.sale.product = this.product;
            this.saleList.push(JSON.parse(JSON.stringify(this.sale)));
            this.showProductModal = false;
          }
        }
      }
    },
    deleteSale(index) {
      this.saleList.splice(index, 1);
    },
    showSubmitConfirm() {
      this.showSubmitDialog = true;
    },
    cancelSubmit() {
      this.showSubmitDialog = false;
    },
    executeSubmit() {
      this.showSubmitDialog = false;
      addSaleList(this.saleList);
      this.saleList = [];
    },
    accMul(arg1, arg2) {
      var m = 0,
        s1 = arg1.toString(),
        s2 = arg2.toString();
      try {
        m += s1.split(".")[1].length;
      } catch (e) {
        m += 0;
      }
      try {
        m += s2.split(".")[1].length;
      } catch (e) {
        m += 0;
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
  computed: {
    totalPrice: function() {
      let totalPrice = 0;
      if (this.saleList.length != 0) {
        for (let i = 0; i < this.saleList.length; i++) {
          let tp = this.accMul(this.saleList[i].amount, this.saleList[i].price);
          totalPrice = this.accAdd(totalPrice, tp);
        }
        return totalPrice.toFixed(2);
      } else {
        return null;
      }
    }
  },
  created() {
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
.sale-page {
  padding: 32px;
}

.sale-header {
  margin-bottom: 32px;
}

.sale-title-section {
  text-align: center;
  margin-bottom: 32px;
}

.sale-toolbar {
  display: flex;
  justify-content: flex-end;
}

.sale-table-wrapper {
  overflow-x: auto;
  margin-bottom: 24px;
  border-radius: var(--farm-radius);
  background: white;
}

.farm-table {
  min-width: 500px;
}

.sale-actions {
  display: flex;
  gap: 8px;
  justify-content: center;
}

.sale-total-row {
  background: var(--farm-bg-light);
}

.sale-total-label {
  font-weight: 600;
  text-align: center;
  color: var(--farm-text-primary);
}

.sale-total-value {
  font-weight: 600;
  text-align: center;
  color: var(--farm-primary);
  font-size: 16px;
}

.sale-empty {
  text-align: center;
  padding: 32px 0;
  color: var(--farm-text-muted);
  font-size: 14px;
}

.sale-submit {
  display: flex;
  justify-content: center;
  margin-top: 8px;
}

.sale-product-table-wrapper {
  overflow-x: auto;
  margin-bottom: 16px;
  border-radius: var(--farm-radius);
  background: white;
}

.farm-modal-lg {
  max-width: 800px;
  display: flex;
  flex-direction: column;
}

.farm-modal-lg .farm-modal-body {
  flex: 1;
  overflow-y: auto;
}

.farm-modal-lg .farm-modal-footer {
  flex-shrink: 0;
  border-top: 1px solid var(--farm-border-color);
}

.sale-form-area {
  width: 100%;
}

.sale-form-submit {
  display: flex;
  justify-content: center;
  margin-top: 16px;
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
  .sale-page {
    padding: 20px;
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
