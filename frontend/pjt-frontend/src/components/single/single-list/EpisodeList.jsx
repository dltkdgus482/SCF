import ReactModal from 'react-modal';
import S from './styled';
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import SingleInfoStore from '../../../stores/SingleInfoStore';
import store from '../../../store/store';
import SoundStore from '../../../stores/SoundStore';
import { MdFlag } from 'react-icons/md';
import checkIsCompleted from '../checkIsCompleted';
import { IoIosClose } from 'react-icons/io';

const rowList = [0, 1, 2, 3];

export default function EpisodeList({ rownum }) {
  const navigate = useNavigate();
  const { completed, courses, setCompleted } = SingleInfoStore();
  const { playEffectSound } = SoundStore();
  const [ isModalOpen, setIsModalOpen ] = useState(false);
  // useEffect를 사용하여 컴포넌트가 처음 렌더링될 때 데이터 요청
  const nextIndex = completed?.findIndex((e) => {
    // console.log(e);
    return e.complete === 0;
  });
  const characterSource = [
    '/characters/movingGreenSlime.gif',
    '/characters/movingIceSlime.gif',
    '/characters/movingFireSlime.gif',
    '/characters/movingThunderSlime.gif',
    '/characters/movingNyanSlime.gif',
  ];
  const handleClick = (id) => () => {
    playEffectSound('clickSound');
    if (id > 3) {
      setIsModalOpen(true);
      // console.log(isModalOpen)
      return;
    }
    if (id <= nextIndex + 1) {
      navigate(`/story-play/${id}`);
    } else {
      setIsModalOpen(true);
      // alert("이전 에피소드를 먼저 클리어해주세요.");
    }
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
  };
  return (
    <>
      {rowList.map((r) => (
        <React.Fragment key={r}>
          <S.Row key={2 * r} $rowidx={r}>
            {courses.slice(rownum * r, rownum * r + rownum).map((e, index, array) => (
              <React.Fragment key={e.id}>
                <S.CheckPoint
                  key={`checkpoint-${e.id}`}
                  $completed={e.id < nextIndex + 1}
                  $isNext={e.id === nextIndex + 1}
                  onClick={handleClick(e.id)}
                  onMouseEnter={() => {
                    playEffectSound('hoverSound');
                  }}
                >
                  {e.id}. {e.title}
                  {index === array.length - 1 && r < 3 ? (
                    <>
                      <S.VerticalPath
                        key={`verticalpath-${e.id}`}
                        $completed={completed[3 * (r + 1) - 1].complete}
                      ></S.VerticalPath>
                    </>
                  ) : null}
                  {e.id === nextIndex + 1 ? (
                    <S.CharacterImage>
                      <img src={characterSource[store.registerInfo?.characterType]} />
                      <img src={characterSource[0]} style={{ height: '100px' }} />
                    </S.CharacterImage>
                  ) : null}
                  {e.id === 12 ? (
                    <div style={{ position: 'absolute', top: '-50%', right: '-10px', color: 'red', fontSize: '2rem' }}>
                      <MdFlag />
                    </div>
                  ) : null}
                </S.CheckPoint>
                {index < array.length - 1 ? (
                  <S.Path key={`path-${e.id}`} $completed={completed[e.id]?.complete} />
                ) : null}
              </React.Fragment>
            ))}
          </S.Row>
          {r < rowList.length - 1 && <S.RowBeetween key={2 * r + 1}></S.RowBeetween>}
        </React.Fragment>
      ))}
      <ReactModal isOpen={isModalOpen} onRequestClose={handleCloseModal} style={styles.modal}>
        <div style={styles.modalHeader}>
          <h2>에피소드 입장</h2>
        </div>
        <div onClick={handleCloseModal} style={styles.closeButton}>
          <IoIosClose></IoIosClose>
        </div>
        <hr />
        <div style={styles.modalContent}>
          {/* <p>
            Ep.{modalId}.{' '}
            {
              courses.findIndex((e) => {
                e.id === modalId;
              }).title
            }
          </p> */}
          <p>To Be Continue...</p>
          <div style={styles.modalButtons}>
            <S.CompleteButton onClick={handleCloseModal}>닫기</S.CompleteButton>
          </div>
        </div>
      </ReactModal>
    </>
  );
}
const styles = {
  directionBox: {
    display: 'flex',
    alignItems: 'center',
    magrinBottom: '20px',
  },
  clickIcon: {
    fontSize: '60px',
  },
  chatButton: {
    position: 'fixed',
    right: '10px',
    bottom: '30vh',
    backgroundColor: 'yellow',
    borderRadius: '5px',
    padding: '10px',
  },
  modalHeader: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    height: '30px',
    margin: '0px',
  },
  closeButton: {
    position: 'absolute',
    top: '5px',
    right: '5px',
    fontSize: '30px',
  },
  modalContent: {
    textAlign: 'center',
  },
  modalButtons: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    gap: '10px',
    padding: '10px',
    border: 'none',
  },
  completeButton: {
    padding: '10px 20px',
    backgroundColor: '#76DCFE', // 진한 하늘색
    color: '#233551',
    border: 'none',
    borderRadius: '5px',
  },
  cancelButton: {
    padding: '10px 20px',
    backgroundColor: '#D1E7EF', // 하늘색
    color: '#233551',
    border: 'none',
    borderRadius: '5px',
  },
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
