import { height, width } from "@mui/system";
import reactStringReplace from "react-string-replace";

const styles = {
  codeWithBlanks: {
    whiteSpace: "pre-wrap",
    fontFamily: "'Courier New', Courier, monospace",
    fontSize: "1rem",
    // backgroundColor: '#2e3440',
    backgroundColor: "black",
    color: "#d8dee9",
    padding: "20px",
    borderRadius: "5px",
    // position: 'relative',
    lineHeight: "1.3", // 줄 간격 조정
    letterSpacing: "normal", // 자간 조정
    width: "100%",
    height: "auto",
    overflow: "hidden",
    maxHeight : '90%',
  },
};
let seq = 0;
const StyleToPythonCode = ({ codeString, quizType }) => {
  const type = {
    0: "MultiChoiceQuiz",
    1: "ShortAnswer",
    2: "DragNDropQuiz",
  };
  codeString = reactStringReplace(
    codeString,
    /(def|for|if|else|return|import|from|as|class|try|except|finally|with|yield|raise|assert|del|pass|continue|break)\b/g,
    (match, i) => {
      // console.log("keyword", i, match);
      return (
        <span className="keyword" key={`keyword-${seq++}`}>
          {match}
        </span>
      );
    }
  );
  codeString = reactStringReplace(codeString, /('.*?'|".*?")/g, (match, i) => {
    // console.log("string", i, match);
    return (
      <span className="string" key={`string-${seq++}`}>
        {match}
      </span>
    );
  });
  codeString = reactStringReplace(codeString, /(#.*)/g, (match, i) => {
    // console.log("comment", i, match);
    return (
      <span className="comment" key={`comment-${seq++}`}>
        {match}
      </span>
    );
  });
  codeString = reactStringReplace(codeString, /(\b\d+\b)/g, (match, i) => {
    // console.log("number", i, match);
    return (
      <span className="number" key={`number-${seq++}`}>
        {match}
      </span>
    );
  });
  codeString = reactStringReplace(codeString, "\\n", (match, i) => {
    // console.log("number", i, match);
    return <br key={`newline-${seq++}`} />;
  });

  return (
    <pre className="code-with-blanks" style={styles.codeWithBlanks}>
      {codeString}
      <style>{`
        .code-with-blanks .keyword {
          color: #81a1c1;
          font-family: 'Courier New', Courier, monospace;
          }
        .code-with-blanks .string {
          color: #a3be8c;    
          font-family: 'Courier New', Courier, monospace;
          }
        .code-with-blanks .comment {
          color: #616e88;
          font-family: 'Courier New', Courier, monospace;
          font-style: italic; 
          }
        .code-with-blanks .number {
          color: #b48ead;
          font-family: 'Courier New', Courier, monospace;
          }
        .code-with-blanks .function {
          color: #88c0d0;
          font-family: 'Courier New', Courier, monospace;
          }
      `}</style>
    </pre>
  );
};
export default StyleToPythonCode;
