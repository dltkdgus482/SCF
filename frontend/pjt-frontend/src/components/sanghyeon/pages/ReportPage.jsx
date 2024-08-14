import Header from "../components/Header";
import "../../../css/ReportPage.css";
import axios from "axios";
import store from "../../../store/store.js";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import Swal from "sweetalert2";
import renderCharacter from "../apis/renderCharacter.js";
import createAuthClient from "../apis/createAuthClient.js";

const ReportPage = () => {
  const navigate = useNavigate();
  const [reportInfo, setReportInfo] = useState(null);

  const { baseURL, character, name, accessToken, setAccessToken } = store(
    (state) => ({
      baseURL: state.baseURL,
      character: state.character,
      name: state.name,
      accessToken: state.accessToken,
      setAccessToken: state.setAccessToken,
    })
  );

  const authClient = createAuthClient(
    baseURL,
    () => accessToken,
    setAccessToken
  );

  useEffect(() => {
    const getReportDetail = async () => {
      try {
        const reportDetailRes = await authClient({
          method: "GET",
          url: `${baseURL}/profile/reportdetail`,
        });

        setReportInfo(reportDetailRes.data);
      } catch (error) {
        console.log(error);
      }
    };

    getReportDetail();
  }, []);

  const getReport = async () => {
    try {
      Swal.fire({
        text: "리포트 다운로드 중...",
        icon: "info",
        showConfirmButton: false,
      });
      const reportRes = await axios({
        method: "POST",
        url: "https://www.ssafy11th-songsam.site/bots/",
        data: { name: name, ...reportInfo },
        responseType: "blob",
      });
      const file = new Blob([reportRes.data], { type: "application/pdf" });
      const link = document.createElement("a");
      link.href = window.URL.createObjectURL(file);
      link.download = "analysis_report.pdf";
      document.body.appendChild(link);
      link.click();
      link.parentNode.removeChild(link);
      Swal.fire({
        text: "리포트 다운로드가 완료되었습니다.",
        icon: "success",
        timer: 3000,
      });
    } catch (error) {
      Swal.fire({
        text: "리포트 다운로드에 실패했습니다.",
        icon: "error",
        timer: 3000,
      });
      console.log(error);
    }
  };

  return (
    <>
      <div className="report-entire-container">
        <Header />
        <div className="report-outer-outer-container">
          <div className="report-outer-container">
            <div className="report-upper-container">
              <div className="report-profile-container">
                <img
                  src={renderCharacter(character)}
                  alt="profile-character"
                  className="report-profile-character"
                />
                <div className="report-profile-name">{name}</div>
              </div>
              <div className="report-upper-title-container">
                <div className="report-upper-title-inner-container">
                  <div className="report-upper-sub-title">AI 스킬 평가</div>
                  <div className="report-upper-title">
                    Problem Solving 평가 리포트
                    <span className="report-beta">Beta</span>
                  </div>
                </div>
                <div className="report-upper-title-button-container">
                  <button className="report-upper-title-button">
                    시도한 문제 수:{" "}
                    {reportInfo && reportInfo.solvedCount
                      ? reportInfo.solvedCount
                      : 0}{" "}
                    개
                  </button>
                  <button className="report-upper-title-button">
                    평균 순위:{" "}
                    {reportInfo && reportInfo.averageRank
                      ? reportInfo.averageRank
                      : 0}{" "}
                    위
                  </button>
                  <button
                    onClick={() => getReport()}
                    className="report-upper-title-button"
                  >
                    분석 리포트 PDF 다운로드
                  </button>
                </div>
              </div>
            </div>
            <div className="report-lower-container">
              <div className="report-lower-title-container">
                <div className="report-lower-title">AI 종합 분석</div>
                <br />
                <div className="report-lower-sub-title">
                  SCF AI가 예측한 문제 해결 확률과 점수를 토대로 앞으로의 학습
                  방향을 제안해드립니다.
                </div>
              </div>
              <div className="report-inner-container">
                <div className="report">
                  <div className="report-lower-title">유의 사항</div>
                  <br />
                  <div className="report-lower-sub-title">
                    본 리포트는 스트리트 코딩 파이터 플랫폼에 축적된 데이터를
                    학습한 AI 모델의 예측에 기반하고 있습니다. 본 리포트는
                    학습을 위한 참고 목적으로 활용할 수 있도록 제공되며, 증빙
                    등의 다른 목적으로는 사용할 수 없습니다.
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default ReportPage;
