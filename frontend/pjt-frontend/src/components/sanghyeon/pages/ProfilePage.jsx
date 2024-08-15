import "../../../css/ProfilePage.css";
import Header from "../components/Header";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
import store from "../../../store/store.js";
import SignOutButton from "../components/SignOutButton.jsx";
import createAuthClient from "../apis/createAuthClient.js";
import renderCharacter from "../apis/renderCharacter.js";
import Swal from "sweetalert2";

function ProfilePage() {
  const navigate = useNavigate();
  const { baseURL, accessToken, setAccessToken, memberId } = store((state) => ({
    baseURL: state.baseURL,
    accessToken: state.accessToken,
    setAccessToken: state.setAccessToken,
    memberId: state.memberId,
  }));

  const {
    name,
    schoolName,
    birth,
    character,
    exp,
    setName,
    setSchoolName,
    setBirth,
    setCharacter,
    setExp,
    // authClient,
  } = store((state) => ({
    name: state.name,
    schoolName: state.schoolName,
    birth: state.birth,
    character: state.character,
    exp: state.exp,
    setName: state.setName,
    setSchoolName: state.setSchoolName,
    setBirth: state.setBirth,
    setCharacter: state.setCharacter,
    setExp: state.setExp,
    //authClient : state.authClient,
  }));

  const authClient = createAuthClient(
    baseURL,
    () => accessToken,
    setAccessToken
  );

  useEffect(() => {
    const getProfile = async () => {
      try {
        const profileRes = await authClient({
          method: "GET",
          url: `${baseURL}/profile`,
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        });

        setName(profileRes.data.name);
        setSchoolName(profileRes.data.school);
        setBirth(profileRes.data.birth);
        setCharacter(profileRes.data.userCharacter.characterType);
        setExp(profileRes.data.exp);
      } catch (error) {
        Swal.fire({
          text: "프로필을 불러오는데 실패했습니다.",
          icon: "error",
          timer: 3000,
        });
        console.log(error);
      }
    };

    getProfile();
  }, []);

  return (
    <>
      <div className="outer-container">
        <Header />
        <div className="profile-outer-container">
          <div className="profile-container">
            <h2 className="profile-title">My Profile</h2>
            <hr />
            <div className="profile-character-container">
              <div className="profile-character">
                <div className="profile-status">
                  <h3>Name : {name}</h3>
                  <h3>Birth Day : {birth}</h3>
                  <h3>Exp: {exp ? exp.toLocaleString() : 0} exp</h3>
                  <h3>School : {schoolName}</h3>
                  <div className="profile-status-button">
                    <button
                      className="custom-button"
                      onClick={() => navigate("/reset-password")}
                    >
                      비밀번호 수정
                    </button>
                  </div>
                </div>
                <div className="profile-character-img-container">
                  <img
                    className="profile-character-img"
                    src={renderCharacter(character)}
                    alt="pinia-character"
                  />
                </div>
              </div>
              <div className="profile-button-container">
                <button
                  className="custom-button"
                  onClick={() => navigate("/_record")}
                >
                  RECORD
                </button>
                <button
                  className="custom-button"
                  onClick={() => navigate("/_solved")}
                >
                  SOLVED
                </button>
                <button
                className="custom-button"
                onClick={() => navigate("/_report")}
              >
                REPORT
              </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default ProfilePage;
