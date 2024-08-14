import React, { useRef, useEffect, useState } from "react";
import "../../css/MessageContainer.css";
import { motion } from "framer-motion";
import { Container } from "@mui/system";

const MessageContainer = ({ chatMessages, username }) => {
  const messageEndRef = useRef(null);

  const scrollToBottom = () => {
    messageEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  useEffect(() => {
    scrollToBottom();
  }, [chatMessages]);

  return (
    <div>
      {chatMessages.map((message, index) => {
        return (
          <Container key={ message._id || index } className="message-container">
            {message.sender === "system" ? (
              <div className="system-message-container">
                <p className="system-message">{message.content}</p>
              </div>
            ) : message.sender === username ? (
              <div className="my-message-container">
                <motion.div
                  initial={{ opacity: 0, y: 20 }}
                  whileInView={{
                    opacity: 1,
                    y: 0,
                  transition: { delay: 0.1 },
                  }}
                >
                <div className="my-message">{message.content}</div>
                </motion.div>
              </div>
            ) : (
              <motion.div
                initial={{ opacity: 0, y: 20 }}
                whileInView={{
                  opacity: 1,
                  y: 0,
                transition: { delay: 0.1 },
                }}
              >
              <div className="your-username">{message.sender}</div>
              <div className="your-message-container">
                <div className="your-message">{message.content}</div>
              </div>
              </motion.div>
            )}
            <div ref={messageEndRef} />
          </Container>
        );
      })}
    </div>
  );
};

export default MessageContainer;
