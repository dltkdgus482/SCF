
import { borderRadius, fontSize, height, minWidth, padding, style } from '@mui/system';
import StyleToPythonCode from '../StyleToPythonCode';
import "../../../css/MultiGame.css";
import { useState } from 'react';

const styles = {
  codeContainer: {
    width: '35vw',
    height: '60%',
    margin: '0 auto',
    padding: '1rem',
    borderRadius: '5px',
    boxShadow: '0 0 5px rgba(0, 0, 0, 0.2)',
  },
  answerInput: {
    width: '20vw', 
    height: '20%',
    padding : '10px',
    paddingLeft: '20px',
    fontSize: '1.5rem',
    margin: '5% 0',
  },
  inputDiv: {
    display: 'flex',
    justifyContent: 'space-around',
    alignItems: 'center',
    margin: '10px',
    borderRadius: '5px',
  },
  submitButton: {
    display: 'inline-block',
    padding: '10px 20px',
    fontSize: '16px',
    color: '#fff',
    backgroundColor: '#007bff',
    border: 'none',
    borderRadius: '5px',
    // marginTop: '20px',
    marginLeft: '20px',
  },
};
const MultiShortAnswer = ({ problem, onShortAnswer }) => {
  const [answer, setAnswer] = useState('');

  const onInputChange = (e) =>{
    setAnswer(e.target.value)
  }

  const handleKeyDown = (e) => {
    if(e.key == 'Enter'){
      handleSubmit();
    }
  }

  const handleSubmit = () => {
    onShortAnswer(answer);
    // alert(`${answer}제출됨`)
  };
  return (
    <>
      <div className='multi-game-playing'>
        <div style={styles.codeContainer}>
          <StyleToPythonCode codeString={problem.problemContent.content} />
        </div>
        <div style={styles.inputDiv}>
          <input style={styles.answerInput} type="text" onChange={onInputChange} onKeyDown={handleKeyDown} />
          <button className='multi-button' onClick={handleSubmit}>제출</button>
        </div>
      </div>  
    </>
  );
};

export default MultiShortAnswer;
