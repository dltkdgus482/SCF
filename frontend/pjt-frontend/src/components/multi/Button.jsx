import "../../css/Button.css";


export default function Button(props) {
  return (
    <button className="game-button" onClick={props.onClick}>
      { props.text }
    </button>
  )
}

