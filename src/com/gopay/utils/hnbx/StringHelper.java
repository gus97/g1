package com.gopay.utils.hnbx;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * 
 * @author LuoGang
 */
public class StringHelper {

	/**
	 * 判断字符串是否为null或空,如果为全空白字符串，此方法也返回true
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	/**
	 * 判断字符串不为空,null,全空白字符
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isNotEmpty(String value) {
		return !StringHelper.isEmpty(value);
	}

	/**
	 * 判断字符串是否为null或空,如果为全空白字符串，此方法也返回true
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(CharSequence value) {
		if (value == null) return true;
		// if (value instanceof String) return ((String) value).trim().length() == 0;
		return value.toString().trim().length() == 0;
	}

	/**
	 * 判断字符串不为空,null,全空白字符
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isNotEmpty(CharSequence value) {
		return !StringHelper.isEmpty(value);
	}

	/**
	 * 如果value不为空,返回value,否则返回defaultValue
	 *
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static String value(String value, String defaultValue) {
		return isEmpty(value) ? defaultValue : value;
	}

	/**
	 * 去掉value前后的空格
	 * 
	 * @param value
	 * @return
	 */
	public static String trim(String value) {
		return value == null ? "" : value.trim();
	}

	/**
	 * 将value转换为字符串，并且去掉前后的空格
	 * 
	 * @param value
	 * @return
	 */
	public static String trim(Object value) {
		return value == null ? "" : value.toString().trim();
	}


	public static String trim(Object value, String defaultValue) {
		return value == null || StringHelper.isEmpty(value.toString()) ? defaultValue : value.toString().trim();
	}

	/**
	 * 删除字符串target头部的prefix字符串
	 * 
	 * @param target
	 * @param prefix
	 * @return
	 */
	public static String trimPrefix(String target, String prefix) {
		int prefixLength = prefix.length();
		while (target.startsWith(prefix)) {
			target = target.substring(prefixLength);
		}

		return target;
	}

	/**
	 * 删除字符串target尾部的suffix字符串
	 * 
	 * @param target
	 * @param suffix
	 * @return
	 */
	public static String trimSuffix(String target, String suffix) {
		int suffixLength = suffix.length();
		while (target.endsWith(suffix)) {
			target = target.substring(0, target.length() - suffixLength);
		}
		return target;
	}

	/**
	 * 计算字符串的字节长度.
	 * 
	 * @return
	 * @see String#getBytes()
	 */
	public static int byteLength(String value) {
		try {
			return value == null ? 0 : value.getBytes(SystemHelper.ENCODING).length;
		} catch (UnsupportedEncodingException e) {
			return value.getBytes().length;
		}
	}

	/**
	 * 计算字符串的字符长度,根据字符串的charCode进行计算
	 * 
	 * @param value
	 * @return
	 * @see String#charAt(int)
	 */
	public static int length(String value) {
		int length = 0;
		char c;
		for (int i = 0, l = value.length(); i < l; i++) {
			c = value.charAt(i);
			length++;
			if (c > Character.MAX_VALUE >> 8) length++;
			if (c > Character.MAX_VALUE) length++;
		}
		return length;
	}

	/**
	 * 截断字符串，保留length字节
	 * 
	 * @param value
	 * @param length
	 * @return
	 */
	public static String substring(String value, int length) {
		if (length(value) <= length) return value;
		StringBuilder buf = new StringBuilder();
		int i = 0;
		char c;
		int len = 0;
		while (len < length) {
			c = value.charAt(i++);
			len += length(Character.toString(c));
			if (len > length) break;
			buf.append(c);
		}
		return buf.toString();
	}

	/**
	 * 复制value对象times次
	 * 
	 * @param value
	 *            被复制的对象，如果为null或空字符串则仅返回空字符串
	 * @param times
	 * @return
	 */
	public static String copy(Object value, int times) {
		if (times <= 0) return ""; // 复制0次则返回空
		if (value == null) return ""; // value为null返回空
		/* value为空字符串返回空 */
		if (value instanceof String && ((String) value).length() == 0) return "";

		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < times; i++) {
			buffer.append(value);
		}
		return buffer.toString();
	}

	/**
	 * 在数组values中查找key出现的位置.返回-1则表示没有找到
	 * 
	 * @param values
	 *            数组
	 * @param key
	 *            查找的关键值
	 * @param ignoreCase
	 *            是否忽略大小写
	 * @param ignoreEmpty
	 *            是否忽略字符串的前后空白
	 * @param reverse
	 *            是否倒序查找
	 * @return
	 */
	public static int search(String[] values, String key, boolean ignoreCase, boolean ignoreEmpty, boolean reverse) {
		if (values == null) return -1;
		String valueKey = ignoreEmpty && key != null ? key.trim() : key;
		String value;
		int length = reverse ? 0 : values.length - 1; // 倒序查找是从1-length开始直到0
		int index = reverse ? 1 - values.length : 0;
		int i;
		for (; index <= length; index++) {
			i = Math.abs(index);
			if (values[i] == key) return i; // 相等则找到数据
			if (values[i] == null || key == null) continue; // 有一个为null则必然不相等

			value = values[i];
			if (ignoreEmpty) value = values[i].trim(); // 忽略空白

			/* 忽略大小写则调用equalsIgnoreCase方法 */
			if (ignoreCase ? valueKey.equalsIgnoreCase(value) : valueKey.equals(value)) return i;
		}
		return -1;
	}

	/**
	 * 
	 * 在数组values中查找key出现的位置. 返回-1则表示没有找到.从前往后按顺序查找
	 * 
	 * @param values
	 * @param key
	 * @param ignoreCase
	 * @param ignoreEmpty
	 * @see #search(String[], String, boolean, boolean, boolean)
	 * @return
	 */
	public static int search(String[] values, String key, boolean ignoreCase, boolean ignoreEmpty) {
		return search(values, key, ignoreCase, ignoreEmpty, false);
	}

	/**
	 * 
	 * 在数组values中查找key出现的位置,忽略字符串的前后空白. 返回-1则表示没有找到.从前往后按顺序查找
	 * 
	 * @param values
	 * @param key
	 * @param ignoreCase
	 * @see #search(String[], String, boolean, boolean, boolean)
	 * @return
	 */
	public static int search(String[] values, String key, boolean ignoreCase) {
		return search(values, key, ignoreCase, true, false);
	}

	/**
	 * 
	 * 在数组values中查找key出现的位置,忽略字符串的前后空白,区分大小写. 返回-1则表示没有找到.从前往后按顺序查找
	 * 
	 * @param values
	 * @param key
	 * @see #search(String[], String, boolean, boolean, boolean)
	 * @return
	 */
	public static int search(String[] values, String key) {
		return search(values, key, false, true, false);
	}

	/**
	 * 
	 * 在数组values中查找key出现的位置. 返回-1则表示没有找到.从后往前倒序查找
	 * 
	 * @param values
	 * @param key
	 * @param ignoreCase
	 * @param ignoreEmpty
	 * @see #search(String[], String, boolean, boolean, boolean)
	 * @return
	 */
	public static int lastSearch(String[] values, String key, boolean ignoreCase, boolean ignoreEmpty) {
		return search(values, key, ignoreCase, ignoreEmpty, true);
	}

	/**
	 * 
	 * 在数组values中查找key出现的位置,忽略字符串的前后空白. 返回-1则表示没有找到.从后往前倒序查找
	 * 
	 * @param values
	 * @param key
	 * @param ignoreCase
	 * @see #search(String[], String, boolean, boolean, boolean)
	 * @return
	 */
	public static int lastSearch(String[] values, String key, boolean ignoreCase) {
		return search(values, key, ignoreCase, true, true);
	}

	/**
	 * 
	 * 在数组values中查找key出现的位置,忽略字符串的前后空白,区分大小写. 返回-1则表示没有找到.从后往前倒序查找
	 * 
	 * @param values
	 * @param key
	 * @see #search(String[], String, boolean, boolean, boolean)
	 * @return
	 */
	public static int lastSearch(String[] values, String key) {
		return search(values, key, false, true, true);
	}

	/** true */
	public static String[] TRUE = { "true", "t", "yes", "y", "1", "是" };

	/** false */
	public static String[] FALSE = { "false", "f", "no", "n", "0", "否" };

	/**
	 * 转换字符串转换为boolean类型
	 * 
	 * @param value
	 * @see #TRUE
	 * @return
	 */
	public static boolean parseBoolean(String value) {
		if (value == null || isEmpty(value)) return false;
		return search(TRUE, value, true, true) > -1;
	}

	/**
	 * 得到boolean类型
	 * 
	 * @param value
	 * @return
	 */
	public static boolean parseBoolean(Object value) {
		if (value == null) return false;
		if (value instanceof Boolean) return (Boolean) value;
		return parseBoolean(value.toString());
	}

	/**
	 * 当字符串在TRUE数组中是则返回true
	 * 
	 * @param value
	 * @see #TRUE
	 * @return
	 */
	public static boolean isTrue(String value) {
		if (value == null || isEmpty(value)) return false;
		return search(TRUE, value.toString(), true, true) > -1;
	}

	/**
	 * 当字符串在FALSE数组中是则返回true
	 * 
	 * @param value
	 * @see #FALSE
	 * @return
	 */
	public static boolean isFalse(String value) {
		if (value == null || isEmpty(value)) return false;
		return search(FALSE, value.toString(), true, true) > -1;
	}

	/**
	 * 输入的字符串input是否与所给的正则表达式regex相匹配
	 * 
	 * @param input
	 *            被匹配的字符串
	 * @param regex
	 *            正则表达式
	 * @param flags
	 *            匹配模式. Match flags, a bit mask that may include {@link Pattern#CASE_INSENSITIVE},
	 *            {@link Pattern#MULTILINE}, {@link Pattern#DOTALL} , {@link Pattern#UNICODE_CASE},
	 *            {@link Pattern#CANON_EQ}, {@link Pattern#UNIX_LINES}, {@link Pattern#LITERAL} and
	 *            {@link Pattern#COMMENTS}
	 * @return
	 */
	public static boolean match(CharSequence input, String regex, int flags) {
		Pattern p = Pattern.compile(regex, flags);
		Matcher m = p.matcher(input);
		return m.matches();
	}

	/**
	 * 输入的字符串input是否与所给的正则表达式regex相匹配
	 * 
	 * @param input
	 *            要匹配的字符串
	 * @param regex
	 *            正则表达式
	 * @return
	 */
	public static boolean match(CharSequence input, String regex) {
		if (input instanceof String) return ((String) input).matches(regex);
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		return m.matches();
	}

	/**
	 * 输入的字符串input是否与所给的正则表达式数组regexs中的某个相匹配
	 * 
	 * @param input
	 * @param regexs
	 * @param flags
	 * @return
	 * @see #match(CharSequence, String, int)
	 */
	public static int match(CharSequence input, String[] regexs, int flags) {
		if (regexs == null || regexs.length <= 0) return -1;
		int index = -1;
		for (String regex : regexs) {
			index++;
			if ((input == null || regex == null) && input != regex) continue; // 某一个为null则必然不等
			if (regex == input || regex.equals(input)) return index; // 二者相等
			if (match(input, regex, flags)) return index;
		}
		return -1;
	}


	public static int match(CharSequence input, String[] regexs) {
		if (regexs == null || regexs.length <= 0) return -1;
		int index = -1;
		for (String regex : regexs) {
			index++;
			if ((input == null || regex == null) && input != regex) continue; // 某一个为null则必然不等
			if (regex == input || regex.equals(input)) return index; // 二者相等
			if (match(input, regex)) return index;
		}
		return -1;
	}

	/**
	 * 得到异常堆栈信息
	 * 
	 * @param throwable
	 * @return
	 */
//	public static String toString(Throwable throwable) {
//		if (throwable instanceof I18NException) {
//			return ((I18NException) throwable).toString();
//		}
//		StringWriter out = new StringWriter();
//		((Throwable) throwable).printStackTrace(new PrintWriter(out));
//		return out.toString();
//	}

	/**
	 * 将value对象转换为字符串. 对数组/Map对象特殊处理,其余调用value.toString().
	 * 如果是数组/Iterable/Iterator类型,转换为[X1, X2, ...Xn]的形式 如果是Map,转换为{key1 : value1,
	 * key2 : value2, ... keyn : valuen}
	 * 
	 * @param value
	 * @return
	 */
	public static String toString(Object value) {
		if (value == null) return null;
		if (value instanceof String) return (String) value;
		if (value instanceof CharSequence) return ((CharSequence) value).toString();
		if (value instanceof Throwable) {
			return toString((Throwable) value);
		}
		if (value instanceof Iterable) value = ((Iterable<?>) value).iterator();
		if (value instanceof Iterator) { // 迭代
			StringBuilder b = new StringBuilder();
			b.append('[');
			Iterator<?> it = (Iterator<?>) value;
			while (it.hasNext()) {
				b.append(toString(it.next()));
				b.append(", ");
			}
			if (b.length() > 0) b.delete(b.length() - 2, b.length()); // 删除最后的逗号和空格
			return b.append(']').toString();
		}
		if (value.getClass().isArray()) { // 数组
			int length = Array.getLength(value);
			int iMax = length - 1;
			if (iMax == -1) return "[]";
			StringBuilder b = new StringBuilder();
			b.append('[');
			for (int i = 0;; i++) {
				b.append(toString(Array.get(value, i)));
				if (i == iMax) return b.append(']').toString();
				b.append(", ");
			}
		}

		if (value instanceof Map) { // Map对象
			StringBuilder b = new StringBuilder();
			b.append('{');
			Set<?> keys = ((Map<?, ?>) value).keySet();
			for (Object key : keys) {
				b.append(key).append(" : ");
				b.append(toString(((Map<?, ?>) value).get(key)));
				b.append(",\n");
			}

			if (b.length() > 0) b.delete(b.length() - 2, b.length()); // 删除最后的逗号和换行
			return b.append('}').toString();
		}

		return value.toString();
	}

	/**
	 * 生成32位的UUID,仅由大写字母和数字组成没有短横线-<br>
	 * UUID.randomUUID().toString().replace("-", "").toLowerCase()
	 * 
	 * @return
	 * @see UUID#randomUUID()
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}

	/**
	 * 返回36位的UUID,全部大写，包含短横线-
	 * 
	 * @return
	 * @see UUID#randomUUID()
	 */
	public static String randomUUID() {
		return UUID.randomUUID().toString().toUpperCase();
	}

	/**
	 * 将str转为固定长度的字符串,字符串的截取和添加都是在末尾进行
	 * 
	 * @param str
	 * @param length 固定的长度
	 * @param ch 如果小于固定长度,则在字符串末尾添加ch字符直到满足长度为止
	 * @return
	 */
	public static String suffix(String str, int length, String ch) {
		if (str.length() < length) str = str + copy(ch, (int) Math.ceil((length - str.length()) / ch.length()));
		if (str.length() > length) str = str.substring(0, length);
		return str;
	}

	/**
	 * 
	 * 将str转为固定长度的字符串,字符串的截取和添加都是在字符串头部进行
	 * 
	 * @param str
	 * @param length
	 * @param ch 如果小于固定长度,则在字符串头部添加ch字符直到满足长度为止
	 * @return
	 */
	public static String prefix(String str, int length, String ch) {
		if (str.length() < length) str = copy(ch, (int) Math.ceil((length - str.length()) / ch.length())) + str;
		if (str.length() > length) str = str.substring(str.length() - length, str.length());
		return str;
	}

	/**
	 * 将字符串input按照指定的编码转换为byte数组
	 * 
	 * @param input 字符串
	 * @param charsetName 使用的编码
	 * @return
	 */
	public static byte[] toBytes(String input, String charsetName) {
		byte[] data;
		try {
			data = StringHelper.isEmpty(charsetName) ? input.getBytes() : input.getBytes(charsetName);
		} catch (UnsupportedEncodingException e) {
			// data = input.getBytes();
			throw new RuntimeException(e);
		}
		return data;
	}

	/**
	 * 将字符串input按照系统默认的编码转换为byte数组
	 * 
	 * @param input 字符串
	 * @return
	 * 
	 * @see SystemHelper#ENCODING
	 */
	public static byte[] toBytes(String input) {
		return toBytes(input, SystemHelper.ENCODING);
	}

	/**
	 * 将字节数组input按照charsetName编码转为字符串
	 * 
	 * @param input
	 * @param charsetName 字符编码
	 * @return
	 */
	public static String valueOf(byte[] input, String charsetName) {
		String data;
		try {
			data = StringHelper.isEmpty(charsetName) ? new String(input) : new String(input, charsetName);
		} catch (UnsupportedEncodingException e) {
			// data = input.getBytes();
			throw new RuntimeException(e);
		}
		return data;
	}

	/**
	 * 按默认编码转换byte数组为字符串
	 * 
	 * @param input
	 * @return
	 */
	public static String valueOf(byte[] input) {
		return valueOf(input, SystemHelper.ENCODING);
	}
}
