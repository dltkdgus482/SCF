import styled from 'styled-components';
import { LeaderBoardPlayerLayouts as P } from './RankingListItem';
import useLeaderboardStore from '../../stores/LeaderboardStore.jsx';

const RankingGraph = () => {
  const { rankingList, setRankingList, boardPeriod, setBoardPeriod } = useLeaderboardStore();


  const PodiumPlayer = ({ rank }) => {
    const player = rankingList[boardPeriod]?.[rank];
    if (!player) {
      return <NoPlayerData>No player data</NoPlayerData>;
    }
    return (
      <>
        <ProfileImageContainer rank={rank + 1} />
        <P.Name>{player.name}</P.Name>
        <P.Exp>{player.exp}</P.Exp>
      </>
    );
  };

  return (
    <GraphContainer>
      <LeaderBoardBar rank={2}>
        <PodiumPlayerContainer>
          <PodiumPlayer rank={1} />
        </PodiumPlayerContainer>
        <PlaceHolder>2</PlaceHolder>
      </LeaderBoardBar>
      <LeaderBoardBar rank={1}>
        <PodiumPlayerContainer>
          <PodiumPlayer rank={0} />
        </PodiumPlayerContainer>
        <PlaceHolder>1</PlaceHolder>
      </LeaderBoardBar>
      <LeaderBoardBar rank={3}>
        <PodiumPlayerContainer>
          <PodiumPlayer rank={2} />
        </PodiumPlayerContainer>
        <PlaceHolder>3</PlaceHolder>
      </LeaderBoardBar>
    </GraphContainer>
  );
};

export default RankingGraph;

const GraphContainer = styled.div`
  display: flex;
  background-color: #f8f9fa;
  flex-direction: row;
  align-items: flex-end;
  justify-content: center;
  width: 30vw;
  height: 100%;
  padding: 20px;
  border-radius: 15px;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
`;

const LeaderBoardBar = styled.div`
  position: relative;
  margin: 0 25px;
  width: 20%;
  height: ${(props) => (props.rank === 1 ? '55%' : props.rank === 2 ? '45%' : '35%')};
  background: ${(props) =>
    props.rank === 1
      ? 'linear-gradient(135deg, #ffd700, #ffec8b)'
      : props.rank === 2
      ? 'linear-gradient(135deg, #c0c0c0, #d3d3d3)'
      : 'linear-gradient(135deg, #cd7f32, #d8a684)'};
  border-radius: 15px;
  display: flex;
  align-items: flex-end;
  justify-content: center;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
  transition: transform 0.3s, box-shadow 0.3s;
  &:hover {
    transform: translateY(-10px);
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  }
`;

const LeaderBoardFirst = styled.div`
  position: relative;
  margin: 0 25px;
  background-color: #ffd700;
  width: 25%;
  height: 55%;
  border-radius: 15px;
  display: flex;
  align-items: flex-end;
  justify-content: center;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
  transition: transform 0.3s;
  &:hover {
    transform: translateY(-10px);
  }
`;

const LeaderBoardSecond = styled.div`
  position: relative;
  margin: 0 25px;
  background-color: #c0c0c0;
  width: 20%;
  height: 45%;
  border-radius: 15px;
  display: flex;
  align-items: flex-end;
  justify-content: center;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
  transition: transform 0.3s;
  &:hover {
    transform: translateY(-10px);
  }
`;

const LeaderBoardThird = styled.div`
  position: relative;
  margin: 0 25px;
  background-color: #cd7f32;
  width: 20%;
  height: 35%;
  border-radius: 15px;
  display: flex;
  align-items: flex-end;
  justify-content: center;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
  transition: transform 0.3s;
  &:hover {
    transform: translateY(-10px);
  }
`;

const PodiumPlayerContainer = styled.div`
  position: absolute;
  bottom: 110%;
  width: 100%;
  display: flex;
  align-items: center;
  flex-direction: column;
  justify-content: center;
  padding: 15px;
  background-color: rgba(255, 255, 255, 0.9);
  border-radius: 15px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
`;

const NoPlayerData = styled.div`
  color: #495057;
  font-size: 1em;
  text-align: center;
  padding: 10px;
`;

const ProfileImageContainer = styled.div`
  width: 80px;
  height: 80px;
  background-image: url('/single-test.webp');
  background-size: cover;
  background-position: center;
  border-radius: 50%;
  margin-bottom: 10px;
`;

const PlaceHolder = styled.div`
  position: absolute;
  top: 15px;
  font-size: 1.8em;
  font-weight: bold;
  color: #343a40;
`;
