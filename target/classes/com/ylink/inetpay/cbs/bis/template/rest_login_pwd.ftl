<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<title>验证邮件内容</title>
		<style>
			body {
				width: 100%;
				height: 100%;
				background: #f5f5f5;
				font-family: "microsoft yahei";
			}
			
			.email {
				margin: 10px auto;
				position: relative;
				width: 647px;
				height: 578px;
				padding: 35px;
				background: #fff url(${PORTAL_CALLBACK_ADDR}/img/emailbgnew.png) no-repeat;
			}
			
			.email h2 {
				clear: both;
				font-size: 17px;
				color: #ff6405;
				font-weight: 500;
				line-height: 25px;
				margin-bottom: 5px;
				padding-top: 18px;
				border-top: 1px dashed #dedede;
			}
			.email h2 span{
				padding: 0 5px;
				color: #588fcf;
			}
			.email p {
				font-size: 14px;
				color: #666;
				line-height: 30px;
				word-break: break-all;
			
			}
			
			.email .btn {
				display: inline-block;
				color: #fff;
				background: #eb6800;
				padding: 0 15px;
				font-size: 14px;
				line-height: 35px;
				text-decoration: none;
				border-radius: 3px;
			}
			
			.email p a {
				color: #148cf1;
				text-decoration: underline;
			}
			
			.email .logo {
				position: relative;
				float: left;
				width: 110px;
				height: 48px;
				margin: 26px 0;
				margin-top: 0;
				background: url(${PORTAL_CALLBACK_ADDR}/img/logo.jpg) no-repeat left center;
				
			}
			
			.email .logo a {
				display: inline-block;
				width: 100%;
				height: 100%;
			}
			
			.email .logo span {
				display: inline-block;
				position: absolute;
				width: 300px;
				left: 110px;
				top: 5px;
	
				word-break: keep-all;
				padding: 0 15px;
				font-size: 18px;
				font-weight: 500;
				line-height: 38px;
				color: #666;
				border-left: 1px solid #999;
			}
			.email .copy{
				color: #bbb;
				font-size: 12px;
				position: absolute;
				bottom: 30px;
			}
		</style>
	</head>

	<body>
		<div class="email">
			<div class="logo">
				<a href="javascript:void(0);"></a>
				<span>一户通&bull;设置密码</span>
			</div>
			<h2>尊敬的<span>${custmerName}</span>，您好：</h2>
			<p>感谢您在上海保险交易所办理相关业务！为了更好地为您提供服务，依相关业务流程及您所提交的信息，我们免费为您开立了保险一户通账户（账号为${custId}），您可通过该账户查询在我所办理业务的情况。请点击如下链接设置保险一户通账户登录密码，设置完成后请予妥善保管。
			请在24小时内完成设置，如果未做任何操作，系统将保留原密码。链接仅一次有效【上海保险交易所】</p>
			<a href="${PORTAL_CALLBACK_ADDR}/account/pwdreset/emailcallback/pwdindex.html?callback_key=${callback_key}&loginId=${loginId}" class="btn">立即修改密码</a>
			<p>
				请点击以下链接完成验证：<br />
				<a href="${PORTAL_CALLBACK_ADDR}/account/pwdreset/emailcallback/pwdindex.html?callback_key=${callback_key}&loginId=${loginId}" >
					${PORTAL_CALLBACK_ADDR}/account/pwdreset/emailcallback/pwdindex.html?callback_key=${callback_key}&loginId=${loginId}
				</a>
				<br /> 如果以上链接无法打开，请把以上地址复制到浏览器地址栏中打开（该链接在24小时内有效）。
				<br /> 如有问题，欢迎致电：021-20708881。
				<br /> 感谢您对上海保险交易所的支持，我们将竭诚为您服务！
			</p>
			<div class="copy">
				（此邮件由上海保险交易所系统发出，无需回复）
			</div>
		</div>
	</body>

</html>