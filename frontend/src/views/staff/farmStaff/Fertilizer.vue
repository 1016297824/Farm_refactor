<template>
  <div class="farm-page-container">
    <div class="farm-card-static fertilizer-page">
      <div class="fertilizer-header">
        <div class="fertilizer-title-section">
          <h1 class="farm-title-main">{{ fertilizerType }}管理</h1>
          <div class="farm-divider"></div>
        </div>

        <div class="fertilizer-toolbar">
          <div class="fertilizer-filter">
            <label class="farm-label">选择类型</label>
            <select
              class="farm-select"
              v-model="fertilizerType"
              @change="fertilizerTypeChange"
            >
              <option
                v-for="(fertilizerType, index) in fertilizerTypeList"
                :key="index"
                :value="fertilizerType"
              >
                {{ fertilizerType }}
              </option>
            </select>
          </div>
          <button class="farm-btn farm-btn-primary" @click="addFertilizer">
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

      <div class="fertilizer-table-wrapper">
        <table class="farm-table">
          <thead>
            <tr>
              <th>{{ fertilizerType + "名称" }}</th>
              <th>库存</th>
              <th>安全库存</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(fertilizer, index) in fertilizerList" :key="index">
              <td>{{ fertilizer.name }}</td>
              <td>{{ fertilizer.amount + fertilizer.unit }}</td>
              <td>{{ fertilizer.safeAmount + fertilizer.unit }}</td>
              <td>
                <span
                  v-if="fertilizer.amount >= fertilizer.safeAmount"
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
                <div class="fertilizer-actions">
                  <button
                    class="farm-btn farm-btn-sm farm-btn-primary"
                    @click="addPurchase(fertilizer)"
                  >
                    采购
                  </button>
                  <button
                    class="farm-btn farm-btn-sm farm-btn-outline"
                    @click="abnormalConsumptionFertilizer(fertilizer)"
                  >
                    消耗
                  </button>
                  <button
                    class="farm-btn farm-btn-sm farm-btn-secondary"
                    @click="farmUse(fertilizer)"
                  >
                    使用
                  </button>
                  <button
                    class="farm-btn farm-btn-sm farm-btn-danger"
                    @click="showDeleteConfirm(fertilizer)"
                  >
                    删除
                  </button>
                </div>
              </td>
            </tr>
            <tr v-if="!fertilizerList || fertilizerList.length === 0">
              <td colspan="5" class="fertilizer-empty">暂无{{ fertilizerType }}信息</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="farm-pagination" v-if="pageBody1.pages > 1">
        <button class="farm-pagination-item" @click="doPage1(1)">
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
          @click="doPage1(page)"
        >
          {{ page }}
        </button>
        <button class="farm-pagination-item" @click="doPage1(pageBody1.pages)">
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

    <div class="farm-modal-overlay" v-if="showPurchaseModal" @click="showPurchaseModal = false">
      <div class="farm-modal" @click.stop>
        <div class="farm-modal-header">
          <h3 class="farm-modal-title">{{ fertilizerType + "采购" }}</h3>
          <button class="farm-modal-close" @click="showPurchaseModal = false">&times;</button>
        </div>
        <div class="farm-modal-body">
          <div class="farm-form-group">
            <label class="farm-label">类型</label>
            <select
              class="farm-select"
              v-model="fertilizer.fertilizerType"
              disabled
            >
              <option
                v-for="(fertilizerType, index) in fertilizerTypeList"
                :key="index"
                :value="fertilizerType"
              >
                {{ fertilizerType }}
              </option>
            </select>
          </div>

          <div class="farm-form-group">
            <label class="farm-label">名称</label>
            <input
              v-model="fertilizer.name"
              disabled
              type="text"
              class="farm-input"
              placeholder="名称"
            />
          </div>

          <div class="farm-form-group">
            <label class="farm-label">采购数量</label>
            <input
              v-model="purchase.amount"
              type="text"
              class="farm-input"
              placeholder="请输入采购数量"
              @keyup="amountWrite"
            />
            <p v-if="amountMessage" class="farm-error">{{ amountMessage }}</p>
          </div>

          <div class="farm-form-group">
            <label class="farm-label">单位</label>
            <input
              v-model="fertilizer.unit"
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
              placeholder="请输入单价"
              @keyup="priceWrite"
            />
            <p v-if="priceMessage" class="farm-error">{{ priceMessage }}</p>
          </div>
        </div>
        <div class="farm-modal-footer">
          <button class="farm-btn farm-btn-outline" @click="showPurchaseModal = false">取消</button>
          <button class="farm-btn farm-btn-primary" @click="purchaseModel">提交</button>
        </div>
      </div>
    </div>

    <div class="farm-modal-overlay" v-if="showConsumptionModal" @click="showConsumptionModal = false">
      <div class="farm-modal" @click.stop>
        <div class="farm-modal-header">
          <h3 class="farm-modal-title">{{ fertilizerType + "消耗" }}</h3>
          <button class="farm-modal-close" @click="showConsumptionModal = false">&times;</button>
        </div>
        <div class="farm-modal-body">
          <div class="farm-form-group">
            <label class="farm-label">类型</label>
            <select
              class="farm-select"
              v-model="fertilizer.fertilizerType"
              disabled
            >
              <option
                v-for="(fertilizerType, index) in fertilizerTypeList"
                :key="index"
                :value="fertilizerType"
              >
                {{ fertilizerType }}
              </option>
            </select>
          </div>

          <div class="farm-form-group">
            <label class="farm-label">{{ fertilizerType + "名" }}</label>
            <input
              v-model="fertilizer.name"
              disabled
              type="text"
              class="farm-input"
              placeholder="名称"
            />
          </div>

          <div class="farm-form-group">
            <label class="farm-label">异常消耗数量</label>
            <input
              v-model="fertilizer.amount"
              type="text"
              class="farm-input"
              placeholder="请输入消耗数量"
              @keyup="amountWrite"
            />
            <p v-if="amountMessage" class="farm-error">{{ amountMessage }}</p>
          </div>

          <div class="farm-form-group">
            <label class="farm-label">单位</label>
            <input
              v-model="fertilizer.unit"
              disabled
              type="text"
              class="farm-input"
              placeholder="单位"
            />
          </div>
        </div>
        <div class="farm-modal-footer">
          <button class="farm-btn farm-btn-outline" @click="showConsumptionModal = false">取消</button>
          <button class="farm-btn farm-btn-primary" @click="consumptionModel">提交</button>
        </div>
      </div>
    </div>

    <div class="farm-modal-overlay" v-if="showFarmUseModal" @click="showFarmUseModal = false">
      <div class="farm-modal" @click.stop>
        <div class="farm-modal-header">
          <h3 class="farm-modal-title">{{ fertilizerType + "使用" }}</h3>
          <button class="farm-modal-close" @click="showFarmUseModal = false">&times;</button>
        </div>
        <div class="farm-modal-body">
          <div class="farm-form-group">
            <label class="farm-label">类型</label>
            <select
              class="farm-select"
              v-model="fertilizer.fertilizerType"
              disabled
            >
              <option
                v-for="(fertilizerType, index) in fertilizerTypeList"
                :key="index"
                :value="fertilizerType"
              >
                {{ fertilizerType }}
              </option>
            </select>
          </div>

          <div class="farm-form-group">
            <label class="farm-label">{{ fertilizerType + "名" }}</label>
            <input
              v-model="fertilizer.name"
              disabled
              type="text"
              class="farm-input"
              placeholder="名称"
            />
          </div>

          <div class="farm-form-group">
            <label class="farm-label">使用数量</label>
            <input
              v-model="fertilizer.amount"
              type="text"
              class="farm-input"
              placeholder="请输入使用数量"
              @keyup="amountWrite"
            />
            <p v-if="amountMessage" class="farm-error">{{ amountMessage }}</p>
          </div>

          <div class="farm-form-group">
            <label class="farm-label">单位</label>
            <input
              v-model="fertilizer.unit"
              disabled
              type="text"
              class="farm-input"
              placeholder="单位"
            />
          </div>
        </div>
        <div class="farm-modal-footer">
          <button class="farm-btn farm-btn-outline" @click="showFarmUseModal = false">取消</button>
          <button class="farm-btn farm-btn-primary" @click="farmUseModel">提交</button>
        </div>
      </div>
    </div>

    <div class="farm-modal-overlay" v-if="showAddFertilizerModal" @click="showAddFertilizerModal = false">
      <div class="farm-modal" @click.stop>
        <div class="farm-modal-header">
          <h3 class="farm-modal-title">新建采购信息</h3>
          <button class="farm-modal-close" @click="showAddFertilizerModal = false">&times;</button>
        </div>
        <div class="farm-modal-body">
          <div class="farm-form-group">
            <label class="farm-label">类型</label>
            <select
              class="farm-select"
              v-model="fertilizer.fertilizerType"
              @change="changeFertilizerType"
            >
              <option
                v-for="(fertilizerType, index) in fertilizerTypeList"
                :key="index"
                :value="fertilizerType"
              >
                {{ fertilizerType }}
              </option>
            </select>
            <p v-if="fertilizerTypeMessage" class="farm-error">{{ fertilizerTypeMessage }}</p>
          </div>

          <div class="farm-form-group">
            <label class="farm-label">{{ fertilizerType + "名" }}</label>
            <input
              type="text"
              class="farm-input"
              v-model="fertilizer.name"
              @keyup="nameWrite"
              placeholder="请输入名称"
            />
            <p v-if="nameMessage" class="farm-error">{{ nameMessage }}</p>
          </div>

          <div class="form-row">
            <div class="farm-form-group">
              <label class="farm-label">库存</label>
              <input
                v-model="fertilizer.amount"
                type="text"
                class="farm-input"
                @keyup="amountWrite"
                placeholder="请输入库存"
              />
              <p v-if="amountMessage" class="farm-error">{{ amountMessage }}</p>
            </div>
            <div class="farm-form-group">
              <label class="farm-label">安全库存</label>
              <input
                v-model="fertilizer.safeAmount"
                type="text"
                class="farm-input"
                @keyup="safeAmountWrite"
                placeholder="请输入安全库存"
              />
              <p v-if="safeAmountMessage" class="farm-error">{{ safeAmountMessage }}</p>
            </div>
          </div>

          <div class="farm-form-group">
            <label class="farm-label">单位</label>
            <input
              v-model="fertilizer.unit"
              type="text"
              class="farm-input"
              @keyup="unitWrite"
              placeholder="请输入单位"
            />
            <p v-if="unitMessage" class="farm-error">{{ unitMessage }}</p>
          </div>
        </div>
        <div class="farm-modal-footer">
          <button class="farm-btn farm-btn-outline" @click="showAddFertilizerModal = false">取消</button>
          <button class="farm-btn farm-btn-primary" @click="addFertilizerModel">提交</button>
        </div>
      </div>
    </div>

    <div class="farm-modal-overlay" v-if="showDeleteDialog" @click="cancelDelete">
      <div class="confirm-dialog" @click.stop>
        <h3 class="confirm-title">确认删除</h3>
        <p class="confirm-message">是否删除{{ fertilizerType }}：{{ deleteFertilizerName }}？</p>
        <div class="confirm-buttons">
          <button class="farm-btn farm-btn-outline" @click="cancelDelete">取消</button>
          <button class="farm-btn farm-btn-danger" @click="executeDelete">删除</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import bus from "@/util/Bus";
import {
  initFertilizer,
  doPage1,
  addPurchase1,
  abnormalConsumptionFertilizer,
  farmUse,
  addFertilizer,
  deleteFertilizer
} from "@/api/farmStaff.js";

export default {
  name: "Fertilizer",
  data: () => ({
    fertilizerTypeList: ["饲料", "肥料"],
    fertilizerType: "饲料",
    fertilizerList: [],
    fertilizer: {
      id: null,
      name: null,
      amount: null,
      safeAmount: null,
      unit: null,
      fertilizerType: null
    },
    pageBody1: {
      page: null,
      pages: null,
      pageList: []
    },
    purchase: {
      amount: null,
      price: null,
      fertilizer: null
    },
    nowAmount: null,
    fertilizerTypeMessage: null,
    nameMessage: null,
    amountMessage: null,
    safeAmountMessage: null,
    unitMessage: null,
    priceMessage: null,
    showPurchaseModal: false,
    showConsumptionModal: false,
    showFarmUseModal: false,
    showAddFertilizerModal: false,
    showDeleteDialog: false,
    deleteFertilizerName: ""
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
    changeFertilizerType() {
      this.fertilizerTypeMessage = null;
    },
    fertilizerTypeChange() {
      initFertilizer(this.fertilizerType);
    },
    doPage1(page) {
      this.pageBody1.page = page;
      doPage1(this.pageBody1, this.fertilizerType);
    },
    addPurchase(fertilizer) {
      this.amountMessage = null;
      this.priceMessage = null;
      this.fertilizer = JSON.parse(JSON.stringify(fertilizer));
      this.purchase.price = null;
      this.purchase.amount = null;
      this.showPurchaseModal = true;
    },
    purchaseModel() {
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
          this.purchase.fertilizer = this.fertilizer;
          addPurchase1(this.purchase);
          this.showPurchaseModal = false;
          this.fertilizerType = this.fertilizer.fertilizerType;
        }
      }
    },
    abnormalConsumptionFertilizer(fertilizer) {
      this.nowAmount = fertilizer.amount;
      this.amountMessage = null;
      this.fertilizer = JSON.parse(JSON.stringify(fertilizer));
      this.fertilizer.amount = null;
      this.showConsumptionModal = true;
    },
    consumptionModel() {
      if (this.fertilizer.amount == null) {
        this.amountMessage = "请输入消耗数量！";
      } else {
        let p = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/;
        if (!p.test(this.fertilizer.amount)) {
          this.amountMessage = "请输入正数（最高小数点后两位）！";
        } else if (this.fertilizer.amount > this.nowAmount) {
          this.amountMessage = "库存不足";
        } else {
          abnormalConsumptionFertilizer(this.fertilizer);
          this.showConsumptionModal = false;
          this.fertilizerType = this.fertilizer.fertilizerType;
        }
      }
    },
    farmUse(fertilizer) {
      this.nowAmount = fertilizer.amount;
      this.amountMessage = null;
      this.fertilizer = JSON.parse(JSON.stringify(fertilizer));
      this.fertilizer.amount = null;
      this.showFarmUseModal = true;
    },
    farmUseModel() {
      if (this.fertilizer.amount == null) {
        this.amountMessage = "请输入消耗数量！";
      } else {
        let p = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/;
        if (!p.test(this.fertilizer.amount)) {
          this.amountMessage = "请输入正数（最高小数点后两位）！";
        } else if (this.fertilizer.amount > this.nowAmount) {
          this.amountMessage = "库存不足";
        } else {
          farmUse(this.fertilizer);
          this.showFarmUseModal = false;
          this.fertilizerType = this.fertilizer.fertilizerType;
        }
      }
    },
    addFertilizer() {
      this.fertilizer.id = null;
      this.fertilizer.name = null;
      this.fertilizer.amount = null;
      this.fertilizer.safeAmount = null;
      this.fertilizer.unit = null;
      this.fertilizer.fertilizerType = null;
      this.nameMessage = null;
      this.amountMessage = null;
      this.safeAmountMessage = null;
      this.unitMessage = null;
      this.fertilizerTypeMessage = null;
      this.showAddFertilizerModal = true;
    },
    addFertilizerModel() {
      if (
        this.fertilizer.name == null ||
        this.fertilizer.amount == null ||
        this.fertilizer.safeAmount == null ||
        this.fertilizer.unit == null ||
        this.fertilizer.fertilizerType == null
      ) {
        if (this.fertilizer.name == null) {
          this.nameMessage = "请输入名称！";
        }
        if (this.fertilizer.amount == null) {
          this.amountMessage = "请输入库存！";
        }
        if (this.fertilizer.safeAmount == null) {
          this.safeAmountMessage = "请输入安全库存！";
        }
        if (this.fertilizer.unit == null) {
          this.unitMessage = "请输入单位！";
        }
        if (this.fertilizer.fertilizerType == null) {
          this.fertilizerTypeMessage = "请选择类型！";
        }
      } else {
        let cn = /^[\u4E00-\u9FA5]+$/;
        let re = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/;
        if (
          !cn.test(this.fertilizer.name) ||
          !cn.test(this.fertilizer.unit) ||
          !re.test(this.fertilizer.amount) ||
          !re.test(this.fertilizer.safeAmount)
        ) {
          if (!cn.test(this.fertilizer.name)) {
            this.nameMessage = "请输入中文！";
          }
          if (!cn.test(this.fertilizer.unit)) {
            this.unitMessage = "请输入中文！";
          }
          if (!re.test(this.fertilizer.amount)) {
            this.amountMessage = "请输入正数（最高小数点后两位）！";
          }
          if (!re.test(this.fertilizer.safeAmount)) {
            this.safeAmountMessage = "请输入正数（最高小数点后两位）！";
          }
        } else {
          addFertilizer(this.fertilizer);
          this.showAddFertilizerModal = false;
          this.fertilizerType = this.fertilizer.fertilizerType;
        }
      }
    },
    showDeleteConfirm(fertilizer) {
      this.deleteFertilizerName = fertilizer.name;
      this.fertilizer = JSON.parse(JSON.stringify(fertilizer));
      this.showDeleteDialog = true;
    },
    cancelDelete() {
      this.showDeleteDialog = false;
      this.deleteFertilizerName = "";
    },
    executeDelete() {
      this.showDeleteDialog = false;
      deleteFertilizer(this.fertilizer);
      this.deleteFertilizerName = "";
    }
  },
  created() {
    initFertilizer(this.fertilizerType);
    bus.$on(bus.fertilizerList, data => {
      this.fertilizerList = data;
    });
    bus.$on(bus.pageBody1, data => {
      this.pageBody1 = data;
    });
  },
  beforeDestroy() {
    bus.$off(bus.fertilizerList);
    bus.$off(bus.pageBody1);
  }
};
</script>

<style scoped>
.fertilizer-page {
  padding: 32px;
}

.fertilizer-header {
  margin-bottom: 32px;
}

.fertilizer-title-section {
  text-align: center;
  margin-bottom: 32px;
}

.fertilizer-toolbar {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 20px;
}

.fertilizer-filter {
  display: flex;
  flex-direction: column;
  gap: 8px;
  min-width: 200px;
}

.fertilizer-filter .farm-select {
  width: 200px;
}

.fertilizer-table-wrapper {
  overflow-x: auto;
  margin-bottom: 24px;
  border-radius: var(--farm-radius);
  background: white;
}

.farm-table {
  min-width: 600px;
}

.fertilizer-actions {
  display: flex;
  gap: 8px;
  justify-content: center;
  flex-wrap: wrap;
}

.fertilizer-empty {
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
  .fertilizer-page {
    padding: 20px;
  }

  .fertilizer-toolbar {
    flex-direction: column;
    align-items: stretch;
  }

  .fertilizer-filter {
    width: 100%;
  }

  .fertilizer-filter .farm-select {
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
