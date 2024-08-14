// ChatModal.jsx
import React from 'react';
import S from './styled'; // styled 컴포넌트 파일
import { PiX } from 'react-icons/pi';

export default function ChatModal() {
  // if (!isOpen) return null;

  return <div style={styles.modalOverlay}>채팅창 들갈거임 제발 나와라 ㅜㅜ</div>;
}

const styles = {
  modalOverlay: {
    position: 'absolute',
    width: '400px',
    height: '300px',
    top: '-400px',
    left: '-300px',
    backgroundColor: 'white',
  },
};
