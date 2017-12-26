package com.ylink.inetpay.cbs.bis.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisMessageNotificationDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

/**
 * 短信消息服务类
 * @author pst30
 *
 */
public interface BisMsgNotificationService {
	/**
	 * 分页查询短信消息
	 * @param pageDate
	 * @param BisMessageNotificationDto
	 * @return
	 */
	public PageData<BisMessageNotificationDto> findListPage(PageData<BisMessageNotificationDto> pageDate,BisMessageNotificationDto bisMessageNotificationDto);
	/**
	 * 保存短信消息
	 * @param BisMessageNotificationDto
	 * @throws CbsCheckedException
	 */
	public void saveNotification(BisMessageNotificationDto bisMessageNotificationDto);
	/**
	 * 短信消息详情
	 * @param id
	 * @return
	 * @throws CbsCheckedException
	 */
	public BisMessageNotificationDto details(String id);
}
