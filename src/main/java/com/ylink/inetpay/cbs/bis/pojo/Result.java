/**
 * 版权所有(C) 2013 深圳市雁联计算系统有限公司
 * 创建:LS 2013-8-29
 */

package com.ylink.inetpay.cbs.bis.pojo;

import java.io.Serializable;

import com.ylink.inetpay.common.core.constant.ESchedJobQueueStatus;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedJobQueueDto;

/**
 * @author LS
 * @date 2013-8-29
 * @description：
 */

public class Result implements Serializable {
	private static final long serialVersionUID = -5705613543441028464L;

	// 任务执行状态
	protected ESchedJobQueueStatus status;
	// 计划明细主键
	protected String planDetaiId;
	// 计划明细对象
	protected BisSchedJobQueueDto bisSchedJobQueueDto;
	// 错误信息
	protected String message;

	public Result() {
	}

	public Result(ESchedJobQueueStatus status, String planDetaiId,
			BisSchedJobQueueDto schedJobQueueDto, String message) {
		this.status = status;
		this.planDetaiId = planDetaiId;
		this.bisSchedJobQueueDto = schedJobQueueDto;
		this.message = message;
	}

	public Result(ESchedJobQueueStatus status, String planDetaiId,
			BisSchedJobQueueDto schedJobQueueDto) {
		this(status, planDetaiId, schedJobQueueDto, "");
	}

	public ESchedJobQueueStatus getStatus() {
		return status;
	}

	public void setStatus(ESchedJobQueueStatus status) {
		this.status = status;
	}

	public String getPlanDetaiId() {
		return planDetaiId;
	}

	public void setPlanDetaiId(String planDetaiId) {
		this.planDetaiId = planDetaiId;
	}

	public BisSchedJobQueueDto getBisSchedJobQueueDto() {
		return bisSchedJobQueueDto;
	}

	public void setBisSchedJobQueueDto(BisSchedJobQueueDto bisSchedJobQueueDto) {
		this.bisSchedJobQueueDto = bisSchedJobQueueDto;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}