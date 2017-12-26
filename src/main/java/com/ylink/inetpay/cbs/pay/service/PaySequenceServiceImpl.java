package com.ylink.inetpay.cbs.pay.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.pay.dao.PaySequenceMapper;


@Service("paySequenceService")
public class PaySequenceServiceImpl implements PaySequenceService {
	
	@Autowired
	private PaySequenceMapper paySequenceMapper;

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public String getSequenceId(String prefix, String seqName,int length) {
		StringBuffer seq=new StringBuffer(prefix);
		seq.append(seqName);
		
		Long value=paySequenceMapper.getPaySeqNo(seqName);
		
		String formatStr = "%0" + length + "d";
		seq.append(String.format(formatStr, value));
		
		return  seq.toString();
	}
	
	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public String getSequenceId(String seqName,int length) {
		return  getSequenceId(new SimpleDateFormat("yyyyMMdd").format(new Date()),seqName,length);
	}

}
