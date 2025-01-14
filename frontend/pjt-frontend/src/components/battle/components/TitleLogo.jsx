import { TypeAnimation } from "react-type-animation";

const TitleLogo = () => {
  return (
    <>
      <TypeAnimation
        sequence={["Street Coding", 500, "Street Coding\nFighter", 500]}
        wrapper="h1"
        speed={50}
        cursor= {false}
        style={{
          textAlign: "center",
          fontSize: "9rem",
          color: "white",
          whiteSpace: "pre-line",
          textShadow: '#1B1A55 1px 1px, #1B1A55 -1px 0px, #1B1A55 1px 0px,#1B1A55 0px 1px,#1B1A55 0px -1px,#1B1A55 -0px 0px, #1B1A55 -1px 1px, #1B1A55 -2px 2px, #1B1A55 -3px 3px, #1B1A55 -4px 4px, #1B1A55 -5px 5px, #1B1A55 -6px 6px, #1B1A55 -7px 7px, #1B1A55 -8px 8px, #1B1A55 -9px 9px, #1B1A55 -10px 10px,            #1B1A55 -11px 11px, #1B1A55 -12px 12px, #1B1A55 -13px 13px, #1B1A55 -14px 14px, #1B1A55 -15px 15px,            #1B1A55 -16px 16px, #1B1A55 -17px 17px, #1B1A55 -18px 18px, #1B1A55 -19px 19px, #1B1A55 -20px 20px,            #1B1A55 -21px 21px, #1B1A55 -22px 22px, #1B1A55 -23px 23px, #1B1A55 -24px 24px, #1B1A55 -25px 25px,            #1B1A55 -26px 26px, #1B1A55 -27px 27px, #1B1A55 -28px 28px, #1B1A55 -29px 29px'  
        }}
      />
    </>
  );
};

export default TitleLogo;
