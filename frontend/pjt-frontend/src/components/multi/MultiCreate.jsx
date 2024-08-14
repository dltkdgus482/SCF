import "../../index.css";
import "../../css/GameMain.css";
import "../../css/Container.css";
import "../../css/GameCreate.css";
import MultiHeader from "../game/MultiHeader.jsx";
import { useNavigate } from "react-router-dom";
import multiStore from "../../stores/multiStore.jsx";
import store from "../../store/store.js";
import createAuthClient from "../sanghyeon/apis/createAuthClient.js";

export default function MultiCreate() {
  const navigate = useNavigate();

  const { accessToken, setAccessToken, memberId, userId, name, baseURL } =
    store((state) => ({
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

  const setRoomId = multiStore((state) => state.setRoomId);

  const createMultiRoom = async (data) => {
    data.preventDefault();

    try {
      // const headers = {
      //   'memberId': userId,
      //   'username': name
      // };

      const title = data.target.title.value;
      const maxPlayer = data.target.maxPlayer.value;
      // const password = data.target.password.value;
      const gameRound = data.target.gameRound.value;
      const password =
        data.target.password.value === "" ? null : data.target.password.value;

      // const response = await axios.post(
      //   `${baseURL}/multi/room`, { title, maxPlayer, password, gameRound }, { Authorization: `Bearer ${accessToken}` });

      const response = await authClient({
        method: "POST",
        url: `${baseURL}/multi/room`,
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
        data: { title, maxPlayer, password, gameRound },
      });

      const roomId = response.data;
      setRoomId(roomId);
      navigate(`/_multi-game/${roomId}`, { state: { hostId: memberId } });
    } catch (error) {
      console.error("Error creating room:", error);
    }
  };

  return (
    <>
      <MultiHeader onBackButtonClick={null} />
      {/* <div className="container"> */}
      <div id="container">
        <div className="create-container">
          <h1>Create Room [ Multi Mode ]</h1>
          <div className="pink-line" />
          <form onSubmit={createMultiRoom}>
            <div className="multi-create-form">
              <div className="multi-create-input">
                <div className="create-box">
                  <span>방 제목 : </span>
                  <input
                    name="title"
                    className="create-title"
                    type="text"
                    placeholder="Enter Room Title"
                    maxLength={20}
                  />
                </div>
                <div className="create-box">
                  <span>최대인원 : </span>
                  <input
                    name="maxPlayer"
                    className="create-max-number"
                    type="number"
                    min="2"
                    max="100"
                    placeholder="10"
                    defaultValue={10}
                  />
                </div>
                <div className="create-box">
                  <span>비밀번호 : </span>
                  <input
                    name="password"
                    className="create-password"
                    type="password"
                    maxLength={20}
                  />
                </div>
                <div className="create-box">
                  <span>라운드 : </span>
                  <input
                    name="gameRound"
                    className="create-problems"
                    type="number"
                    min="2"
                    max="20"
                    placeholder="5"
                    defaultValue={5}
                  />
                </div>
              </div>
              <div className="create-button-container">
                <button className="create-button" type="submit">
                  생성
                </button>
                <button
                  className="create-button"
                  onClick={() => navigate("/_multi")}
                >
                  취소
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </>
  );
}
