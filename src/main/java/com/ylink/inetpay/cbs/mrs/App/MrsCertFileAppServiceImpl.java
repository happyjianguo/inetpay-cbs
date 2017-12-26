package com.ylink.inetpay.cbs.mrs.App;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.eu.util.tools.CollectionUtil;
import com.ylink.inetpay.cbs.mrs.service.MrsCertFileService;
import com.ylink.inetpay.common.project.cbs.app.MrsCertFileAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsCertFileDto;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsCertFileVo;


@Service("mrsCertFileAppService")
public class MrsCertFileAppServiceImpl implements MrsCertFileAppService {
	
	@Autowired
	private MrsCertFileService mrsCertFileService;

	@Override
	public MrsCertFileVo findByCustId(String custId) {
		List<MrsCertFileDto> mrsCertFiles = mrsCertFileService.findByCustId(custId);
		//组装的数据
		Set<String> certTypes = new HashSet<String>();
		MrsCertFileVo vo = null;
		if(!CollectionUtil.isEmpty(mrsCertFiles)){
			for( MrsCertFileDto dto : mrsCertFiles ){
				certTypes.add(dto.getCertType());
			}
			vo = new MrsCertFileVo();
			vo.setMrsCertFileDtos(mrsCertFiles);
			vo.setFileTypes(certTypes);
			
		}else {
			return null;
		}
		HashMap<String, String> files = new HashMap<>();
		for(String type : certTypes){
			for( MrsCertFileDto dto : mrsCertFiles ){
				if(type.equals(dto.getCertType())){
					HashMap<String, String> ss = new HashMap<>();
					ss.put(type, dto.getFileRemark());
					
					files.putAll(ss);
				}
			}
		}
		vo.setFiles(files);
		
		return vo;
	}

}
