import "../../css/MultiHeader.css";
import Setting from "../sanghyeon/components/Setting";
import { useNavigate, useLocation } from "react-router-dom";
import { useState } from "react";
import { MdOutlineKeyboardBackspace } from "react-icons/md";
// import axios from "axios";
// import store from "../../store/store.js";
// import Swal from "sweetalert2";

const MultiHeader = ({onBackButtonClick}) => {
  const navigate = useNavigate();
  const userIcon = "/memberIcon.png";
  const settingIcon = "/settingIcon.png";
  const [modalIsOpen, setModalIsOpen] = useState(false);

  const currentPath = useLocation().pathname;

  const openModal = () => {
    setModalIsOpen(true);
  };

  const closeModal = () => {
    setModalIsOpen(false);
  };

  const handleBackClick = () => {
    if (onBackButtonClick) {
      onBackButtonClick();
    }
    navigate("/_multi");
  };

  return (
    <>
      <div className="multi-header-container">
        <div
          className="multi-header-title"
          onClick={() => {
            navigate('/main');
          }}
        >
          Street Coding Figther
        </div>
      </div>
        <div className="multi-header-back-button"  onClick={handleBackClick}>
          <MdOutlineKeyboardBackspace />
        </div>
        <div className='multi-header-right'>
          <div className="multi-header-icon">
            <img onClick={openModal} className="setting-icon" src={settingIcon} alt="settingIcon" />
          </div>
        </div>
      <Setting isOpen={modalIsOpen} onClose={closeModal} />
    </>
  );
};

export default MultiHeader;
