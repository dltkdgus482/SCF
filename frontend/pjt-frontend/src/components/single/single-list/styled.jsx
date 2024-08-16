import styled from 'styled-components';

const Row = styled.div`
  display: flex;
  flex-direction: ${(props) => (props.$rowidx % 2 ? 'row-reverse' : 'row')};
  justify-content: center;
  border-radius: 10px;
  // background-color: rgba(83, 92, 145, 0.9);
  align-items: center;
  height: 70px;
  width: 100%;
`;
const RowBeetween = styled.div`
  height: 50px;
  width: 100%;
`;

const CheckPoint = styled.div`
  position: relative;
  width: 170px;
  height: 100%;
  background-color: red;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 10%;
  transition: background-color 0.3s;
  background-color: ${(props) => (props.$completed ? '#02285A' : 'gray')};
  border: ${(props) => (props.$isNext ? '2px solid white' : null)};
  &:hover {
    background-color: rgba(83, 92, 145, 1);
  }
`;

const Path = styled.div`
  width: 50px;
  height: 10px;
  background-color: ${(props) => (props.$completed ? '#4F4F4F' : 'gray')};
  position: relative;
  transition: background-color 0.3s;

  &::after {
    content: '';
    position: absolute;
    top: 50%;
    left: 0;
    width: 100%;
    height: 2px;
    background-image: linear-gradient(to right, white 50%, transparent 50%);
    background-size: 10px 2px;
    transform: translateY(-50%);
  }
`;

const VerticalPath = styled.div`
  width: 10px;
  height: 50px;
  background-color: ${(props) => (props.$completed ? '#4F4F4F' : 'gray')};
  transition: background-color 0.3s;
  position: absolute;
  top: 100%;

  &::after {
    content: '';
    position: absolute;
    left: 50%;
    top: 0;
    width: 2px;
    height: 100%;
    background-image: linear-gradient(to bottom, white 50%, transparent 50%);
    background-size: 2px 10px;
    transform: translateX(-50%);
  }
`;
const CharacterImage = styled.div`
  position: absolute;
  width: 100px;
  height: 100px;
  top: -120%
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
const S = {
  Row,
  CheckPoint,
  Path,
  RowBeetween,
  VerticalPath,
  CharacterImage,
  CompleteButton,
  CancelButton,
 
};

export default S;
