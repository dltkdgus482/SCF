import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Modal from "react-modal";
import PasswordModal from "./PasswordModal.jsx";
import axios from "axios";
import lock from '/lock.svg'
import unlock from '/unlock.svg'
import multiStore from '../../stores/multiStore.jsx';
import store from '../../store/store.js';
import createAuthClient from "../sanghyeon/apis/createAuthClient.js";

function MultiRoom(props) {
  const navigate = useNavigate();
  const [isModalOpen, setIsModalOpen] = useState(false);

  const {
    roomId,
    setRoomId,
  } = multiStore((state) => ({
    roomId: state.roomId,
    setRoomId: state.setRoomId,
  }));

  const {
    accessToken,
    setAccessToken,
    memberId,
    userId,
    name,
    baseURL,
  } = store((state) => ({
    memberId: state.memberId,
    accessToken: state.accessToken,
    setAccessToken: state.setAccessToken,
    userId: state.userId,
    name: state.name,
    baseURL: state.baseURL,
  }));

  const authClient = createAuthClient(
    baseURL,
    () => accessToken,
    setAccessToken
  );

  const [errorMessage, setErrorMessage] = useState("");
  const [modalIsOpen, setModalIsOpen] = useState(false);

  const openModal = () => {
    setModalIsOpen(true);
  };

  const closeModal = () => {
    setModalIsOpen(false);
  };


  const handleOpenModal = () => {
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
  };

  const handleOpenSubmit = async () => {
    try {
      const response = await axios.post(
        `${baseURL}/multi/room/${props.roomId}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
            'Content-Type': 'text/plain',
          }
        }
      );

      if (response.status === 200) {
        setRoomId(props.roomId);
        console.log('props.roomId', props.roomId);
        console.log('state.roomId', roomId);
        
        navigate(`/_multi-game/${props.roomId}`);
      } else {  
        alert('Enterance error');
      }
    } catch (error) {
      console.error('Error entering the room:', error);
      // 실패 모달창
      setErrorMessage("잘못된 접근입니다!"); 
      openModal();
    }
    handleCloseModal();
  };

  const handleSubmit = async (password) => {
    try {
      const response = await axios.post(
        `${baseURL}/multi/room/${props.roomId}`,
        password,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
            'Content-Type': 'text/plain',
          }
        }
      );

      if (response.status === 200) {
        setRoomId(props.roomId);
        console.log('props.roomId', props.roomId);
        console.log('state.roomId', roomId);
        
        navigate(`/_multi-game/${props.roomId}`);
      } else {  
        alert('Incorrect password');
      }
    } catch (error) {
      console.error('Error entering the room:', error);
      // 실패 모달창
      setErrorMessage("잘못된 비밀번호입니다!"); 
      openModal();
      // alert('Failed to enter the room');
    }
    handleCloseModal();
  };

  const handleRoomClick = () => {
    if (props.isStart || props.curPlayer === props.maxPlayer) {
      // 방이 이미 시작되었거나, 현재 플레이어 수가 최대 플레이어 수와 같으면 방 입장을 막음
      setErrorMessage("입장할 수 없습니다!"); 
      openModal();
      return;
    }
    handleCloseModal();

    if (props.isLock) {
      setRoomId(props.roomId);
      handleOpenModal();
    } else {
      setRoomId(props.roomId);
      handleOpenSubmit();
    }
  };

  const roomStyle = {
    border: `4px solid ${props.borderColor === 'neon-red' ? '#ff073a' : '#39ff14'}` // 형광 빨간색 또는 형광 초록색
  };

  const warningSign = "/warningSign.png"
  return(
    <>
      <div style={roomStyle} className='room-items' onClick={handleRoomClick}>
        <h3>{props.roomNum}</h3>
        <h3>{props.room}</h3>
        <h4>{props.hostname}</h4>
        <h4>{props.curPlayer} / {props.maxPlayer}</h4>
        <h4>{props.gameRound}</h4>
        <img src={props.isLock ? lock : unlock} alt="lock" />
      </div>
        {isModalOpen && (
          <PasswordModal
            onClose={handleCloseModal}
            onSubmit={handleSubmit}
          />
        )}
        <Modal
          isOpen={modalIsOpen}
          onRequestClose={closeModal}
          contentLabel="Alert Modal"
          className="modal"
          overlayClassName="overlay"
        >
          <div className="modal-container">
            <img className="warning-img" src={warningSign} alt="warning-sign" />
            <h4 className="warning-text">{errorMessage}</h4>
          </div>
        </Modal>
    </>
  )
}

export default MultiRoom;
