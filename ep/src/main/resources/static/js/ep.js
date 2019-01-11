//判断正整数
function checkRate(str)
{
var re = /^[1-9]+[0-9]*]*$/;

if (!re.test(str))
{
// alert("请输入正整数");
// input.rate.focus();
return false;
}else{
	return true;
}
}