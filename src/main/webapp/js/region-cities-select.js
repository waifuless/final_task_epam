function placeCitiesOrDistricts(regionSelectId, cityOrDistrictSelectId, funcAfterSuccess) {

    $(`#${cityOrDistrictSelectId} option:not(:first)`).remove();

    if($(`#${regionSelectId}`).children('option:first-child').is(':selected')){
        $(`#${cityOrDistrictSelectId}`).prop('disabled', true);
        return;
    }

    $.ajax({
        url: "ControllerServlet",
        type: "POST",
        dataType: "json",
        data: {
            requestIsAjax: true,
            command: "find_cities_or_districts_by_region",
            region: $(`#${regionSelectId} option:selected`).val()
        },
        success: function (response) {
            response.forEach((cityOrDistrict)=>{
                $(`#${cityOrDistrictSelectId}`).append('<option value="'
                    + cityOrDistrict.cityOrDistrictName
                    + '">'
                    + cityOrDistrict.cityOrDistrictName
                    + '</option>');
            });
            $(`#${cityOrDistrictSelectId}`).prop('disabled', false);
            funcAfterSuccess();
        }
    });
}