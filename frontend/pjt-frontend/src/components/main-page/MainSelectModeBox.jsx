import { useNavigate } from "react-router-dom";
import { useState } from "react";
function MainSelectModeBox(props) {
  const { mode, playEffectSound } = props;
  const navigate = useNavigate();
  const [isHovered, setIsHovered] = useState(false);

  const selectMode = (mode) => {
    if (mode === "스토리모드") {
      navigate("/story-main");
    } else if (mode === "멀티 모드") {
      navigate("/_multi");
    } else if (mode === "배틀 모드") {
      navigate("/_battle-list");
    } else if (mode === "랭킹") {
      navigate("/leaderboard");
    }
    playEffectSound("clickSound"); // 클릭 시 소리 재생
  };
  const styles = {
    selectBox: {
      cursor: "pointer",

      backgroundColor: isHovered ? "#6a1b9a" : "#3f51b5", // 단색 배경으로 변경
      border: isHovered ? "2px solid #FFD700" : "2px solid transparent",
      boxShadow: isHovered
        ? "0 8px 16px rgba(0, 0, 0, 0.3)"
        : "0 4px 8px rgba(0, 0, 0, 0.2)",
      transform: isHovered ? "scale(1.05)" : "scale(1)",
      width: "500px",
      height: "80px",
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
      margin: "10px 0",
      transition: "transform 0.2s, backgroundColor 0.2s, border 0.2s",
      borderRadius: "10px", // 기본 테두리 반경
    },
    mode: {
      color: "white",
      fontSize: "24px",
    },
  };

  return (
    <div
      onClick={() => selectMode(mode)}
      onMouseEnter={() => {
        playEffectSound("hoverSound");
        setIsHovered(true);
      }} // 마우스 오버 시 소리 재생
      onMouseLeave={() => setIsHovered(false)}
      style={styles.selectBox}
    >
      <h1 style={styles.mode}>{mode}</h1>
    </div>
  );
}

export default MainSelectModeBox;
