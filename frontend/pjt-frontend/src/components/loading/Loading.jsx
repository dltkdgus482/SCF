import { fontSize, style, textAlign, width } from '@mui/system';

const Loading = () => {
  const sampleTips = [
    '배열의 인덱스는 0부터 시작합니다.',
    '=는 값을 할당하고 ==는 값을 비교합니다.',
    '의미 있는 변수 이름이 코드를 더 이해하기 쉽게 만듭니다.',
    '세미콜론(;)은 많은 언어에서 문장의 끝을 표시합니다.',
    '공백과 들여쓰기는 코드의 가독성을 높입니다.',
    '함수는 코드의 재사용성을 높여줍니다.',
    '주석을 사용하여 코드의 중요한 부분을 설명할 수 있습니다.',
    '효율적인 코드를 위해 알고리즘과 자료구조를 공부하는 것이 중요합니다.',
  ];
  const characters = {
    1: { moving: '/characters/movingFireSlime.gif', stop: '/characters/fireSlime.png' },
    2: { moving: '/characters/movingGreenSlime.gif', stop: '/characters/greenSlime.png' },
    3: { moving: '/characters/movingIceSlime.gif', stop: '/characters/iceSlime.png' },
    4: { moving: '/characters/movingThunderSlime.gif', stop: '/characters/thunderSlime.png' },
    5: { moving: '/characters/movingNyanSlime.gif', stop: '/characters/fireSlime.png' },
  };
  const tipNum = Math.floor(Math.random() * sampleTips.length);
  const characterNum = Math.floor(Math.random() * Object.keys(characters).length) + 1;
  return (
    <div style={styles.loading}>
      <div style={styles.tip}>
        <p>알고 계셨나요?</p>
        <p>{sampleTips[tipNum]}</p>
      </div>
      <div style={styles.loadingMessage}>
        <p>Now Loading...</p>
        <img style={styles.characterContainer} src={characters[characterNum]?.moving} alt={characterNum} />
      </div>
    </div>
  );
};

const styles = {
  loading: {
    width: '100%',
    height: '100vh',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(0, 0, 0, 0.9)',
    fontSize: '25px',
    color: 'white',
  },
  characterContainer: {
    position: 'absolutes',
    bottom: '10vh',
    width: '100px',
    overflow: 'hidden',
    margin: '10px',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
  },
  loadingMessage: {
    position: 'fixed',
    bottom: '5vh',
    right: '5vw',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'end',
    whiteSpace : 'nowrap',
  },
  tip: {
    textAlign: 'center',
    width : '100%',
  },
};

export default Loading;
