
let index = {

    init : function() {
        $("#comment-save").on("click", ()=>{
            this.save();
        });
    },

    save: function () {
        data = {
            boardId : boardId,
            content : $("#comment-content").val()
        }

        $.ajax({
            type : "POST",
            url : "/api/comment",
            contentType : "application/json; charset=utf-8",
            data : JSON.stringify(data),
            dataType : "json"
        }).done(res =>{
            console.log(res);
            $("#comment-box").append(getCommentComp(res.data));
            $("#comment-content").val('');
        }).fail((err) => {
            JSON.stringify(err);
        })
    },
}

index.init();

function getCommentComp(data) {
    let comp =
        `<div id="${data.id}" class="mb-6 text-base bg-white rounded-lg dark:bg-gray-900">
            <div class="flex justify-between items-center mb-2">
                <div class="flex items-center">
                    <p class="inline-flex items-center mr-3 text-sm text-gray-900 dark:text-white font-semibold">
                        ${data.commentUserName}</p>
                    <p class="text-sm text-gray-600 dark:text-gray-400">
                        <time pubdate datetime="2022-02-08" title="February 8th, 2022">${data.createdDate}</time></p>
                </div>

                <div class="flex items-center">
                    <button onclick="updateComment(${data.id})" class="inline-flex items-center mr-3 text-sm text-gray-900 hover:underline dark:text-white font-semibold">
                        수정</button>
                    <button onclick="deleteComment(${data.id})" class="inline-flex items-center mr-3 text-sm text-gray-900 hover:underline dark:text-white font-semibold">
                        삭제</button>
                </div>
            </div>
            
            <p class="text-gray-500 py-4 dark:text-gray-400">${data.content}</p>

            <div class="flex items-center py-6">
                <button type="button"
                        class="flex items-center text-sm text-gray-500 hover:underline dark:text-gray-400 font-medium">
                    답글달기
                </button>
            </div>

        </div>`;
    return comp;
}

function updateComment(comment_id) {
    alert("update 실행");
}
function deleteComment(comment_id) {

    $.ajax({
        type : "DELETE",
        url : "/api/comment/" + comment_id,
    }).done(res =>{
        console.log(res);
        $(`#comment-${comment_id}`).remove();
    }).fail((err) => {
        JSON.stringify(err);
    })
}