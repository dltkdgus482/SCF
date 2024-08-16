import styled from 'styled-components';
import { IoIosClose } from 'react-icons/io';
import React, { useEffect } from 'react';
import ReactModal from 'react-modal';
import PropTypes from 'prop-types';

const CustomAlert = ({ title, text, timer = null, isModalOpen, closeModal, onRequestOkay }) => {
  useEffect(() => {
    if (timer) {
      const timeout = setTimeout(() => {
        closeModal();
      }, timer);
      return () => clearTimeout(timeout);
    }
  }, [timer, closeModal]);

  return (
    <ReactModal isOpen={isModalOpen} onRequestClose={closeModal} style={styles.modal} ariaHideApp={false}>
      <ModalHeader>
        <h2>{title}</h2>
        <CloseButton onClick={closeModal}>
          <IoIosClose />
        </CloseButton>
      </ModalHeader>
      <hr/>
      <ModalContent>
        <p>{text}</p>
        <ModalButtons>
          <CompleteButton onClick={onRequestOkay}>Okay</CompleteButton>
          <CancelButton onClick={closeModal}>Cancel</CancelButton>
        </ModalButtons>
      </ModalContent>
    </ReactModal>
  );
};

const styles = {
  modal: {
    content: {
      width: '500px',
      height: 'auto',
      position: 'absolute',
      top: '50%',
      left: '50%',
      right: 'auto',
      bottom: 'auto',
      transform: 'translate(-50%, -50%)',
      backgroundColor: 'white',
      padding: '10px',
      borderRadius: '10px',
    },
    overlay: {
      position: 'fixed',
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
      backgroundColor: 'rgba(0, 0, 0, 0.75)',
      zIndex: 1000,
    },
  },
};

const ModalHeader = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 30px;
  margin: 0px;
`;

const CloseButton = styled.div`
  cursor: pointer;
  position : absolute;
  right : 3px;
  top : 3px;
`;

const ModalContent = styled.div`
  text-align: center;
`;

const ModalButtons = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  padding: 10px;
  border: none;
`;

const CompleteButton = styled.button`
  margin-right: 1rem;
  padding: 10px 20px;
  background-color: #76dcfe;
  color: #233551;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  &:hover {
    background-color: #466398;
    color: #e4f0f6;
  }
`;

const CancelButton = styled.button`
  padding: 10px 20px;
  background-color: #d1e7ef;
  color: #233551;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  &:hover {
    background-color: #466398;
    color: #e4f0f6;
  }
`;

CustomAlert.propTypes = {
  title: PropTypes.string.isRequired,
  text: PropTypes.string.isRequired,
  timer: PropTypes.number,
  isModalOpen: PropTypes.bool.isRequired,
  closeModal: PropTypes.func.isRequired,
};

export default CustomAlert;
