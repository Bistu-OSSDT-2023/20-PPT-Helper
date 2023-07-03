var source;

function uploadFile() {
    // 获取文件输入元素和文件
    var fileInput = document.getElementById('file');
    var file = fileInput.files[0];

    // 创建一个新的FormData对象，用于存储要发送的数据
    var formData = new FormData();
    formData.append('file', file);

    // 创建新的XMLHttpRequest对象
    var xhr = new XMLHttpRequest();

    // 发送POST请求到服务器
    xhr.open('POST', '/upload2', true);
    xhr.send(formData);

    // 设置进度条
    // source = new EventSource("http://yourserver.com/sse_endpoint");
    //
    // source.onmessage = function(event) {
    //     var data = JSON.parse(event.data);
    //
    //     if (data.progress) {
    //         document.getElementById('progress-bar').value = data.progress;
    //         if (data.progress == 100) {
    //             document.getElementById('next-btn').disabled = false;
    //         }
    //     } else if (data.message) {
    //         printMessage(data.message);
    //     }
    // };
}

function printMessage(message) {
    var chat = document.getElementById('chat');
    var newMessageDiv = document.createElement("div");
    newMessageDiv.className = "message";
    chat.appendChild(newMessageDiv);

    var i = 0;
    function typeWriter() {
        if (i < message.length) {
            newMessageDiv.textContent += message.charAt(i);
            i++;
            setTimeout(typeWriter, 50); // 每50毫秒添加一个字符
        }
    }
    typeWriter();
}

function sendMessage() {
    var messageInput = document.getElementById('message-input');
    var message = messageInput.value;

    if (message) {
        var chat = document.getElementById('chat');
        chat.innerHTML += '<div class="message">You: ' + message + '</div>';
        messageInput.value = '';

        // 在这里，你可以将消息发送到你的服务器，然后从服务器获取响应，然后显示在聊天区域。
        // 这是一个基本的示例，你需要根据你的具体需求进行修改。
    } else {
        alert('Please enter a message!');
    }
}
