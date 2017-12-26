package com.ylink.inetpay.cbs.cls.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.bis.dao.BisSysParamDtoMapper;
import com.ylink.inetpay.cbs.cls.dao.ClsAccessCheckMapper;
import com.ylink.inetpay.common.core.SystemParamConstants;
import com.ylink.inetpay.common.core.util.SftpUtil;
import com.ylink.inetpay.common.project.clear.dto.ClsAccessCheck;

@Service("clsAccessCheckService")
public class ClsAccessCheckServiceImpl implements ClsAccessCheckService {

	@Autowired
	ClsAccessCheckMapper clsAccessCheckMapper;
	@Autowired
	BisSysParamDtoMapper bisSysParamDtoMapper;
	
	protected static final Logger logger = LoggerFactory.getLogger(ClsAccessCheckServiceImpl.class);
	
	@Override
	public PageData<ClsAccessCheck> queryAllData(PageData<ClsAccessCheck> pageData, ClsAccessCheck clsAccessCheck) {
		
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<ClsAccessCheck> list = clsAccessCheckMapper.queryAllData(clsAccessCheck);
		Page<ClsAccessCheck> page = (Page<ClsAccessCheck>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public ClsAccessCheck selectByPrimaryKey(String id) {
		return clsAccessCheckMapper.selectByPrimaryKey(id);
	}

	/**
	 * 下载对账单
	 */
	@Override
	public byte[] download(ClsAccessCheck clsAccessCheck) {
		
		String ftpUrl = clsAccessCheck.getFtpUrl();
		//FTP上文件存放的路径
		String downloadPath = bisSysParamDtoMapper.selectByKey(SystemParamConstants.MER_UPLOAD_PATH).getValue()+ clsAccessCheck.getFileDay() + "/";
		createFileIfNecessary(bisSysParamDtoMapper.selectByKey(SystemParamConstants.TEMP_LOCAL_PATH).getValue());
		//临时存放路径
		String localpath = bisSysParamDtoMapper.selectByKey(SystemParamConstants.TEMP_LOCAL_PATH).getValue();
		//文件名从accessFile截取出来
		String downloadFileName = StringUtils.substringAfterLast(clsAccessCheck.getAccessFile(), "/");
		
		return downloadFTPFile(ftpUrl, downloadPath, downloadFileName, localpath);
	}
	/**
	 * @方法描述:  下载ftp文件
	 * @作者： pst23
	 * @日期： 2017-04-18
	 * @param filePath 
	 * @返回类型： void
	 */
	private byte[] downloadFTPFile(String host,String downloadPath,String downloadFileName,String localpath){
		logger.info("下载对账文件，文件名称："+downloadFileName);
		try {
			Long current = System.currentTimeMillis();
			String user = bisSysParamDtoMapper.selectByKey(SystemParamConstants.MER_FTP_USER).getValue();
			String pwd = bisSysParamDtoMapper.selectByKey(SystemParamConstants.MER_FTP_PW).getValue();
			Integer port = Integer.parseInt(bisSysParamDtoMapper.selectByKey(SystemParamConstants.MER_FTP_PORT).getValue());
            //下载文件
			File file = SftpUtil.download(host,port,user,pwd,downloadPath,downloadFileName,localpath);
			if(null != file){
				logger.info("下载对账文件完成，文件名称：{"+downloadFileName+"}，耗时：{"+(System.currentTimeMillis()-current)+"}ms");
			}
			 byte[] files =  File2byte(file);
			//删除临时文件
			delFile(localpath+downloadFileName);
			return files;
		} catch (Exception e) {
			String msg="下载对账文件出现异常，文件名称："+downloadFileName+",异常原因："+e;
			logger.error(msg);
		}
		return null;
	}
	/**
	 * 删除文件
	 * 
	 * @param filePathAndName
	 *            String 文件路径及名称 如c:/fqf.txt
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public void delFile(String filePathAndName) {
		try {
			File myDelFile = new File(filePathAndName);
			if(myDelFile.exists()){
				myDelFile.delete();
				logger.info("需要删除的文件:"+filePathAndName+"已删除");
			}else{
				logger.info("需要删除的文件:"+filePathAndName+"不存在");
			}
		} catch (Exception e) {
			logger.info("删除文件操作出错");
		}
	}
	/**
	 * 文件转换处理
	 * @param file
	 * @return
	 */
	public static byte[] File2byte(File file)  
    {  
        byte[] buffer = null;  
        try  
        {  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream();  
            byte[] b = new byte[1024];  
            int n;  
            while ((n = fis.read(b)) != -1)  
            {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        }  
        catch (FileNotFoundException e)  
        {  
            e.printStackTrace();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
        return buffer;  
    }  
	/**
	 * @方法描述:  判断目录是否存在，如果不存在的话则创建
	 * @作者： 1603254
	 * @日期： 2016-6-27-下午4:40:42
	 * @param folder 
	 * @返回类型： void
	*/
	private void createFileIfNecessary(String folder){
		File file=new File(folder);
		if(!file.exists()){
			if(file.mkdirs() == false){
				logger.info(folder+"文件目录创建失败");
			}
		}
	}
}
