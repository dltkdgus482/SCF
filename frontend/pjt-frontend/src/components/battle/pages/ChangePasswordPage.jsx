import "../../../css/ChangePasswordPage.css";
import axios from "axios";
import { useRef } from "react";
import store from "../../../store/store.js";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
import Header from "../components/Header.jsx";
import LoginPageButton from "../../buttons/LoginPageButton.jsx";

import Container from "@mui/material/Container";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import InputAdornment from "@mui/material/InputAdornment";
import Grid from "@mui/material/Grid";
import Avatar from "@mui/material/Avatar";

const ChangePasswordPage = () => {
  const newPassword = useRef(null);
  const newPasswordCheck = useRef(null);
  const navigate = useNavigate();

  const { userId, baseURL } = store((state) => ({
    userId: state.userId,
    baseURL: state.baseURL,
  }));

  const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/;

  const changePassword = async () => {
    if (passwordRegex.test(newPassword.current.value) === false) {
      Swal.fire({
        text: "비밀번호는 숫자+영문자+특수문자 조합으로 8자리 이상 25자리 이하로 입력해주세요!",
        icon: "warning",
        timer: 3000,
      });
      return;
    }

    if (newPassword.current.value !== newPasswordCheck.current.value) {
      Swal.fire({
        text: "비밀번호가 일치하지 않습니다.",
        icon: "warning",
        timer: 3000,
      });
      return;
    } else {
      try {
        const res = await axios({
          method: "POST",
          url: `${baseURL}/user/public/change-password`,
          data: {
            userId: userId,
            newPassword: newPassword.current.value,
          },
        });

        Swal.fire({
          text: "비밀번호가 변경되었습니다.",
          icon: "success",
          timer: 3000,
        });
        navigate("/login");
      } catch (error) {
        Swal.fire({
          text: "비밀번호 변경에 실패했습니다.",
          icon: "error",
          timer: 3000,
        });
        console.log(error);
      }
    }
  };
  return (
    <>
      <Header />
      <div className="change-password-outer-container">
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
            변경하실 비밀번호를 작성해주세요.
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
              placeholder="변경하실 비밀번호를 입력해주세요."
              inputRef={newPassword}
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
              placeholder="비밀번호를 다시 입력해주세요."
              inputRef={newPasswordCheck}
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
              onClick={changePassword}
              sx={{ mt: 2 }} // Margin top for spacing
            >
              비밀번호 변경
            </LoginPageButton>
          </Box>
        </Container>

        {/* <div className="change-password-title">비밀번호 변경</div>
        <div className="change-password-container">
          <input type="password" placeholder="새 비밀번호" ref={newPassword} />
          <input
            type="password"
            placeholder="새 비밀번호 확인"
            ref={newPasswordCheck}
          />
          <button onClick={changePassword} className="change-password-button">
            비밀번호 변경
          </button>
        </div> */}
      </div>
    </>
  );
};

export default ChangePasswordPage;
