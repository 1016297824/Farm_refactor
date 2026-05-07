import axios from "@/util/MyAxios";
import bus from "@/util/Bus";

// 根据所选类型初始化产品信息
export function initProduct(productType) {
  axios.post("farmStaff/initProduct", productType).then(response => {
    setTimeout(() => {
      bus.$emit(bus.productList, response.data.productList);
      bus.$emit(bus.pageBody1, response.data.pageBody1);
    }, 200);
  });
}

// 农产品分页
export function doPage(pageBody1, productType) {
  axios.post(`/farmStaff/doPage/${productType}`, pageBody1).then(response => {
    setTimeout(() => {
      bus.$emit(bus.productList, response.data.productList);
      bus.$emit(bus.pageBody1, response.data.pageBody1);
    }, 200);
  });
}

// 修改密码
export function changePassword(userBodyChangePassword) {
  axios
    .post("farmStaff/changePassword", userBodyChangePassword)
    .then(response => {
      setTimeout(() => {
        alert(response.data.message);
      }, 200);
    });
}

// 添加农产品
export function addProduct(product) {
  axios.post("farmStaff/addProduct", product).then(response => {
    setTimeout(() => {
      alert(response.data.message);
    }, 200);
    initProduct(product.productType);
  });
}

// 删除农产品
export function deleteProduct(product) {
  axios.post("farmStaff/deleteProduct", product).then(response => {
    setTimeout(() => {
      alert(response.data.message);
    }, 200);
    initProduct(product.productType);
  });
}

// 修改农产品信息
export function modifyProduct(product) {
  axios.post("farmStaff/modifyProduct", product).then(response => {
    setTimeout(() => {
      alert(response.data.message);
    }, 200);
    initProduct(product.productType);
  });
}

// 异常消耗
export function abnormalConsumption(product) {
  axios.post("farmStaff/abnormalConsumption", product).then(response => {
    setTimeout(() => {
      alert(response.data.message);
    }, 200);
    initProduct(product.productType);
  });
}

// 农产品生产
export function produce(product) {
  axios.post("farmStaff/produce", product).then(response => {
    setTimeout(() => {
      alert(response.data.message);
    }, 200);
    initProduct(product.productType);
  });
}

// 异常消耗(原料)
export function abnormalConsumption1(product) {
  axios.post("farmStaff/abnormalConsumption1", product).then(response => {
    setTimeout(() => {
      alert(response.data.message);
    }, 200);
    initProduct(product.productType);
  });
}

// 采购原材料
export function addPurchase(purchase) {
  axios.post("farmStaff/addPurchase", purchase).then(response => {
    setTimeout(() => {
      alert(response.data.message);
    }, 200);
    initProduct(purchase.product.productType);
  });
}

// 农产品成熟
export function produce1(product) {
  axios.post("farmStaff/produce1", product).then(response => {
    setTimeout(() => {
      alert(response.data.message);
    }, 200);
    initProduct(product.productType);
  });
}

// 初始化饲料化肥表
export function initFertilizer(fertilizerType) {
  axios.post("farmStaff/initFertilizer", fertilizerType).then(response => {
    setTimeout(() => {
      bus.$emit(bus.fertilizerList, response.data.fertilizerList);
      bus.$emit(bus.pageBody1, response.data.pageBody1);
    }, 200);
  });
}

// 饲料肥料分页
export function doPage1(pageBody1, fertilizerType) {
  axios
    .post(`/farmStaff/doPage1/${fertilizerType}`, pageBody1)
    .then(response => {
      setTimeout(() => {
        bus.$emit(bus.fertilizerList, response.data.fertilizerList);
        bus.$emit(bus.pageBody1, response.data.pageBody1);
      }, 200);
    });
}

// 采购饲料肥料
export function addPurchase1(purchase) {
  axios.post("farmStaff/addPurchase1", purchase).then(response => {
    setTimeout(() => {
      alert(response.data.message);
    }, 200);
    initFertilizer(purchase.fertilizer.fertilizerType);
  });
}

// 异常消耗（饲料肥料）
export function abnormalConsumptionFertilizer(fertilizer) {
  axios
    .post("farmStaff/abnormalConsumptionFertilizer", fertilizer)
    .then(response => {
      setTimeout(() => {
        alert(response.data.message);
      }, 200);
      initFertilizer(fertilizer.fertilizerType);
    });
}

// 农场使用（饲料肥料）
export function farmUse(fertilizer) {
  axios.post("farmStaff/farmUse", fertilizer).then(response => {
    setTimeout(() => {
      alert(response.data.message);
    }, 200);
    initFertilizer(fertilizer.fertilizerType);
  });
}

// 新建采购信息（饲料肥料）
export function addFertilizer(fertilizer) {
  axios.post("farmStaff/addFertilizer", fertilizer).then(response => {
    setTimeout(() => {
      alert(response.data.message);
    }, 200);
    initFertilizer(fertilizer.fertilizerType);
  });
}

// 删除采购信息（饲料肥料）
export function deleteFertilizer(fertilizer) {
  axios.post("farmStaff/deleteFertilizer", fertilizer).then(response => {
    setTimeout(() => {
      alert(response.data.message);
    }, 200);
    initFertilizer(fertilizer.fertilizerType);
  });
}

// 添加销售信息
export function addSaleList(saleList) {
  axios.post("farmStaff/addSaleList", saleList).then(response => {
    setTimeout(() => {
      alert(response.data.message);
    }, 200);
  });
}
