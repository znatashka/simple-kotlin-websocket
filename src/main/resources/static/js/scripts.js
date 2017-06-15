var stompClient = null;

function connect() {
    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', function (msg) {
            showGreeting(JSON.parse(msg.body));
        });
    });
}

function disconnect() {
    stompClient.disconnect();
    console.log('Disconnected');
}

function sendMsg() {
    stompClient.send('/app/chat', {}, document.getElementById('msg').value);
    document.getElementById('msg').value = '';
}

function showGreeting(message) {
    document.getElementById("guid").innerHTML = message.chat;

    var chat = document.getElementById('chat');
    // var p = document.createElement('p');
    // p.style.wordWrap = 'break-word';
    // p.appendChild(document.createTextNode('(' + message.date + '): ' + message.text));

    var divComment = document.createElement('div');
    divComment.classList.add('comment');
    var divContent = document.createElement('div');
    divContent.classList.add('content');
    var divMeta = document.createElement('div');
    divMeta.classList.add('metadata');
    var divText = document.createElement('div');
    divText.classList.add('text');
    var spanDate = document.createElement('span');
    spanDate.innerHTML = message.date;
    divMeta.appendChild(spanDate);
    divText.appendChild(document.createTextNode(message.text));
    divContent.appendChild(divMeta);
    divContent.appendChild(divText);
    divComment.appendChild(divContent);

    chat.appendChild(divComment);
}