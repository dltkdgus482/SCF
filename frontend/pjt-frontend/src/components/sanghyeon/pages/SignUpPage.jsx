import axios from "axios";
import { useRef, useState, useEffect } from "react";
import "../../../css/SignUpPage.css";
import { useNavigate } from "react-router-dom";
import store from "../../../store/store.js";
import Swal from "sweetalert2";
import LoginPageButton from "../../buttons/LoginPageButton.jsx";
import Header from "../components/Header";

import Container from "@mui/material/Container";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import InputAdornment from "@mui/material/InputAdornment";
import Grid from "@mui/material/Grid";

const SignUpPage = () => {
  const { baseURL, registerInfo, setRegisterInfo } = store((state) => ({
    baseURL: state.baseURL,
    registerInfo: state.registerInfo,
    setRegisterInfo: state.setRegisterInfo,
  }));

  const userId = useRef(null);
  const name = useRef(null);
  const password1 = useRef(null);
  const password2 = useRef(null);
  const email = useRef(null);
  const schoolName = useRef(null);
  const birth = useRef(null);
  const navigate = useNavigate();
  const idCheckComplete = useRef(false);
  const [passwordMatch, setPasswordMatch] = useState(true);
  const [passwordError, setPasswordError] = useState("");
  const nameRegex = /^[a-zA-Z0-9]*$/;

  useEffect(() => {
    const checkPasswordMatch = () => {
      if (password1.current && password2.current) {
        setPasswordMatch(password1.current.value === password2.current.value);
      }
    };

    const checkPasswordValidity = () => {
      if (password1.current) {
        const passwordValue = password1.current.value;
        if (
          !passwordValue ||
          passwordValue.length < 8 ||
          !passwordRegex.test(passwordValue)
        ) {
          setPasswordError(
            "숫자+영문자+특수문자 조합으로 8자리 이상 25자리 이하로 입력해주세요!"
          );
        } else {
          setPasswordError("");
        }
      }
    };

    // 비밀번호 입력 값이 변경될 때마다 호출
    if (password1.current) {
      password1.current.addEventListener("input", checkPasswordMatch);
      password1.current.addEventListener("input", checkPasswordValidity);
    }
    if (password2.current) {
      password2.current.addEventListener("input", checkPasswordMatch);
    }

    // cleanup
    return () => {
      if (password1.current) {
        password1.current.removeEventListener("input", checkPasswordMatch);
        password1.current.removeEventListener("input", checkPasswordValidity);
      }
      if (password2.current) {
        password2.current.removeEventListener("input", checkPasswordMatch);
      }
    };
  }, []);

  const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/;

  const signUp = async (event) => {
    event.preventDefault();

    // if (!nameRegex.test(name.current.value)) {
    //   Swal.fire({
    //     text: "닉네임은 영문자와 숫자만 입력 가능합니다.",
    //     icon: "warning",
    //     timer: 3000,
    //   });
    //   return;
    // }

    if (userId.current.value.length < 3 || userId.current.value.length > 15) {
      Swal.fire({
        text: "아이디는 3글자 이상 15글자 미만으로 입력해주세요.",
        icon: "warning",
        timer: 3000,
      });
      return;
    }

    if (!idCheckComplete.current) {
      Swal.fire({
        text: "아이디 중복 확인 을 해주세요.",
        icon: "warning",
        timer: 3000,
      });
      return;
    }

    if (password1.current.value !== password2.current.value) {
      Swal.fire({
        text: "비밀번호가 일치하지 않습니다.",
        icon: "warning",
        timer: 3000,
      });
      return;
    }

    if (passwordRegex.test(password1.current.value) === false) {
      Swal.fire({
        text: "비밀번호는 숫자+영문자+특수문자 조합으로 8자리 이상 25자리 이하로 입력해주세요!",
        icon: "warning",
        timer: 3000,
      });
      return;
    }

    setRegisterInfo({
      userId: userId.current.value,
      name: name.current.value,
      password: password1.current.value,
      email: email.current.value,
      schoolName: schoolName.current.value,
      birth: birth.current.value,
      characterType: "",
    });
    navigate("/signup-character");
  };

  const idCheck = async () => {
    try {
      const idCheckRes = await axios({
        method: "GET",
        url: `${baseURL}/user/public/validate/${userId.current.value}`,
      });

      Swal.fire({
        text: "사용 가능한 아이디입니다.",
        icon: "success",
        timer: 3000,
      });
      idCheckComplete.current = true;
    } catch (error) {
      if (error.response.status === 409) {
        Swal.fire({
          text: "이미 사용 중인 아이디입니다.",
          icon: "error",
          timer: 3000,
        });
      } else {
        console.log(error.response);
        Swal.fire({
          text: "알 수 없는 오류가 발생했습니다.",
          icon: "error",
          timer: 3000,
        });
      }
    }
  };

  const handleSignup = (e) => {
    if (e.key === "Enter") {
      signUp();
    }
  };

  const handleIdCheck = (e) => {
    if (e.key === "Enter") {
      idCheck();
    }
  };

  return (
    <>
      <Header />
      <div className="signup-outer-container">
        <Container
          component="main"
          maxWidth="md"
          sx={{
            backgroundColor: "rgba(255, 255, 255, 0.5)", // 반투명 화이트 배경
            backdropFilter: "blur(40px)", // 블러 효과 추가
            padding: 4, // 패딩 조정
            borderRadius: 10, // 둥근 모서리
            boxShadow: "0px 0px 15px rgba(0, 0, 0, 0.2)", // 그림자 추가
            display: "flex",
            flexDirection: "column",
            alignItems: "center", // 수직 중앙 정렬
            justifyContent: "center", // 수평 중앙 정렬
            height: "80vh",
            mx: "auto",
          }}
        >
          <CssBaseline />
          <Typography
            variant="h3"
            sx={{
              mt: 4, // Add margin top for spacing
              mb: 4, // Add margin bottom for spacing
              fontWeight: "bold", // Make the text bold
              color: "#1b1a55", // Customize color
              fontFamily: "Galmuri11-Bold",
            }}
          >
            Register
          </Typography>
          <Box
            component="form"
            noValidate
            onSubmit={signUp}
            sx={{
              width: "100%",
              flex: 1,
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
            }}
          >
            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <TextField
                  margin="dense"
                  required
                  fullWidth
                  id="userId"
                  name="userId"
                  inputRef={userId}
                  label="아이디"
                  autoComplete="username"
                  autoFocus
                  InputProps={{
                    endAdornment: (
                      <InputAdornment position="end">
                        <Button
                          onClick={idCheck}
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
                          중복 확인
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
                <Typography
                  variant="body2"
                  sx={{
                    mt: -1,
                    ml: 1,
                    color: "inherit", // 비밀번호 유효성 오류가 있을 경우 빨간색으로 표시
                    fontWeight: 500,
                  }}
                >
                  * 3글자 이상 15글자 미만으로 작성해주세요.
                </Typography>
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  margin="dense"
                  required
                  fullWidth
                  id="name"
                  name="name"
                  label="닉네임"
                  inputRef={name}
                  autoComplete="name"
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
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  margin="dense"
                  required
                  fullWidth
                  id="password1"
                  name="password1"
                  label="비밀번호"
                  inputRef={password1}
                  type="password"
                  autoComplete="new-password"
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
                <Typography
                  variant="body2"
                  sx={{
                    mt: -1,
                    ml: 1,
                    color: passwordError ? "red" : "inherit", // 비밀번호 유효성 오류가 있을 경우 빨간색으로 표시
                    fontWeight: 500,
                  }}
                >
                  {passwordError}
                </Typography>
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  margin="dense"
                  required
                  fullWidth
                  id="password2"
                  name="password2"
                  label="비밀번호 확인"
                  inputRef={password2}
                  type="password"
                  autoComplete="new-password"
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
                <Typography
                  variant="body2"
                  sx={{
                    mt: -1,
                    ml: 1,
                    color:
                      passwordMatch &&
                      password1.current?.value &&
                      password2.current?.value
                        ? "green"
                        : "red", // 비밀번호 일치 여부에 따라 색상 변경
                    fontWeight: 500,
                  }}
                >
                  {password1.current?.value && password2.current?.value
                    ? passwordMatch
                      ? "비밀번호가 일치합니다."
                      : "비밀번호가 일치하지 않습니다."
                    : ""}
                </Typography>
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  margin="dense"
                  required
                  fullWidth
                  id="email"
                  name="email"
                  inputRef={email}
                  label="이메일 주소"
                  type="email"
                  autoComplete="email"
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
                <Typography
                  variant="body2"
                  sx={{
                    mt: -1, // 간격 조정 (텍스트 필드와 가까이 붙게)
                    ml: 1, // 텍스트 필드와 정렬 맞춤
                    fontWeight: 500, // 글자 굵기를 약간 굵게 설정
                  }}
                >
                  * 실제 이메일을 입력해 주세요.
                </Typography>
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  margin="dense"
                  required
                  fullWidth
                  id="schoolName"
                  name="schoolName"
                  inputRef={schoolName}
                  label="학교명"
                  autoComplete="school"
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
              </Grid>
              <Grid item xs={12}>
                <TextField
                  margin="dense"
                  required
                  fullWidth
                  id="birth"
                  name="birth"
                  inputRef={birth}
                  label="생년월일"
                  type="date"
                  InputLabelProps={{ shrink: true }}
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
              </Grid>
            </Grid>
            <LoginPageButton type="submit">NEXT</LoginPageButton>
          </Box>
        </Container>
      </div>
    </>
  );
};

export default SignUpPage;
