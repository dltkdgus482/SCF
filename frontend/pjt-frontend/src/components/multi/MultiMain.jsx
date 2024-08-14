import "../../index.css";
import Header from"../sanghyeon/components/Header.jsx";
import "../../css/GameMain.css";
import '../../css/Container.css';
import MultiRoom from "../game/MultiRoom.jsx";
import axios from "axios";
import { useState, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import store from '../../store/store.js';
import createAuthClient from "../sanghyeon/apis/createAuthClient.js";
import SoundStore from "../../stores/SoundStore.jsx";

export default function MultiMain() {
  const navigate = useNavigate();
  const [multiList, setMultiList] = useState([]);
  const [currentMultiList, setCurrentMultiList] = useState([]);
  const searchKeyword = useRef(null);

  const multiListSearch = (searchKeyword) => {
    setMultiList(
      multiList.filter((data) =>
        data.title.includes(searchKeyword.current.value)
      )
    );
  };

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

  const loadData = async () => {
    try {
      const response = await axios.get(`${baseURL}/multi/room`, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        }
      });
      if (Array.isArray(response.data)) {
        setMultiList(response.data);
        setCurrentMultiList(response.data);
      } else {
        console.error('배열을 기대했으나 다음과 같은 데이터를 받았습니다:', response.data);
        setMultiList([]);
      }
    } catch (error) {
      console.error('데이터를 가져오는 중 오류 발생:', error);
      setMultiList([]);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const refreshPage = () => {
    loadData();
  }

  return (
    <>
      <Header />
      {/* <div className="container"> */}
      <div id="container">
        <div className="main-container">
          <div className="title-container">
            <h1>Multi Game</h1>
          </div>
          <div className="sub-container">
            <div className="list-container">
              <div className="game-list-header">
                <div className="game-room-index">
                  <p>No.</p>
                  <p>제목</p>
                  <p>호스트</p>
                  <p>인원</p>
                  <p>라운드</p>
                  <p>공개여부</p>
                </div>
                <div className="white-line"></div>
              </div>
              <div className="game-list">
                <div>
                  { multiList.length > 0 ? (
                    multiList.map((room, i) => {
                      if (room.curPlayer === 0) {
                        return null;
                      }
                      
                      const borderColor = room.isStart || room.curPlayer === room.maxPlayer ? 'neon-red' : 'neon-green';
                      
                      return (
                        <MultiRoom 
                          roomNum={i + 1} 
                          room={room.title} 
                          hostname={room.hostname} 
                          maxPlayer={room.maxPlayer} 
                          curPlayer={room.curPlayer} 
                          gameRound={room.gameRound} 
                          isLock={room.isLock} 
                          roomId={room.roomId}
                          isStart={room.isStart}
                          key={i}
                          borderColor={borderColor} // 테두리 색상을 prop으로 전달
                        />
                      );
                      
                    })                    
                    // multiList.map((room, i) => {
                    //   return <MultiRoom roomNum={i+1} room={room.title} hostname={room.hostname} maxPlayer={room.maxPlayer} curPlayer={room.curPlayer} gameRound={room.gameRound} isLock={room.isLock} roomId={room.roomId} key={i} />
                    // })
                  ) : (
                    <div className="empty-room">
                      <h2>검색결과가 없습니다.</h2>
                    </div>
                  )}
                </div>
              </div>
            </div>
            <div className="side-container">
              <div className="multi-list-search-container">
                <input
                  ref={searchKeyword}
                  type="text"
                  placeholder="검색어 입력"
                  onKeyDown={(e) =>
                    e.key === "Enter" && multiListSearch(searchKeyword)
                  }
                />
                <button onClick={multiListSearch}>검색</button>
              </div>
              <div className="button-container">
                <button onClick={refreshPage}>새로고침</button>
                <button onClick={() => navigate("/_multi-create")}>방 만들기</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

const styles = {
  container: {
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    backgroundColor: "rgba(27, 26, 85, 0.8)",
    width: "80vw",
    borderRadius: "30px",
    height: "80vh",
    marginTop: "20px",
    color: "white",
    display: "flex",
  },
};

