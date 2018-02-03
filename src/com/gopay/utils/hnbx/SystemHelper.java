package com.gopay.utils.hnbx;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

/**
 * 系统设置
 * 
 * @author LuoGang
 */
public class SystemHelper {
	/** 日志对象使用log4j */
	//public static final Logger logger = LogManager.getLogger(SystemHelper.class);

	/** 文件目录分隔符/ */
	public static final String FILE_SEPARATOR = "/";

	/** WEB容器根目录,以斜杠结尾 */
	//public static final String WEB_ROOT;

//	static {
//		String path = Helper.decodeURL(SystemHelper.class.getResource("/").getFile()); // WEB-INF/classes目录
//		File file = new File(path);
//		// int c = SystemHelper.class.getName().split(".").length;//
//		// 与WEB-INF/classes目录的距离
//		// c += 2; // 返回至根目录下的距离
//		int c = 2;
//		while (c > 0) {
//			file = file.getParentFile();
//			c--;
//		}
//		String root = file.getAbsolutePath();
//		// if (!File.separator.equals("/")) root = root.replace("/",
//		// File.separator);
//		// if (!File.separator.equals("\\")) root = root.replace("\\",
//		// File.separator);
//		root.replace("\\", FILE_SEPARATOR);
//		if (!root.endsWith(FILE_SEPARATOR)) {
//			root += FILE_SEPARATOR;
//		}
//		WEB_ROOT = root;
//	}

//	/** WEB-INF目录,以斜杠结尾 */
//	public static final String WEB_INF_ROOT = WEB_ROOT + "WEB-INF" + FILE_SEPARATOR;
//	/** WEB-INF/classes目录,以斜杠结尾 */
//	public static final String WEB_INF_CLASSES_ROOT = WEB_ROOT + "WEB-INF" + FILE_SEPARATOR + "classes"
//			+ FILE_SEPARATOR;

	/**
	 * 得到相对路径path的真实文件路径
	 * 
	 * @param path
	 * @return
//	 */
//	public static final String getAbsoluteFile(String path) {
//		if (path.startsWith(FILE_SEPARATOR)) path = path.substring(1);
//		return WEB_ROOT + path;
//	}

	/** 加载过的所有配置 */
	static final Map<String, Properties> caches = new HashMap<String, Properties>();
	/** 默认的配置文件 */
	public static final String DEFAULT_RESOURCE = System.getProperty("system.defaultResource", "ems.cfg.properties");
	/** 配置文件 */
	public static Properties props = getProperties(DEFAULT_RESOURCE);

	/** 系统代码,ems */
	public static final String SYSTEM_CODE = getProperty("system.code", "ems");

	/** 是否为调试模式 */
	public static final boolean DEBUG = StringHelper.parseBoolean(getProperty("system.devMode"));

	/** ascii编码 */
	public static final String ENCODING_ASCII = "ascii";

	/** big5编码 */
	public static final String ENCODING_BIG5 = "big5";

	/** gbk编码 */
	public static final String ENCODING_GBK = "gbk";

	/** gb2312编码 */
	public static final String ENCODING_GB2312 = "gb2312";

	/** utf-8编码 */
	public static final String ENCODING_UTF_8 = "utf-8";

	/** iso-8859-1编码 */
	public static final String ENCODING_ISO_8859_1 = "iso-8859-1";

	/** 系统使用的默认编码.未指定则使用utf-8 */
	public static final String ENCODING = getProperty("system.encoding", ENCODING_UTF_8);

	/** 系统的换行符 */
	public static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");

	/** windows下的换行符，r\n,与LINE_SEPARATOR可能不一致 */
	public static final String ENTER = "\r\n";

	/** 配置数组属性的分隔符.默认为逗号 */
	public static final String ARRAY_SEPARATOR = ",";
	/**
	 * 数组分隔符的正则表达式：\s*${separator}\s*
	 * 
	 * @see String#split(String)
	 */
	public static final String ARRAY_SEPARATOR_REGEX = "\\s*(" + ARRAY_SEPARATOR + ")+\\s*";

	/**
	 * 读取文件,转换为InputStream
	 * 
	 * @param resource
	 * @return
	 */
	public static InputStream getResourceAsStream(String resource) {
		String stripped = resource.startsWith("/") ? resource.substring(1) : resource;
		InputStream stream = null;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader != null) {
			stream = classLoader.getResourceAsStream(stripped);
		}
		if (stream == null) {
			stream = SystemHelper.class.getResourceAsStream(resource);
		}
		if (stream == null) {
			stream = SystemHelper.class.getClassLoader().getResourceAsStream(stripped);
		}
		if (stream == null) {
			//logger.warn(resource + "不存在!");
		}
		return stream;
	}

	/**
	 * 读取配置文件
	 * 
	 * @param resource 资源文件名
	 * @return 配置文件的键值,此方法不会返回null,如果文件不存在或者为空,则返回一个空的Properties对象
	 */
	public static final Properties getProperties(String resource) {
		Properties props = caches.get(resource);
		if (props != null && !props.isEmpty()) return props;

		if (props == null) {
			props = new Properties();
			caches.put(resource, props);
		}
		InputStream in = getResourceAsStream(resource);
		if (in == null) return props;
		try {
			props.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//logger.error(resource + "加载失败!", e);
		} finally {
			//in.close();
			//Helper.close(in);
		}

		String include = props.getProperty("include");
		//logger.debug("include=" + include);
		if (include == null || include.length() <= 0) return props;

		// 读取include的文件
		String[] includes = include.trim().split(ARRAY_SEPARATOR_REGEX);
		for (String res : includes) {
			if (res == null || res.length() <= 0) continue;
			props.putAll(getProperties(res));
		}
		return props;
	}

	/**
	 * 加载配置文件
	 * 
	 * @param resource
	 * @return
	 */
	public static final Properties load(String resource) {
		Properties props = getProperties(resource);
		if (!props.isEmpty()) {
			// 将读取到的配置增加到当前默认的配置中去
			SystemHelper.props.putAll(props);
		}
		return props;
	}

	/**
	 * 取得配置属性key的值
	 * 
	 * @param key
	 * @return
	 */
	public static final String getProperty(String key) {
		return props.getProperty(key);
	}

	/**
	 * 取得配置属性key的值,如果没有则返回默认值defaultValue
	 * 
	 * @param key
	 * @param defaultValue
	 *            如果key未找到则返回defaultValue
	 * @return
	 */
	public static final String getProperty(String key, String defaultValue) {
		return props.getProperty(key, defaultValue);
	}

	/**
	 * 得到key配置的数组
	 * 
	 * @param key
	 * @param defaultArray
	 *            如果key对应的配置为空，则返回defaultArray
	 * @return
	 */
	public static final String[] getArrayProperty(String key, String[] defaultArray) {
		String prop = getProperty(key);
		if (StringHelper.isEmpty(prop)) return defaultArray;
		return prop.split(ARRAY_SEPARATOR_REGEX);
	}

	/**
	 * 
	 * 得到key配置的数组
	 * 
	 * @param key
	 * @return
	 */
	public static final String[] getArrayProperty(String key) {
		return getArrayProperty(key, (String[]) null);
	}

	/**
	 * 得到key配置的数组
	 * 
	 * @param key
	 * @param defaultValue
	 *            如果key对应的配置为空，则返回defaultValue.split
	 * @return
	 */
	public static final String[] getArrayProperty(String key, String defaultValue) {
		String prop = getProperty(key, defaultValue);
		if (StringHelper.isEmpty(prop)) return null;
		return prop.split(ARRAY_SEPARATOR_REGEX);
	}

	/**
	 * 执行系统命令行脚本
	 * 
	 * @param command
	 *            命令行
	 * @return [0: 退出代码,0为正常退出; 1:执行中的输出信息]
	 */
	public static final Object[] execute(List<String> command) {
		//if (Helper.isEmpty(command)) return null;
		Process process = null;
		StringBuffer message = new StringBuffer();
		List<Serializable> results = new ArrayList<Serializable>();
		ProcessBuilder builder = null;
		BufferedReader in = null;
		try {
			builder = new ProcessBuilder(command);
			builder.redirectErrorStream(true);
			process = builder.start();
			in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				// 读取输出信息
				message.append(line).append(LINE_SEPARATOR);
			}
			process.waitFor();
		} catch (Exception e) {
			// throw new MortalException(e);
		} finally {
			//Helper.close(in);
			if (process != null) process.destroy();
		}
		process.destroy();
		results.add(process.exitValue());
		results.add(message);
		return results.toArray();
	}

	public static final Object[] execute(String[] command) {
		//if (Helper.isEmpty(command)) return null;
		List<String> c = new ArrayList<String>();
		//Helper.add(c, command);
		return execute(c);
	}

}
