import { create } from "zustand";

const multiStore = create((set) => ({
  // 방정보
  roomId: null,
  setRoomId: (roomId) => set({ roomId }),

  // 입장 패스워드
  password: null,
  setPassword: (password) => set({ password }),

  // 게임중인지 여부
  playing: false,
  setPlaying: (playing) => set({ playing }),

  // 문제 리스트
  problemList: [{
    "problemId": null,
    "title": "",
    "problemType": "",
    "category": "",
    "difficulty": null,
    "problemContent": {
        "problemId": null,
        "content": "",
        "numberOfBlanks": null
    },
    "problemChoices": [],
    "problemAnswers": [
        {
            "answerId": null,
            "problemId": null,
            "blankPosition": null,
            "correctChoice": {
                "choiceId": null,
                "problemId": null,
                "choiceText": ""
            },
            "correctAnswerText": ""
        }
    ]
  }], 
  setProblemList: (problems) => set({ problemList: problems }),
  clearProblemList: () =>
    set({
      problemList: [
        {
          "problemId": null,
          "title": "",
          "problemType": "",
          "category": "",
          "difficulty": null,
          "problemContent": {
              "problemId": null,
              "content": "",
              "numberOfBlanks": null
          },
          "problemChoices": [],
          "problemAnswers": [
              {
                  "answerId": null,
                  "problemId": null,
                  "blankPosition": null,
                  "correctChoice": {
                      "choiceId": null,
                      "problemId": null,
                      "choiceText": ""
                  },
                  "correctAnswerText": ""
              }
          ]
        }
      ],
    }),

  // 문제 타입
  type: "",
  setType: (type) => set({ type }),
  clearType: () => set({ type: "" }),

  // 현재라운드
  currentRound: 0,
  setCurrentRound: (currentRound) => set({ currentRound }),
  clearCurrentRound: () => set({ currentRound: 0 }),

  // 소켓에 연결된 플레이어리스트
  playerList: [],
  setPlayerList: (playerList) => set({ playerList }),
  clearPlayerList: () => set({ playerList: [] }),

  // 라운드 랭크
  roundRank: [],
  setRoundRank: (roundRank) => set({ roundRank }),
  clearRoundRank: () => set({ roundRank: [] }),

  // 게임랭크
  gameRank: [],
  setGameRank: (gameRank) => set({ gameRank }),
  clearGameRank: () => set({ gameRank: [] }),

  // 제출 리스트
  submitList: [],
  setSubmitList: (submitList) => set({ submitList }),
  clearSubmitList: () => set({ submitList: [] }),

  // 빈칸 답변
  blankSolve: null,
  setBlankSolve: (blankSolve) => set({ blankSolve }),
  clearBlankSolve: () => set({ blankSolve: null }),

  // 얻은 점수
  getScore: 0,
  setGetScore: (getScore) => set({ getScore }),
}));

export default multiStore;
