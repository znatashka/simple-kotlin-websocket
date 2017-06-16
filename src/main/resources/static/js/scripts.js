var stompClient = null;

function setConnected(connected) {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    document.getElementById('chat').innerHTML = '';
}

function connect() {
    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', function (msg) {
            showGreeting(JSON.parse(msg.body));
        });
    });
}

function disconnect() {
    stompClient.disconnect();
    setConnected(false);
    console.log('Disconnected');
}

function sendMsg() {
    stompClient.send('/app/chat', {}, document.getElementById('msg').value);
    document.getElementById('msg').value = '';
}

function showGreeting(message) {
    document.getElementById("guid").innerHTML = message.chat;

    var str = '<div class="comment">' +
        '    <div class="content">' +
        '      <a class="author">' + message.user + '</a>' +
        '      <div class="metadata">' +
        '        <span class="date">' + message.date + '</span>' +
        '      </div>' +
        '      <div class="text">' + message.text + '</div>' +
        '    </div>' +
        '  </div>';

    document.getElementById('chat').insertAdjacentHTML('beforeend', str);
}