import React from 'react'
import { Input } from "@mui/base/Input";
import { Button } from "@mui/base/Button";
import '../../css/InputField.css'
const InputField = ({message,setMessage,sendMessage}) => {

  return (
    <div className="input-area">
          {/* onSubmit 실행 -> sendMessage 함수 호출 */}
          <form onSubmit={sendMessage} className="input-container">
            <Input
              placeholder="Type in here…"
              value={message}
              onChange={(event) => setMessage(event.target.value)}
              multiline={false}
              rows={1}
            />

            <Button
              disabled={message === ""}
              // onClick이 아닌 onSubmit 이벤트가 실행됨
              type="submit"
              className="send-button"
            >
              전송
            </Button>
          </form>
        </div>
  )
}

export default InputField