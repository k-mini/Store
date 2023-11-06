
let index = {

    init : function() {
        $("#comment-save").on("click", ()=>{
            this.save();
        });
    },

    save: function () {
        let data = {
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

function replyToggle(commentId) {
    let selector = $(`#reply-bar-${commentId}`);
    if (selector.is(':visible')) {
        selector.hide();
    } else {
        selector.show();
    }
    // return selector.is(':visible') ? selector.hide() : selector.show();
}

function saveReply(boardId, topCommentId) {

    let data = {
        boardId : boardId,
        topCommentId : topCommentId,
        content : $(`#reply-content-${topCommentId}`).val()
    }

    $.ajax({
        type : "POST",
        url : "/api/comment/" + topCommentId + "/reply" ,
        contentType : "application/json; charset=utf-8",
        data : JSON.stringify(data),
        dataType : "json"
    }).done(res =>{
        console.log(res);
        $(`#reply-bar-${topCommentId}`).before(getReplyComp(res.data));
        $(`#reply-content-${topCommentId}`).val('');
    }).fail((err) => {
        JSON.stringify(err);
    })
}

function getCommentComp(data) {
    let comp =
        `<div id="comment-${data.id}" class="mb-6 text-base bg-white rounded-lg dark:bg-gray-900">
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
            
            <div class="flex items-center py-4">
                <button id="reply-toggle-${data.id}" 
                        onclick="replyToggle(${data.id})"
                        type="button"
                        class="flex items-center text-sm text-gray-500 hover:underline dark:text-gray-400 font-medium">
                    답글 달기
                </button>
            </div>
            
            <div id="reply-bar-${data.id}" class="mb-6 lg:ml-6" style="display: none;">
                <div class="py-2 px-4 mb-4 bg-white rounded-lg rounded-t-lg border border-gray-200 dark:bg-gray-800 dark:border-gray-700">
                <textarea id="reply-content-${data.id}" rows="3"
                          class="px-0 w-full text-sm text-gray-900 border-0 focus:ring-0 focus:outline-none dark:text-white dark:placeholder-gray-400 dark:bg-gray-800"
                          placeholder="답글 적기..."></textarea>
                </div>
                <button onclick="saveReply(${boardId}, ${data.id})" type="button"
                        class="mb-5 inline-block rounded bg-primary py-1 px-4 text-center text-xs font-semibold leading-loose text-white">
                    작성
                </button>
            </div>
        </div>`;
    return comp;
}

function getReplyComp(reply) {

    let comp = `
    <div id="comment-${reply.replyId}" class="py-4 lg:ml-6 text-base bg-white rounded-lg dark:bg-gray-900">
        <div class="flex justify-between items-center mb-2">
            <div class="flex items-center">
                <p class="inline-flex items-center mr-3 text-sm text-gray-900 dark:text-white font-semibold">
                    ${reply.replyUserName}</p>
                <p class="text-sm text-gray-600 dark:text-gray-400">
                    <time pubdate datetime="2022-02-08" title="February 8th, 2022">${reply.createdDate}</time></p>
            </div>

            <div class="flex items-center">
                <button
                        onclick="updateComment(${reply.replyId})"
                        class="inline-flex items-center mr-3 text-sm text-gray-900 hover:underline dark:text-white font-semibold">
                    수정</button>
                <button
                        onclick="deleteComment(${reply.replyId})"
                        class="inline-flex items-center mr-3 text-sm text-gray-900 hover:underline dark:text-white font-semibold">
                    삭제</button>
            </div>

        </div>
        <p class="text-gray-500 py-4 dark:text-gray-400">${reply.content}</p>
    </div>`;
    return comp;
}
