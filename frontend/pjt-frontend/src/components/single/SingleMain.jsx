import SinglePlay from './single-play/SinglePlay.jsx';
import EpisodeList from './single-list/EpisodeList.jsx';
import Container from '../../components/sanghyeon/components/Container.jsx';
import '../../css/Container.css';
import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import axios from 'axios';
import store from '../../store/store.js';
import SingleInfoStore from '../../stores/SingleInfoStore.jsx';
import Header from '../sanghyeon/components/Header.jsx';
export default function SingleMain() {
  const { setCompleted } = SingleInfoStore();
  const { baseURL, accessToken } = store();
  const navigate = useNavigate();
  // const tempToken = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5MCIsImF1dGgiOiJVU0VSIiwibWVtYmVySWQiOjkwLCJ1c2VybmFtZSI6IuqwgOyghOyCrOyXheu2gC3quYDrr7zsmrEiLCJpYXQiOjE3MjMwMTMzOTksImV4cCI6MTcyMzAxNjk5OX0.39EpgNaDW4Pwp1taoqcsgB74ORG4ZbIfc7mG9ZWfj0s'
  useEffect(() => {
    const getSingleInfo = () => {
      axios({
        method: 'get',
        url: `${baseURL}/single`,
        headers: {
          Authorization: `Bearer ${accessToken}`,
          // Authorization: `Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5MCIsImF1dGgiOiJVU0VSIiwibWVtYmVySWQiOjkwLCJ1c2VybmFtZSI6IuqwgOyghOyCrOyXheu2gC3quYDrr7zsmrEiLCJpYXQiOjE3MjMwOTczNjgsImV4cCI6MTcyMzEwMDk2OH0.4wZxyxS2RwHVFiZZWq7e3QIV54UZ1_XNdYb0x-92qAQ`,
        },
      })
        .then((res) => {
          console.log(res.data);
          setCompleted(res.data.contentList);
        })
        .catch((error) => {
          console.log('완료목록 로드 실패');
          console.log(error);
        });
    };
    getSingleInfo();
    setTimeout(() => {
      getSingleInfo();
    }, 1000);
  }, []);
  return (
    <>
      <Header />
      <div style={styles.container}>
        <TitleDiv>
          <h1>Story Mode</h1>
        </TitleDiv>
        <EpisodeList rownum={3}></EpisodeList>
      </div>
    </>
  );
}
const styles = {
  container:{  position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    backgroundColor: 'rgba(27, 26, 85, 0.8)',
    width: '80vw',
    borderRadius: '20px',
    height: '80vh',
    marginTop : '20px',
    color: 'white',
  }
}
const TitleDiv = styled.div`
  display: flex;
  justify-content: center;
  margin: 15px 0px;
  margin-bottom : 50px;
`;
