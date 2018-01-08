function getCurDate()
{
    var d = new Date();
    var hours = add_zero(d.getHours());
    var minutes = add_zero(d.getMinutes());
    var seconds=add_zero(d.getSeconds());
    var ndate =hours+":"+minutes+":"+seconds;
    divT.innerHTML= ndate;
}

function add_zero(temp)
{
    if(temp<10) return "0"+temp;
    else return temp;
}

setInterval("getCurDate()",100);