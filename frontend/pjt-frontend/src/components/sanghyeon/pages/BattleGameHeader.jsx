import "../../../css/Header.css";
import Setting from "../components/Setting";
import { useNavigate, useLocation } from "react-router-dom";
import { useState } from "react";
import { MdOutlineKeyboardBackspace } from "react-icons/md";
import axios from "axios";
import store from "../../../store/store.js";
import Swal from "sweetalert2";

const BattleGameHeader = () => {
  const navigate = useNavigate();
  const userIcon = "/memberIcon.png";
  const settingIcon = "/settingIcon.png";
  const giftIcon = "/giftIcon.png";
  const [modalIsOpen, setModalIsOpen] = useState(false);

  const openModal = () => {
    setModalIsOpen(true);
  };

  const closeModal = () => {
    setModalIsOpen(false);
  };
  const { baseURL, accessToken, roomId, setNormalQuit } = store((state) => ({
    baseURL: state.baseURL,
    accessToken: state.accessToken,
    roomId: state.roomId,
    setNormalQuit: state.setNormalQuit,
  }));

  const quitBattleRoom = async () => {
    Swal.fire({
      text: "방을 나가시겠습니까?",
      icon: "question",
      showCancelButton: true,
      confirmButtonText: "나가기",
      cancelButtonText: "취소",
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          const quitRes = await axios({
            method: "POST",
            url: `${baseURL}/battle/room/${roomId}/leave`,
            headers: {
              Authorization: `Bearer ${accessToken}`,
            },
          });
          setNormalQuit(true);
          // Swal.fire({
          //   text: "방을 나갔습니다.",
          //   icon: "success",
          //   timer: 3000,
          // });
          navigate("/_battle-list");
        } catch (error) {
          navigate("/_battle-list");
          console.log(error);
        }
      }
    });
  };

  return (
    <>
      <div className="header-container">
        <div
          className="header-title"
          onClick={() => {
            navigate("/");
          }}
        >
          Street Coding Fighter
        </div>
        <div className="header-back-button" onClick={quitBattleRoom}>
          <MdOutlineKeyboardBackspace />
        </div>
        <div className="header-right">
          <div className="header-icon">
            {/* {accessToken ? (
              <img
                onClick={() => navigate("/gacha")}
                className="gift-icon"
                src={giftIcon}
                alt="giftIcon"
              />
            ) : null}
            {accessToken ? (
              <img
                onClick={() => {
                  navigate("/account");
                }}
                className="user-icon"
                src={userIcon}
                alt="memberIcon"
              />
            ) : null} */}
            <img
              onClick={openModal}
              className="setting-icon"
              src={settingIcon}
              alt="settingIcon"
            />
          </div>
        </div>
      </div>
      <Setting isOpen={modalIsOpen} onClose={closeModal} />
    </>
  );
};

export default BattleGameHeader;
