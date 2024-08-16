import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import multiStore from '../../stores/multiStore.jsx';

const MultiRanking = () => {
  const { gameRank, roundRank, playing, getScore } = multiStore((state) => ({
    gameRank: state.gameRank,
    roundRank: state.roundRank,
    playing: state.playing,
    getScore: state.getScore,
  }));

  let userList = [];
  userList = playing ? roundRank : gameRank;

  return (
    <TotalContainers>
      <GetScore>
        + {getScore}
      </GetScore>
      <GraphContainer>
        <LeaderBoardSecond>
          <PodiumPlayerContainer>
            {userList.length > 1 ? (
              <>
                <div>{userList[1].username}</div>
                <div>{userList[1].score}</div>
              </>
            ) : (
              <NoPlayerData>No Data</NoPlayerData>
            )}
          </PodiumPlayerContainer>
          <PlaceHolder>2</PlaceHolder>
        </LeaderBoardSecond>
        <LeaderBoardFirst>
          <PodiumPlayerContainer>
            {userList.length > 0 ? (
              <>
                <div>{userList[0].username}</div>
                <div>{userList[0].score}</div>
              </>
            ) : (
              <NoPlayerData>No Data</NoPlayerData>
            )}
          </PodiumPlayerContainer>
          <PlaceHolder>1</PlaceHolder>
        </LeaderBoardFirst>
        <LeaderBoardThird>
          <PodiumPlayerContainer>
            {userList.length > 2 ? (
              <>
                <div>{userList[2].username}</div>
                <div>{userList[2].score}</div>
              </>
            ) : (
              <NoPlayerData>No Data</NoPlayerData>
            )}
          </PodiumPlayerContainer>
          <PlaceHolder>3</PlaceHolder>
        </LeaderBoardThird>
      </GraphContainer>
    </TotalContainers>
  );
};

export default MultiRanking;

const TotalContainers = styled.div`
  display: flex;
  background-color: #f1f3f5;
  flex-direction: column;
  flex-wrap: wrap;
  align-items: center;
  justify-content: center;
  width: 30vw;
  height: 100%;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
`;

const GetScore = styled.div`
  font-size: 4rem;
  color: green;
  width: 30vw;
  height: 20%;
  text-align: center;
  font-weight: bold;
`;

// const GraphContainer = styled.div`
//   display: flex;
//   background-color: #f1f3f5;
//   flex-direction: row;
//   flex-wrap: wrap;
//   align-items: flex-end;
//   justify-content: center;
//   width: 30vw;
//   height: 80%;
//   padding: 20px;
//   border-radius: 10px;
//   box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
// `;

const GraphContainer = styled.div`
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  align-items: flex-end;
  justify-content: center;
  width: 30vw;
  height: 80%;
`;

const LeaderBoardFirst = styled.div`
  position: relative;
  margin: 0 20px;
  background-color: #ffd700;
  width: 25%;
  height: 70%;
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
  height: 55%;
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

const PodiumPlayerContainer = styled.div`
  position: absolute;
  bottom: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  flex-direction: column;
  justify-content: center;
  padding: 10px;
`;

const NoPlayerData = styled.div`
  color: #495057;
  font-size: 1em;
  text-align: center;
  padding: 10px;
`;

const PlaceHolder = styled.div`
  position: absolute;
  top: 10px;
  font-size: 1.5em;
`;
