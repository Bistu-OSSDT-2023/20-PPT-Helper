


function sendMessage() {
    // 获取消息输入元素和用户输入的消息
    var messageInput = document.getElementById('message-input');
    var message = messageInput.value;

    // 检查用户是否输入了消息
    if (message !== '') {
        var chatContainer = document.getElementById('chat');
        var messageDiv = document.createElement('div');
        var assistantDiv = document.createElement('div');
        messageDiv.classList.add('message', 'from-user');
        assistantDiv.classList.add('message', 'from-assistant');
        messageDiv.textContent = 'You: ' + message;
        assistantDiv.textContent = 'ChatGLM: ';
        var thinking = document.createElement('span');
        thinking.textContent = '正在思考...';
        assistantDiv.appendChild(thinking);

        chatContainer.appendChild(messageDiv);
        chatContainer.appendChild(assistantDiv);
        scrollToBottom();

        // 清空消息输入框
        messageInput.value = '';


// 使用 fetch API 发送 POST 请求
        fetch('/sse', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                text:message
            })
        }).then(function(response) {
            // 获取一个 ReadableStream
            var reader = response.body.getReader();

            // 使用一个递归函数来读取数据
            function read() {
                return reader.read().then(({ value, done }) => {
                    if (done) {
                        console.log('Stream complete');
                        // 结束消息
                        return;
                    }

                    // 处理服务器发送的数据
                    let receivedData = new TextDecoder().decode(value);

                    // 根据你的SSE流格式，数据应该是按行分隔的
                    let lines = receivedData.split('\n');

                    for(let line of lines) {
                        // 解析每一行，获取事件类型和数据
                        let [key, ...values] = line.split(':');
                        let value = values.join(':').trim();


                        if(key === 'data') {
                            // 如果这一行是data，那么将数据添加到chat元素中
                            assistantDiv.textContent += value ;
                            scrollToBottom();
                        }
                    }


                    // 继续读取
                    return read();
                });
            }
            thinking.innerHTML = '';
            // 开始读取数据
            read().catch(function(err) {
                console.error('Error while reading:', err);
            });
        }).catch(function(err) {
            console.error('Error while sending:', err);
        });}}


// 新建对话
function createNewConversation() {
    var chatContainer = document.getElementById('chat');
    chatContainer.innerHTML = "";

    // 清空对话列表样式
    var conversationList = document.getElementById('conversation-list');
    var conversations = conversationList.getElementsByClassName('conversation');
    for (var i = 0; i < conversations.length; i++) {
        conversations[i].classList.remove('active');
    }

    // 添加新建对话样式
    var newConversationButton = document.querySelector('.conversations button');
    var newConversationLi = document.createElement('li');
    newConversationLi.classList.add('conversation', 'active');
    newConversationLi.innerHTML = '<button onclick="createNewConversation()">New Conversation</button>';
    conversationList.insertBefore(newConversationLi, newConversationButton.parentNode);

    // 滚动到聊天框底部
    scrollToBottom();
}
// 滚动到聊天框底部
function scrollToBottom() {
    var chatContainer = document.getElementById('chat');
    chatContainer.scrollTop = chatContainer.scrollHeight;
}
        // 使用fetch API向服务器发送POST请求
//         fetch('/sse', {
//             method: 'POST',  // 使用POST方法
//             headers: {
//                 'Content-Type': 'application/json'  // 设置内容类型为JSON
//             },
//             body: JSON.stringify({  // 将消息对象转化为JSON格式
//                 message: message
//             })
//         })
//             .then(response => response.json())  // 将响应转化为JSON格式
//             .then(data => {
//                 // 处理服务器返回的响应
//                 console.log(data);
//             });
//     } else {
//         // 如果用户没有输入消息，显示一个警告
//         alert('Please enter a message!');
//     }
// }
// var source = new EventSource('/sse');
//
// source.onmessage = function(event) {
//     // 这里直接使用event.data，而不是试图解析JSON
//     var data = event.data;
//
//     console.log('Received data: ' + data);
// };
//
// source.onerror = function(event) {
//     console.error('Error occurred:', event);
// };
//
// source.onmessage = function(event) {
//     // 将事件的data属性（JSON格式字符串）转化为对象
//     var data = JSON.parse(event.data);
//
//     // 根据事件类型进行不同的处理
//     if (event.event == 'add') {
//         // 如果事件类型是"add"，调用printMessage函数打印消息
//         printMessage(data);
//     } else if (event.event == 'finish') {
//         // 如果事件类型是"finish"，激活"Next Step"按钮并关闭EventSource
//         document.getElementById('next-btn').disabled = false;
//         source.close();
// //     }
// };


