import { useEffect, useState } from "react";
import Header from "../components/Header";
import "../../../css/SolvedPage.css";
import createAuthClient from "../apis/createAuthClient";
import store from "../../../store/store.js";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";

function SolvedPage() {
  const navigate = useNavigate();

  const [solvedData, setSolvedData] = useState([]);
  const [sortedData, setSortedData] = useState([]);
  const [sortOption, setSortOption] = useState("recent");
  const [currentPage, setCurrentPage] = useState(1);
  const [currentPageGroup, setCurrentPageGroup] = useState(1);
  const itemsPerPage = 4;
  const pagesPerGroup = 10;

  const { accessToken, setAccessToken, baseURL, memberId, name } = store(
    (state) => ({
      accessToken: state.accessToken,
      setAccessToken: state.setAccessToken,
      baseURL: state.baseURL,
      memberId: state.memberId,
      name: state.name,
    })
  );

  const authClient = createAuthClient(
    baseURL,
    () => accessToken,
    setAccessToken
  );

  useEffect(() => {
    const getSolved = async () => {
      try {
        const solvedRes = await authClient({
          method: "GET",
          url: `${baseURL}/profile/solved`,
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        });

        setSolvedData(
          solvedRes.data.solvedList ? solvedRes.data.solvedList : []
        );
      } catch (error) {
        Swal.fire({
          text: "문제를 불러오는데 실패했습니다.",
          icon: "error",
          timer: 3000,
        });
        console.error("Failed to fetch record", error);
      }
    };

    getSolved();
  }, []);

  useEffect(() => {
    if (sortOption && solvedData.length > 0) {
      const sorted = [...solvedData];
      if (sortOption === "title") {
        sorted.sort((a, b) => a.title.localeCompare(b.title));
      } else if (sortOption === "difficulty") {
        sorted.sort((a, b) => a.difficulty - b.difficulty);
      } else if (sortOption === "correct") {
        sorted.sort((a, b) =>
          a.correct === b.correct ? 0 : a.correct ? -1 : 1
        );
      }
      setSortedData(sorted);
    }
  }, [sortOption, solvedData]);

  const handleSortChange = (e) => {
    setSortOption(e.target.value);
  };

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = sortedData.slice(indexOfFirstItem, indexOfLastItem);

  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  const totalPages = Math.ceil(sortedData.length / itemsPerPage);
  const startPage = (currentPageGroup - 1) * pagesPerGroup + 1;
  const endPage = Math.min(startPage + pagesPerGroup - 1, totalPages);
  const pageNumbers = [];

  for (let i = startPage; i <= endPage; i++) {
    pageNumbers.push(i);
  }

  return (
    <>
      <div className="outer-container">
        <Header />
        <div className="solved-outer-container">
          <div className="solved-container">
            <div className="solved-title-container">
              <div></div>
              <h2 className="solved-title">{name}님의 최근 푼 문제</h2>
              <div className="solved-title-select-container">
                <select name="" id="" className="" onChange={handleSortChange}>
                  <option value="recent">최신순</option>
                  <option value="title">제목순</option>
                  <option value="difficulty">난이도순</option>
                  <option value="correct">정답순</option>
                </select>
              </div>
            </div>
            <div className="solved-inner-container-outer">
              <div className="solved-inner-container">
                <div className="solved-inner-title">
                  <p>제목</p>
                  <p>정답 유무</p>
                  <p>난이도</p>
                </div>
                <hr />
                <div className="solved-inner">
                  {currentItems && currentItems.length > 0 ? (
                    currentItems.map((data, index) => (
                      <div
                        className="solved"
                        onClick={() => navigate("/solved/" + data.solvedId)}
                        key={index}
                      >
                        <p>{data.title}</p>
                        <p>{data.correct ? "O" : "X"}</p>
                        <p>{data.difficulty}</p>
                      </div>
                    ))
                  ) : (
                    <>
                      <h1>푼 문제가 없습니다.</h1>
                    </>
                  )}
                </div>
                <div className="solved-pagination">
                  {currentPageGroup > 1 && (
                    <button
                      onClick={() => setCurrentPageGroup(currentPageGroup - 1)}
                    >
                      {"<"}
                    </button>
                  )}
                  {pageNumbers.map((number) => (
                    <button
                      key={number}
                      onClick={() => handlePageChange(number)}
                      className={number === currentPage ? "active" : ""}
                    >
                      {number}
                    </button>
                  ))}
                  {endPage < totalPages && (
                    <button
                      onClick={() => setCurrentPageGroup(currentPageGroup + 1)}
                    >
                      {">"}
                    </button>
                  )}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default SolvedPage;
