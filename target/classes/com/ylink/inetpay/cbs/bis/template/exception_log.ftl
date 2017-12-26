<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<title>异常信息</title>
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
				height: 478px;
				padding: 35px;
				background: #fff url(${PORTAL_CALLBACK_ADDR}/img/emailbg.png) no-repeat;
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
				<span>登记结算管理平台&bull;异常监控</span>
			</div>
			<h2>Hi ! 亲爱的<span>${currentAdministrator}</span></h2>
			<p>系统出现异常了，请及时安排程序员解决【上海保险交易所】</p>
			<p>
				异常信息：<br />
				${errorMsg}
			</p>
			<div class="copy">
				此邮件由上海保险交易所系统发出，无需回复
			</div>
		</div>
	</body>
</html>