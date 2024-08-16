import { React, useRef, useEffect } from 'react';
import '../../css/PasswordModal.css';
import '../../css/Button.css';


function PasswordModal({ onClose, onSubmit }) {
  const modalRef = useRef();

  const handleClickOutside = (event) => {
    if (modalRef.current && !modalRef.current.contains(event.target)) {
      onClose();
    }
  };

  useEffect(() => {
    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  const handleSubmit = (event) => {
    event.preventDefault();
    const password = event.target.password.value;
    onSubmit(password);
  };

  return (
    <div className="password-modal">
      <div className="modal-content" ref={modalRef}>
        <span className="close" onClick={onClose}>&times;</span>
        <form className="password-form" onSubmit={handleSubmit}>
          <h2>PassWord : </h2>
          <input className="password-input" type="password" name="password" required />
          <button className='password-button' type="submit">입장</button>
        </form>
      </div>
    </div>
  );
}

export default PasswordModal;