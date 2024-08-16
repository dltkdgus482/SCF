import React, { useRef, useEffect, useState } from "react";
import MultiRanking from "./MultiRanking.jsx";


const MultiResultModal = () => {
  
  const [modalOpen, setModalOpen] = useState(true);
  const modalOutsisde = useRef();

  return (
    <>
      {
        modalOpen &&
        <div className={'result-modal-container'} ref={modalOutsisde} onClick={e => {
          if (e.target === modalOutsisde.current) {
            setModalOpen(false);
          }
        }}>
          <div className="result-modal-content">
              <MultiRanking />
          </div>
        </div>
      }
    
    </>
  );
}

export default MultiResultModal;