$(".answer-write input[type=submit]").click(addAnswer);

function addAnswer(e) {
    e.preventDefault();
    console.log("click");

    var queryString = $(".answer-write").serialize();
    console.log("query : " + queryString);

    var url = $(".answer-write").attr("action");
    console.log("url : " + url);

    $.ajax({
        type : 'post',
        url : url,
        data : queryString,
        dataType : 'json',
        error : onError,
        success : onSuccess
    });
}

function onError(error, status) {
console.log("> " + status);
    console.log(error);

}

function onSuccess(data, status) {
    console.log("> " + status);
    console.log(data);
}

