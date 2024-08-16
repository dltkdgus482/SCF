import Button from "@mui/material/Button";
import { styled } from "@mui/system";

const CustomLoginPageButton = styled(Button)(({ theme }) => ({
  appearance: "none",
  fontFamily: "'Galmuri11-Bold'",
  fontSize: "1rem", // MUI 기본 버튼 크기와 일치하도록 조정
  color: "white",
  border: "none",
  borderRadius: "5px",
  padding: "10px 20px",
  cursor: "pointer",
  position: "relative",
  overflow: "hidden",
  backgroundColor: "#1b1a55", // 마우스를 올렸을 때 배경색
  transition:
    "background-color 0.6s ease-in-out, transform 0.3s ease-in-out, box-shadow 0.3s ease-in-out, color 0.3s ease-in-out",
  // boxShadow: "0 0 10px 0 #1b1a55 inset, 0 0 10px 4px #1b1a55",

  // "&:hover": {
  //   transform: "scale(1.05)", // 약간 확대
  //   boxShadow: "0 0 10px 0 #3498db inset, 0 0 10px 4px #3498db",
  //   backgroundColor: "#1b1a55", // Hover 시의 배경색 유지
  // },
}));

export default CustomLoginPageButton;
