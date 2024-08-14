import React, { useState } from 'react';
import '../../css/Problem.css';

const MultipleChoice = ({ problem, onChoiceSelect  }) => {
  const [selectedChoice, setSelectedChoice] = useState(null);

  const handleChoiceClick = (choiceId) => {
    setSelectedChoice(choiceId);
    onChoiceSelect(choiceId);  // choiceId를 부모 컴포넌트로 전달
  };

  return (
    <>
      <div className='problem-container'>
        <div className='problem-content'>
          <h2>{problem.title}</h2>
          <p>{problem.problemContent.content}</p>
        </div>
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
    </>
  );
};

export default MultipleChoice;
