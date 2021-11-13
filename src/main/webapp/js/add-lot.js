$(document).ready(function () {

    let maxImageCount = 3;

    let validateFile = function (file) {
        let maxFileSize = 5242880;
        return ((file.size <= maxFileSize) && ((file.type === 'image/png') || (file.type === 'image/jpeg')
            || (file.type === 'image/jpg') || (file.type === 'image/gif')));
    }

    let placeImage = function (path) {
        let divToFill = $($.parseHTML('<div class="div__added-image"></div>'))
            .appendTo('div.gallery');

        $($.parseHTML('<img alt="" />'))
            .attr('src', path)
            .appendTo(divToFill);
    };

    $('#gallery-photo-add-input').on('change', function () {
        if ($('.div__added-image').length < maxImageCount) {
            if (this.files && this.files[0]) {
                if(validateFile(this.files[0])) {
                    let input = $('#gallery-photo-add-input')[0];
                    let formData = new FormData();
                    formData.append( 'imageInput', input.files[0] );
                    $.ajax({
                        type: 'POST',
                        url: 'ControllerServlet?command=upload_image&requestIsAjax=true',
                        data:formData,
                        cache:false,
                        contentType: false,
                        processData: false,
                        success: function(path) {
                            placeImage(path);
                        }
                    });
                }else{
                    alert("Не допустипый формат или слишком большой размер файла. " +
                        "Допустмые форматы: png, jpeg, jpg, gif");
                }
            }
        }
    });

    let dropZone = $('div.input-image');

    dropZone.on('drag dragstart dragend dragover dragenter dragleave drop', function () {
        return false;
    });

    dropZone.on('dragover dragenter', function () {
        dropZone.addClass('dragover');
    });

    dropZone.on('dragleave', function (e) {
        dropZone.removeClass('dragover');
    });

    dropZone.on('drop', function (e) {
        if ($('.div__added-image').length < maxImageCount) {
            dropZone.removeClass('dragover');
            let file = e.originalEvent.dataTransfer.files[0];
            if (validateFile(file)) {
                loadAndPlaceImage(file);
            }else{
                alert("Не допустипый формат или слишком большой размер файла. " +
                    "Допустмые форматы: png, jpeg, jpg, gif");
            }
        }
    })
});