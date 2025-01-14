import '../../../index.css';
import '../../../css/TitlePage.css';
import TitleLogo from '../components/TitleLogo';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import SoundStore from '../../../stores/SoundStore';
import FloatingButton from '../../buttons/FloatingButton.jsx';
function TitlePage() {
  const navigate = useNavigate();
  const [isVisible, setIsVisible] = useState(false);
  const { playEffectSound } = SoundStore();
  const handleClick = () => {
    navigate('/login');
    console.log('play click');
    playEffectSound('btnClickSound');
  };
  return (
    <>
      <div className="title-container">
        <TitleLogo />
      </div>
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 2, duration: 2 }}
        className={`start-button-container ${isVisible ? 'visible' : ''}`}
        onAnimationComplete={() => setIsVisible(true)}
      >
        <FloatingButton disabled={!isVisible} onClick={handleClick}>
          START
        </FloatingButton>
      </motion.div>
    </>
  );
}

export default TitlePage;
