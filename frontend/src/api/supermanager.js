import axios from "@/util/MyAxios";
import bus from "@/util/Bus";
import { setTimeout } from "core-js";
// import Qs from "qs";

// 获得员工信息
export function getStaff() {
  axios.get("/superManager/getStaff").then(response => {
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
  axios.post("/superManager/addStaff", staff).then(response => {
    setTimeout(() => {
      alert(response.data.message);
      window.location.href = "/supermanager/staffShow";
    }, 200);
  });
}

// 解雇员工
export function deleteStaff(username) {
  axios.post(`superManager/deleteStaff/${username}`).then(response => {
    setTimeout(() => {
      alert(response.data.message);
      getStaff();
    }, 200);
  });
}

// 任职员工
export function takeOffice(staff, id) {
  axios.post(`superManager/takeOffice/${id}`, staff).then(response => {
    setTimeout(() => {
      alert(response.data.message);
      getStaff();
    }, 200);
  });
}

// 修改密码
export function changePassword(userBodyChangePassword) {
  axios
    .post("superManager/changePassword", userBodyChangePassword)
    .then(response => {
      setTimeout(() => {
        alert(response.data.message);
      }, 200);
    });
}

// 初始化考勤信息
export function initAttendance() {
  axios.get("superManager/initAttendance").then(response => {
    setTimeout(() => {
      bus.$emit(bus.attendanceList, response.data.attendanceList);
      bus.$emit(bus.attendanceList1, response.data.attendanceList1);
      bus.$emit(bus.pageBody1, response.data.pageBody1);
    }, 200);
  });
}

// 选择日期，获得考勤信息
export function chooseDate(choosedDate) {
  axios.post("superManager/chooseDate", choosedDate).then(response => {
    setTimeout(() => {
      bus.$emit(bus.attendanceList, response.data.attendanceList);
      bus.$emit(bus.attendanceList1, response.data.attendanceList1);
      bus.$emit(bus.pageBody1, response.data.pageBody1);
    }, 200);
  });
}

// 提交考勤信息
export function updateAttendance(attendanceList, choosedDate) {
  axios.post("superManager/updateAttendance", attendanceList).then(response => {
    setTimeout(() => {
      alert(response.data.message);
      chooseDate(choosedDate);
    }, 200);
  });
}

// 导出考勤Excel
export function downloadAttendanceExcel(choosedDate) {
  axios
    .post("superManager/downloadAttendanceExcel", choosedDate, {
      responseType: "blob"
    })
    .then(response => {
      // if (!response) {
      //   this.$notify.error({ title: "操作失败", message: "文件下载失败" });
      // }
      // let filename = response.headers["content-disposition"];
      // const url = window.URL.createObjectURL(response.data);
      // const link = document.createElement("a");
      // link.style.display = "none";
      // link.href = url;
      // link.setAttribute("download", filename);
      // document.body.appendChild(link);
      // link.click();

      // let filename = response.headers["content-disposition"].split("=")[1];
      // const link = document.createElement("a");
      // const blob = new Blob([response.data], {
      //   type: "application/vnd.ms-excel"
      // });
      // link.style.display = "none";
      // link.href = URL.createObjectURL(blob);
      // link.setAttribute("download", filename);
      // document.body.appendChild(link);
      // link.click();
      // document.body.removeChild(link);

      let getDate = response.headers["content-disposition"];
      // let fileName = response.headers["content-disposition"].split("=")[1];
      let fileName = "考勤信息(" + getDate + ").xls";
      let objectUrl = URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.download = fileName;
      link.href = objectUrl;
      link.click();
    });
}
