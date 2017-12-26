package com.ylink.inetpay.cbs.mrs.App;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.mrs.dao.MrsContactListDtoMapper;
import com.ylink.inetpay.cbs.mrs.service.MrsContactListService;
import com.ylink.inetpay.common.core.constant.EStatusEnum;
import com.ylink.inetpay.common.project.cbs.app.MrsContactListAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsContactListDto;


@Service("mrsContactListAppService")
public class MrsContactListAppServiceImpl implements MrsContactListAppService {
	
	@Autowired
	private MrsContactListService mrsContactListService;
	@Autowired
	private MrsContactListDtoMapper mrsContactListDtoMapper;

	@Override
	public int insert(MrsContactListDto dto) {
		return mrsContactListService.insert(dto);
	}

	@Override
	public int updateByPrimaryKey(MrsContactListDto dto) {
		return mrsContactListService.updateByPrimaryKey(dto);
	}
	

	@Override
	public MrsContactListDto selecte(String mrsContact) {
		// 
		MrsContactListDto mrsContactListDto =mrsContactListDtoMapper.selectByPrimaryKey(mrsContact);
		return mrsContactListDto;
	}

	@Override
	public int add(MrsContactListDto list) {
		return	mrsContactListDtoMapper.insert(list);

	}

	@Override
	public int delect(MrsContactListDto mrsContactListDto) {
//		mrsContactListDto.setStatus(EStatusEnum.UNEFFECTIVE.getValue());
		return	mrsContactListDtoMapper.updateByPrimaryKey(mrsContactListDto);

	}

	@Override
	public int update(MrsContactListDto mrsContactListDto) {
		return mrsContactListDtoMapper.updateByPrimaryKey(mrsContactListDto);

	}

	@Override
	public PageData<MrsContactListDto> listWidthRolesPage(PageData<MrsContactListDto> pageDate,
			MrsContactListDto params) {
			PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
			List<MrsContactListDto> list = mrsContactListDtoMapper.queryAllData(params);
			Page<MrsContactListDto> page = (Page<MrsContactListDto>) list;
			pageDate.setTotal(page.getTotal());
			pageDate.setRows(list);
			return pageDate;
		}

	@Override
	public MrsContactListDto selecteCustidandCertno(MrsContactListDto mrsContactListDto) {
		MrsContactListDto ContactList =mrsContactListDtoMapper.selectBycustId( mrsContactListDto);
		return ContactList;
	}

	@Override
	public List<MrsContactListDto> findByCustId(String custId) {
		return mrsContactListService.findByCustId(custId);
	}

	@Override
	public List<MrsContactListDto> queryAllData(MrsContactListDto param) {
		return mrsContactListService.queryAllData(param);
	}

}
