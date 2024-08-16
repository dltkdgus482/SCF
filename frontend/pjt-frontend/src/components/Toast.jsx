import { useEffect } from "react";

function Toast({ setToast, text, style }) {

  useEffect(() => {
    const timer = setTimeout(() => {
      setToast(false);
    }, 1500);
    return () => {
      clearTimeout(timer);
    };
  }, [setToast]);

  return (
    <div style={style}>
      <p style={{whiteSpace:'pre-wrap'}}>{text}</p>
    </div>
  );
}

export default Toast;
