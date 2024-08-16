import { useDrag } from 'react-dnd';

const Choice = ({ choice }) => {
  const [{ isDragging }, drag] = useDrag({
    type: 'CHOICE',
    item: { choice },
    collect: (monitor) => ({
      isDragging: monitor.isDragging(),
    }),
  });

  const styles = {
    choice: {
      display: 'inline-block',
      minWidth: '100px',
      minHeight: '2.5rem',
      width: 'auto',
      height: 'auto',
      alignContent: 'center',
      textAlign: 'center',
      margin: '5px 5px',
      backgroundColor: isDragging ? 'gray' : 'black',
      color: 'white',
      opacity: isDragging ? 0.5 : 1,
      transition: 'opacity 0.3s ease',
      borderRadius: '5px',
    },
  };

  return (
    <div ref={drag} style={styles.choice}>
      {choice}
    </div>
  );
};

export default Choice;
