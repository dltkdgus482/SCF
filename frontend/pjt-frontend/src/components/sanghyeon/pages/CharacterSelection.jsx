import { useState } from "react";
import "../../../css/CharacterSelection.css";
import greenSlime from "../../../assets/characters/greenSlime.png";
import thunderSlime from "../../../assets/characters/thunderSlime.png";
import fireSlime from "../../../assets/characters/fireSlime.png";
import iceSlime from "../../../assets/characters/iceSlime.png";

import movingGreenSlime from "../../../assets/characters/movingGreenSlime.gif";
import movingThunderSlime from "../../../assets/characters/movingThunderSlime.gif";
import movingFireSlime from "../../../assets/characters/movingFireSlime.gif";
import movingIceSlime from "../../../assets/characters/movingIceSlime.gif";

import axios from "axios";
import store from "../../../store/store.js";
import { useNavigate } from "react-router-dom";

const CharacterSelection = () => {
  const navigate = useNavigate();
  const [selectedCharacter, setSelectedCharacter] = useState(null);
  const [hoveredCharacter, setHoveredCharacter] = useState(null);
  const [prevCharacter, setPrevCharacter] = useState(null);

  const { baseURL, registerInfo, setRegisterInfo } = store((state) => ({
    baseURL: state.baseURL,
    registerInfo: state.registerInfo,
    setRegisterInfo: state.setRegisterInfo,
  }));

  const getImageSrc = (character) => {
    if (hoveredCharacter) {
      switch (character) {
        case "green":
          return hoveredCharacter === "green" ? movingGreenSlime : greenSlime;
        case "ice":
          return hoveredCharacter === "ice" ? movingIceSlime : iceSlime;
        case "fire":
          return hoveredCharacter === "fire" ? movingFireSlime : fireSlime;
        case "thunder":
          return hoveredCharacter === "thunder"
            ? movingThunderSlime
            : thunderSlime;
        default:
          return greenSlime;
      }
    } else {
      switch (character) {
        case "green":
          return selectedCharacter === "green" ? movingGreenSlime : greenSlime;
        case "ice":
          return selectedCharacter === "ice" ? movingIceSlime : iceSlime;
        case "fire":
          return selectedCharacter === "fire" ? movingFireSlime : fireSlime;
        case "thunder":
          return selectedCharacter === "thunder"
            ? movingThunderSlime
            : thunderSlime;
        default:
          return greenSlime;
      }
    }
  };

  const signup = async function () {
    if (!selectedCharacter) {
      alert("캐릭터를 선택해주세요.");
      return;
    }

    setRegisterInfo({
      ...registerInfo,
      characterType: selectedCharacter,
    });

    try {
      const signupRes = await axios({
        method: "POST",
        url: `${baseURL}/user/join`,
        data: registerInfo,
      });

      alert("회원가입이 완료되었습니다.");
      navigate("/login");
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <>
      <div className="character-selection-outer-outer-container">
        <div className="character-selection-outer-container">
          <div className="character-selection-title-container">
            <div className="character-selection-title">CHARACTER SELECT</div>
          </div>
          <hr />
          <div className="character-selection-container">
            {["green", "ice", "fire", "thunder"].map((character) => (
              <div
                key={character}
                className={`character-selection-character ${
                  selectedCharacter === character
                    ? "character-selection-character-selected"
                    : ""
                }`}
                onMouseEnter={() => {
                  setHoveredCharacter(character);
                  setSelectedCharacter(null);
                }}
                onMouseLeave={() => {
                  setHoveredCharacter(null);
                  setSelectedCharacter(prevCharacter);
                }}
                onClick={() => {
                  setSelectedCharacter(character);
                  setPrevCharacter(character);
                }}
              >
                <img
                  className="character-gif"
                  src={getImageSrc(character)}
                  alt={character}
                />
                <div className="character-name">
                  {character.charAt(0).toUpperCase() + character.slice(1)}
                </div>
              </div>
            ))}
          </div>
          <div className="character-selection-button-container">
            <button onClick={signup} className="character-selection-button">
              CREATE
            </button>
          </div>
        </div>
      </div>
    </>
  );
};

export default CharacterSelection;
