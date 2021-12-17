function printCashAccountInHeader(){
    let printArea = $('#cashAccountModalBody');
    printArea.empty();
    printArea.append(`
<div class="spinner-border" role="status">
  <span class="visually-hidden">Loading...</span>
</div>`);
    $.ajax({
        type: 'GET',
        url: 'ControllerServlet',
        dataType: "json",
        cache: false,
        data: {
            requestIsAjax: true,
            command: "find_user_cash_account",
        },
        success: function (answer) {
            printArea.empty();
            printArea.append(answer);
        },
        error: function (xhr, textStatus, thrownError) {
            alert("Code error: " + xhr.status + "\nMessage: " + xhr.responseText);
        }
    });
}