import Header from "../components/Header";
import "../../../css/SolvedDetailPage.css";
import store from "../../../store/store.js";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import DragNDropQuiz from "../../../components/game/quiz_with_blank/DragNDropQuiz.jsx";
import ShortAnswer from "../../../components/game/short_answer/ShortAnswer";
import MultipleChoice from "../../../components/game/multipleChoice/MultipleChoice.jsx";
import createAuthClient from "../apis/createAuthClient.js";

const SolvedDetailPage = () => {
  const { solvedId } = useParams();
  const [currentProblem, setCurrentProblem] = useState(null);

  const { baseURL, accessToken, setAccessToken } = store((state) => ({
    baseURL: state.baseURL,
    accessToken: state.accessToken,
    setAccessToken: state.setAccessToken,
  }));

  const authClient = createAuthClient(
    baseURL,
    () => accessToken,
    setAccessToken
  );

  useEffect(() => {
    const getProblemInfo = async () => {
      try {
        const res = await authClient({
          method: "GET",
          url: `${baseURL}/solved/${solvedId}`,
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        });
        setCurrentProblem(res.data);
      } catch (error) {
        console.log(error);
      }
    };

    getProblemInfo();
  }, []);

  const renderQuestion = (problem) => {
    switch (problem.problemType) {
      case "FILL_IN_THE_BLANK":
        return <DragNDropQuiz />;
      case "SHORT_ANSWER_QUESTION":
        return <ShortAnswer />;
      case "MULTIPLE_CHOICE":
        return <MultipleChoice />;
      default:
        return <div>Unknown problem type</div>;
    }
  };

  return (
    <>
      <div className="solved-detail-entire-container">
        <Header />
        <div className="solved-detail-outer-outer-container">
          <div className="solved-detail-outer-container">
            <div className="solved-detail-inner-container">
              <div className="solved-detail-content">
                {currentProblem && renderQuestion(currentProblem)}
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default SolvedDetailPage;
