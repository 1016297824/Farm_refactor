import axios from "@/util/MyAxios";

// 登录
export function login(userBody) {
  axios.post("/login", userBody).then(response => {
    let token = response.headers["token"];
    let role = response.headers["role"];
    let username = response.headers["username"];

    if (token != null) {
      sessionStorage.setItem("token", token);
    }

    if (username != null) {
      sessionStorage.setItem("username", username);
    }

    if (role == "Customer") {
      window.location.href = "/customer/reserve";
    } else if (role == "SuperManager") {
      window.location.href = "/supermanager/staffShow";
    } else if (role == "FarmManager") {
      window.location.href = "/farmManager/staffShow1";
    } else if (role == "RestaurantManager") {
      window.location.href = "/restaurantManager/staffShow2";
    } else if (role == "FarmStaff") {
      window.location.href = "/farmStaff/productShow";
    } else if (role == "RestaurantStaff") {
      window.location.href = "/restaurantStaff/settleAccounts";
    }
  });
}

// 注册
export function register(customer) {
  axios.post("/register", customer).then(response => {
    alert(response.data.message);
  });
}
