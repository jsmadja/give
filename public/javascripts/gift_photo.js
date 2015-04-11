var video = document.querySelector('video');
if (video) {
    video.addEventListener('click', snapshot, false);
    var canvas = document.querySelector('canvas');
    canvas.width = 640;
    canvas.height = 480;
    var ctx = canvas.getContext('2d');
    var localMediaStream = null;

    function snapshot() {
        if (localMediaStream) {
            ctx.drawImage(video, 0, 0);
            var data = canvas.toDataURL('image/webp');
            document.querySelector('#snapshot-img').src = data;
            document.querySelector('#photowebcam').value = data;
        }
    }

    navigator.getUserMedia = navigator.getUserMedia ||
    navigator.webkitGetUserMedia ||
    navigator.mozGetUserMedia ||
    navigator.msGetUserMedia;
    navigator.getUserMedia({video: true},
        function (stream) {
            document.querySelector('#snapshot-img').style.display = 'inline-block';
            document.querySelector('video').style.display = 'inline-block';
            video.src = window.URL.createObjectURL(stream);
            localMediaStream = stream;
        },
        function (ev) {
            console.log(ev);
        });
}
