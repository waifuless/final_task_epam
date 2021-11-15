function placeCitiesOrDistricts(regionSelectId, cityOrDistrictSelectId) {

    $(`#${cityOrDistrictSelectId} option:not(:first)`).remove();

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
            console.log(response);
            response.forEach((cityOrDistrict)=>{
                $(`#${cityOrDistrictSelectId}`).append('<option value="'
                    + cityOrDistrict.cityOrDistrictName
                    + '">'
                    + cityOrDistrict.cityOrDistrictName
                    + '</option>');
            });
            $(`#${cityOrDistrictSelectId}`).removeAttr('disabled');
        }
    });
}