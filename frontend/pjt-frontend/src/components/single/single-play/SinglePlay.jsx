import React, { useEffect, useRef, useState } from 'react';
import S from './styled';
import { FaRegCommentDots } from 'react-icons/fa';
import { CgProfile } from 'react-icons/cg';
import { PiMouseRightClick, PiMouseLeftClick } from 'react-icons/pi';
import { TypeAnimation } from 'react-type-animation';
import { useParams, useNavigate } from 'react-router-dom';
import SoundStore from '../../../stores/SoundStore.jsx';
import SingleBanner from './SingleBanner.jsx';
import ChatModal from './chat-modal/ChatModal.jsx';
import axios from 'axios';
import store from '../../../store/store';
import Loading from '../../loading/Loading.jsx';
import ReactModal from 'react-modal';
import SingleInfoStore from '../../../stores/SingleInfoStore.jsx';
import Setting from '../../sanghyeon/components/Setting.jsx';
import { IoIosLogOut, IoIosSettings, IoIosClose } from 'react-icons/io';
import { display } from '@mui/system';

const testDialogueList = [
  {
    pageNo: 0,
    scriptContent: '안녕! 나는 너희에게 프로그래밍을 \n 가르쳐줄 고양이 선생님이다냥.',
    action: 0,
    imageCount: 0,
  },
  {
    pageNo: 1,
    scriptContent: 'dummy_data',
    action: 0,
    imageCount: 0,
  },
];

export default function SinglePlay() {
  const [page, setPage] = useState(0);
  const [showDialogue, setShowDialogue] = useState(false);
  const [isChatOpen, setIsChatOpen] = useState(false);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isExitModalOpen, setIsExitModalOpen] = useState(false);
  const [isSettingOpen, setIsSettingOpen] = useState(false);
  const { content_id } = useParams();
  const navigate = useNavigate();
  const [dialogueList, setDialogueList] = useState(testDialogueList);
  const { completed } = SingleInfoStore();
  const { switchBackgroundMusic, playBackgroundMusic, playEffectSound } = SoundStore();
  const { baseURL, accessToken, memberId } = store();
  const [showCharacter, setShowCharacter] = useState(false); //캐릭터 보이기/안보이기 상태 추가
  const [isClickable, setIsClickable] = useState(true);
  //캐릭터 애니메이션 상태 추가
  // const [isVibrating, setIsVibrating] = useState(false);

  const isVibrating = dialogueList?.[page].action === 1;
  const isCloseUp = dialogueList?.[page].action === 2;
  //useEffect 시작

  //타이핑 애니메이션 진행중인지 확인여부 상태 추가
  const [isTyping, setIsTyping] = useState(true);
  const currentPageRef = useRef(page);

  useEffect(() => {
    switchBackgroundMusic('single', (newBackgroundMusic) => {
      newBackgroundMusic.play();
    });
    axios({
      method: 'get',
      url: `${baseURL}/single/${content_id}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        // Authorization: `Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5MCIsImF1dGgiOiJVU0VSIiwibWVtYmVySWQiOjkwLCJ1c2VybmFtZSI6IuqwgOyghOyCrOyXheu2gC3quYDrr7zsmrEiLCJpYXQiOjE3MjMxMDM3NjgsImV4cCI6MTcyMzEwNzM2OH0.Qy2T-1du9CNOfrGZi_1axMkE4jSsmeKKjXR_TdZCqY0`,
      },
    })
      .then((response) => {
        console.log(response.data);
        console.log(content_id);
        console.log(`${baseURL}/single/${content_id}`);
        const data = response.data;
        setDialogueList(data.detailList);
        // 여기서 데이터 로드함!! 로그 확인하고 주석해제 ㄱㄱ
        setLoading(true);
      })
      .catch((error) => {
        console.error('Error fetching content: ', error);
        setLoading(false);
      });

    //로딩화면
    const loadingTimer = setTimeout(() => {
      setLoading(false);
    }, 2000);

    const timer = setTimeout(() => {
      setShowDialogue(true);
    }, 3000);

    return () => {
      switchBackgroundMusic('main', (newBackgroundMusic) => {
        newBackgroundMusic.play();
      });
      clearTimeout(timer);
    };
  }, [content_id]);
  //useEffect 끝

  useEffect(() => {
    setIsTyping(true);
    currentPageRef.current = page;
  }, [page]);

  const { setCompleted } = SingleInfoStore();

  const getSingleInfo = () => {
    axios({
      method: 'get',
      url: `${baseURL}/single`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        // Authorization: `Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5MCIsImF1dGgiOiJVU0VSIiwibWVtYmVySWQiOjkwLCJ1c2VybmFtZSI6IuqwgOyghOyCrOyXheu2gC3quYDrr7zsmrEiLCJpYXQiOjE3MjMwOTczNjgsImV4cCI6MTcyMzEwMDk2OH0.4wZxyxS2RwHVFiZZWq7e3QIV54UZ1_XNdYb0x-92qAQ`,
      },
    })
      .then((res) => {
        console.log(res.data);
        setCompleted(res.data.contentList);
      })
      .catch((error) => {
        console.log('완료목록 로드 실패');
        console.log(error);
      });
  };

  //페이지 변경함수
  const changePage = (increment) => {
    console.log('click');
    if (loading || !showDialogue) return;

    // 모달이 열려있으면 모달을 닫고 함수 종료
    if (isModalOpen) {
      handleCloseModal();
      return;
    }
    // console.log(isModalOpen);
    // 채팅이 열려있으면 페이지 변경을 하지 않고 함수 종료
    // if (isChatOpen) {
    //   // console.log(isChatOpen);
    //   handleChatOpen();
    //   return;
    // }
    // console.log('111');
    // console.log(page);

    //넘어가면 안될때 누르면 바로 return!
    // console.log(isClickable);
    if (isTyping) {
      setIsTyping(false);
      return;
    }
    if (!isClickable) {
      return;
    }
    // 클릭이 불가능하게 만든 후 0.5초 후 클릭이 가능하게 상태 변경
    // 대사 넘어가고나서 0.5초동안 못넘어가게하기
    // 0.5초 후에 한번 더 클릭하면 바꿔
    setIsClickable(false);
    // console.log('false로 바뀜');
    setTimeout(() => {
      setIsClickable(true);
      // console.log('true로 바뀜');
    }, 500);
    if (increment) {
      if (page < dialogueList.length - 1) {
        setPage((prevPage) => prevPage + 1);
      } else {
        setIsModalOpen(true);
      }
    } else {
      if (0 < page) {
        setPage((prevPage) => prevPage - 1);
      } else {
        setPage(0);
      }
    }
    if (dialogueList[page].action == 1) {
      // setIsVibrating(true);
      // console.log('vibrating');
      // setTimeout(() => {
      // setIsVibrating(false);
      // }, 2000);
    }
    setIsTyping(true);
    setIsClickable(true);
    playEffectSound('singleClickSound');
    // console.log(page);
  };

  //채팅 열고 닫기 함수, 이부분은 추후 채팅 추가 시 주석 해제
  // const handleChatOpen = () => {
  //   // console.log('chat handle');
  //   if (isChatOpen) {
  //     setIsChatOpen(false); // 채팅 닫기
  //   } else {
  //     setIsChatOpen(true); // 채팅 열기
  //   }
  // };

  const handleCloseModal = () => {
    setIsModalOpen(false);
  };
  const openExitModal = () => {
    setIsExitModalOpen(true);
  };
  const closeExitModal = () => {
    setIsExitModalOpen(false);
  };
  const checkIsCompleted = () => {
    const target = completed.find((e) => {
      // console.log(e);
      return e.contentId == content_id;
    });
    return target.complete;
  };
  const goToList = (isFinish) => {
    if (isFinish && !checkIsCompleted()) {
      console.log('axios 요청 드갑니다');
      axios({
        method: 'post',
        url: `${baseURL}/single/${content_id}`,
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      })
        .then(getSingleInfo())
        .catch(console.log('요청보내기 실패 ㅜㅜ'))
        .then(navigate('/story-main'));
    } else {
      getSingleInfo();
      navigate('/story-main');
      console.log('비정상적인 나가기 또는 이미 학습한 컨텐츠임');
    }
  };

  const currentDialogue = dialogueList[page] || {};

  const openSettingModal = () => {
    setIsSettingOpen(true);
  };
  const closeSettingModal = () => {
    setIsSettingOpen(false);
  };
  const ChatButton = () => {
    const handleClick = (e) => {
      e.stopPropagation(); // 페이지 이동을 방지
      handleChatOpen();
    };
    if (!isChatOpen) {
      return (
        <div style={styles.chatButton} onClick={handleClick}>
          채팅
        </div>
      );
    }

    return (
      <div style={styles.chatButton} onClick={handleClick}>
        채팅
        <ChatModal />
      </div>
    );
  };

  const renderImages = () => {
    const images = [];
    for (let i = 0; i < currentDialogue.imageCount; i++) {
      images.push(
        <S.ImageContentBox
          key={i}
          style={{
            top: currentDialogue.imageCount === 1 ? '30vh' : `${20 + i * 25}vh`,
            right: currentDialogue.imageCount === 1 ? '20vw' : '30vw',
            width: currentDialogue.imageCount === 1 ? '30vw' : '20vw',
            height: currentDialogue.imageCount === 1 ? '40vh' : '20vh',
          }}
        >
          <img
            src={`/image-content/ep${content_id}/${content_id}-${page}-${i + 1}.png`}
            alt={`content ${i + 1}`}
            style={{
              width: '100%',
              height: '100%',
              objectFit: 'contain',
            }}
          />
        </S.ImageContentBox>
      );
    }
    return images;
  };

  if (loading) {
    return <Loading></Loading>;
  }

  return (
    <>
      <S.PlayView
        onClick={() => {
          changePage(true);
        }}
        onContextMenu={(e) => {
          e.preventDefault();
          changePage(false);
        }}
      >
        <SingleBanner />

        <S.ImageBox>
          {renderImages()}
          <S.CharacterImage
            src="/character-test-removebg-preview.png"
            alt=""
            style={{
              left: currentDialogue.imageCount > 0 ? '10vw' : '30vw',
            }}
            $isVibrating={isVibrating}
            $isCloseUp={isCloseUp}
          />
        </S.ImageBox>
        {showDialogue && (
          <S.DialogueSection>
            {/* <ChatButton></ChatButton> */}
            {/* 채팅 버튼 비활성화 */}
            <S.DialogueBox>
              <S.DialogueHeader>
                <S.CharacterName>
                  <span>
                    <CgProfile />
                    <span> 야옹 선생</span>
                  </span>
                </S.CharacterName>
              </S.DialogueHeader>

              <S.DialogueBody>
                <S.DialogueBodyLeft>
                  <p>
                    <FaRegCommentDots />
                  </p>
                </S.DialogueBodyLeft>
                <S.DialogueContent>
                  {isTyping ? (
                    <TypeAnimation
                      key={page}
                      style={{
                        color: 'black',
                        whiteSpace: 'pre-line',
                      }}
                      sequence={[
                        currentDialogue.scriptContent,
                        500,
                        () => {
                          if (currentPageRef.current === page) {
                            setIsTyping(false);
                          }
                        },
                      ]}
                      wrapper="p"
                      speed={50}
                    />
                  ) : (
                    <p
                      style={{
                        color: 'black',
                        whiteSpace: 'pre-line',
                      }}
                    >
                      {currentDialogue.scriptContent}
                    </p>
                  )}
                </S.DialogueContent>
              </S.DialogueBody>
              <S.DialogueBodyRight>
                <div style={styles.directionBox}>
                  <span style={styles.clickIcon}>
                    <PiMouseLeftClick />
                  </span>
                  클릭 : 다음
                </div>
                <div style={styles.directionBox}>
                  <span style={styles.clickIcon}>
                    <PiMouseRightClick />
                  </span>
                  우클릭 : 이전
                </div>
              </S.DialogueBodyRight>
            </S.DialogueBox>
          </S.DialogueSection>
        )}
      </S.PlayView>
      <ReactModal isOpen={isModalOpen} onRequestClose={handleCloseModal} style={styles.modal}>
        <div style={styles.modalHeader}>
          <h2>학습 완료</h2>
        </div>
        <div onClick={handleCloseModal} style={styles.closeButton}>
          <IoIosClose></IoIosClose>
        </div>
        <hr />
        <div style={styles.modalContent}>
          <h2>학습을 완료하시겠습니까?</h2>
          <div style={styles.modalButtons}>
            <S.CompleteButton onClick={() => goToList(true)}>학습 완료</S.CompleteButton>
            <S.CancelButton onClick={handleCloseModal}>돌아가기</S.CancelButton>
          </div>
        </div>
      </ReactModal>
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
            <S.CompleteButton onClick={() => goToList(false)}>나가기</S.CompleteButton>
            <S.CancelButton onClick={closeExitModal}>돌아가기</S.CancelButton>
          </div>
        </div>
      </ReactModal>
      <S.MenuContainer>
        <S.MenuButton onClick={openSettingModal}>
          <IoIosSettings />
        </S.MenuButton>
        <S.MenuButton onClick={openExitModal}>
          <IoIosLogOut />
        </S.MenuButton>
      </S.MenuContainer>
      <Setting isOpen={isSettingOpen} onClose={closeSettingModal}></Setting>
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
