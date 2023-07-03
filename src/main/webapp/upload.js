function uploadFile() {
    var fileInput = document.getElementById('file');
    var file = fileInput.files[0];

    var formData = new FormData();
    formData.append('file', file);

    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/upload2', true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            console.log('File uploaded successfully.');
        }
    };
    xhr.send(formData);
}