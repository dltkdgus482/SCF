import Header from '../sanghyeon/components/Header.jsx';
import S from './styled.jsx';
import RankingListItem from './RankingListItem.jsx';
import RankingGraph from './RankingGraph.jsx';
import { useState, useEffect } from 'react';
import useLeaderboardStore from '../../stores/LeaderboardStore.jsx';
import store from '../../store/store.js';
import axios from 'axios';

export default function Ranking() {
  const { rankingList, setTotalList, setWeeklyList, setDailyList, boardPeriod, setBoardPeriod } = useLeaderboardStore();
  const { baseURL, accessToken } = store();
  useEffect(() => {
    const fetchRankingData = async () => {
      // const tempUrl = 'http://192.168.30.171:8080';
      
      try {
        await Promise.all([
          
          // axios.get(`${tempUrl}/rank/total`),
          // axios.get(`${tempUrl}/rank/weekly`),
          // axios.get(`${tempUrl}/rank/daily`),
          axios
          .get(`${baseURL}/rank/total`, { headers: { Authorization: `Bearer ${accessToken}` } })
            .then((res) => {
              setTotalList(res.data)
              console.log("total", rankingList.total);
            })
            .catch((err) => console.log(err)),
          axios
            .get(`${baseURL}/rank/weekly`, { headers: { Authorization: `Bearer ${accessToken}` } })
            .then((res) => {
              setWeeklyList(res.data)
              console.log("weekly", rankingList.weekly);
            })
            .catch((err) => console.log(err)),
          axios
            .get(`${baseURL}/rank/daily`, { headers: { Authorization: `Bearer ${accessToken}` } })
            .then((res) => {
              setDailyList(res.data)
              console.log("daily", rankingList.daily);
            })
            .catch((err) => console.log(err)),
        ]).then(() => {
          console.log(rankingList)
          console.log('전부 불러오기 성공');
        });
      } catch (error) {
        console.error('Error fetching ranking data:', error);
      }
    };

    fetchRankingData();
  }, []);

  const handleTabClick = (tab) => {
    console.log(tab)
    setBoardPeriod(tab);
    console.log('period', boardPeriod)
    console.log('board with period []', rankingList.boardPeriod)
    console.log('board with period . dot', rankingList[boardPeriod])
  };

  return (
    <>
      <Header />
      <S.PageBody>
        <S.Container>
          <S.Title>{boardPeriod.toUpperCase()} Ranking</S.Title>
          <S.RankingInfoContainer>
            <S.PeriodButton onClick={() => handleTabClick('total')} $isSelected={boardPeriod === 'total'}>
              Total
            </S.PeriodButton>
            <S.PeriodButton onClick={() => handleTabClick('weekly')} $isSelected={boardPeriod === 'weekly'}>
              Weekly
            </S.PeriodButton>
            <S.PeriodButton onClick={() => handleTabClick('daily')} $isSelected={boardPeriod === 'daily'}>
              Daily
            </S.PeriodButton>
          </S.RankingInfoContainer>
          <S.FlexContainer>
            <S.RankingInfoSection>
              <S.GraphContainer>
                <RankingGraph />
              </S.GraphContainer>
            </S.RankingInfoSection>
            <S.RankingListSection>
              {rankingList && rankingList[boardPeriod]
                ? rankingList[boardPeriod]
                    .slice(3)
                    .map((player, idx) => <RankingListItem key={idx} rank={idx + 4} player={player} />)
                : null}
            </S.RankingListSection>
          </S.FlexContainer>
        </S.Container>
      </S.PageBody>
      <style></style>
    </>
  );
}
