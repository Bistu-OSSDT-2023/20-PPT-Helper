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
    // 获取消息输入元素和用户输入的消息
    var messageInput = document.getElementById('message-input');
    var message = messageInput.value;

    // 检查用户是否输入了消息
    if (message) {
        // 将用户的消息添加到聊天窗口
        var chat = document.getElementById('chat');
        chat.innerHTML += '<div class="message">You: ' + message + '</div>';

        // 清空消息输入框
        messageInput.value = '';

        // 使用fetch API向服务器发送POST请求
        fetch('/sse', {
            method: 'POST',  // 使用POST方法
            headers: {
                'Content-Type': 'application/json'  // 设置内容类型为JSON
            },
            body: JSON.stringify({  // 将消息对象转化为JSON格式
                message: message
            })
        })
            .then(response => response.json())  // 将响应转化为JSON格式
            .then(data => {
                // 处理服务器返回的响应
                console.log(data);
            });
    } else {
        // 如果用户没有输入消息，显示一个警告
        alert('Please enter a message!');
    }
}
var source = new EventSource('/sse');

source.onmessage = function(event) {
    // 这里直接使用event.data，而不是试图解析JSON
    var data = event.data;

    console.log('Received data: ' + data);
};

source.onerror = function(event) {
    console.error('Error occurred:', event);
};

source.onmessage = function(event) {
    // 将事件的data属性（JSON格式字符串）转化为对象
    var data = JSON.parse(event.data);

    // 根据事件类型进行不同的处理
    if (event.event == 'add') {
        // 如果事件类型是"add"，调用printMessage函数打印消息
        printMessage(data);
    } else if (event.event == 'finish') {
        // 如果事件类型是"finish"，激活"Next Step"按钮并关闭EventSource
        document.getElementById('next-btn').disabled = false;
        source.close();
    }
};


