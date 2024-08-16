let stompClient = null;
let currentProblems = [];

function setConnected(connected) {
    console.log('Connection status: ', connected);
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    const socket = new SockJS('http://localhost:8080/ws-room');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);

        const roomId = $("#roomId").val();
        console.log(roomId);
        stompClient.subscribe('/room/' + roomId, function (message) {
            console.log('Received: ' + message.body);
            showGreeting(JSON.parse(message.body));
        });
        const userId = $("#userId").val();
        stompClient.subscribe('/room/' + roomId + '/' + userId, function (message) {
            console.log('Received selected problem: ' + message.body);
            showSelectedProblem(JSON.parse(message.body));
        });
    }, function (error) {
        console.log('STOMP error: ' + error);
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function joinRoom() {
    const roomPassword = $("#roomPassword").val() || null;
    const joinRoomDTO = {
        roomId: $("#roomId").val(),
        userId: $("#userId").val(),
        username: $("#username").val(),
        roomPassword: roomPassword
    };
    console.log('Joining room with data: ', joinRoomDTO);
    stompClient.send("/send/game/" + joinRoomDTO.roomId + "/join", {}, JSON.stringify(joinRoomDTO));
}

function createRoom() {
    const userId = $("#userId").val();
    const roomPassword = $("#roomPasswordCreate").val() || null;
    const createRoomDTO = {
        roomPassword: roomPassword
    };
    $.ajax({
        url: 'http://localhost:8080/battle/room',
        type: 'POST',
        headers: {
            'userId': userId
        },
        contentType: 'application/json',
        data: JSON.stringify(createRoomDTO),
        success: function(response) {
            console.log('Room created with ID: ', response);
            $("#roomId").val(response);
        },
        error: function(error) {
            console.error('Error creating room: ', error);
        }
    });
}

function sendAnswer() {
    const solveMap = {};
    $(".solve").each(function() {
        const key = parseInt($(this).data("key"));
        const value = parseInt($(this).val());
        solveMap[key] = value;
    });

    const solved = {
        problemId: parseInt($("#problemId").val()),
        userId: parseInt($("#userId").val()),
        solve: solveMap,
        submitTime: parseInt($("#submitTime").val()),
        roomId: $("#roomId").val(),
        round: parseInt($("#round").val())
    };

    console.log('Sending answer: ', JSON.stringify(solved));
    stompClient.send("/send/game/" + solved.roomId + "/answer", {}, JSON.stringify(solved));
}

function selectProblemByIndex() {
    const roomId = $("#roomId").val();
    const userId = $("#userId").val();
    const problemIndex = parseInt($("#problemIndex").val());
    if (problemIndex >= 0 && problemIndex < currentProblems.length) {
        const problemId = currentProblems[problemIndex].problemId;
        console.log('Selecting problem with index: ', problemIndex, 'and problemId: ', problemId);
        const payload = {
            problemId: problemId
        };
        stompClient.send("/send/game/" + roomId + "/selectProblem", { userId: userId }, JSON.stringify(payload));
    } else {
        console.log('Invalid problem index');
    }
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

function showProblem(problems) {
    currentProblems = problems; // 저장하여 나중에 인덱스로 접근 가능하게 함
    $("#problems").html("");
    problems.forEach((problem, index) => {
        $("#problems").append(
            `<div>문제 ${index + 1} - ${problem.title}</div>`
        );
    });
}

function showSelectedProblem(problem) {
    $("#selectedProblem").html(`<div>${problem.title}</div>`);
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () { 
        console.log('Connect button clicked');
        connect(); 
    });
    $("#disconnect").click(function () { 
        console.log('Disconnect button clicked');
        disconnect(); 
    });
    $("#join").click(function () { 
        console.log('Join button clicked');
        joinRoom(); 
    });
    $("#createRoom").click(function () { 
        console.log('Create Room button clicked');
        createRoom(); 
    });
    $("#sendAnswer").click(function () { 
        console.log('Send Answer button clicked');
        sendAnswer(); 
    });
    $("#selectProblemButton").click(function () {
        console.log('Select Problem button clicked');
        selectProblemByIndex();
    });
});
