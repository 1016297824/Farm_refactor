import axios from "axios";
import bus from "@/util/Bus";

axios.defaults.baseURL = "/api";
axios.defaults.withCredentials = true;
axios.defaults.headers["Content-Type"] = "application/json;charset=UTF-8";

axios.interceptors.request.use(
  function(request) {
    request.headers.token = sessionStorage.getItem("token");
    return request;
  },
  function(error) {
    return Promise.reject(error);
  }
);

axios.interceptors.response.use(
  function(response) {
    return response;
  },
  function(error) {
    let msg = "网络异常，请稍后重试";
    if (error.response) {
      msg = (error.response.data && error.response.data.message) || `请求错误 ${error.response.status}`;
    } else if (error.request) {
      msg = "服务器无响应，请检查网络连接";
    }
    bus.$emit(bus.error, msg);
    return Promise.reject(error);
  }
);

export default axios;
