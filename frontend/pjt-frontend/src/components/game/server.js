const newSocket = (roomId, memberId, name) => {
  const baseUrl = "wss://www.ssafy11s.com";
  const url = `${baseUrl}/ws-multi?roomId=${roomId}&userId=${memberId}&username=${name}`;
  console.log(url);

  let socket;
  let reconnectInterval = 1000; // 1초 후 재연결 시도
  let reconnectAttempts = 0; // 재연결 시도 횟수
  const maxReconnectAttempts = 3; // 최대 재연결 시도 횟수

  const connect = () => {
    socket = new WebSocket(url);

    socket.onopen = () => {
      console.log('WebSocket connection established');
      reconnectAttempts = 0; // 연결 성공 시도 횟수 초기화
    };

    socket.onmessage = (event) => {
      console.log('WebSocket message received:', event.data);
    };

    socket.onclose = (event) => {
      console.log('WebSocket connection closed');
      console.log(`Close code: ${event.code}, reason: ${event.reason}, wasClean: ${event.wasClean}`);
      if (event.code != 1000 && reconnectAttempts < maxReconnectAttempts) {
        console.log('Reconnecting...');
        reconnectAttempts += 1; // 재연결 시도 횟수 증가
        setTimeout(connect, reconnectInterval); // 재연결 시도
      } else if (reconnectAttempts >= maxReconnectAttempts) {
        console.log('Maximum reconnect attempts reached. No further attempts will be made.');
      }
    };

    socket.onerror = (error) => {
      console.error('WebSocket error:', error);
    };
  };

  connect(); // 초기 연결 시도

  return socket;
};

export default newSocket;
    
