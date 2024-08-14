import "../../../css/FindPasswordPage.css";
import { useRef } from "react";
import store from "../../../store/store.js";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
import LoginPageButton from "../../buttons/LoginPageButton.jsx";
import Header from "../components/Header";

import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import FormControlLabel from "@mui/material/FormControlLabel";
import Checkbox from "@mui/material/Checkbox";
import Link from "@mui/material/Link";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import InputAdornment from "@mui/material/InputAdornment";

const FindPasswordPage = () => {
  const { userId, baseURL, code, setCode, setUserId } = store((state) => ({
    userId: state.userId,
    baseURL: state.baseURL,
    code: state.code,
    setCode: state.setCode,
    setUserId: state.setUserId,
  }));
  const currentUserId = useRef(null);
  const verificationCode = useRef(null);
  const isVerified = useRef(false);
  const navigate = useNavigate();

  const sendVerificationCode = async () => {
    try {
      const sendRes = await axios({
        method: "POST",
        url: `${baseURL}/user/public/request-verification-code`,
        data: {
          userId: currentUserId.current.value,
        },
      });
      Swal.fire({
        text: "인증코드가 발송되었습니다.",
        icon: "success",
        timer: 3000,
      });
    } catch (error) {
      Swal.fire({
        text: "인증코드 발송에 실패했습니다.",
        icon: "error",
        timer: 3000,
      });
      console.log(error);
    }
  };

  const checkVerificationCode = async () => {
    try {
      const chkRes = await axios({
        method: "POST",
        url: `${baseURL}/user/public/request-verification`,
        data: {
          userId: currentUserId.current.value,
          code: verificationCode.current.value,
        },
      });
      isVerified.current = true;
      setUserId(currentUserId.current.value);
      Swal.fire({
        text: "인증코드가 일치합니다.",
        icon: "success",
        timer: 3000,
      });
    } catch (error) {
      Swal.fire({
        text: "인증코드가 일치하지 않습니다.",
        icon: "error",
        timer: 3000,
      });
      console.log(error);
    }
  };

  const completeVerification = () => {
    if (!isVerified.current) {
      Swal.fire({
        text: "인증코드를 확인해주세요.",
        icon: "warning",
        timer: 3000,
      });
    } else {
      setCode(verificationCode.current.value);
      navigate("/reset-password");
    }
  };

  return (
    <>
      <Header />
      <div className="login-outer-container">
        {/* <div className="login-title">Login Page</div> */}
        <Container
          component="main"
          maxWidth={false}
          sx={{
            maxWidth: "500px",
            backgroundColor: "rgba(255, 255, 255, 0.5)", // 반투명 화이트 배경
            backdropFilter: "blur(40px)", // 블러 효과 추가
            padding: 4, // 패딩 조정
            borderRadius: 10, // 둥근 모서리
            boxShadow: "0px 0px 15px rgba(0, 0, 0, 0.2)", // 그림자 추가
            display: "flex",
            height: "60vh",
            flexDirection: "column",
            alignItems: "center", // 수직 중앙 정렬
            justifyContent: "center", // 수평 중앙 정렬
            mx: "auto",
          }}
        >
          <CssBaseline />
          {/* Log-in Title */}
          <Avatar
            sx={{
              m: 1,
              mt: 3,
              width: 70,
              height: 56,
              p: 0, // Remove padding
              borderRadius: 5,
            }}
          >
            <img
              src="/background5.gif" // Adjust the path if necessary
              alt="background gif"
              style={{
                width: "100%",
                height: "100%",

                objectFit: "cover", // Ensure the image covers the avatar
              }}
            />
          </Avatar>
          <Typography
            variant="h6"
            sx={{
              mb: 4, // Add margin bottom for spacing
              fontWeight: "bold", // Make the text bold
              color: "#1b1a55", // Customize color
              fontFamily: "Galmuri11-Bold",
            }}
          >
            비밀번호를 찾을 아이디를 입력해주세요.
          </Typography>
          <Box
            sx={{
              flex: 1,
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              width: "100%", // Ensure full width within the container
              gap: 2, // Space between elements
            }}
          >
            <TextField
              margin="dense"
              required
              fullWidth
              autoFocus
              placeholder="인증코드를 받을 아이디를 작성하세요."
              inputRef={currentUserId}
              InputProps={{
                endAdornment: (
                  <InputAdornment position="end">
                    <Button
                      onClick={sendVerificationCode}
                      variant="contained"
                      color="primary"
                      sx={{
                        borderRadius: "15px",
                        padding: "0.5rem 1rem",
                        backgroundColor: "#1b1a55",
                        "&:hover": {
                          backgroundColor: "#161b40",
                        },
                      }}
                    >
                      인증번호 전송
                    </Button>
                  </InputAdornment>
                ),
              }}
              sx={{
                "& .MuiOutlinedInput-root": {
                  "& fieldset": {
                    borderColor: "#1b1a55",
                    borderWidth: 2,
                    borderRadius: "15px",
                  },
                  "&:hover fieldset": {
                    borderColor: "#1b1a55",
                  },
                  "&.Mui-focused fieldset": {
                    borderColor: "#1b1a55",
                  },
                },
                marginBottom: "1rem",
              }}
            />

            <TextField
              margin="dense"
              required
              fullWidth
              autoFocus
              placeholder="인증번호를 입력해주세요."
              inputRef={verificationCode}
              InputProps={{
                endAdornment: (
                  <InputAdornment position="end">
                    <Button
                      onClick={checkVerificationCode}
                      variant="contained"
                      color="primary"
                      sx={{
                        borderRadius: "15px",
                        padding: "0.5rem 1rem",
                        backgroundColor: "#1b1a55",
                        "&:hover": {
                          backgroundColor: "#161b40",
                        },
                      }}
                    >
                      인증번호 확인
                    </Button>
                  </InputAdornment>
                ),
              }}
              sx={{
                "& .MuiOutlinedInput-root": {
                  "& fieldset": {
                    borderColor: "#1b1a55",
                    borderWidth: 2,
                    borderRadius: "15px",
                  },
                  "&:hover fieldset": {
                    borderColor: "#1b1a55",
                  },
                  "&.Mui-focused fieldset": {
                    borderColor: "#1b1a55",
                  },
                },
                marginBottom: "1rem",
              }} // Margin top for spacing
            />

            <LoginPageButton
              onClick={completeVerification}
              sx={{ mt: 2 }} // Margin top for spacing
            >
              NEXT
            </LoginPageButton>
          </Box>
        </Container>
      </div>
    </>
  );
};

export default FindPasswordPage;
