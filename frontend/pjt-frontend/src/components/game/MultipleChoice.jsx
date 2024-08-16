import React, { useState } from 'react';
import '../../css/Problem.css';
import StyleToPythonCode from "./StyleToPythonCode";

const MultipleChoice = ({ problem, onChoiceSelect  }) => {
  const [selectedChoice, setSelectedChoice] = useState(null);

  const handleChoiceClick = (choiceId) => {
    setSelectedChoice(choiceId);
    onChoiceSelect(choiceId);  // choiceId를 부모 컴포넌트로 전달
  };

  return (
    <>
      <div className='problem-container'>
          {<StyleToPythonCode codeString={problem.problemContent.content} />}
        <div className='choice-container'>
          {problem.problemChoices.map((choice) => (
            <button
            className='choice-button'
            key={choice.choiceId}
            onClick={() => handleChoiceClick(choice.choiceId)}
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
