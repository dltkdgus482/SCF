import React from 'react';

const ShortAnswerQuestion = ({ problem }) => {
  const content = `??? ${problem.problemContent.content}`;

  return (
    <div>
      <h2>{problem.title}</h2>
      <p>{content}</p>
      <input type="text" placeholder="Your answer" />
    </div>
  );
};

export default ShortAnswerQuestion;
