import styled from 'styled-components';

const Holder = styled.div`
  -moz-user-select: -moz-none;
  -khtml-user-select: none;
  -webkit-user-select: none;
  background: white;
  padding: 10px;
  width: 250px;
  margin: 0 auto;
  position: relative;
  top: 50%;
  font-size: 30px;
  transform: translateY(-65%);
  box-shadow: 0px 3px 8px rgba(0, 0, 0, 0.25);
  border-radius: 2px;
`;

const Button = styled.div`
  background: #3d4c53;
  margin: 20px auto;
  width: 250px;
  height: 100px;
  overflow: hidden;
  text-align: center;
  transition: 0.2s;
  border-radius: 3px;
  box-shadow: 0px 1px 2px rgba(0, 0, 0, 0.2);

  &:hover .btnTwo {
    left: -130px;
  }

  &:hover .btnText {
    margin-left: 65px;
  }

  &:active {
    box-shadow: 0px 5px 6px rgba(0, 0, 0, 0.3);
  }
`;

const BtnTwo = styled.div`
  position: relative;
  width: 200px;
  height: 100px;
  margin-top: -100px;
  padding-top: 2px;
  background: #26a69a;
  left: -250px;
  transition: 0.3s;
`;

const BtnText = styled.p`
  color: white;
  transition: 0.3s;
`;

const BtnText2 = styled.p`
  margin-top: 63px;
  margin-right: -130px;
  color: #fff;
`;


export {Button, BtnTwo, BtnText, BtnText2, Holder}