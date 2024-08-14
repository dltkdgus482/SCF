import styled, { keyframes, css } from 'styled-components';

const PlayView = styled.div`
  position: fixed;
  top: 0px;
  left: 0px;
  width: 100vw;
  height: 100vh;
  background-image: url('/background-single.png');
  background-size: cover;
  color: black;
  z-index: 0;
  -webkit-user-select: none;
`;

const ImageBox = styled.div`
  position: fixed;
  top: 0px;
  left: 0px;
  width: 100vw;
  height: 100%;
`;
const DialogueSection = styled.div`
  width: 100%;
  height: 250px;
  background-color: #212121;
  position: fixed;
  bottom: 0px;
  background: linear-gradient(rgba(240, 240, 240, 0), rgba(25, 42, 81, 0.7), rgba(25, 42, 81, 0.9));
`;
const DialogueBox = styled.div`
  position: fixed;
  bottom: 10px;
  left: 5vw;
  width: 70vw;
  height: 180px;
  background-color: rgba(240, 240, 240, 0.9);
  border-radius: 0.4em;
  font-size: 1.3em;
  &:after {
    content: '';
    position: absolute;
    left: 0;
    top: 85%;
    width: 0;
    height: 0;
    border: 20px solid transparent;
    border-right-color: rgba(240, 240, 240, 0.9);
    border-left: 0;
    border-top: 0;
    margin-top: -10px;
    margin-left: -20px;
  }
`;

const DialogueHeader = styled.div`
  // display: flex;
  justify-content: space-between;
`;

const DialogueButton = styled.button`
  /* 버튼 스타일을 여기에 추가하세요 */
`;
const DialogueBody = styled.div`
  /* 버튼 스타일을 여기에 추가하세요 */
  display: flex;
  padding: 10px;
  // justify-content: space-between;
`;
const DialogueBodyLeft = styled.div`
margin-top: 28px;
font-size:1.3em;
padding-left : 10px;`;
const DialogueBodyRight = styled.div`
  position: fixed;
  height: 180px;
  left: 78vw;
  width : 100%;
  bottom: 10px;
  color: white;
  display : flex;
  flex-direction: column;
  
  // background-color: rgba(240, 240, 240, 0.9);
`;
const DialogueContent = styled.div`
  /* 버튼 스타일을 여기에 추가하세요 */
  margin-top: 13px;
  width: 100%;
`;
// 캐릭터 움직이는 애니메이션 추가...!
const vibration = keyframes`
  from {
    transform: rotate(1deg);
  }
  to {
    transform: rotate(-1deg);
`;

const vibratingAnimation = css`
  animation: ${vibration} 0.1s infinite;
`;

// 캐릭터 클로즈업 애니메이션 추가
const closeUp = keyframes`
  from {
    transform: scale(1);
  }
  to {
    transform: scale(1.5);
  }
`;


const closeUpAnimation = css`
    animation : ${closeUp} 0.5s forwards;
`; 


const CharacterImage = styled.img`
  ${(props) => (props.$isVibrating ? vibratingAnimation : null)};
  ${props => props.$isCloseUp ? closeUpAnimation : null};

  position: fixed;
  height: 70vh;
  bottom: 0px;
`;

const CharacterName = styled.div`
  position: absolute;
  top: -15px;
  left: 50px;
  height: 30px;
  font-size: 1em;
  border-radius: 10px;
  border: 1px solid black;
  background-color: rgba(240, 240, 240);
  padding: 5px;
  padding-right: 15px;
  padding-left: 15px;
`;

const ImageContentBox = styled.div`
  position: absolute;
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: 0.4em;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 10px;
`;
const MenuContainer = styled.div`
  position: fixed;
  display: flex;
  top: 10px;
  right: 10px;
`;
const MenuButton = styled.div`
  width: 80px;
  height: 80px;

  background-color: #e4f0f6;
  color: #466398;
  border-radius: 40%;
  border: 2px solid #466398;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 50px;
  margin: 0px 10px;
  &:hover {
    background-color: #466398;
    color: #e4f0f6;
  }
`;
const ModalHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
`;
const ModalContent = styled.div`
  text-align: center;
`;
const ModalButtons = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 1rem;
`;
const CompleteButton = styled.button`
  margin-right: 1rem;
  padding: 10px 20px;
  background-color: #76dcfe;
  color: #233551;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  &:hover {
    background-color: #466398;
    color: #e4f0f6;
  }
`;
const CancelButton = styled.button`
  padding: 10px 20px;
  background-color: #d1e7ef;
  color: #233551;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  &:hover {
    background-color: #466398;
    color: #e4f0f6;
  }
`;
const ChatButton = styled.button`
  position: absolute;
  top: 20px;
  left: 20px;
  z-index: 1000;
  cursor: pointer;
  border: 1px solid black;
  border-radius: 5px;
  padding: 10px;
  background: #fff;
  &:hover {
    background-color: #466398;
    color: #e4f0f6;
  }
`;

const S = {
  PlayView,
  DialogueSection,
  DialogueBody,
  DialogueBodyLeft,
  DialogueBodyRight,
  DialogueBox,
  DialogueButton,
  DialogueHeader,
  DialogueContent,
  CharacterImage,
  CharacterName,
  ImageBox,
  ImageContentBox,
  MenuContainer,
  MenuButton,
  ModalButtons,
  CompleteButton,
  CancelButton,
  ModalHeader,
  ModalContent,
  ChatButton,
};
export default S;
