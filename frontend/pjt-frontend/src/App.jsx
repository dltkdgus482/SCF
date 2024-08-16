import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import TitlePage from "./components/sanghyeon/pages/TitlePage.jsx";
import LoginPage from "./components/sanghyeon/pages/LoginPage.jsx";
import FindPasswordPage from "./components/sanghyeon/pages/FindPasswordPage.jsx";
import ChangePasswordPage from "./components/sanghyeon/pages/ChangePasswordPage.jsx";
import SignUpPage from "./components/sanghyeon/pages/SignUpPage.jsx";
import MainPage from "./components/main-page/MainPage.jsx";
//mainpage 경로 수정했습니다. //
import ProfilePage from "./components/sanghyeon/pages/ProfilePage.jsx";
import RecordPage from "./components/sanghyeon/pages/RecordPage.jsx";
import ReportPage from "./components/sanghyeon/pages/ReportPage.jsx";
import SolvedPage from "./components/sanghyeon/pages/SolvedPage.jsx";
import SingleMain from "./components/single/SingleMain.jsx";
import SinglePlay from "./components/single/single-play/SinglePlay.jsx";
import MultiMain from "./components/multi/MultiMain.jsx";
import MultiCreate from "./components/multi/MultiCreate.jsx";
import MultiGame from "./components/multi/MultiGame.jsx";
import BattleGameListPage from "./components/sanghyeon/pages/BattleGameListPage.jsx";
import TempBattleGamePagePage from "./components/sanghyeon/pages/TempBattleGamePage.jsx"; // 여기
import BattleGamePage from "./components/sanghyeon/pages/BattleGamePage.jsx";
import Ranking from "./components/ranking/Ranking.jsx";
import CharacterSelection from "./components/sanghyeon/pages/CharacterSelection.jsx";
import store from "./store/store.js";
import Loading from "./components/loading/Loading.jsx";

import ItemPage from "./components/sanghyeon/pages/ItemPage.jsx";

import { useEffect, useState } from "react";
import SoundStore from "./stores/SoundStore.jsx";
import SolvedDetailPage from "./components/sanghyeon/pages/SolvedDetailPage.jsx";
function App() {
  const { playBackgroundMusic } = SoundStore();
  const [isLoading, setIsLoading] = useState(true);
  const [backgroundImageUrl, setBackgroundImageUrl] = useState("");
  const { accessToken, setAccessToken } = store((state) => ({
    accessToken: state.accessToken,
    setAccessToken: state.setAccessToken,
  }));

  useEffect(() => {
    //초기 화면 렌더링 시 accessToken 불러오기
    if (!accessToken) {
      setAccessToken(localStorage.getItem("accessToken"));
      console.log("새로고침 후 토큰 불러오기...!");
    }

    // 배경음악 초기화 및 재생
    setTimeout(() => {
      setIsLoading(false);
      console.log("loading end");
      playBackgroundMusic();
    }, 2000);

    // 시간에 따라 배경화면 변화    const updateBackgroundImage = () => {
    const now = new Date();
    const minutes = now.getMinutes();

    if (minutes >= 0 && minutes < 30) {
      setBackgroundImageUrl("/background1.gif");
    } else {
      setBackgroundImageUrl("/background2.gif");
    }
  }, []);

  return (
    <div
      style={{
        backgroundImage: `url(${backgroundImageUrl})`,
        minHeight: "100vh",
        backgroundSize: "100vh",
      }}
    >
      <Router>
        <Routes>
          <Route path="/" element={isLoading ? <Loading /> : <TitlePage />} />
          <Route
            path="/login"
            element={accessToken ? <MainPage /> : <LoginPage />}
          />
          <Route path="/find-password" element={<FindPasswordPage />} />
          <Route path="/reset-password" element={<ChangePasswordPage />} />
          <Route path="/signup" element={<SignUpPage />} />
          <Route path="/signup-character" element={<CharacterSelection />} />
          {/* <Route
            path="/ranking"
            element={accessToken ? <Ranking /> : <LoginPage />}
          /> */}
          <Route path="/leaderboard" element={<Ranking />} />
          <Route path="/main" element={<MainPage />} />
          {/* <Route path="/main" element={accessToken ? <MainPage /> : <LoginPage />} /> */}
          <Route
            path="/account"
            element={<ProfilePage />}
            // element={accessToken ? <ProfilePage /> : <LoginPage />}
          />
          <Route
            path="/_record"
            // element={accessToken ? <RecordPage /> : <LoginPage />}
            element={<RecordPage />}
          />
          <Route
            path="/_report"
            // element={accessToken ? <ReportPage /> : <LoginPage />}
            element={<ReportPage />}
          />
          <Route
            path="/_solved"
            // element={accessToken ? <SolvedPage /> : <LoginPage />}
            element={<SolvedPage />}
          />
          <Route
            path="/_solved/:solvedId"
            // element={accessToken ? <SolvedDetailPage /> : <LoginPage />}
            element={<SolvedDetailPage />}
          />
          {/* <Route path="/multi" element={accessToken ? <MultiMain /> : <LoginPage />} /> */}
          <Route path="/_multi" element={<MultiMain />} />
          <Route path="/_multi-create" element={<MultiCreate />} />
          <Route
            path="/_battle-list"
            element={accessToken ? <BattleGameListPage /> : <LoginPage />}
            // element={<BattleGameListPage />}
          />
          <Route path="/_battle-game" element={<BattleGamePage />} />
          {/* <Route path="/multi-game" element={accessToken ? <MultiGame /> : <LoginPage />} /> */}
          <Route path="/_multi-game" element={<MultiGame />} />
          <Route path="/_multi-game/:room_id" element={<MultiGame />} />
          {/* <Route path="/single-main" element={accessToken ? <SingleMain /> : <LoginPage />} /> */}
          <Route path="/story-main" element={<SingleMain />} />
          {/* <Route path="/single-play" element={accessToken ? <SinglePlay /> : <LoginPage />} /> */}
          <Route path="/story-play/:content_id" element={<SinglePlay />} />
          <Route path="/gacha" element={<ItemPage />} />
          <Route path="*" element={<TitlePage />} />
          <Route path="/temp" element={<TempBattleGamePagePage />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
