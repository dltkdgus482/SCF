import { useState, useEffect } from 'react';
import reactStringReplace from 'react-string-replace';
import Choice from './Choice';
import Blank from './Blank';
import ChoiceContainer from './ChoiceContainer';
import { HTML5Backend } from 'react-dnd-html5-backend';
import { DndProvider } from 'react-dnd';
import StyleToPythonCode from './StyleToFillCode.jsx';
import multiStore from '../../../stores/multiStore.jsx';
import '../../../css/MultiGame.css';
import '../../../css/Fill.css';
import Toast from '../../Toast.jsx';

const FillInTheBlank = ({ problem, onFillBlank }) => {
  const [blanks, setBlanks] = useState({});
  const [choices, setChoices] = useState([]);
  const [choiceMap, setChoiceMap] = useState({}); // choiceId와 choiceText의 매핑
  const [modifiedContent, setModifiedContent] = useState(''); // modifiedContent 상태를 추가
  const [problemData, setProblemData] = useState(problem);
  // DnD 문제 렌더링 시, 알림창
  const [isToastOpen, setIsToast] = useState(true);
  const { blankSolve, setBlankSolve } = multiStore((state) => ({
    blankSolve: state.blankSolve,
    setBlankSolve: state.setBlankSolve,
  }));

  useEffect(() => {
    if (problemData && problemData.problemChoices) {
      // problem.problemChoices 배열에서 choiceText와 choiceId를 매핑합니다.
      const choiceTexts = problemData.problemChoices.map((choice) => choice.choiceText);
      const choiceMap = problemData.problemChoices.reduce((acc, choice) => {
        acc[choice.choiceText] = choice.choiceId;
        return acc;
      }, {});
      setChoices(choiceTexts);
      setChoiceMap(choiceMap);

      // 각 블랭크에 대한 초기화
      const initialBlanks = {};
      for (let i = 1; i <= problemData.problemContent.numberOfBlank; i++) {
        initialBlanks[i] = 'ssafy'; // 각 블랭크의 초기값을 null로 설정
      }
      setBlanks(initialBlanks);
    }
  }, [problemData]);

  useEffect(() => {
    if (Object.keys(blanks).length > 0) {
      setBlankSolve(blanks);
    }
  }, [blanks]);

  useEffect(() => {
    if (problemData && problemData.problemContent && problemData.problemContent.content) {
      const problemContent = problemData.problemContent.content;

      const newModifiedContent = reactStringReplace(problemContent, /\$blank(\d+)\$/g, (match, i) => {
        return (
          <Blank key={match} id={match} onDrop={handleDrop}>
            {blanks[match]
              ? choices.find(
                  (choice) => choice === Object.keys(choiceMap).find((key) => choiceMap[key] === blanks[match])
                )
              : ''}
          </Blank>
        );
      });

      setModifiedContent(newModifiedContent);
    } else {
      setModifiedContent(''); // problemContent가 없으면 빈 문자열로 설정
    }
  }, [problemData, blanks, choices, choiceMap]); // problemContent와 관련된 의존성 배열 업데이트

  const handleDrop = (blankId, choiceText) => {
    const choiceId = choiceMap[choiceText];
    setBlanks((prevBlanks) => ({
      ...prevBlanks,
      [blankId]: choiceId,
    }));
  };

  const handleSubmit = () => {
    onFillBlank();
  };

  return (
    <>
      <div className="fill-container">
        {isToastOpen && <Toast text="보기를 드래그하여 빈칸을 채워주세요" setToast={setIsToast} style={styles.toast} />}
        <DndProvider backend={HTML5Backend}>
          <div className="fill-problem">{modifiedContent && <StyleToPythonCode codeString={modifiedContent} />}</div>

          <div className="fill-choices">
            <ChoiceContainer>
              {choices.map((choice, idx) => {
                return <Choice key={`choice-${idx}`} choice={choice} />;
              })}
            </ChoiceContainer>
          </div>
        </DndProvider>
        <div className="fill-submit">
          <button className="fill-button" onClick={handleSubmit}>
            답안 제출
          </button>
        </div>
      </div>
    </>
  );
};

const styles = {
  toast: {
    position: 'absolute',
    top: '0px',
    left: '50%',
    width: '100%',
    textAlign: 'center',
    transform: 'translate(-50%, -50%)',
    zIndex: 1000,
    backgroundColor: 'rgba(0, 0, 0, 0.7)',
    color: 'white',
    padding: '10px 20px',
    borderRadius: '10px',
    whiteSpace: 'pre-wrap',
    fontSize: '1rem',
    boxShadow: '0 10px 20px rgba(0, 0, 0, 0.15)',
    transition: 'opacity 0.3s ease-in-out',
  },
};

export default FillInTheBlank;
