// apiClient.js
import axios from "axios";
import { jwtDecode } from "jwt-decode";
import Swal from "sweetalert2";
import store from "../../../store/store.js";

const createAuthClient = (baseURL, getAccessToken, setAccessToken) => {
  const { clearLocalStorage } = store((state) => ({
    clearLocalStorage: state.clearLocalStorage,
  }));

  const authClient = axios.create({
    baseURL,
    withCredentials: true,
  });

  const getNewToken = async () => {
    try {
      const res = await axios({
        method: "POST",
        url: `${baseURL}/user/public/reissue`,
        withCredentials: true,
      });

      const authorizationHeader = res.headers["authorization"];
      const accessToken = authorizationHeader
        ? authorizationHeader.replace(/^Bearer\s+/i, "")
        : null;

      setAccessToken(accessToken);
    } catch (error) {
      console.log(error);
      clearLocalStorage();
      Swal.fire({
        icon: "error",
        title: "세션이 만료되었습니다.",
        text: "다시 로그인해주세요.",
        timer: 2000,
        showConfirmButton: false,
        showCloseButton: false,
      });

      setTimeout(() => {
        window.location.href = "/login";
        window.location.reload();
      }, 2000);
    }
  };

  authClient.interceptors.request.use(async (config) => {
    let accessToken = getAccessToken();
    
    if (accessToken) {
      const { exp } = jwtDecode(accessToken);
      if (Date.now() >= exp * 1000) {
        await getNewToken();
        accessToken = getAccessToken();
      }

      config.headers.Authorization = `Bearer ${accessToken}`;
    }

    return config;
  });

  return authClient;
};

export default createAuthClient;
