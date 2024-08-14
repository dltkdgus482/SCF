import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Modal from "react-modal";
import "../../css/Setting.css"; // 이 파일은 필요 없을 수 있습니다
import axios from "axios";
import store from "../../store/store.js";
import createAuthClient from "../sanghyeon/apis/createAuthClient.js";
import SoundStore from "../../stores/SoundStore.jsx";
import { FaVolumeMute, FaVolumeUp } from "react-icons/fa"; //react-icons import
import VolumeSlider from "./VolumeSlider.jsx";

import Swal from "sweetalert2";

Modal.setAppElement("#root");
const settingIcon = "/settingIcon.png";

const styles = {
  modal: {
    width: "500px",
    height: "auto",
    position: "absolute",
    top: "50%",
    left: "50%",
    right: "auto",
    bottom: "auto",
    transform: "translate(-50%, -50%)",
    backgroundColor: "white",
    padding: "20px",
    borderRadius: "10px",
  },
  overlay: {
    position: "fixed",
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: "rgba(0, 0, 0, 0.75)",
    zIndex: 1000,
  },
  settingClose: {
    width: "25px",
    height: "25px",
  },
  settingTitle: {
    display: "flex",
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    marginBottom: "10px",
  },
  settingTitleText: {
    margin: 0,
    userSelect: "none",
  },
  setting: {
    border: "solid 1px black",
    padding: "10px",
    userSelect: "none",
  },
};

const Setting = ({ isOpen, onClose }) => {
  const {
    accessToken,
    setAccessToken,
    baseURL,
    memberId,
    setMemberId,
    clearLocalStorage,
  } = store((state) => ({
    accessToken: state.accessToken,
    setAccessToken: state.setAccessToken,
    baseURL: state.baseURL,
    memberId: state.memberId,
    setMemberId: state.setMemberId,
    clearLocalStorage: state.clearLocalStorage,
  }));
  const { setBgmVolume, setEffectVolume, bgmVolume, effectVolume } =
    SoundStore();

  const navigate = useNavigate();
  const [modalIsOpen, setModalIsOpen] = useState(false);
  const authClient = createAuthClient(
    baseURL,
    () => accessToken,
    setAccessToken
  );

  const logout = async () => {
    try {
      const logoutRes = await authClient({
        method: "POST",
        url: `${baseURL}/user/public/logout`,
        data: {
          memberId: memberId,
        },
      });
      clearLocalStorage();
      Swal.fire({
        text: "로그아웃 되었습니다.",
        icon: "success",
        timer: 2000,
        showConfirmButton: false,
        showCloseButton: false,
      });

      setTimeout(() => {
        navigate("/login");
        window.location.reload();
      }, 2000);
    } catch (error) {
      Swal.fire({
        text: "로그아웃에 실패했습니다.",
        icon: "error",
        timer: 3000,
      });
      console.log(error);
    }
  };

  return (
    <div>
      <Modal
        isOpen={isOpen}
        onRequestClose={onClose}
        contentLabel="Example Modal"
        style={{ content: styles.modal, overlay: styles.overlay }}
      >
        <div className="setting-container">
          <div style={styles.settingTitle}>
            <h2 style={styles.settingTitleText}>Setting</h2>
            <img
              onClick={onClose}
              style={styles.settingClose}
              src="/close.png"
              alt="close-setting"
            />
          </div>
          <hr />
          <div className="settings">
            <div style={styles.setting}>
              BGM Volume
              <VolumeSlider volume={bgmVolume * 100} handler={setBgmVolume} />
            </div>
            <div style={styles.setting}>
              Effect Volume
              <VolumeSlider
                volume={effectVolume * 100}
                handler={setEffectVolume}
              />
            </div>
            <p style={styles.setting} onClick={() => navigate("/")}>
              Back to Title
            </p>
            {accessToken && (
              <p style={styles.setting} onClick={logout}>
                Logout
              </p>
            )}
          </div>
        </div>
      </Modal>
    </div>
  );
};

export default Setting;
