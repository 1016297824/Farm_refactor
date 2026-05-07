import axios from "@/util/MyAxios";
import bus from "@/util/Bus";

// 获得所有订单信息
export function getReserve() {
  axios.get("/customer/reserve/getReserve").then(response => {
    setTimeout(() => {
      bus.$emit(bus.reserveList, response.data.reserveList);
      bus.$emit(bus.reserveLists, response.data.reserveLists);
    }, 200);
  });
}

// 删除订单
export function deleteReserve(no) {
  axios.post(`/customer/reserve/deleteReserve/${no}`).then(response => {
    setTimeout(() => {
      bus.$emit(bus.reserveList, response.data.reserveList);
    }, 200);
  });
}

// 初始化桌位表
export function initDiningTable() {
  axios.get("/customer/reserveAdd/initDiningTable").then(response => {
    setTimeout(() => {
      bus.$emit(bus.diningTableList, response.data.diningTableList);
      bus.$emit(bus.pageBody, response.data.pageBody);
    }, 200);
  });
}

// 订单分页
export function doPage(pageBody) {
  axios.post("/customer/reserveAdd/doPage", pageBody).then(response => {
    setTimeout(() => {
      bus.$emit(bus.diningTableList, response.data.diningTableList);
      bus.$emit(bus.pageBody, response.data.pageBody);
    }, 200);
  });
}

// 添加订单
export function reserveAddApi(diningTable, pageBody, callback) {
  axios
    .post(`/customer/reserveAdd/${diningTable.id}`, pageBody)
    .then(response => {
      if (callback) {
        callback(response.data.message);
      }
    });
}

// 获得点餐信息
export function getOrdering(no) {
  axios.get(`/customer/getOrdering/${no}`).then(response => {
    setTimeout(() => {
      bus.$emit(bus.isOrdered, response.data.isOrdered);
      bus.$emit(bus.pageBody1, response.data.pageBody1);
      bus.$emit(bus.orderingList, response.data.orderingList);
      bus.$emit(bus.menuList1, response.data.menuList1);
      bus.$emit(bus.menuList, response.data.menuList);
      bus.$emit(bus.reserve, response.data.reserve);
    }, 200);
  });
}

// 添加点餐信息
export function addOrderingApi(no, orderingList, callback) {
  axios.post("/customer/addOrdering", orderingList).then(response => {
    if (callback) {
      callback(response.data.message);
    }
    setTimeout(() => {
      getOrdering(no);
    }, 200);
  });
}

// 修改密码
export function changePassword(userBodyChanePassword) {
  axios
    .post("/customer/changePassword", userBodyChanePassword)
    .then(response => {
      setTimeout(() => {
        alert(response.data.message);
      }, 200);
    });
}
