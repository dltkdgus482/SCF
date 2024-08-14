import axios from "axios";
import { useRef } from "react";
import AccountButton from "../components/AccountButton.jsx";
import "../../../css/LoginPage.css";
import store from "../../../store/store.js";
import { useNavigate } from "react-router-dom";
import { Howl } from "howler";
import SoundStore from "../../../stores/SoundStore.jsx";
import createAuthClient from "../apis/createAuthClient.js";
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

const LoginPage = () => {
  const {
    accessToken,
    setAccessToken,
    baseURL,
    memberId,
    setMemberId,
    setUserId,
    setName,
    setSchoolName,
    setBirth,
    setRefreshToken,
    setAuthClient,
  } = store((state) => ({
    accessToken: state.accessToken,
    setAccessToken: state.setAccessToken,
    baseURL: state.baseURL,
    memberId: state.memberId,
    setMemberId: state.setMemberId,
    setUserId: state.setUserId,
    setName: state.setName,
    setSchoolName: state.setSchoolName,
    setBirth: state.setBirth,
    setRefreshToken: state.setRefreshToken,
    setAuthClient: state.setAuthClient,
  }));
  const userId = useRef(null);
  const password = useRef(null);
  const navigate = useNavigate();
  const { playEffectSound } = SoundStore();

  const noAuthLogin = async () => {
    playEffectSound("mainStartSound");
    try {
      const res = await axios({
        method: "POST",
        url: `${baseURL}/user/public/login`,
        data: {
          userId: userId.current.value,
          password: password.current.value,
        },
      });

      const authorizationHeader = res.headers["authorization"];
      const accessToken = authorizationHeader
        ? authorizationHeader.replace(/^Bearer\s+/i, "")
        : null;
      const memberId = res.data["memberId"];
      setAccessToken(accessToken);
      setMemberId(memberId);
      setUserId(userId.current.value);
      navigate("/main");
    } catch (error) {
      Swal.fire({
        text: "아이디 또는 비밀번호가 일치하지 않습니다.",
        icon: "error",
        timer: 3000,
      });
      console.log(error);
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter") {
      noAuthLogin();
    }
  };

  return (
    <>
      <Header />
      {/* <Header /> */}
      <div className="login-outer-container">
        {/* <div className="login-title">Login Page</div> */}
        <Container
          component="main"
          maxWidth={false}
          sx={{
            maxWidth: "500px",
            backgroundColor: "rgba(255, 255, 255, 0.5)", // 반투명 화이트 배경
            backdropFilter: "blur(30px)", // 블러 효과 추가
            padding: 4, // 패딩 조정
            borderRadius: 10, // 둥근 모서리
            boxShadow: "0px 0px 15px rgba(0, 0, 0, 0.2)", // 그림자 추가
            display: "flex",
            height: "80vh",
            maxHeight: "80vh",
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
              mt: 7,
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
            variant="h3"
            sx={{
              mb: 4, // Add margin bottom for spacing
              fontWeight: "bold", // Make the text bold
              color: "#1b1a55", // Customize color
              fontFamily: "Galmuri11-Bold",
            }}
          >
            Log-in
          </Typography>
          {/* 로그인 폼 */}
          <Box
            sx={{
              flex: 1,
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
            }}
          >
            <Box component="form" noValidate sx={{ mt: 1 }}>
              <Typography variant="h7" sx={{ mb: 0.5 }}>
                {" "}
                {/* Reduced marginBottom */}
                아이디
              </Typography>
              <TextField
                margin="dense"
                required
                fullWidth
                id="userId"
                name="userId"
                inputRef={userId}
                autoComplete="username"
                autoFocus
                onKeyDown={handleKeyDown}
                sx={{
                  "& .MuiOutlinedInput-root": {
                    "& fieldset": {
                      borderColor: "#1b1a55", // Thicker border color
                      borderWidth: 2, // Increase border thickness
                      borderRadius: "15px", // Make the borders rounded
                    },
                    "&.Mui-focused fieldset": {
                      borderColor: "#1b1a55", // Border color when focused
                    },
                  },

                  marginBottom: "1rem",
                }}
              />
              <Typography variant="h7" sx={{ mt: 2, mb: 0.5 }}>
                {" "}
                {/* Reduced marginTop and marginBottom */}
                비밀번호
              </Typography>
              <TextField
                margin="dense"
                required
                fullWidth
                name="password"
                type="password"
                id="password"
                inputRef={password}
                autoComplete="current-password"
                onKeyDown={handleKeyDown}
                sx={{
                  "& .MuiOutlinedInput-root": {
                    "& fieldset": {
                      borderColor: "black", // Thicker border color
                      borderWidth: 2, // Increase border thickness
                      borderRadius: "15px", // Make the borders rounded
                    },
                    "&:hover fieldset": {
                      borderColor: "black", // Border color on hover
                    },
                    "&.Mui-focused fieldset": {
                      borderColor: "black", // Border color when focused
                    },
                  },
                }}
              />
              <LoginPageButton
                type="button"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2 }}
                onClick={noAuthLogin}
              >
                Login
              </LoginPageButton>
              <Grid container>
                <Grid item xs>
                  <div className="find-and-kakao">
                    <div
                      className="find-password"
                      onClick={() => navigate("/find-password")}
                    >
                      비밀번호 찾기
                    </div>
                  </div>
                </Grid>
                <Grid item>
                  <div className="find-and-kakao">
                    <div
                      className="find-password"
                      onClick={() => navigate("/signup")}
                    >
                      {"계정이 없으신가요?"}
                    </div>
                  </div>
                </Grid>
              </Grid>
            </Box>
          </Box>
        </Container>
      </div>
    </>
  );
};

export default LoginPage;
