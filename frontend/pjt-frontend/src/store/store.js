import { create } from "zustand";
import { persist } from "zustand/middleware";
//
const testBlankProblem = {
  problemId: 5,
  title: "파이썬 반복문 활용 문제",
  problemType: "FILL_IN_THE_BLANK",
  category: "Programming",
  difficulty: 3,
  problemContent: {
    problemId: 5,
    content:
      "다음 파이썬 코드를 완성하세요.\n# 리스트의 요소를 출력하는 반복문\nnumbers = [1, 2, 3, 4, 5]\nfor _____ in _____:\n    print(num)\n빈칸에 알맞은 내용을 채우세요.",
    numberOfBlanks: 2,
  },
  problemChoices: [
    {
      choiceId: 14,
      problemId: 5,
      choiceText: "number",
    },
    {
      choiceId: 13,
      problemId: 5,
      choiceText: "num",
    },
    {
      choiceId: 15,
      problemId: 5,
      choiceText: "numbers",
    },
    {
      choiceId: 17,
      problemId: 5,
      choiceText: "i",
    },
    {
      choiceId: 16,
      problemId: 5,
      choiceText: "nums",
    },
  ],
};

const testMultipleChoiceProblem = {
  problemId: 2,
  title: "배열의 최대값 찾기",
  problemType: "MULTIPLE_CHOICE",
  category: "알고리즘",
  difficulty: 2,
  problemContent: {
    problemId: 2,
    content:
      "다음 파이썬 코드를 완성하세요.\n# 리스트의 요소를 출력하는 반복문\nnumbers = [1, 2, 3, 4, 5]\nfor _____ in _____:\n    print(num)\n빈칸에 알맞은 내용을 채우세요. ",
    numberOfBlanks: 0,
  },
  problemChoices: [
    {
      choiceId: 4,
      problemId: 2,
      choiceText: "배열의 요소를 모두 더한 값을 반환한다.",
    },
    {
      choiceId: 1,
      problemId: 2,
      choiceText: "배열을 정렬한 후 마지막 요소를 반환한다.",
    },
    {
      choiceId: 2,
      problemId: 2,
      choiceText: "배열의 각 요소를 반복하며 가장 큰 값을 추적하여 반환한다.",
    },
    {
      choiceId: 3,
      problemId: 2,
      choiceText:
        "배열의 첫 번째 요소를 최대값으로 설정하고, 배열을 순회하며 최대값을 갱신한다.",
    },
  ],
};

const testShortAnswerProblem = {
  problemId: 8,
  title: "문자열 슬라이싱 문제",
  problemType: "SHORT_ANSWER_QUESTION",
  category: "문자열",
  difficulty: 1,
  problemContent: {
    problemId: 8,
    content:
      "# 다음 코드를 실행했을 때 출력될 값을 적으시오.\ns = 'Python Programming'\nprint(s[0:6])",
    numberOfBlanks: 0,
  },
  problemChoices: [],
};
const store = create(
  persist(
    (set) => ({
      baseURL: "https://www.ssafy11s.com",
      // baseURL: "http://localhost:8080",

      authClient: "",
      setAuthClient: (authClient) => set({ authClient }),

      wsBattle: "ws-battle",
      wsChat: "ws-chat",

      memberId: "",
      setMemberId: (memberId) => set({ memberId }),

      userId: "",
      setUserId: (userId) => set({ userId }),

      name: "",
      setName: (name) => set({ name }),

      email: "",
      setEmail: (email) => set({ email }),

      schoolName: "",
      setSchoolName: (schoolName) => set({ schoolName }),

      birth: "",
      setBirth: (birth) => set({ birth }),

      accessToken: "",
      setAccessToken: (accessToken) => {
        set({ accessToken });
      },

      exp: "",
      setExp: (exp) => set({ exp }),

      normalQuit: false,
      setNormalQuit: (normalQuit) => set({ normalQuit }),

      registerInfo: {
        userId: "",
        name: "",
        password: "",
        email: "",
        schoolName: "",
        birth: "",
        characterType: "",
      },
      setRegisterInfo: (registerInfo) => set({ registerInfo }),

      code: "",
      setCode: (code) => set({ code }),

      character: "",
      setCharacter: (character) => set({ character }),

      roomId: "",
      setRoomId: (roomId) => set({ roomId }),

      hostId: "",
      setHostId: (hostId) => set({ hostId }),

      roomPassword: "",
      setRoomPassword: (roomPassword) => set({ roomPassword }),

      enemyId: "",
      setEnemyId: (enemyId) => set({ enemyId }),

      enemyName: "",
      setEnemyName: (enemyName) => set({ enemyName }),

      enemyCharacterType: "",
      setEnemyCharacterType: (enemyCharacterType) =>
        set({ enemyCharacterType }),

      rarity: "",
      setRarity: (rarity) => set({ rarity }),

      characterRarity: "",
      setCharacterRarity: (characterRarity) => set({ characterRarity }),

      characterClothRarity: "",
      setClothRarity: (characterClothRarity) => set({ characterClothRarity }),

      blankSolve: {},
      setBlankSolve: (blankSolve) => set({ blankSolve }),

      myBlankProblem: testBlankProblem,
      setMyBlankProblem: (myBlankProblem) => set({ myBlankProblem }),

      shortAnswerSolve: "",
      setShortAnswerSolve: (shortAnswerSolve) => set({ shortAnswerSolve }),

      myShortAnswerProblem: testShortAnswerProblem,
      setMyShortAnswerProblem: (myShortAnswerProblem) =>
        set({ myShortAnswerProblem }),

      multipleChoiceSolve: -1,
      setMultipleChoiceSolve: (multipleChoiceSolve) =>
        set({ multipleChoiceSolve }),

      myMultipleChoiceProblem: testMultipleChoiceProblem,
      setMyMultipleChoiceProblem: (myMultipleChoiceProblem) =>
        set({ myMultipleChoiceProblem }),

      isSubmit: false,
      setIsSubmit: (isSubmit) => set({ isSubmit }),

      clearLocalStorage: () => {
        localStorage.clear();
      },
    }),
    {
      name: "userStorage",
      getStorage: () => localStorage,
    }
  )
);

export default store;
