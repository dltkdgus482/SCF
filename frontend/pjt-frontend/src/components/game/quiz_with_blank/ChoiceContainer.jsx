import { border } from "@mui/system";

const style = {
  display: "flex",
  flexWrap: "wrap",
  padding: "10px",
  height: "auto",
  maxWidth: "100%",
  // backgroundColor: '#007BFF',
  // border: '2px solid #8216f5',
  borderRadius: "5px",
};

const ChoiceContainer = ({ children }) => {
  return <div style={style}>{children}</div>;
};

export default ChoiceContainer;
