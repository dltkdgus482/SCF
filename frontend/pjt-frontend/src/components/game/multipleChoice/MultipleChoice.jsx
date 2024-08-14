import { useState, useEffect } from "react";
import reactStringReplace from "react-string-replace";
import StyleToPythonCode from "../StyleToPythonCode";
import "../../../css/MultiGame.css";
import store from "../../../store/store.js";
import {
  bgcolor,
  color,
  display,
  fontSize,
  fontWeight,
  height,
  letterSpacing,
  maxHeight,
  maxWidth,
  textAlign,
} from "@mui/system";

const styles = {
  codeContainer: {
    width: "43vw",
    minHeight: "16%",
    height: "16%",
    maxHeight: "16%",
    margin: "0 auto",
    padding: "0",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
  },
  choiceContainer: {
    width: "43vw",
    display: "grid",
    gridTemplateColumns: "1fr",
    gap: "10px",
    margin: "0 auto",
    mt: "20px",
    whiteSpace: "normal",
    wordWrap: "break-word",
    minHeight: "10%",
    height: "10%",
    maxHeight: "10%",
  },
  choiceButton: {
    display: "inline-block",
    padding: "10px 10px",
    fontFamily: "'Fira Code', Consolas, 'Courier New', Courier, monospace",
    fontSize: "1.2rem",
    margin: "5px 0",
    fontWeight: "bold",
    color: "white",
    border: "1px solid white",
    borderRadius: "5px",
    letterSpacing: "1px",
    textAlign: "center",
    maxWidth: "95%",
    wordWrap: "break-word",
  },
  submitButton: {
    display: "inline-block",
    padding: "10px 20px",
    fontSize: "16px",
    color: "#fff",
    backgroundColor: "#007bff",
    border: "none",
    borderRadius: "5px",
    marginTop: "20px",
  },
};

const MultipleChoice = () => {
  const [problem, setProblem] = useState(null); // 문제 데이터를 저장할 상태 추가
  const [modifiedContent, setModifiedContent] = useState(""); // modifiedContent 상태를 추가
  const [selectedChoice, setSelectedChoice] = useState(null); // 선택된 choiceId를 저장할 상태 추가

  const {
    multipleChoiceSolve,
    setMultipleChoiceSolve,
    myMultipleChoiceProblem,
  } = store((state) => ({
    multipleChoiceSolve: state.multipleChoiceSolve,
    setMultipleChoiceSolve: state.setMultipleChoiceSolve,
    myMultipleChoiceProblem: state.myMultipleChoiceProblem,
  }));

  useEffect(() => {
    // store에서 문제 데이터를 가져오는 함수 호출
    const problemData = myMultipleChoiceProblem; // 또는 store.getState().problem과 같은 방식으로 직접 접근할 수 있음
    setProblem(problemData);
  }, [myMultipleChoiceProblem]);

  useEffect(() => {
    if (problem && problem.problemContent && problem.problemContent.content) {
      const problemContent = problem.problemContent.content;
      const newModifiedContent = reactStringReplace(
        problemContent,
        /\$blank(\d+)\$/g,
        (match, i) => (
          <span key={match}>
            <input
              style={styles.answerInput}
              type="text"
              value={selectedChoice === match ? multipleChoiceSolve : ""}
              onChange={(e) => handleChoiceSelect(match, e.target.value)}
            />
          </span>
        )
      );
      setModifiedContent(newModifiedContent);
    } else {
      setModifiedContent(""); // problemContent가 없으면 빈 문자열로 설정
    }
  }, [problem]);

  const handleChoiceSelect = (choiceId) => {
    setSelectedChoice(choiceId); // 선택된 choiceId를 상태에 저장
    setMultipleChoiceSolve(choiceId); // 선택된 값을 로컬 저장소에 저장
  };

  return (
    <>
      <div className="multi-game-choice-problem-outer-container">
        <div style={styles.codeContainer}>
          {modifiedContent && (
            <StyleToPythonCode codeString={modifiedContent} />
          )}
        </div>
        <div className="multi-game-choice-temp-problem-container">
          {problem &&
            problem.problemChoices &&
            problem.problemChoices.map((choice, index) => (
              <button
                className="multi-game-choice-button-new"
                key={choice.choiceId}
                onClick={() => handleChoiceSelect(choice.choiceId)}
              >
                {choice.choiceText}
              </button>
            ))}
        </div>
      </div>
    </>
  );
};

export default MultipleChoice;
