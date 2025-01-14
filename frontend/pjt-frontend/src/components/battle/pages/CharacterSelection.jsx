import { useState, useEffect } from "react";
import "../../../css/CharacterSelection.css";
import Swal from "sweetalert2";
import greenSlime from "../../../assets/characters/greenSlime.png";
import thunderSlime from "../../../assets/characters/thunderSlime.png";
import fireSlime from "../../../assets/characters/fireSlime.png";
import iceSlime from "../../../assets/characters/iceSlime.png";

import movingGreenSlime from "../../../assets/characters/movingGreenSlime.gif";
import movingThunderSlime from "../../../assets/characters/movingThunderSlime.gif";
import movingFireSlime from "../../../assets/characters/movingFireSlime.gif";
import movingIceSlime from "../../../assets/characters/movingIceSlime.gif";
import movingNyanSlime from "../../../assets/characters/movingNyanSlime.gif";

import axios from "axios";
import store from "../../../store/store.js";
import { useNavigate } from "react-router-dom";
import { MdClosedCaptionDisabled } from "react-icons/md";

const CharacterSelection = () => {
  const navigate = useNavigate();
  const [selectedCharacter, setSelectedCharacter] = useState(null);
  const [hoveredCharacter, setHoveredCharacter] = useState(null);
  const [prevCharacter, setPrevCharacter] = useState(null);
  const [hiddenCount, setHiddenCount] = useState(0);

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

  const hiddenSelect = () => {
    setSelectedCharacter("nyan");
  };

  useEffect(() => {
    if (selectedCharacter === "nyan") {
      Swal.fire({
        text: "히든 캐릭터가 선택되었습니다.",
        icon: "success",
        timer: 3000,
      });
    }
  }, [selectedCharacter]);

  const characterTypeConvert = (characterType) => {
    switch (characterType) {
      case "green":
        return 1;
      case "ice":
        return 5;
      case "fire":
        return 4;
      case "thunder":
        return 3;
      case "nyan":
        return 12;
      default:
        return 1;
    }
  };

  const signup = async function () {
    if (!selectedCharacter && !prevCharacter) {
      Swal.fire({
        text: "캐릭터를 선택해주세요.",
        icon: "warning",
        timer: 3000,
      });
      return;
    }

    const updatedRegisterInfo = {
      ...registerInfo,
      characterType:
        characterTypeConvert(selectedCharacter) ||
        characterTypeConvert(prevCharacter),
      characterCloth: 1,
    };

    try {
      const signupRes = await axios({
        method: "POST",
        url: `${baseURL}/user/public/join`,
        data: updatedRegisterInfo,
      });

      Swal.fire({
        text: "회원가입에 성공했습니다.",
        icon: "success",
        timer: 3000,
      });
      navigate("/login");
    } catch (error) {
      Swal.fire({
        text: "회원가입에 실패했습니다.",
        icon: "error",
        timer: 3000,
      });
      console.log(error);
    }
  };

  return (
    <>
      <div className="character-selection-outer-outer-container">
        <div className="character-selection-outer-container">
          <div className="character-selection-title-container">
            <div
              onClick={() => {
                hiddenCount < 10
                  ? setHiddenCount(hiddenCount + 1)
                  : hiddenSelect();
              }}
              className="character-selection-title"
            >
              CHARACTER SELECT
            </div>
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
