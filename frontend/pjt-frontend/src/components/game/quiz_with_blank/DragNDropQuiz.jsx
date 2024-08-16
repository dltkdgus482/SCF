import { useState, useEffect } from "react";
import reactStringReplace from "react-string-replace";
import Choice from "./Choice";
import Blank from "./Blank";
import ChoiceContainer from "./ChoiceContainer";
import { HTML5Backend } from "react-dnd-html5-backend";
import { DndProvider } from "react-dnd";
import StyleToPythonCode from "../StyleToPythonCode.jsx";
import store from "../../../store/store.js";
import "../../../css/BattleFill.css";
import Toast from "../../Toast.jsx";

const DragNDropQuiz = ({ propSubmit }) => {
  const [blanks, setBlanks] = useState({});
  const [choices, setChoices] = useState([]);
  const [choiceMap, setChoiceMap] = useState({}); // choiceId와 choiceText의 매핑
  const [modifiedContent, setModifiedContent] = useState(""); // modifiedContent 상태를 추가
  const [problem, setProblem] = useState(null); // 문제 데이터를 저장할 상태 추가
  // DnD 문제 렌더링 시, 알림창
  const [isToastOpen, setIsToast] = useState(true);

  const { blankSolve, setBlankSolve, myBlankProblem, isSubmit, setIsSubmit } =
    store((state) => ({
      blankSolve: state.blankSolve,
      setBlankSolve: state.setBlankSolve,
      myBlankProblem: state.myBlankProblem,
      isSubmit: state.isSubmit,
      setIsSubmit: state.setIsSubmit,
    }));

  useEffect(() => {
    const problemData = myBlankProblem; // 또는 store.getState().problem과 같은 방식으로 직접 접근할 수 있음
    setProblem(problemData);
  }, [myBlankProblem]);

  useEffect(() => {
    if (problem && problem.problemChoices) {
      // problem.problemChoices 배열에서 choiceText와 choiceId를 매핑합니다.
      const choiceTexts = problem.problemChoices.map(
        (choice) => choice.choiceText
      );
      const choiceMap = problem.problemChoices.reduce((acc, choice) => {
        acc[choice.choiceText] = choice.choiceId;
        return acc;
      }, {});
      setChoices(choiceTexts);
      setChoiceMap(choiceMap);

      // 각 블랭크에 대한 초기화
      const initialBlanks = {};
      for (let i = 1; i <= problem.problemContent.numberOfBlank; i++) {
        initialBlanks[i] = "ssafy"; // 각 블랭크의 초기값을 null로 설정
      }
      setBlanks(initialBlanks);
    }
  }, [problem]);

  useEffect(() => {
    if (Object.keys(blanks).length > 0) {
      setBlankSolve(blanks);
    }
  }, [blanks]);

  useEffect(() => {
    if (problem && problem.problemContent && problem.problemContent.content) {
      const problemContent = problem.problemContent.content;

      const newModifiedContent = reactStringReplace(
        problemContent,
        /\$blank(\d+)\$/g,
        (match, i) => {
          return (
            <Blank key={match} id={match} onDrop={handleDrop}>
              {blanks[match]
                ? choices.find(
                    (choice) =>
                      choice ===
                      Object.keys(choiceMap).find(
                        (key) => choiceMap[key] === blanks[match]
                      )
                  )
                : ""}
            </Blank>
          );
        }
      );

      setModifiedContent(newModifiedContent);
    } else {
      setModifiedContent(""); // problemContent가 없으면 빈 문자열로 설정
    }
  }, [problem, blanks, choices, choiceMap]); // problemContent와 관련된 의존성 배열 업데이트

  const handleDrop = (blankId, choiceText) => {
    const choiceId = choiceMap[choiceText];
    setBlanks((prevBlanks) => ({
      ...prevBlanks,
      [blankId]: choiceId,
    }));
  };

  const styles = {
    quizContainer: {
      marginBottom: "20px",
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
    submitButtonHover: {
      backgroundColor: "#0056b3",
    },
  };

  return (
    <>
      <div className="fill-container">
        {/* {isToastOpen && <Toast text="보기를 드래그하여 빈칸을 채워주세요" setToast={setIsToast} style={styles.toast} />} */}

        <DndProvider backend={HTML5Backend}>
          <div className="fill-problem">
            {modifiedContent && (
              <StyleToPythonCode codeString={modifiedContent} />
            )}
          </div>

          <div className="fill-choices">
            <ChoiceContainer>
              {choices.map((choice, idx) => {
                return <Choice key={`choice-${idx}`} choice={choice} />;
              })}
            </ChoiceContainer>
          </div>
        </DndProvider>
        <div className="dragndrop-game-choice-button-container">
          <button
            className="dragndrop-game-choice-button"
            onClick={() => {
              propSubmit();
              setIsSubmit(true);
            }}
          >
            제출
          </button>
        </div>
      </div>
    </>
  );
};

export default DragNDropQuiz;
