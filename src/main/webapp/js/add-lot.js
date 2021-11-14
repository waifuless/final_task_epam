$(document).ready(function () {

    let maxImageCount = 3;

    let validateFile = function (file) {
        let maxFileSize = 5242880;
        return ((file.size <= maxFileSize) && ((file.type === 'image/png') || (file.type === 'image/jpeg')
            || (file.type === 'image/jpg') || (file.type === 'image/gif')));
    }

    let placeImage = function (path, imageName) {
        let divToFill = $($.parseHTML('<div class="div__added-image"></div>'))
            .appendTo('div.gallery');

        $($.parseHTML('<img alt="" />'))
            .attr('src', path)
            .appendTo(divToFill);

        $($.parseHTML('<input type="hidden" />'))
            .attr('name', imageName)
            .attr('value', path)
            .appendTo(divToFill);
    };

    let saveImageAndPlace = function(file){
        let formData = new FormData();
        formData.append( 'imageInput', file);
        $.ajax({
            type: 'POST',
            url: 'ControllerServlet?command=upload_image&requestIsAjax=true',
            data:formData,
            cache:false,
            contentType: false,
            processData: false,
            success: function(path) {
                let imageName = $('.div__added-image').length>0 ? 'otherImage[]' : 'mainImage';
                placeImage(path, imageName);
            },
            error: function (error){
                alert(error);
            }
        });
    }

    $('#gallery-photo-add-input').on('change', function () {
        if ($('.div__added-image').length < maxImageCount) {
            if (this.files && this.files[0]) {
                if(validateFile(this.files[0])) {
                    saveImageAndPlace(this.files[0]);
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
                saveImageAndPlace(file);
            }else{
                alert("Не допустипый формат или слишком большой размер файла. " +
                    "Допустмые форматы: png, jpeg, jpg, gif");
            }
        }
    })
});
