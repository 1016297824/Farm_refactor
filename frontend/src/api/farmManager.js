import axios from "@/util/MyAxios";
import bus from "@/util/Bus";
import { setTimeout } from "core-js";

// 获得员工信息
export function getStaff() {
  axios.get("/farmManager/getStaff").then(response => {
    setTimeout(() => {
      bus.$emit(bus.staffList, response.data.staffList);
      bus.$emit(bus.staffList1, response.data.staffList1);
      bus.$emit(bus.pageBody1, response.data.pageBody1);
      bus.$emit(bus.positionList, response.data.positionList);
    }, 200);
  });
}

// 添加员工
export function addStaff(staff) {
  axios.post("/farmManager/addStaff", staff).then(response => {
    setTimeout(() => {
      alert(response.data.message);
      window.location.href = "/farmManager/staffShow1";
    }, 200);
  });
}

// 解雇员工
export function deleteStaff(username) {
  axios.post(`farmManager/deleteStaff/${username}`).then(response => {
    setTimeout(() => {
      alert(response.data.message);
      getStaff();
    }, 200);
  });
}

// 任职员工
export function takeOffice(staff, id) {
  axios.post(`farmManager/takeOffice/${id}`, staff).then(response => {
    setTimeout(() => {
      alert(response.data.message);
      getStaff();
    }, 200);
  });
}

// 修改密码
export function changePassword(userBodyChangePassword) {
  axios
    .post("farmManager/changePassword", userBodyChangePassword)
    .then(response => {
      setTimeout(() => {
        alert(response.data.message);
      }, 200);
    });
}

// 初始化考勤信息
export function initAttendance() {
  axios.get("farmManager/initAttendance").then(response => {
    setTimeout(() => {
      bus.$emit(bus.attendanceList, response.data.attendanceList);
      bus.$emit(bus.attendanceList1, response.data.attendanceList1);
      bus.$emit(bus.pageBody1, response.data.pageBody1);
    }, 200);
  });
}

// 选择日期，获得考勤信息
export function chooseDate(choosedDate) {
  axios.post("farmManager/chooseDate", choosedDate).then(response => {
    setTimeout(() => {
      bus.$emit(bus.attendanceList, response.data.attendanceList);
      bus.$emit(bus.attendanceList1, response.data.attendanceList1);
      bus.$emit(bus.pageBody1, response.data.pageBody1);
    }, 200);
  });
}

// 提交考勤信息
export function updateAttendance(attendanceList, choosedDate) {
  axios.post("farmManager/updateAttendance", attendanceList).then(response => {
    setTimeout(() => {
      alert(response.data.message);
      chooseDate(choosedDate);
    }, 200);
  });
}

// 导出考勤Excel
export function downloadAttendanceExcel(choosedDate) {
  axios
    .post("farmManager/downloadAttendanceExcel", choosedDate, {
      responseType: "blob"
    })
    .then(response => {
      let getDate = response.headers["content-disposition"];
      let fileName = "考勤信息(" + getDate + ").xls";
      let objectUrl = URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.download = fileName;
      link.href = objectUrl;
      link.click();
    });
}

// 导出农场日收入excel
export function downloadDaySaleExcel(choosedDate) {
  axios
    .post("/farmManager/downloadDaySaleExcel", choosedDate, {
      responseType: "blob"
    })
    .then(response => {
      let getDate = response.headers["content-disposition"];
      let fileName = "农场收入(" + getDate + ").xls";
      let objectUrl = URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.download = fileName;
      link.href = objectUrl;
      link.click();
    });
}

// 导出农场月收入excel
export function downloadMonthSaleExcel(choosedDate) {
  axios
    .post("/farmManager/downloadMonthSaleExcel", choosedDate, {
      responseType: "blob"
    })
    .then(response => {
      let getDate = response.headers["content-disposition"];
      let fileName = "农场收入(" + getDate + ").xls";
      let objectUrl = URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.download = fileName;
      link.href = objectUrl;
      link.click();
    });
}

// 导出农场日支出
export function downloadDayPurchaseExcel(choosedDate) {
  axios
    .post("/farmManager/downloadDayPurchaseExcel", choosedDate, {
      responseType: "blob"
    })
    .then(response => {
      let getDate = response.headers["content-disposition"];
      let fileName = "农场支出(" + getDate + ").xls";
      let objectUrl = URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.download = fileName;
      link.href = objectUrl;
      link.click();
    });
}

// 导出农场月支出
export function downloadMonthPurchaseExcel(choosedDate) {
  axios
    .post("/farmManager/downloadMonthPurchaseExcel", choosedDate, {
      responseType: "blob"
    })
    .then(response => {
      let getDate = response.headers["content-disposition"];
      let fileName = "农场支出(" + getDate + ").xls";
      let objectUrl = URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.download = fileName;
      link.href = objectUrl;
      link.click();
    });
}
