
function uploadFile() {
    // 获取文件输入元素和文件
    var fileInput = document.getElementById('file');
    var file = fileInput.files[0];

    // 创建一个新的FormData对象，用于存储要发送的数据
    var formData = new FormData();
    formData.append('file', file);

    // 创建新的XMLHttpRequest对象
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (this.readyState === 4 ) {
            document.getElementById('status').innerText = this.responseText;
        }
    };
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