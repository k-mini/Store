
let trade_index = {

    init : function() {
        $("#board-trade").on("click", () =>{
            this.submitTrade();
        })
    },

    submitTrade : function() {

        let data = {
            boardId : boardId
        };

        $.ajax({
            type : "POST",
            url : `/api/trade/${boardId}`,
            dataType : "json"
        }).done(res =>{
            console.log(res.data);
            alert("거래 신청 완료되었습니다.")
            location.href=`/boards/${category}/${subCategory}`;
        }).fail((err) => {
            alert("거래 신청이 실패했습니다.")
            console.log(err);
        })
    },
}

trade_index.init();

function acceptTrade(tradeId, userId) {
    $.ajax({
        type : "PATCH",
        url : `/api/trade/${tradeId}/accept`,
        dataType : "json"
    }).done(res =>{
        console.log(res.data);
        alert("거래 승낙되었습니다.")
        location.href=`/user/${userId}/trade-history`;
    }).fail((err) => {
        alert("거래 승낙이 실패했습니다.")
        console.log(err);
    })
}

function denyTrade(tradeId, userId) {
    $.ajax({
        type : "PATCH",
        url : `/api/trade/${tradeId}/deny`,
        contentType : "application/json; charset=utf-8",
        dataType : "json"
    }).done(res =>{
        console.log(res.data);
        alert("거래 거절 완료되었습니다.")
        location.href=`/user/${userId}/trade-history`;
    }).fail((err) => {
        alert("거래 거절 실패했습니다.")
        console.log(err);
    })
}

function cancelTrade(tradeId, userId) {
    $.ajax({
        type : "PATCH",
        url : `/api/trade/${tradeId}/cancel`,
        contentType : "application/json; charset=utf-8",
        dataType : "json"
    }).done(res =>{
        console.log(res.data);
        alert("거래 취소 완료되었습니다.")
        location.href=`/user/${userId}/trade-history`;
    }).fail((err) => {
        alert("거래 취소 실패했습니다.")
        console.log(err);
    })
}

function completeTrade(tradeId, userId) {
    $.ajax({
        type : "PATCH",
        url : `/api/trade/${tradeId}/complete`,
        dataType : "json"
    }).done(res =>{
        console.log(res.data);
        alert("거래 완료 신청 되었습니다.")
        location.href=`/user/${userId}/trade-history`;
    }).fail((err) => {
        alert("거래 완료 신청 실패했습니다.")
        console.log(err);
    })
}