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
        <ProfileImageContainer />
        <P.Name>{player.name}</P.Name>
        <P.Exp>{player.exp}</P.Exp>
      </>
    );
  };

  return (
    <GraphContainer>
      <LeaderBoardSecond>
        <PodiumPlayerContainer>
          <PodiumPlayer rank={1} />
        </PodiumPlayerContainer>
        <PlaceHolder>2</PlaceHolder>
      </LeaderBoardSecond>
      <LeaderBoardFirst>
        <PodiumPlayerContainer>
          <PodiumPlayer rank={0} />
        </PodiumPlayerContainer>
          <PlaceHolder>1</PlaceHolder>
      </LeaderBoardFirst>
      <LeaderBoardThird>
        <PodiumPlayerContainer>
          <PodiumPlayer rank={2} />
        </PodiumPlayerContainer>
          <PlaceHolder>3</PlaceHolder>
      </LeaderBoardThird>
    </GraphContainer>
  );
};

export default RankingGraph;

const GraphContainer = styled.div`
  display: flex;
  background-color: #f1f3f5;
  flex-direction: row;
  flex-wrap: wrap;
  align-items: flex-end;
  justify-content: center;
  width: 30vw;
  height: 100%;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
`;

const LeaderBoardFirst = styled.div`
  position: relative;
  margin: 0 20px;
  background-color: #ffd700;
  width: 25%;
  height: 50%;
  border-radius: 10px;
  display: flex;
  align-items: flex-end;
  justify-content: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
  &:hover {
    transform: translateY(-10px);
  }
`;

const LeaderBoardSecond = styled.div`
  position: relative;
  margin: 0 20px;
  background-color: #c0c0c0;
  width: 20%;
  height: 40%;
  border-radius: 10px;
  display: flex;
  align-items: flex-end;
  justify-content: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
  &:hover {
    transform: translateY(-10px);
  }
`;

const LeaderBoardThird = styled.div`
  position: relative;
  margin: 0 20px;
  background-color: #cd7f32;
  width: 20%;
  height: 30%;
  border-radius: 10px;
  display: flex;
  align-items: flex-end;
  justify-content: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
  &:hover {
    transform: translateY(-10px);
  }
`;

const PodiumPlayerContainer = styled.div`
  position: absolute;
  bottom: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  flex-direction: column;
  justify-content: center;
  padding: 10px;
  // background-color: rgba(255, 255, 255, 0.8);
`;

const NoPlayerData = styled.div`
  color: #495057;
  font-size: 1em;
  text-align: center;
  padding: 10px;
`;

const ProfileImageContainer = styled.div`
  width: 70px;
  height: 70px;
  background-image: url('/single-test.webp');
  background-size: cover;
  background-position: center;
  border-radius: 50%;
`;

const PlaceHolder = styled.div`
  position: absolute;
  top: 10px;
  font-size : 1.5em;
`;
