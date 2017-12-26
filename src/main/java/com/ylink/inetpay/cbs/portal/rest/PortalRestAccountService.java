package com.ylink.inetpay.cbs.portal.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ylink.inetpay.common.project.portal.restVO.AccountBaseRespVO;
import com.ylink.inetpay.common.project.portal.restVO.CheckPayPwdRespVO;
import com.ylink.inetpay.common.project.portal.restVO.EditLoginPwdRespVO;
import com.ylink.inetpay.common.project.portal.restVO.InitPayPwdRespVO;
import com.ylink.inetpay.common.project.portal.restVO.UpdatePayPwdRespVO;
import com.ylink.inetpay.common.project.portal.restVO.BankRestVO.BankAddRespVO;
import com.ylink.inetpay.common.project.portal.restVO.BankRestVO.BankBusiRespListVO;
import com.ylink.inetpay.common.project.portal.restVO.BankRestVO.BankBusiRespVO;
import com.ylink.inetpay.common.project.portal.restVO.BankRestVO.BankSetDefaultRespVO;
import com.ylink.inetpay.common.project.portal.restVO.BankRestVO.GetCodeRespVO;
import com.ylink.inetpay.common.project.portal.restVO.actBillRestVO.ActBillRespListVO;
import com.ylink.inetpay.common.project.portal.restVO.actBillRestVO.ActBillRespVO;
import com.ylink.inetpay.common.project.portal.restVO.paymentRestVO.PaymentRespListVO;
import com.ylink.inetpay.common.project.portal.restVO.paymentRestVO.PaymentRespVO;
import com.ylink.inetpay.common.project.portal.restVO.rechargeRestVO.RechargeRespListVO;
import com.ylink.inetpay.common.project.portal.restVO.rechargeRestVO.RechargeRespVO;
import com.ylink.inetpay.common.project.portal.restVO.withdrawRestVO.WithdrawRespListVO;
import com.ylink.inetpay.common.project.portal.restVO.withdrawRestVO.WithdrawRespVO;
import com.ylink.inetpay.common.project.portal.vo.AccountMsgInfoRespVo;
import com.ylink.inetpay.common.project.portal.vo.AccountVO;
import com.ylink.inetpay.common.project.portal.vo.BaseRespVO;
import com.ylink.inetpay.common.project.portal.vo.ContactListMsgRespVo;
import com.ylink.inetpay.common.project.portal.vo.LoginMsgSearchResponseVO;
import com.ylink.inetpay.common.project.portal.vo.account.AccountAmtResponseVO;
import com.ylink.inetpay.common.project.portal.vo.account.AccountInfoResponseVO;
import com.ylink.inetpay.common.project.portal.vo.account.BankauthResponseVO;
import com.ylink.inetpay.common.project.portal.vo.account.LoginCheckResponseVO;
import com.ylink.inetpay.common.project.portal.vo.account.LoginResponseVO;
import com.ylink.inetpay.common.project.portal.vo.account.QuickRechageResponseVO;
import com.ylink.inetpay.common.project.portal.vo.account.SendsmsResponseVO;

/**
 * 门户对外rest处理接口
 * 
 * @author yuqingjun
 *
 */
@Path("portal")
public interface PortalRestAccountService {

	/**
	 * 根据一户通查询账户余额接口
	 *
	 * @param 传入JsonParams
	 * @return 返回Json结果
	 * @throws Exception
	 */
	@POST
	@Path("account/findAccountAmt")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	AccountAmtResponseVO findAccountAmt(String params);

	/**
	 * 账户资金查询接口
	 *
	 * @param 传入JsonParams
	 * @return 返回Json结果
	 * @throws Exception
	 */
	@POST
	@Path("account/actInfo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	AccountInfoResponseVO actInfo(String params);

	/**
	 * 客户登录接口
	 *
	 * @param 传入JsonParams
	 * @return 返回Json结果
	 * @throws Exception
	 */
	@POST
	@Path("login/index")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	LoginResponseVO loginIndex(String params);

	/**
	 * 验证用户接口
	 *
	 * @param 传入JsonParams
	 * @return 返回Json结果
	 * @throws Exception
	 */
	@POST
	@Path("account/pwdreset/index")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	LoginCheckResponseVO checkLoginUser(String params);

	/**
	 * 发送手机短信接口
	 *
	 * @param 传入JsonParams
	 * @return 返回Json结果
	 * @throws Exception
	 */
	@POST
	@Path("account/sendsms")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	SendsmsResponseVO sendsms(String params);

	/**
	 * 重置用户登录密码接口
	 *
	 * @param 传入JsonParams
	 * @return 返回Json结果
	 * @throws Exception
	 */
	@POST
	@Path("account/pwdreset/updateLoginPwd")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BaseRespVO updateLoginPwd(String params);

	/**
	 * 发送邮件接口
	 *
	 * @param 传入JsonParams
	 * @return 返回Json结果
	 * @throws Exception
	 */
	@POST
	@Path("account/pwdreset/sendemail")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BaseRespVO sendemail(String params);

	/**
	 * 校验邮件接口
	 *
	 * @param 传入JsonParams
	 * @return 返回Json结果
	 * @throws Exception
	 */
	@POST
	@Path("account/checkEmail")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	LoginCheckResponseVO checkEmail(String params);

	/**
	 * 提交资料(个人)身份证和被动开户
	 *
	 * @param 传入JsonParams
	 * @return 返回Json结果
	 * @throws Exception
	 */
	@POST
	@Path("account/openact/aduitByPerson")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BaseRespVO aduitByPerson(String params);

	/**
	 * 授信绑卡实名认证
	 *
	 * @param 传入JsonParams
	 * @return 返回Json结果
	 * @throws Exception
	 */
	@POST
	@Path("account/openact/dobankauth")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BankauthResponseVO dobankauth(String params);

	/**
	 * 充值处理接口---快捷和授信共用
	 *
	 * @param 传入JsonParams
	 * @return 返回Json结果
	 * @throws Exception
	 */
	@POST
	@Path("account/recharge")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	QuickRechageResponseVO quickRecharge(String params);

	/**
	 * 提现处理接口---快捷和授信共用
	 *
	 * @param 传入JsonParams
	 * @return 返回Json结果
	 * @throws Exception
	 */
	@POST
	@Path("account/withdraw")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BaseRespVO dowithdraw(String params);

	/**
	 * 一户通信息查询接口
	 *
	 * @param 传入JsonParams
	 * @return 返回Json结果
	 * @throws Exception
	 */
	@POST
	@Path("account/findCustInfo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	LoginResponseVO findCustInfo(String params);

	/**
	 * 用户免密登录接口
	 *
	 * @param 传入JsonParams
	 * @return 返回Json结果
	 * @throws Exception
	 */
	@POST
	@Path("login/loginToken")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	LoginResponseVO loginToken(String params);

	/**
	 * 昵称修改
	 */
	@POST
	@Path("account/base/editalias/edit")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	AccountBaseRespVO aliasEdit(String params);

	/**
	 * 手机校验
	 */
	@POST
	@Path("ajaxcheck/checkMobile")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	AccountBaseRespVO checkMobile(String params);
	
	/**
	 * 手机设置
	 */
	@POST
	@Path("account/base/addMobile")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	AccountBaseRespVO addMobile(String params);

	/**
	 * 邮箱设置
	 */
	@POST
	@Path("account/base/editemail/sendmail")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	AccountBaseRespVO addEmail(String params);
	
	/**
	 * 校验登录密码
	 */
	@POST
	@Path("account/checkLoginPwd")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	EditLoginPwdRespVO checkLoginPwd(String params);

	/**
	 * 修改登录密码
	 */
	@POST
	@Path("account/base/password/editLoginPwd")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	EditLoginPwdRespVO editLoginPwd(String params);

	/**
	 * 初始化支付密码
	 */
	@POST
	@Path("account/safe/initPayPwd")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	InitPayPwdRespVO initPayPwd(String params);

	/**
	 * 检查支付密码
	 */
	@POST
	@Path("account/checkPayPwd")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	CheckPayPwdRespVO checkPayPwd(String params);

	/**
	 * 修改支付密码
	 */
	@POST
	@Path("account/safe/edit")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	UpdatePayPwdRespVO updatePayPwd(String params);

	/**
	 * 生成快捷绑卡认证码接口
	 */
	@POST
	@Path("quick/getCode")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	GetCodeRespVO getCode(String params);

	/**
	 * 快捷绑卡
	 */
	@POST
	@Path("quick/doQuick")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BankAddRespVO doQuick(String params);

	/**
	 * 快捷解绑
	 */
	@POST
	@Path("account/bank/delQuick")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BaseRespVO delQuick(String params);

	/**
	 * 授信绑卡
	 */
	@POST
	@Path("account/bank/addsx")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BankAddRespVO bankAddsx(String params);

	/**
	 * 授信解绑
	 */
	@POST
	@Path("account/bank/doDelete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BankBusiRespVO doDelete(String params);

	/**
	 * 银行账户列表
	 */
	@POST
	@Path("account/bank/index")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BankBusiRespListVO bankCardQueryAllData(String params);

	/**
	 * 银行账户详情
	 */
	@POST
	@Path("account/bank/view")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BankBusiRespVO bankCardDetail(String params);

	/**
	 * 删除银行账户
	 */
	@POST
	@Path("account/bank/doDelCard")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BankBusiRespVO doDelCard(String params);

	/**
	 * 默认卡设置
	 */
	@POST
	@Path("account/bank/updateDefault")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BankSetDefaultRespVO bankSetDefault(String params);

	/**
	 * 收支明细列表
	 */
	@POST
	@Path("transaction/actbill/index")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	ActBillRespListVO actbillQueryAllData(String params);

	/**
	 * 收支明细详情
	 */
	@POST
	@Path("transaction/actbill/detail")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	ActBillRespVO actBillDetail(String params);

	/**
	 * 支付记录列表
	 */
	@POST
	@Path("transaction/payment/index")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	PaymentRespListVO paymentQueryAllData(String params);

	/**
	 * 支付记录详情
	 */
	@POST
	@Path("transaction/payment/detail")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	PaymentRespVO paymentDetail(String params);

	/**
	 * 充值记录列表
	 */
	@POST
	@Path("transaction/recharge/index")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RechargeRespListVO rechageQueryAllData(String params);

	/**
	 * 充值记录详情
	 */
	@POST
	@Path("transaction/recharge/detail")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RechargeRespVO rechageDetail(String params);

	/**
	 * 提现记录列表
	 */
	@POST
	@Path("transaction/withdraw/index")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	WithdrawRespListVO withdrawQueryAllData(String params);

	/**
	 * 提现记录详情
	 */
	@POST
	@Path("transaction/withdraw/detail")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	WithdrawRespVO withdrawDetail(String params);

	/**
	 * 根据一户通账号查询 客户信息(个人客户信息)
	 * 
	 * @param params
	 * @return
	 */
	@POST
	@Path("accountMsgInfo/searchMsgInfo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	AccountMsgInfoRespVo findMrsPersonDtoByCustId(String params);

	/**
	 * 修改客户信息(个人客户信息)
	 * 
	 * @param params
	 */
	@POST
	@Path("accountMsgInfo/updatePersonInfo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	AccountMsgInfoRespVo updateMrsPersonDto(String params);

	/**
	 * 新增联系人
	 * 
	 * @param params
	 * @return
	 */
	@POST
	@Path("mrsContactMsg/saveMsg")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BaseRespVO saveMrsContactDto(String params);

	/**
	 * 修改联系人信息
	 * 
	 * @param params
	 * @return
	 */
	@POST
	@Path("mrsContactMsg/updateMsg")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BaseRespVO updateMrsContactDto(String params);

	/**
	 * 查询联系人信息列表
	 * 
	 * @param params
	 * @return
	 */
	@POST
	@Path("mrsContactMsg/selectMsg")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	List<Object> selectMrsContactDto(String params);

	/**
	 * 查询单个联系人详情
	 * 
	 * @param params
	 * @return
	 */
	@POST
	@Path("mrsContactMsg/selectDetailMsg")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	ContactListMsgRespVo selectMrsContactDetail(String params);

	/**
	 * 删除联系人信息
	 * 
	 * @param params
	 * @return
	 */
	@POST
	@Path("mrsContactMsg/deleteMsg")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BaseRespVO deleteMrsContactDto(String params);

	/**
	 * 根据用户微信号查询用户信息
	 * 
	 * @return
	 */
	@POST
	@Path("mrsLoginUserMsg/loginUserMsg")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	LoginMsgSearchResponseVO findUserByWeChatId(String params);

	/**
	 * 根据微信号查询一互通信息
	 * 
	 * @param weChatId
	 * @return
	 */
	@POST
	@Path("account/findMrsAccountByWeChatId")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	List<Object> findMrsAccountByWeChatId(String params);

	/**
	 * 根据微信号查询一互通状态、子账户状态
	 * 
	 * @param weChatId
	 * @return
	 */
	@POST
	@Path("account/findMrsAccountStatusByWeChatId")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	LoginMsgSearchResponseVO findMrsAccountStatusByWeChatId(String params);

	/**
	 * 微信号、一户通账号绑定
	 * 
	 * @param params
	 * @return
	 */
	@POST
	@Path("account/bindWeChatId")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BaseRespVO bindingWeChatIdandCustId(String params);

	/**
	 * 个人客户注册
	 * 
	 * @return
	 */
	@POST
	@Path("register/doreg")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BaseRespVO doRegisterPersonDto(String params);

	/**
	 * 个人实名认证开户
	 * 
	 * @return
	 */
	@POST
	@Path("account/openact/openAndAduit")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BaseRespVO doRealRegister(String params);

	/**
	 * 开通一户通账户、资金账户
	 * 
	 * @return
	 */
	@POST
	@Path("account/openact/directlyOpenAccount")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	AccountVO createAccountandActAccount(String params);

	/**
	 * 附件上传
	 * 
	 * @param params
	 * @return
	 */
	@POST
	@Path("account/base/authentication/uploadOrganFile")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BaseRespVO uploadFiles(String params);
	
	
	/**
	 * 设置密码和手机接口
	 *
	 * @param 传入JsonParams
	 * @return 返回Json结果
	 * @throws Exception
	 */
	@POST
	@Path("account/safe/setloginpwd/dosetinfo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BaseRespVO dosetinfo(String params);
}
