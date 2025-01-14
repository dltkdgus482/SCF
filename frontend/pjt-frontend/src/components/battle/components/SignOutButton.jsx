import createAuthClient from "../apis/createAuthClient.js";
import store from "../../../store/store.js";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";

const SignOutButton = function () {
  const {
    userId,
    accessToken,
    setAccessToken,
    baseURL,
    memberId,
    setMemberId,
    clearLocalStorage,
  } = store((state) => ({
    userId: state.userId,
    accessToken: state.accessToken,
    setAccessToken: state.setAccessToken,
    baseURL: state.baseURL,
    memberId: state.memberId,
    setMemberId: state.setMemberId,
    clearLocalStorage: state.clearLocalStorage,
  }));
  const navigate = useNavigate();
  const authClient = createAuthClient(
    baseURL,
    () => accessToken,
    setAccessToken
  );

  const signOut = async () => {
    try {
      await authClient({
        method: "DELETE",
        url: `${baseURL}/user/quit`,
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });
      clearLocalStorage();
      Swal.fire({
        text: "회원탈퇴에 성공했습니다.",
        icon: "success",
        timer: 3000,
      });
      navigate("/login");
    } catch (error) {
      Swal.fire({
        text: "회원탈퇴에 실패했습니다.",
        icon: "error",
        timer: 3000,
      });
      console.log(error);
    }
  };

  return (
    <>
      <button onClick={signOut}>회원탈퇴</button>
    </>
  );
};

export default SignOutButton;
