import { borderRadius } from '@mui/system';
import { useDrop } from 'react-dnd';

const Blank = ({ id, onDrop, children }) => {
  const [{ isOver }, drop] = useDrop({
    accept: 'CHOICE',
    drop: (item) => {
      onDrop(id, item.choice);
    },
    collect: (monitor) => ({
      isOver: monitor.isOver(),
    }),
  });

  // 색깔 결정 로직
  let backgroundColor;
  if (isOver) {
    backgroundColor = 'lightblue'; // 빈칸이 채워졌을 때
  } else if (children) {
    backgroundColor = 'lightgreen'; // isOver일 때
  } else {
    backgroundColor = 'white'; // 기본 색깔
  }
  let width, height;
  if (isOver) {
    width = '100px';
    height = '2.3em';
  } else if (children) {
    width = '85px';
    height = '2em';
  } else {
    width = '85px';
    height = '2em';
  }
  const styles = {
    blank: {
      display: 'inline-block',
      width: width,
      height: height,
      backgroundColor: backgroundColor,
      border: '1px solid black',
      margin: '0 2px',
      padding: '3px',
      color: 'black',
      textAlign: 'center',
      alignContent : 'center',
      transition: 'all 0.3s ease',
      borderRadius: '5px',
    },
  };

  return (
    <span ref={drop} style={styles.blank}>
      {children || '       '}
    </span>
  );
};

export default Blank;
