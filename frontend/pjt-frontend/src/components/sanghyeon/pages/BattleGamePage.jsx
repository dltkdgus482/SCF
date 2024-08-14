import BattleGameHeader from "./BattleGameHeader.jsx";
import "../../../css/BattleGamePage.css";
import store from "../../../store/store.js";
import { useEffect, useState, useRef } from "react";
import { useNavigate } from "react-router";
import createAuthClient from "../apis/createAuthClient.js";
import renderCharacter from "../apis/renderCharacter.js";

import SockJS from "sockjs-client/dist/sockjs";
import Stomp from "stompjs";
import Swal from "sweetalert2";

import DragNDropQuiz from "../../game/quiz_with_blank/DragNDropQuiz.jsx";
import ShortAnswer from "../../game/short_answer/ShortAnswer.jsx";
import MultipleChoice from "../../game/multipleChoice/MultipleChoice.jsx";

import Modal from "react-modal";

import SoundStore from "../../../stores/SoundStore.jsx";
import { MdBloodtype } from "react-icons/md";

const TempPage1 = () => {
  const healEffect = "/heal-effect.gif";

  const delay = (ms) => new Promise((resolve) => setTimeout(resolve, ms));
  const { switchBackgroundMusic, playBackgroundMusic, playEffectSound } =
    SoundStore();
  useEffect(() => {
    switchBackgroundMusic("multi", (newBackgroundMusic) => {
      newBackgroundMusic.play();
    });
    return () => {
      switchBackgroundMusic("main", (newBackgroundMusic) => {
        newBackgroundMusic.play();
      });
    };
  }, []);

  // --------------------------페이지에서 나가면 다시 음악바뀝니다.-----------------------//
  const battleStompClient = useRef(null);
  const chatStompClient = useRef(null);
  const navigate = useNavigate();

  const {
    memberId,
    accessToken,
    setAccessToken,
    hostId,
    userId,
    name,
    roomId,
    roomPassword,
    baseURL,
    wsBattle,
    wsChat,
    enemyId,
    setEnemyId,
    enemyName,
    setEnemyName,
    enemyCharacterType,
    setEnemyCharacterType,
    normalQuit,
    setNormalQuit,
    blankSolve,
    setMyBlankProblem,
    shortAnswerSolve,
    setMyShortAnswerProblem,
    multipleChoiceSolve,
    setMyMultipleChoiceProblem,
    setRoomId,
    character,
  } = store((state) => ({
    memberId: state.memberId,
    accessToken: state.accessToken,
    setAccessToken: state.setAccessToken,
    hostId: state.hostId,
    userId: state.userId,
    name: state.name,
    character: state.character,
    roomId: state.roomId,
    setRoomId: state.setRoomId,
    roomPassword: state.roomPassword,
    baseURL: state.baseURL,
    wsBattle: state.wsBattle,
    wsChat: state.wsChat,
    enemyId: state.enemyId,
    setEnemyId: state.setEnemyId,
    enemyName: state.enemyName,
    setEnemyName: state.setEnemyName,
    enemyCharacterType: state.enemyCharacterType,
    setEnemyCharacterType: state.setEnemyCharacterType,
    normalQuit: state.normalQuit,
    setNormalQuit: state.setNormalQuit,
    blankSolve: state.blankSolve,
    setMyBlankProblem: state.setMyBlankProblem,
    shortAnswerSolve: state.shortAnswerSolve,
    setMyShortAnswerProblem: state.setMyShortAnswerProblem,
    multipleChoiceSolve: state.multipleChoiceSolve,
    setMyMultipleChoiceProblem: state.setMyMultipleChoiceProblem,
  }));

  const authClient = createAuthClient(
    baseURL,
    () => accessToken,
    setAccessToken
  );

  const [chatMessages, setChatMessages] = useState([]);
  const [battleHistory, setBattleHistory] = useState([]);
  const [message, setMessage] = useState("");
  const chatEndRef = useRef(null);
  const battleHistoryEndRef = useRef(null);
  const [myHealth, setMyHealth] = useState(100);
  const [enemyHealth, setEnemyHealth] = useState(100);

  // ---------------------- 배틀 문제 선택 모달 ----------------------

  const [EnemyProblems, setEnemyProblems] = useState([]); // 여기
  const [gameStart, setGameStart] = useState(false); // 여기
  const [modalIsOpen, setModalIsOpen] = useState(false); // 여기

  // ---------------------- 배틀 문제 선택 모달 ----------------------

  const [currentRound, setCurrentRound] = useState(0);
  const [count, setCount] = useState(30);
  const [myProblem, setMyProblem] = useState({});
  const [selectMyProblem, setSelectMyProblem] = useState(false); // 상대가 내 문제를 선택했는지
  const [selectOpponentProblem, setSelectOpponentProblem] = useState(false); // 내가 상대방의 문제를 선택했는지
  const [gameEnded, setGameEnded] = useState(false);
  const [winner, setWinner] = useState("");
  const [loser, setLoser] = useState("");
  const [count2, setCount2] = useState(5);
  const [answerSubmitted, setAnswerSubmitted] = useState(false);
  const timerRef = useRef(null);

  const [item1, setItem1] = useState(false); // 온전한 공격: 100% 데미지
  const [item2, setItem2] = useState(false); // 시간 연장: +10초
  const [item3, setItem3] = useState(false); // 시간 단축: -10초
  const [item4, setItem4] = useState(false); // 답안 제출 방지: submitAnswer 버튼 비활성화

  const [blink, setBlink] = useState(false);
  const [blinkEnemy, setBlinkEnemy] = useState(false);

  const [isLeftCamBlinking, setIsLeftCamBlinking] = useState(false);
  const [isRightCamBlinking, setIsRightCamBlinking] = useState(false);

  const [isHealing, setIsHealing] = useState(false);
  const [isEnemyHealing, setIsEnemyHealing] = useState(false);

  // ---------------------- WebSocket ----------------------

  // WebSocket 연결 및 초기화 함수

  const connect = () => {
    return new Promise((resolve, reject) => {
      const battleSocket = new SockJS(`${baseURL}/ws-battle`);
      const chatSocket = new SockJS(`${baseURL}/ws-chat`);
      battleStompClient.current = Stomp.over(battleSocket);
      chatStompClient.current = Stomp.over(chatSocket);

      battleStompClient.current.debug = null;
      chatStompClient.current.debug = null;

      let battleConnected = false;
      let chatConnected = false;

      // 배틀 서버 연결
      battleStompClient.current.connect(
        {},
        (frame) => {
          subscribeEnterRoom();
          subscribeEnemyProblem();
          subscribeMyProblem();
          subscribeResult();

          battleConnected = true;
          if (battleConnected && chatConnected) {
            resolve(); // 두 서버가 모두 연결되면 Promise를 해결합니다.
          }
        },
        (error) => {
          console.log("Battle server connection error:", error);
          reject(error); // 연결 실패 시 Promise 거부
        }
      );

      // 채팅 서버 연결
      chatStompClient.current.connect(
        {},
        (frame) => {
          subscribeMessage();

          chatConnected = true;
          if (battleConnected && chatConnected) {
            resolve(); // 두 서버가 모두 연결되면 Promise를 해결합니다.
          }
        },
        (error) => {
          console.log("Chat server connection error:", error);
          reject(error); // 연결 실패 시 Promise 거부
        }
      );
    });
  };

  const enterRoom = () => {
    const joinRoomDTO = {
      userId: memberId,
      username: name,
      roomPassword: roomPassword,
      guestCharacterType: character,
    };
    battleStompClient.current.send(
      `/room/${roomId}/join`,
      {},
      JSON.stringify(joinRoomDTO)
    );
  };

  const subscribeEnterRoom = () => {
    const endpoint = `/room/${roomId}/join`;
    battleStompClient.current.subscribe(endpoint, (message) => {
      const body = JSON.parse(message.body);

      console.log(message);

      if (body.userId !== memberId && body.guestCharacterType) {
        setEnemyCharacterType(body.guestCharacterType);
      }

      // setChatMessages((prevMessages) => [
      //   ...prevMessages,
      //   `${body.username}님이 입장하셨습니다.`,
      // ]);
    });
  };

  const subscribeEnemyProblem = () => {
    const endpoint = `/room/${roomId}/RoundChoiceProblem`;
    battleStompClient.current.subscribe(endpoint, async (message) => {
      const body = JSON.parse(message.body);
      await delay(3000);
      setItem1(false);
      setItem2(false);
      setItem3(false);
      setItem4(false);
      setEnemyProblems(body);
      setAnswerSubmitted(false);
      openModal();
      setCurrentRound((prevRound) => prevRound + 1);

      if (body && body.item && body.item.name === "시간 연장") {
        setItem2(true);
        setCount(40);
      } else if (body && body.item && body.item.name === "시간 단축") {
        setItem3(true);
        setCount(20);
      } else {
        setCount(30);
      }

      if (body && body.item && body.item.name === "온전한 공격") {
        setItem1(true);
      }
    });
  };

  const selectEnemyProblem = (problemId) => {
    const endpoint = `/send/game/${roomId}/selectProblem`;
    const payload = {
      problemId: problemId,
      memberId: memberId, // payload에 memberId 추가
    };

    battleStompClient.current.send(endpoint, {}, JSON.stringify(payload));
    setSelectOpponentProblem(true);
  };

  const subscribeMyProblem = () => {
    const endpoint = `/room/${roomId}/${memberId}`;
    battleStompClient.current.subscribe(endpoint, (message) => {
      setMyProblem(JSON.parse(message.body));

      const responseProblem = JSON.parse(message.body);

      switch (responseProblem.problemType) {
        case "FILL_IN_THE_BLANK":
          setMyBlankProblem(responseProblem);
          break;
        case "SHORT_ANSWER_QUESTION":
          setMyShortAnswerProblem(responseProblem);
          break;
        case "MULTIPLE_CHOICE":
          setMyMultipleChoiceProblem(responseProblem);
          break;
        default:
          Swal.fire({
            text: "문제 타입을 알 수 없습니다.",
            icon: "error",
            timer: 3000,
          });
          console.log("Unknown problem type");
      }
      setSelectMyProblem(true);
    });
  };

  useEffect(() => {
    if (selectOpponentProblem && selectMyProblem) {
      Swal.fire({
        text: "3초 후 라운드가 시작됩니다!",
        icon: "warning",
        showConfirmButton: false,
        timer: 3000,
      });

      setTimeout(() => {
        if (item2) {
          setCount(40);
        } else if (item3) {
          setCount(20);
        } else {
          setCount(30);
        }
      }, 3000);

      const timer = setTimeout(async () => {
        await closeModal();
        await stopTimer();
        await setSelectMyProblem(false);
        await setSelectOpponentProblem(false);
        await setGameStart(true);
        await setAnswerSubmitted(false);

        if (item2) {
          await setCount(40);
        } else if (item3) {
          await setCount(20);
        }

        await startTimer();
      }, 3000);
    }
  }, [selectOpponentProblem, selectMyProblem]);

  const submitAnswer = () => {
    const endpoint = `/send/game/${roomId}/answer`;
    let solveData = null;
    let solveText = null;

    switch (myProblem.problemType) {
      case "FILL_IN_THE_BLANK":
        solveData = blankSolve;
        solveText = null;
        break;
      case "SHORT_ANSWER_QUESTION":
        solveData = null;
        solveText = shortAnswerSolve === "" ? "ssafy" : shortAnswerSolve;
        break;
      case "MULTIPLE_CHOICE":
        solveData = multipleChoiceSolve ? { 1: multipleChoiceSolve } : { 1: 0 };
        solveText = null;
        break;
      default:
        Swal.fire({
          text: "문제 타입을 알 수 없습니다.",
          icon: "error",
          timer: 3000,
        });
        console.log("Unknown problem type");
        break;
    }

    const submitAnswerDTO = {
      problemType: myProblem.problemType,
      problemId: myProblem.problemId,
      userId: memberId,
      solve: solveData,
      submitTime: item1
        ? 30
        : item2
        ? 40 - count > 30
          ? 30
          : 40 - count
        : 30 - count,
      solveText: solveText,
      round: currentRound - 1,
    };
    battleStompClient.current.send(
      endpoint,
      {},
      JSON.stringify(submitAnswerDTO)
    );
    setAnswerSubmitted(true);
    Swal.fire({
      text: "답안을 제출하셨습니다.",
      icon: "success",
      timer: 3000,
    });
  };

  const subscribeResult = () => {
    const endpoint = `/room/${roomId}`;
    battleStompClient.current.subscribe(endpoint, (message) => {
      const body = JSON.parse(message.body);

      if (body.result && typeof body.result === "object") {
        setWinner(body.result.winner);
        setLoser(body.result.loser);
        setGameEnded(true);
        setGameStart(false);
        openModal();
        setTimeout(() => {
          Swal.fire({
            text: "게임이 종료되었습니다.",
            icon: "success",
            timer: 3000,
          }).then(() => {
            setTimeout(async () => {
              await initBattleGame();
              setSelectOpponentProblem(false);
            }, 3000);
          });
        }, 3000);
      } else {
        if (body.power === 0) {
          setBattleHistory((prevHistory) => [...prevHistory, body]);
        } else {
          if (body.userId === memberId) {
            if (body.isAttack === true) {
              setBlinkEnemy(true);
              setIsRightCamBlinking(true);
              setTimeout(() => {
                setBlinkEnemy(false);
                setIsRightCamBlinking(false);
                setEnemyHealth((prevHealth) => prevHealth - body.power);
                setBattleHistory((prevHistory) => [...prevHistory, body]);
              }, 1000);
            } else {
              setIsHealing(true);
              setBattleHistory((prevHistory) => [...prevHistory, body]);
              setTimeout(() => {
                setMyHealth((prevHealth) =>
                  prevHealth + body.power > 100 ? 100 : prevHealth + body.power
                );
              }, 1000);
              setTimeout(() => {
                setIsHealing(false);
              }, 2000);
            }
          } else {
            if (body.isAttack === true) {
              setBlink(true);
              setIsLeftCamBlinking(true);
              setTimeout(() => {
                setBlink(false);
                setIsLeftCamBlinking(false);
                setMyHealth((prevHealth) => prevHealth - body.power);
                setBattleHistory((prevHistory) => [...prevHistory, body]);
              }, 1000);
            } else {
              setIsEnemyHealing(true);
              setBattleHistory((prevHistory) => [...prevHistory, body]);
              setTimeout(() => {
                setEnemyHealth((prevHealth) =>
                  prevHealth + body.power > 100 ? 100 : prevHealth + body.power
                );
              }, 1000);
              setTimeout(() => {
                setIsEnemyHealing(false);
              }, 2000);
            }
          }
        }
      }
    });
  };

  const enterChat = () => {
    const endpoint = `/send/chat/${roomId}/enter`;
    const enterDTO = {
      sender: name,
      content: `${name}님이 입장하셨습니다.`,
      type: "JOIN",
      roomId: roomId,
    };
    chatStompClient.current.send(endpoint, {}, JSON.stringify(enterDTO));
  };

  const sendMessage = async () => {
    if (message.trim() === "") return;
    const endpoint = `/send/chat/${roomId}`;
    const chatMessage = {
      sender: name,
      content: message,
      type: "CHAT",
      roomId: roomId,
    };
    chatStompClient.current.send(endpoint, {}, JSON.stringify(chatMessage));
    setMessage("");
  };

  const subscribeMessage = () => {
    const endpoint = `/room/${roomId}`;
    chatStompClient.current.subscribe(endpoint, (message) => {
      const body = JSON.parse(message.body);

      if (body.type === "JOIN" && body.sender !== name) {
        setEnemyName(body.sender);
      }

      if (body.type === "LEAVE") {
        setEnemyName("");
        setEnemyCharacterType("");
      }

      setChatMessages((prevMessages) => [
        ...prevMessages,
        body.type === "CHAT"
          ? `${body.sender}: ${body.content}`
          : `${body.content}`,
      ]);
    });
  };

  const sendQuitMessage = () => {
    const endpoint = `/send/chat/${roomId}/leave`;
    const chatMessage = {
      sender: name,
      content: `${name}님이 퇴장하셨습니다.`,
      type: "LEAVE",
      roomId: roomId,
    };
    chatStompClient.current.send(endpoint, {}, JSON.stringify(chatMessage));
  };

  const quitBattleRoom = async () => {
    try {
      const quitRes = await authClient({
        method: "POST",
        url: `${baseURL}/battle/room/${roomId}/leave`,
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });
      setNormalQuit(true);
      navigate("/_battle-list");
    } catch (error) {
      navigate("/_battle-list");
      console.log(error);
    }
  };

  const reconnectWebSocket = () => {
    setTimeout(async () => {
      try {
        await connect();
        await enterRoom();
        await enterChat();
      } catch (error) {
        console.log("Reconnection failed: ", error);
        reconnectWebSocket(); // 재연결 시도
      }
    }, 2000); // 2초 후 재연결 시도
  };

  useEffect(() => {
    const initializeConnections = async () => {
      try {
        await connect();
        await enterRoom();
        await enterChat();
      } catch (error) {
        console.log("Connection error:", error);
        reconnectWebSocket();
      }
    };

    initializeConnections();

    // 클린업 함수는 비동기 함수로 직접 정의할 수 없으므로
    // 비동기 작업을 수행할 내부 함수를 정의하고 호출합니다.
    const cleanup = async () => {
      await delay(5000);

      sendQuitMessage();
      // quitBattleRoom();
      if (battleStompClient.current) battleStompClient.current.disconnect();
      if (chatStompClient.current) chatStompClient.current.disconnect();
    };

    // return 내부에서 cleanup 함수를 호출
    return () => {
      cleanup();
    };
  }, []);

  // ---------------------- WebSocket ----------------------

  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [chatMessages]);

  useEffect(() => {
    battleHistoryEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [battleHistory]);

  useEffect(() => {
    if (count === 0 && answerSubmitted === false) {
      submitAnswer();
    }
  }, [count]);

  const openModal = () => {
    setModalIsOpen(true);
  };

  const closeModal = (type = "default") => {
    if (selectOpponentProblem === true || type === "clear") {
      setModalIsOpen(false);
    }
    return;
  };

  const handleStart = async () => {
    try {
      const handleStartRes = await authClient({
        method: "POST",
        url: `${baseURL}/battle/room/${roomId}/start`,
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });
    } catch (error) {
      console.log(error);
    }
  };

  const handleSubmit = () => {
    if (item4) {
      Swal.fire({
        text: "답안 제출 방지 아이템으로 인해 답안 제출이 불가능합니다.",
        icon: "warning",
        timer: 3000,
      });
      return;
    }

    if (answerSubmitted === true) {
      Swal.fire({
        text: "이미 답안을 제출하셨습니다.",
        icon: "warning",
        timer: 3000,
      });
    } else {
      submitAnswer();
    }
  };

  const startTimer = async () => {
    if (timerRef.current) {
      clearInterval(timerRef.current);
    }

    timerRef.current = setInterval(() => {
      setCount((prevCount) => {
        if (prevCount <= 1) {
          clearInterval(timerRef.current);
          return 0;
        }
        return prevCount - 1;
      });
    }, 1000);
  };

  const stopTimer = () => {
    if (timerRef.current) {
      clearInterval(timerRef.current);
      timerRef.current = null;
    }
  };

  const renderQuestion = (problem) => {
    switch (problem.problemType) {
      case "FILL_IN_THE_BLANK":
        return <DragNDropQuiz />;
      case "SHORT_ANSWER_QUESTION":
        return <ShortAnswer />;
      case "MULTIPLE_CHOICE":
        return <MultipleChoice />;
      default:
        return <div>Unknown problem type</div>;
    }
  };

  const renderProblemType = (problemType) => {
    switch (problemType) {
      case "FILL_IN_THE_BLANK":
        return "빈 칸 채우기";
      case "SHORT_ANSWER_QUESTION":
        return "단답형";
      case "MULTIPLE_CHOICE":
        return "객관식";
    }
  };

  const initBattleGame = () => {
    closeModal("clear");
    stopTimer();
    setCount(30);
    setSelectMyProblem(false);
    setSelectOpponentProblem(false);
    setNormalQuit(false);
    setBattleHistory([]);
    setEnemyProblems([]);
    setMyHealth(100);
    setEnemyHealth(100);
    setCurrentRound(0);
    setMyProblem({});
    setGameEnded(false);
    setWinner("");
    setLoser("");
    setCount2(5);
    setAnswerSubmitted(false);
  };

  return (
    <>
      <div className="battle-game-entire-container">
        <Modal
          isOpen={modalIsOpen}
          onRequestClose={closeModal}
          contentLabel="Example Modal"
          className="battle-game-select-problem-modal"
          overlayClassName="overlay"
        >
          {gameEnded ? (
            <div className="battle-game-result-container">
              <div className="battle-game-result-title">
                <div className="battle-game-result-title-container">
                  게임 결과
                </div>
              </div>
              <div className="battle-game-result-content">
                {winner === -1
                  ? "무승부입니다."
                  : winner === memberId
                  ? "승리하셨습니다."
                  : "패배하셨습니다."}
              </div>
              <div className="battle-game-result-footer">
                {count2}초 후 대기방으로 이동합니다.
              </div>
            </div>
          ) : selectOpponentProblem ? (
            <div className="battle-game-select-problem-title">
              상대방이 문제를 선택하는 중입니다.
            </div>
          ) : (
            <>
              <div className="battle-game-select-problem-title">
                상대방이 풀 문제를 선택 해주세요.
              </div>
              <div className="battle-game-select-problem-container">
                {Array.isArray(EnemyProblems) &&
                  EnemyProblems.map((data, index) => (
                    <div
                      className="battle-game-select-problem"
                      onClick={() => selectEnemyProblem(data.problemId)}
                      key={index}
                    >
                      <div className="battle-game-select-problem-sub-title">
                        {data.title}
                      </div>
                      <hr />
                      <div className="battle-game-select-problem-type">
                        문제 유형: {renderProblemType(data.problemType)}
                      </div>
                      <div className="battle-game-select-problem-category">
                        카테고리: {data.category}
                      </div>
                      <div className="battle-game-select-problem-difficulty">
                        난이도: {data.difficulty}
                      </div>
                    </div>
                  ))}
              </div>
            </>
          )}
        </Modal>
        <BattleGameHeader />
        <div className="battle-game-outer-outer-container">
          <div className="battle-game-outer-container">
            <div className="battle-game-title-container">
              <div className="health-bar">
                <div
                  className="health-bar-inner"
                  style={{ width: `${myHealth}%` }}
                ></div>
              </div>
              <h2 className="battle-game-title">Round {currentRound}</h2>
              <div className="health-bar">
                <div
                  className="health-bar-inner"
                  style={{ width: `${enemyHealth}%` }}
                ></div>
              </div>
            </div>
            <div className="battle-game-sub-title-container">
              <div className="battle-game-sub-title-player">{name}</div>
              <div className="battle-game-sub-title-timer">{count}</div>
              <div className="battle-game-sub-title-player">{enemyName}</div>
            </div>
            <div className="battle-game-container">
              <div className="battle-game-left-container">
                <div
                  className={`battle-game-left-cam ${
                    isLeftCamBlinking ? "left-cam-blink" : ""
                  }`}
                >
                  <div className="battle-game-my-character-container">
                    <img
                      className={`${blink ? "blink" : ""}`}
                      src={renderCharacter(character)}
                      alt="battle-game-my-character"
                    />
                    {isHealing && (
                      <img
                        src={healEffect}
                        alt="Healing Effect"
                        className={`heal-effect ${isHealing ? "active" : ""}`}
                      />
                    )}
                  </div>
                </div>
                <div className="battle-game-history-container">
                  <div className="battle-game-history">
                    <div className="battle-game-history-title">전투 기록</div>
                    {battleHistory.map((data, index) => (
                      <div className="battle-game-history-message" key={index}>
                        {data.power === 0
                          ? data.userId === memberId
                            ? `${name}님이 문제를 틀리셨습니다.`
                            : `${enemyName}님이 문제를 틀리셨습니다.`
                          : data.isAttack === true
                          ? `${
                              data.userId === memberId ? name : enemyName
                            }님이 ${
                              data.userId === memberId ? enemyName : name
                            }님에게 ${data.power}만큼 데미지를 주었습니다.`
                          : `${
                              data.userId === memberId ? name : enemyName
                            }님이 ${data.power}만큼 체력을 회복하였습니다.`}
                      </div>
                    ))}
                    <div ref={battleHistoryEndRef} />
                  </div>
                </div>
              </div>
              <div className="battle-game-inner-container">
                {gameStart ? (
                  <>
                    {renderQuestion(myProblem)}
                    <div className="battle-game-submit-answer-container">
                      <button
                        onClick={handleSubmit}
                        className="battle-game-submit-answer"
                      >
                        답안 제출
                      </button>
                    </div>
                  </>
                ) : (
                  <div className="battle-game-game-start-container">
                    <h2 className="battle-game-game-start-title">
                      게임 시작 전입니다.
                    </h2>
                    {hostId === memberId ? (
                      <button
                        onClick={handleStart}
                        className="battle-game-game-start-button"
                      >
                        게임 시작
                      </button>
                    ) : (
                      <div className="battle-game-game-start-waiting">
                        대기 중...
                      </div>
                    )}
                  </div>
                )}
              </div>
              <div className="battle-game-right-container">
                <div
                  className={`battle-game-right-cam ${
                    isRightCamBlinking ? "right-cam-blink" : ""
                  }`}
                >
                  <div className="battle-game-enemy-character-container">
                    {enemyCharacterType === null ||
                    enemyCharacterType === "" ? (
                      <></>
                    ) : (
                      <>
                        <img
                          className={`${blinkEnemy ? "enemy-blink" : ""}`}
                          src={renderCharacter(enemyCharacterType)}
                          alt="battle-game-enemy-character"
                        />
                      </>
                    )}
                    {isEnemyHealing && (
                      <img
                        src={healEffect}
                        alt="Healing Effect"
                        className={`enemy-heal-effect ${
                          isEnemyHealing ? "active" : ""
                        }`}
                      />
                    )}
                  </div>
                </div>
                <div className="battle-game-chatting-container">
                  <div className="battle-game-chatting">
                    {chatMessages.map((msg, index) => (
                      <div className="battle-game-chatting-message" key={index}>
                        {msg}
                      </div>
                    ))}
                    <div ref={chatEndRef} />
                  </div>
                </div>
                <div className="battle-game-chat-input">
                  <input
                    type="text"
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    placeholder="메시지를 입력하세요"
                    onKeyUp={(e) => {
                      if (e.key === "Enter" && message.trim() !== "") {
                        e.preventDefault();
                        sendMessage();
                      }
                    }}
                  />
                  <button
                    type="button"
                    onClick={() => {
                      if (message.trim() !== "") {
                        sendMessage();
                      }
                    }}
                  >
                    전송
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default TempPage1;
