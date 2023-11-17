
let board_index = {

    init : function() {
        $("#board-delete").on("click", ()=>{
            this.delete(category, subCategory);
        });
        $("#form-update").on("click", () =>{
            this.update(category, subCategory);
        });
        $("#board-trade").on("click", () =>{
           this.trade();
        });
    },

    delete : function(category, subCategory) {

        $.ajax({
            type : "DELETE",
            url : `/api/board/${category}/${subCategory}/${boardId}`
        }).done(res =>{
            console.log(res.data);
            location.href=`/boards/${category}/${subCategory}`;
        }).fail((err) => {
            console.log(err);
        })
    },

    load: function () {
        // alert("load 함수 실행!");
        
        $.ajax({
            type : "GET",
            url : `/api/boards/${category}`
        }).done(res =>{
            console.log(res.data);
            res.data.forEach((board)=>{
                let component = getBoardComponent(board);
                $("#boardList").append(component);
            });
        }).fail((err) => {
            console.log(err);
        })
    },

    update : function(category, subCategory) {

        let data = {
            boardId : boardId,
            title : $("#title").val(),
            file : $("#file").val(),
            content : $("#content").val()
        }
        let form = $("#itemBoardUpdateFormDto")[0];
        let formData = new FormData(form);
        console.log(data);
        $.ajax({
            type : "PATCH",
            url : `/api/board/${category}/${subCategory}/${boardId}`,
            data : formData,
            contentType : false,
            processData : false
        }).done(res =>{
            console.log(res.data);
            location.href=`/boards/${category}/${subCategory}`;
        }).fail((err) => {
            console.log(err);
        })
    },

    trade : function() {

        let data = {
            boardId : boardId,
            buyerId : userId
        };

        $.ajax({
            type : "POST",
            url : `/api/trade/${boardId}`,
            data : data
        }).done(res =>{
            console.log(res.data);
            alert("거래 신청 완료되었습니다.")
            location.href=`/boards/${category}/${subCategory}`;
        }).fail((err) => {
            alert("거래 신청이 실패했습니다. 다시 요청해 주세요.")
            console.log(err);
        })
    },


}

board_index.init();

function getBoardComponent(board) {
    let item =
        `<div class="w-full px-4 md:w-1/2 lg:w-1/3">
                <div class="wow fadeInUp group mb-10" data-wow-delay=".1s">
                    <div class="mb-8 overflow-hidden rounded">
                        <a href="/boards/trade/1" class="block">
                            <img
                                    src="/assets/images/blog/blog-01.jpg"
                                    alt="image"
                                    class="w-full transition group-hover:rotate-6 group-hover:scale-125"
                            />
                        </a>
                    </div>
                    <div>
                <span
                        class="mb-5 inline-block rounded bg-primary py-1 px-4 text-center text-xs font-semibold leading-loose text-white"
                >
                  ${board.createdDate}
                </span>
                        <h3>
                            <a
                                    href="trade-detail.html"
                                    class="mb-4 inline-block text-xl font-semibold text-dark hover:text-primary sm:text-2xl lg:text-xl xl:text-2xl"
                            >
                                ${board.title}
                            </a>
                        </h3>
                        <p class="text-base text-body-color">
                            ${board.content}
                        </p>
                    </div>
                </div>
            </div>`;
    return item;
}
