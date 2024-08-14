import ReactModal from 'react-modal';
import { IoIosClose } from 'react-icons/io';
import styled from 'styled-components';


const SingleModal = () => {
  return (
    <ReactModal isOpen={isExitModalOpen} onRequestClose={closeExitModal} style={styles.modal}>
      <div style={styles.modalHeader}>
        <h2>나가기</h2>
      </div>
      <div onClick={closeExitModal} style={styles.closeButton}>
        <IoIosClose></IoIosClose>
      </div>
      <hr />
      <div style={styles.modalContent}>
        <p>목록으로 돌아가시겠습니까?</p>
        <p>미완료된 진행사항은 저장되지 않습니다.</p>
        <div style={styles.modalButtons}>
          <CompleteButton onClick={() => goToList(false)}>나가기</CompleteButton>
          <CancelButton onClick={closeExitModal}>돌아가기</CancelButton>
        </div>
      </div>
    </ReactModal>
  );
};
const styles = {

}

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
