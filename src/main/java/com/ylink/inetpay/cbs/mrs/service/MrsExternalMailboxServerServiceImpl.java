package com.ylink.inetpay.cbs.mrs.service;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.mrs.dao.MrsExternalMailboxServerDtoMapper;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsExternalMailboxServerDto;
@Service("mrsExternalMailboxServerService")
@Transactional(value=CbsConstants.TX_MANAGER_MRS)
public class MrsExternalMailboxServerServiceImpl implements
		MrsExternalMailboxServerService{
	@Autowired
	private MrsExternalMailboxServerDtoMapper mrsExternalMailboxServerDtoMapper;
	@Override
	public PageData<MrsExternalMailboxServerDto> findListPage(
			PageData<MrsExternalMailboxServerDto> pageData,
			MrsExternalMailboxServerDto mrsExternalMailboxServerDto){
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<MrsExternalMailboxServerDto> findListPage=mrsExternalMailboxServerDtoMapper.list(mrsExternalMailboxServerDto);
		Page<MrsExternalMailboxServerDto> page=(Page<MrsExternalMailboxServerDto>) findListPage;
		pageData.setTotal(page.getTotal());
		pageData.setRows(findListPage);
		return pageData;
	}

	@Override
	public void insert(MrsExternalMailboxServerDto mrsExternalMailboxServerDto){
		mrsExternalMailboxServerDto.setId(UUID.randomUUID().toString());
		mrsExternalMailboxServerDto.setCreateTime(new Date());
		mrsExternalMailboxServerDtoMapper.insert(mrsExternalMailboxServerDto);
	}

	@Override
	public void update(MrsExternalMailboxServerDto mrsExternalMailboxServerDto){
		mrsExternalMailboxServerDto.setUpdateTime(new Date());
		mrsExternalMailboxServerDtoMapper.updateByPrimaryKey(mrsExternalMailboxServerDto);
	}

	@Override
	public void delete(String id){
		mrsExternalMailboxServerDtoMapper.deleteByPrimaryKey(id);
	}

	@Override
	public MrsExternalMailboxServerDto details(String id) {
		return mrsExternalMailboxServerDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public String getServerPath(String suffi){
		return mrsExternalMailboxServerDtoMapper.getServerPath(suffi);
	}
}
