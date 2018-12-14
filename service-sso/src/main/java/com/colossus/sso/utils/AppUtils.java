package com.colossus.sso.utils;


import com.alibaba.fastjson.JSON;
import com.colossus.sso.exception.ServiceException;
import com.colossus.sso.vo.ApiError;
import okhttp3.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 系统通用的工具类方法
 */
public class AppUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(AppUtils.class);

	public static void sendCaptcha(String phone, String bizType, String memberUrl, OkHttpClient okHttpClient){
		RequestBody formBody = new FormBody.Builder()
				.add("phone", phone)
				.add("bizType",bizType)
				.build();
		Request request=new Request.Builder().url(memberUrl+"/api/member/send-captcha").post(formBody).build();
		try {
			Response response=okHttpClient.newCall(request).execute();
			if(!response.isSuccessful()){
				throw new ServiceException(JSON.parseObject(response.body().source().readUtf8(), ApiError.class).getMessages());
			}
		} catch (IOException e) {
			throw new ServiceException(e);
		}
	}



	/**
	 * 密码的MD5加密
	 * 
	 * @param plainText
	 *            待加密字符
	 * @return 返回MD5加密后的字符
	 */
	public static final String getMD5(String plainText) {
		String str = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			str = buf.toString();// 32位的加密
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
		return str;
	}

	/**
	 * 获取shiro的subject对象
	 * 
	 * @return
	 */
	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	/**
	 * 获得当前用户Id
	 *
	 * @return the current member id
	 */
	public static String getCurrentUserId() {
		try {
			return (String) SecurityUtils.getSubject().getPrincipal();
		}catch (UnavailableSecurityManagerException e){
			return null;
		}
	}

	/**
	 * shiro退出当前用户登录
	 * 
	 * @author chendilin
	 * @date 2016年8月29日
	 */
	public static void loginOut() {
		AppUtils.getSubject().logout();
	}


	/**
	 * 生成UUID
	 * 
	 * @author chendilin
	 * @date 2016年8月15日
	 */
	public static final String getUUID() {
		UUID uuid = UUID.randomUUID();
		if (uuid.toString().length() <= 32) { 
			return uuid.toString();
		} else {
			return uuid.toString().replace("-", "");
		}
	}


	/**
	 * 随机生成指定范围内的多少个随机数
	 * 
	 * @param number
	 *            生成随机数的范围，输入10则表示随机生成0-9的随机数字
	 * @param size
	 *            生成随机数的个数
	 * @return 返回随机数组成的string
	 * @author chendilin
	 * @date 2016年8月16日
	 */
	public static String getRadomNumber(int number, int size) {
		if (size <= 0 || number <= 0) {
			return "";
		}
		String radomNumber = "";
		Random random = new Random();
		for (int i = 0; i < size; i++) {
			radomNumber += random.nextInt(number);
		}
		return radomNumber;
	}

	/**
	 * 查询指定日期所在的周的第一天和最后一天
	 * 
	 * @param date
	 *            需要查询的指定日期
	 * @return 返回map类型key：beginDate 本周第一天，endDate 本周最后一天
	 * @author chendilin
	 * @date 2016年8月25日
	 */
	public static Map<String, String> getWeekDate(Date date) {
		Map<String, String> map = new HashMap<String, String>();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		map.put("beginDate", calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH));

		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		map.put("endDate", calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH) + " 23:59:59");

		return map;
	}

	/**
	 * 获取指定当天的时间起止，终止的时间段
	 * 
	 * @param date
	 *            指定时间
	 * @return 返回map类型key：beginDate 当天的起止时间，endDate 当天的最后时间（下一天的凌晨时间）
	 * @author chendilin
	 * @date 2016年8月25日
	 */
	public static Map<String, String> getDateTime(Date date) {
		Map<String, String> map = new HashMap<String, String>();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		map.put("beginDate", calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH));

		calendar.add(Calendar.DAY_OF_MONTH, 1);
		map.put("endDate", calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH));

		return map;
	}

	/**
	 * 获取指定日期所在月的第一天和最后一天
	 * 
	 * @param date
	 *            指定时间
	 * @return 返回map类型key：beginDate 当天的起止时间，endDate 当天的最后时间（下一天的凌晨时间）
	 * @author chendilin
	 * @date 2016年8月25日
	 */
	public static Map<String, String> getMonthDate(Date date) {
		Map<String, String> map = new HashMap<String, String>();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, 1);
		map.put("beginDate", calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH));
		calendar.roll(Calendar.DATE, -1);
		map.put("endDate", calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH) + " 23:59:59");

		return map;
	}

	/**
	 * 获取当前时间所在的本月起始，终止日期——————依据设定时区
	 * 
	 * @return
	 * @author chendilin
	 * @date 2016年8月25日
	 */
	public static Map<String, String> getMonthDate() {
		return AppUtils.getMonthDate(AppUtils.getSiteDate());
	}

	/**
	 * 获取当天起始，终止日期————依据设定的时区
	 * 
	 * @return 返回map类型key：beginDate 当天的起止时间，endDate 当天的最后时间（下一天的凌晨时间）
	 * @author chendilin
	 * @date 2016年8月25日
	 */
	public static Map<String, String> getDateTime() {
		return AppUtils.getDateTime(AppUtils.getSiteDate());
	}

	/**
	 * 获取当前时间所在的本周起始，终止日期——————依据设定时区
	 * 
	 * @return
	 * @author chendilin
	 * @date 2016年8月25日
	 */
	public static Map<String, String> getWeekDate() {
		return AppUtils.getWeekDate(AppUtils.getSiteDate());
	}

	/**
	 * 获取比当前时间几天前或者几天后的日期——————依据设定时区
	 * 
	 * @param days
	 *            -4代表是4天前的日期，4代表4天后的日期
	 * @return
	 * @author chendilin
	 * @date 2016年8月25日
	 */
	public static Date getBeforeOrAfterDay(int days) {
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(AppUtils.getSiteDate());
		// calendar.add(Calendar.DAY_OF_MONTH, days);
		return DateUtils.addDays(AppUtils.getSiteDate(), days);
		// return calendar.getTime();
	}

	/**
	 * 获取相对当前日期的指定月份——————依据设定时区
	 * 
	 * @param month
	 *            前或者后的月份，-1上个月，1下个月
	 * @return
	 * @author chendilin
	 * @date 2016年9月1日
	 */
	public static Date getBeforeMonth(int month) {
		return DateUtils.addMonths(AppUtils.getSiteDate(), month);
	}

	/**
	 * 两个日期的差值
	 * 
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return
	 * @author chendilin
	 * @date 2016年8月29日
	 */
	public static int getBetweenDays(String start, String end) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(AppUtils.parseDateTime(start));
		long time1 = cal.getTimeInMillis();
		cal.setTime(AppUtils.parseDateTime(end));
		long time2 = cal.getTimeInMillis();
		return (int) ((time2 - time1) / (1000 * 3600 * 24));
	}
	public static int getBetweenDays(Date start, Date end) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		long time1 = cal.getTimeInMillis();
		cal.setTime(end);
		long time2 = cal.getTimeInMillis();
		return (int) ((time2 - time1) / (1000 * 3600 * 24));
	}

	/**
	 * 获取一周的具体几号
	 * @param firstDay 一周的第一天
	 * @return
	 * @author chendilin
	 * @date 2016年10月10日
	 */
	public static List<Integer> getWeekDays(String firstDay){
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(AppUtils.parseDateTime(firstDay));
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		
		List<Integer> weekDays = new ArrayList<>(Arrays.asList(new Integer[7]));
		weekDays.set(0,calendar.get(Calendar.DAY_OF_MONTH));
		for(int i = 0 ; i < 6; i++){
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			weekDays.set(i+1,calendar.get(Calendar.DAY_OF_MONTH));
		}
		return weekDays;
	}
	/**
	 * 将Date类型时间转换成String类型，格式化后：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 *            时间
	 * @return String 类型时间
	 * @author chendilin
	 * @date 2016年8月25日
	 */
	public static String formartDateTime(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(date);
	}

	/**
	 * 将String类型时间解析为Date类型,格式为yyyy-MM-dd HH:mm:ss或者yyyy-MM-dd
	 * 
	 * @param date
	 *            需要解析的时间
	 * @return 返回Date类型时间
	 * @author chendilin
	 * @throws ParseException
	 *             日期解析错误
	 * @date 2016年8月25日
	 */
	public static Date parseDateTime(String date) {
		SimpleDateFormat simpleDateFormat = null;
		if (StringUtils.isNotEmpty(date) && date.length() > 11) {
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} else if (StringUtils.isNotEmpty(date) && date.length() < 11) {
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		} else {
			return null;
		}

		try {
			return simpleDateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取相对指定时间偏移几个小时的日期
	 * 
	 * @param date
	 *            指定的时间
	 * @param number
	 *            偏移量,eg：-5相对当前时间偏移5个小时的日期
	 * @return
	 * @author chendilin
	 * @date 2016年10月8日
	 */
	public static Date getSiteDate(Date date, int number) {
		if (date == null) {
			return null;
		}
		return DateUtils.addHours(date, number);
	}

	/**
	 * 依据时区获取当前的时间
	 * 
	 * @return
	 * @author chendilin
	 * @date 2016年10月8日
	 */
	public static Date getSiteDate() {
		return AppUtils.getSiteDate(new Date(), -16);
	}

	/**
	 * 获取日期中的天数
	 * 
	 * @param date
	 *            日期
	 * @return
	 * @author chendilin
	 * @date 2016年8月25日
	 */
	public static Integer getDateNumber(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取日期的小时数
	 * 
	 * @param date
	 *            日期
	 * @return
	 * @author chendilin
	 * @date 2016年8月25日
	 */
	public static Integer getDateHour(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 创建密匙
	 * 
	 * @param algorithm
	 *            加密算法,可用 DES,DESede,Blowfish
	 * @return SecretKey 秘密（对称）密钥 *@author pengsg @date2016/8/26
	 */
	public static SecretKey createSecretKey(String algorithm) {
		// 声明KeyGenerator对象
		KeyGenerator keygen;
		// 声明 密钥对象
		SecretKey deskeys = null;
		// byte[] enCodeFormat =null;
		// SecretKey deskey = new SecretKeySpec(null,"DES");
		try {
			// 返回生成指定算法的秘密密钥的 KeyGenerator 对象
			keygen = KeyGenerator.getInstance(algorithm);
			// 生成一个密钥
			deskeys = keygen.generateKey();
			// byte[] enCodeFormats = deskey.getEncoded();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// 返回密匙
		return deskeys;
	}

	/**
	 * 根据密匙进行DES加密
	 * 
	 * @param key
	 *            密匙
	 * @param info
	 *            要加密的信息
	 * @return String 加密后的字符串
	 * @author pengsg @date2016/8/26
	 * 
	 */
	public static String encryptToDES(SecretKey key, String info) {
		// 定义 加密算法,可用 DES,DESede,Blowfish
		String Algorithm = "DES";
		// 加密随机数生成器 (RNG),(可以不写)
		SecureRandom sr = new SecureRandom();
		// 定义要生成的密文
		byte[] cipherByte = null;
		try {
			// 得到加密/解密器
			Cipher c1 = Cipher.getInstance(Algorithm);
			// 用指定的密钥和模式初始化Cipher对象
			// 参数:(ENCRYPT_MODE, DECRYPT_MODE, WRAP_MODE,UNWRAP_MODE)
			c1.init(Cipher.ENCRYPT_MODE, key, sr);
			// 对要加密的内容进行编码处理,
			cipherByte = c1.doFinal(info.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 返回密文的十六进制形式
		return byte2hex(cipherByte);
	}

	/**
	 * 将二进制转化为16进制字符串
	 * 
	 * @param b
	 *            二进制字节数组
	 * @return String
	 * @author pengsg @date2016/8/26
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}

	/**
	 * 根据密匙进行DES解密
	 * 
	 * @param key
	 *            密匙
	 * @param sInfo
	 *            要解密的密文
	 * @return String 返回解密后信息
	 * @author pengsg @date2016/8/26
	 */
	public static String decryptByDES(SecretKey key, String sInfo) {
		// 定义 加密算法,
		String Algorithm = "DES";
		// 加密随机数生成器 (RNG)
		SecureRandom sr = new SecureRandom();
		byte[] cipherByte = null;
		try {
			// 得到加密/解密器
			Cipher c1 = Cipher.getInstance(Algorithm);
			// 用指定的密钥和模式初始化Cipher对象
			c1.init(Cipher.DECRYPT_MODE, key, sr);
			// 对要解密的内容进行编码处理
			cipherByte = c1.doFinal(hex2byte(sInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return byte2hex(cipherByte);
		return new String(cipherByte);
	}

	/**
	 * 十六进制字符串转化为2进制
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hex2byte(String hex) {
		byte[] ret = new byte[hex.length() / 2];
		byte[] tmp = hex.getBytes();
		for (int i = 0; i < hex.length() / 2; i++) {
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}
		return ret;
	}

	/**
	 * 将两个ASCII字符合成一个字节； 如："EF"--> 0xEF
	 * 
	 * @param src0
	 *            byte
	 * @param src1
	 *            byte
	 * @return byte
	 */
	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	@SuppressWarnings("restriction")
	public static String StringBASE64(byte[] b) {

		String hs = new sun.misc.BASE64Encoder().encodeBuffer(b);
		return hs;
	}

	@SuppressWarnings("restriction")
	public static byte[] byteBASE64(String s) throws IOException {
		byte[] hss = new sun.misc.BASE64Decoder().decodeBuffer(s);
		return hss;
	}

	/**
	 * 根据密匙进行AES加密
	 * 
	 * @param sKey
	 *            密匙
	 * @param sSrc
	 *            要加密的信息
	 * @return String 加密后的字符串
	 * @author pengsg @date2016/9/2
	 * 
	 */
	@SuppressWarnings("restriction")
	public static String encrypt(String sSrc, String sKey) throws Exception {
		if (sKey == null) {
			System.out.print("Key为空null");
			return null;
		}
		// 判断Key是否为16位
		if (sKey.length() != 16) {
			System.out.print("Key长度不是16位");
			return null;
		}
		byte[] raw = sKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		// "算法/模式/补码方式"
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
		IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes());
		// 此处使用BASE64做转码功能，同时能起到2次加密的作用。
		return new sun.misc.BASE64Encoder().encodeBuffer(encrypted);
	}

	/**
	 * 根据密匙进行AES解密
	 * 
	 * @param sKey
	 *            密匙
	 * @param sSrc
	 *            要加密的信息
	 * @return String 加密后的字符串
	 * @author pengsg @date2016/9/2
	 * 
	 */
	@SuppressWarnings("restriction")
	public static String decrypt(String sSrc, String sKey) throws Exception {
		try {
			// 判断Key是否正确
			if (sKey == null) {
				System.out.print("Key为空null");
				return null;
			}
			// 判断Key是否为16位
			if (sKey.length() != 16) {
				System.out.print("Key长度不是16位");
				return null;
			}
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			// 先用base64解密
			byte[] encrypted1 = new sun.misc.BASE64Decoder().decodeBuffer(sSrc);
			try {
				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original);
				return originalString;
			} catch (Exception e) {
				System.out.println(e.toString());
				return null;
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
			return null;
		}
	}

	/**
	 * bigdecimal 除法 (originalPrice-discountPrice)/originalPrice
	 * 
	 * @param originalPrice 原价
	 * @param discountPrice 折扣价
	 * @return
	 * @author chendilin
	 * @date 2016年9月6日
	 */
	public static double discountRate(BigDecimal originalPrice, BigDecimal discountPrice) {
		if(originalPrice == null ||originalPrice.equals(BigDecimal.ZERO) || originalPrice.doubleValue() == 0){
			return 1;
		}
		return originalPrice.subtract(discountPrice).divide(originalPrice, 3, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * 普通除法 first/second ,保留小数
	 * 
	 * @param first
	 * @param second
	 * @return
	 * @author chendilin
	 * @date 2016年9月6日
	 */
	public static double mathDiv(int first, int second) {
		BigDecimal bigDecimal1 = new BigDecimal(first);
		BigDecimal bigDecimal2 = new BigDecimal(second);
		return bigDecimal1.divide(bigDecimal2, 3, RoundingMode.HALF_UP).doubleValue();
	}


	
	/**
	 * 文本内容将特殊符号转换成html的标识("&","<","\"",">","'")
	 * @param content 需要转换的文字内容
	 * @return
	 */
	public static String htmlContent2Str(String content){
		if(StringUtils.isEmpty(content)){
			return content;
		}
		String[] regex = {"<","\"",">","'"};
		String[] replacement = {"&lt;","&quot;","&gt;","&#39;"};
		for (int i = 0; i < regex.length; i++) {
			content = content.replaceAll(regex[i], replacement[i]);
		}
		return content;
	}
	
	/**
	 * 将html标签去掉，将p标签替换为br标签
	 * @param content
	 * @return
	 */
	public static String html_br(String content) {
		content = content.replaceAll("</(?i)p>", "<br/>")
				.replaceAll("<(?i)br[^>]?>", "______").replaceAll("<[^>]+>", "")
				.replaceAll("______", "\n");

		content = content.replaceAll("&nbsp;", " ").replaceAll("&lt", "<").replaceAll("&gt", ">");
		return content;
	}


	/**
	 * 将纯文本中的/n替换为br标签
	 * @param content
	 * @return
	 */
	public static String text2html(String content){
		return content.replaceAll("\n", "<br/>");
	}

	/**
	 * 复制Bean
	 *
	 * @param dest the dest
	 * @param orig the orig
	 */
	public static void copyProperties(Object dest, Object orig) {
		try{
			BeanUtils.copyProperties(dest, orig);
		}catch(Exception e){
			throw new ServiceException("复制Bean属性出现异常", e);
		}
	}
	
	/**
	 * 克隆Bean
	 *
	 * @param <T> the generic type
	 * @param orig the orig
	 * @param clazz the clazz
	 * @return the t
	 */
	public static <T> T clone(Object orig,Class<T> clazz){
		try {
			T dest = clazz.newInstance();
			BeanUtils.copyProperties(dest, orig);
			return dest;
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new ServiceException("克隆Bean出现异常", e);
		}
		
	}
	
	private static final String[] IP_HEADER_CANDIDATES = { 
		    "X-Forwarded-For",
		    "Proxy-Client-IP",
		    "WL-Proxy-Client-IP",
		    "HTTP_X_FORWARDED_FOR",
		    "HTTP_X_FORWARDED",
		    "HTTP_X_CLUSTER_CLIENT_IP",
		    "HTTP_CLIENT_IP",
		    "HTTP_FORWARDED_FOR",
		    "HTTP_FORWARDED",
		    "HTTP_VIA",
		    "REMOTE_ADDR" };

	/**
	 * 获取客户端的Ip地址
	 *
	 * @return the client ip address
	 */
	public static String getClientIpAddress(){
		RequestAttributes requestAttributes;
		try {
			requestAttributes = RequestContextHolder.currentRequestAttributes();
		}catch (IllegalStateException e) {
			logger.debug("获取当前客户端IP失败:{}",e.getMessage());
			return "0.0.0.0";
		}

		if(requestAttributes == null ||!(requestAttributes instanceof ServletRequestAttributes) ){
			return "";
		}
		
		HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
		if(request == null){
			return "";
		}
		
		for (String header : IP_HEADER_CANDIDATES) {
			String ip = request.getHeader(header);
			if (StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
				return ip;
			}
		}	
		
		return request.getRemoteAddr();
	}

	public static String hashPassword(String password, String salt) {
		return getMD5(password+salt);
	}

	public static void sleep(int millis) {
		try{
			Thread.sleep(millis);
		}catch(Exception e){
			throw new ServiceException("休眠失败",e);
			
		}
	}
}
