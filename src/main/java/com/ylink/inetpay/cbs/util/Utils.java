package com.ylink.inetpay.cbs.util;
/**
 * 工具类
 * @author haha
 *
 */
public class Utils {
	public static String getIndex(String index,int number){
		if(index!=null && index.length()<number){
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < number-index.length(); i++) {
				sb.append("0");
			}
			sb.append(index);
			return sb.toString();
		}else{
			return index;
		}
	}
}
