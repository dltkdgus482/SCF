import styled from 'styled-components';

const PageBody = styled.div`
  display: flex;
  margin-top: 15px;
  justify-content: center;
  height: 100vh;
`;


const Container = styled.div`
  // display : flex;
  text-align: center;
  justify-content: center;
  background-color: rgba(255, 255, 255, 0.5);
  width: 80vw;
  margin: 20px;
  margin-top: 30px;
  padding: 20px;
  border-radius: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
`;
const FlexContainer = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
`;

const RankingInfoSection = styled.div`
  text-align: center;
  // width : 200px;
  margin-bottom: 20px;
  margin-right: 60px;
  font-size: 1.2em;
  color: #495057;
`;

const RankingListSection = styled.div`
  // width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  overflow-y: auto;
  height: 500px;
  /* 스크롤바의 폭 너비 */
  &::-webkit-scrollbar {
    width: 10px;
  }

  /* 스크롤바 막대 */
  &::-webkit-scrollbar-thumb {
    background: linear-gradient(
      to bottom,
      rgb(148, 226, 148),
      rgb(72, 129, 199),
      rgb(229, 141, 229),
      pink,
      rgb(228, 103, 103)
    );
    border-radius: 10px;
  }

  &::-webkit-scrollbar-track {
    background: rgba(214, 193, 212, 0.1);
  }

  &::-webkit-scrollbar-thumb:hover {
    background: linear-gradient(
      to bottom,
      rgb(148, 226, 148),
      rgb(72, 129, 199),
      rgb(229, 141, 229),
      pink,
      rgb(228, 103, 103)
    );
  }
`;

const RankingInfoContainer = styled.div`
  display: flex;
  flex-wrap: nowrap;
  justify-content: center;
  align-items: center;
  width: 100%;
  margin-bottom: 20px;
  gap: 15px; /* 버튼들 간의 간격을 추가 */
`;

const RankingInfo = styled.div`
  flex: 1;
  padding: 10px;
  text-align: center;
  color: #343a40;
`;

const RankingList = styled.div`
  width: 100%;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
`;

const RankingItem = styled.div`
  padding: 10px;
  border-bottom: 1px solid #dee2e6;
  &:last-child {
    border-bottom: none;
  }
  &:hover {
    background-color: #f1f3f5;
  }
`;

const GraphContainer = styled.div`
  width: 100%;
  height: 50vh;
  margin: 20px 0;
`;

const PeriodButton = styled.button`
  padding: 15px 25px;
  font-size: 1.5rem;
  font-weight: bold;
  letter-spacing: 2px;
  color: white;
  background: ${(props) =>
    props.$isSelected
      ? 'linear-gradient(135deg, #ff7f50 0%, #ff4500 100%)' // 선택된 경우의 색상
      : 'linear-gradient(135deg, #6b73ff 0%, #000dff 100%)'}; // 기본 색상
  border: none;
  border-radius: 30px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
  cursor: pointer;
  transition: all 0.3s ease;
  margin: 0 10px; /* 버튼 간 간격 */

  &:hover {
    background: ${(props) =>
      props.$isSelected
        ? 'linear-gradient(135deg, #ff6347 0%, #ff2400 100%)' // 선택된 상태에서의 호버 색상
        : 'linear-gradient(135deg, #5a63e0 0%, #000ae0 100%)'}; // 일반 상태에서의 호버 색상
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.4);
    transform: translateY(-2px);
  }

  &:active {
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
    transform: translateY(0);
  }
`;

const Title = styled.h1`
  color: #ffffff;
  font-size: 2.5rem;
  margin-bottom: 20px;
`;

const S = {
  FlexContainer,
  RankingInfoContainer,
  RankingInfo,
  RankingList,
  RankingItem,
  GraphContainer,
  RankingInfoSection,
  RankingListSection,
  PeriodButton,
  Container,
  PageBody,
  Title,
};

export default S;
