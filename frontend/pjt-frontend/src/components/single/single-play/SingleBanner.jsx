import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { useParams } from 'react-router-dom';
import SingleInfoStore from '../../../stores/SingleInfoStore'
const BannerWrapper = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(5px);
  opacity: ${({ $isVisible }) => ($isVisible ? 1 : 0)};
  transition: opacity 1s ease-in-out;
  z-index: ${({ $isVisible }) => ($isVisible ? 9999 : -1)};
`;

const Banner = styled.div`
  width: 100vw;
  height: 250px;
  padding: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-weight: 700;
  text-align: center;
  background-color: #BED0DE;
  border: 1px solid #ccc;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
  font-size: 2em; /* 글씨 크기 키움 */
`;

const SingleBanner = () => {
  const [isVisible, setIsVisible] = useState(true);
  const [isBehind, setIsBehind] = useState(false);
  const {courses} = SingleInfoStore();
  const { content_id } = useParams();
  useEffect(() => {
    const timer = setTimeout(() => {
      setIsVisible(false);
      setTimeout(() => {
        setIsBehind(true);
      }, 1000); // opacity transition 시간 이후에 z-index를 변경
    }, 2000);
    return () => clearTimeout(timer);
  }, []);

  return (
    <BannerWrapper $isVisible={isVisible} $isBehind={isBehind}>
      <Banner>{content_id}. {courses[content_id-1].title}</Banner>
    </BannerWrapper>
  );
};

export default SingleBanner
