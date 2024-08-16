// BasicModal.js
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import Modal from "@mui/material/Modal";
import { useState } from "react";

const style = {
  position: "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 800,
  bgcolor: "background.paper",
  border: "2px solid #000",
  boxShadow: 24,
  p: 4,
};

function ChildModal() {
  const [open, setOpen] = useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  return (
    <>
      <Button onClick={handleOpen}>게임 방법 정리</Button>
      <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="child-modal-title"
        aria-describedby="child-modal-description"
      >
        <Box sx={{ ...style, width: 600 }}>
          <Typography id="child-modal-title" variant="h5" component="h2">
            게임 방법 설명
          </Typography>
          <Typography
            id="child-modal-description"
            sx={{ mt: 2, fontSize: "1.1rem", lineHeight: "1.8rem" }}
          >
            <Box sx={{ mb: 3 }}>
              <Typography
                variant="h6"
                component="div"
                sx={{ display: "block", mb: 1 }}
              >
                <strong>1. 객관식 문제</strong>
              </Typography>
              <Box>주어진 보기 중 정답을 선택하여 제출하세요.</Box>
              <Box
                sx={{
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                }}
              >
                <img
                  src="/selectnumber.PNG" // 객관식 문제에 대한 이미지 경로
                  alt="객관식 문제 이미지"
                  style={{ width: "50%", height: "auto" }} // 이미지 스타일
                />
              </Box>
            </Box>

            <Box sx={{ mb: 2 }}>
              <Typography
                variant="h6"
                component="div"
                sx={{ display: "block", mb: 1 }}
              >
                <strong>2. 단답식 문제</strong>
              </Typography>
              <Box>정답을 직접 입력하여 제출하세요.</Box>
              <Box
                sx={{
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                }}
              >
                <img
                  src="/shortanswer.PNG" // 단답식 문제에 대한 이미지 경로
                  alt="단답식 문제 이미지"
                  style={{ width: "50%", height: "auto" }} // 이미지 스타일
                />
              </Box>
            </Box>

            <Box sx={{ mb: 2 }}>
              <Typography
                variant="h6"
                component="div"
                sx={{ display: "block", mb: 1 }}
              >
                <strong>3. 드래그 앤 드랍</strong>
              </Typography>
              <Box>아래쪽에 주어진 보기들을 문제의 빈칸에 끌어다 놓으세요.</Box>
              <Box
                sx={{
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                }}
              >
                <img
                  src="/draganddrop.PNG" // 드래그 앤 드랍 문제에 대한 이미지 경로
                  alt="드래그 앤 드랍 이미지"
                  style={{ width: "50%", height: "auto" }} // 이미지 스타일
                />
              </Box>
            </Box>
          </Typography>
          <Box sx={{ display: "flex", justifyContent: "center", mt: 2 }}>
            <Button onClick={handleClose} variant="contained">
              닫기
            </Button>
          </Box>
        </Box>
      </Modal>
    </>
  );
}

export default function BasicModal() {
  const [open, setOpen] = useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  return (
    <div style={{ display: "inline-block" }}>
      <Button onClick={handleOpen}>
        <img
          src="/info3.png"
          alt="정보 아이콘"
          style={{ width: "40px", height: "40px" }} // 이미지 크기를 조절
        />
      </Button>
      <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box sx={style}>
          <Typography
            id="modal-modal-description"
            sx={{
              mt: 2,
              fontSize: "1.2rem", // 전반적인 텍스트 크기 증가
              lineHeight: "2.0rem", // 줄 간격 증가
              fontWeight: "medium", // 글씨 굵게
            }}
          >
            <Typography
              variant="h5" // 소제목 크기 설정
              component="div"
              sx={{ mb: 2, fontWeight: "bold" }} // 아래 여백 추가 및 글씨 굵게 설정
            >
              게임 모드
            </Typography>
            <ul>
              <li>
                <strong>스토리모드:</strong> 혼자서 학습할 수 있는
                스토리모드입니다. 다양한 주제의 컨텐츠가 마련되어 있어 게임을
                하기 전 이론적인 학습을 체계적으로 할 수 있습니다.
              </li>
              <li>
                <strong>배틀 모드:</strong> 1 대 1 대결 모드입니다. 서로의
                실력을 겨뤄보세요. 실시간으로 다른 사용자와 대결하며 문제 풀이
                속도와 정확성을 겨룹니다.
              </li>
              <li>
                <strong>멀티 모드:</strong> 여러 명이 동시에 참여할 수 있는
                멀티플레이 모드입니다. 다양한 문제 형식과 팀워크 요소를 통해
                학습의 재미를 더하고, 다양한 학습 스타일에 맞출 수 있습니다.
              </li>
            </ul>
            <hr />
            <Typography
              variant="h5" // 소제목 크기 설정
              component="div"
              sx={{ mt: 3, mb: 2, fontWeight: "bold" }} // 위, 아래 여백 추가 및 글씨 굵게 설정
            >
              추가 기능
            </Typography>
            <ul>
              <li>
                <strong>상점:</strong> 캐릭터 뽑기와 옷입히기를 할 수 있습니다.
                오른쪽 상단의 선물상자를 클릭해보세요!
              </li>
              <li>
                <strong>레포트:</strong> 개인 맞춤형 학습 콘텐츠 추천과 반복
                학습을 통해 부족한 부분을 개선할 수 있는 분석 레포트 기능을
                제공합니다. 프로필 페이지에서 만나보세요!
              </li>
              <li>
                <strong>캐릭터:</strong> 학습에 게임 요소를 도입하여 동기를
                유발하고 재미있는 학습 환경을 제공합니다.
              </li>
            </ul>
            <hr />
            <strong>
              아래 버튼을 클릭하여 문제를 어떻게 풀면 될지 정보를 얻어보세요!
            </strong>
          </Typography>
          <ChildModal />
        </Box>
      </Modal>
    </div>
  );
}
