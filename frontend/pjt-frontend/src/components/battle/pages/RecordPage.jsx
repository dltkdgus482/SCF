import { useEffect, useState } from "react";
import Header from "../components/Header";
import "../../../css/RecordPage.css";
import store from "../../../store/store.js";
import createAuthClient from "../apis/createAuthClient.js";
import Swal from "sweetalert2";

const RecordPage = () => {
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

  const [recordData, setRecordData] = useState([]);
  const [sortedData, setSortedData] = useState([]);
  const [sortOption, setSortOption] = useState("recent");
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 4;

  useEffect(() => {
    const getRecord = async function () {
      try {
        const recordRes = await authClient({
          method: "GET",
          url: `${baseURL}/profile/record`,
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        });

        setRecordData(
          recordRes.data.historyLists ? recordRes.data.historyLists : []
        );
      } catch (error) {
        Swal.fire({
          text: "기록을 불러오는데 실패했습니다.",
          icon: "error",
          timer: 3000,
        });
        console.error("Failed to fetch record", error);
      }
    };

    getRecord();
  }, []);

  useEffect(() => {
    if (sortOption && recordData.length > 0) {
      const sorted = [...recordData];
      if (sortOption === "recent") {
        sorted.sort((a, b) => new Date(b.time) - new Date(a.time)); // 최신순 정렬
      } else if (sortOption === "rank") {
        sorted.sort((a, b) => a.rank - b.rank); // 순위순 정렬
      } else if (sortOption === "mode") {
        sorted.sort((a, b) => a.gametype - b.gametype); // 모드순 정렬
      } else if (sortOption === "score") {
        sorted.sort((a, b) => b.score - a.score); // 점수순 정렬
      }
      setSortedData(sorted);
    }
  }, [sortOption, recordData]);

  const handleSortChange = (e) => {
    setSortOption(e.target.value);
  };

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = sortedData.slice(indexOfFirstItem, indexOfLastItem);

  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  const pageNumbers = [];
  for (let i = 1; i <= Math.ceil(sortedData.length / itemsPerPage); i++) {
    pageNumbers.push(i);
  }

  return (
    <>
      <div className="outer-container">
        <Header />
        <div className="record-outer-container">
          <div className="record-container">
            <div className="record-title-container">
              <div></div>
              <h2 className="record-title">{name}님의 최근 전적</h2>
              <div className="record-title-select-container">
                <select name="" id="" className="" onChange={handleSortChange}>
                  <option value="recent">최신순</option>
                  <option value="rank">순위순</option>
                  <option value="mode">모드순</option>
                  <option value="score">점수순</option>
                </select>
              </div>
            </div>
            <div className="record-inner-container-outer">
              <div className="record-inner-container">
                <div className="record-inner-title">
                  <p>모드</p>
                  <p>시간</p>
                  <p>순위</p>
                  <p>점수</p>
                </div>
                <hr />
                <div className="record-inner">
                  {currentItems && currentItems.length > 0 ? (
                    currentItems.map((data, index) => (
                      <div className="record" key={index}>
                        <p>{data.gametype === 0 ? "멀티" : "배틀"}</p>
                        <p>
                          {new Date(
                            new Date(data.time).getTime() + 9 * 60 * 60 * 1000
                          ).toLocaleString("ko-KR")}
                        </p>
                        <p>{data.rank} 위</p>
                        <p>{data.score} 점</p>
                      </div>
                    ))
                  ) : (
                    <>
                      <h1>최근 플레이한 게임이 없습니다.</h1>
                    </>
                  )}
                </div>
                <div className="record-pagination">
                  {pageNumbers.map((number) => (
                    <button
                      key={number}
                      onClick={() => handlePageChange(number)}
                      className={number === currentPage ? "active" : ""}
                    >
                      {number}
                    </button>
                  ))}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default RecordPage;
