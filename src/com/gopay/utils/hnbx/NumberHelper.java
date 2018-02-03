package com.gopay.utils.hnbx;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * 数字处理工具类
 * 
 * @author LuoGang
 * 
 */
public class NumberHelper {

	/**
	 * 数字正则表达式
	 */
	public static final String NUMERIC_REGEX = "[\\+\\-]?(\\d+|(\\d{1,3}(,\\d{3})*))(\\.[0-9]+)?";

	/**
	 * 判断cls是否为整数类型
	 * 
	 * @param cls
	 * @return
	 */
	public static boolean isInteger(Class<?> cls) {
		if (cls == Long.TYPE || cls == Integer.TYPE || cls == Byte.TYPE || cls == Short.TYPE) return true;
		return Long.class.isAssignableFrom(cls) || Integer.class.isAssignableFrom(cls) || Byte.class.isAssignableFrom(cls)
				|| Short.class.isAssignableFrom(cls);
	}

	/**
	 * 判断cls是否为数字类型的class
	 * 
	 * @param cls
	 * @return
	 */
	public static boolean isNumber(Class<?> cls) {
		if (cls == Long.TYPE || cls == Integer.TYPE || cls == Float.TYPE || cls == Double.TYPE || cls == Byte.TYPE || cls == Short.TYPE)
			return true;
		return Number.class.isAssignableFrom(cls);
	}

	/**
	 * 判断number是否可转换为Number类型数据
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isNumber(Object number) {
		if (number == null) return false;
		if (number instanceof Number) return true;
		if (isNumber(number.getClass())) return true;
		return number.toString().trim().matches(NUMERIC_REGEX);
	}

	/**
	 * 判断字符串number是否符合小数格式
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isNumber(String number) {
		if (StringHelper.isEmpty(number)) return false;
		return number.trim().matches(NUMERIC_REGEX);
	}

	/**
	 * 将number转换为数字
	 * 
	 * @param number
	 * @return
	 * @throws ParseException
	 */
	public static Number parse(Object number) throws ParseException {
		if (number == null) return 0;
		if (isNumber(number.getClass())) return (Number) number;
		NumberFormat numberFormat = NumberFormat.getInstance();
		return numberFormat.parse(StringHelper.trim(number));
	}

	/**
	 * 将number转换为BigDecimal
	 * 
	 * @param number
	 * @return
	 */
	public static BigDecimal toBigDecimal(Object number) {
		if (number instanceof BigDecimal) return (BigDecimal) number;
		if (number instanceof String) return new BigDecimal(StringHelper.trim(number));
		if (isNumber(number.getClass())) return BigDecimal.valueOf(((Number) number).doubleValue());
		return BigDecimal.valueOf(NumberHelper.doubleValue(number));
	}

	/**
	 * 将其他类型返回double,转换失败则返回defaultValue
	 * 
	 * @param number
	 * @param defaultValue 如果number不是数字则返回该数字
	 * @return
	 */
	public static double doubleValue(Object number, double defaultValue) {
		if (number == null) return defaultValue;
		try {
			return parse(number).doubleValue();
		} catch (ParseException ex) {
			// number无法转换为数字
			return defaultValue;
		}
	}

	/**
	 * 将其他类型返回double,转换失败则返回0
	 * 
	 * @param number
	 * @return
	 */
	public static double doubleValue(Object number) {
		return doubleValue(number, 0);
	}

	/**
	 * 将其他类型返回float
	 * 
	 * @param number
	 * @return
	 */
	public static float floatValue(Object number) {
		return (float) doubleValue(number);
	}

	/**
	 * 将其他类型返回float
	 * 
	 * @param number
	 * @param defaultValue number不为数字时的返回值
	 * @return
	 */
	public static float floatValue(Object number, float defaultValue) {
		return (float) doubleValue(number, defaultValue);
	}

	/**
	 * 将其他类型返回long
	 * 
	 * @param number
	 * @return
	 */
	public static long longValue(Object number) {
		return longValue(number, 0);
	}

	/**
	 * 将其他类型返回long,如果不是数字则返回defaultValue
	 * 
	 * @param number
	 * @param defaultValue number不为数字时的返回值
	 * @return
	 */
	public static long longValue(Object number, long defaultValue) {
		if (number == null) return defaultValue;
		try {
			return parse(number).longValue();
		} catch (ParseException ex) {
			// number无法转换为数字
			return defaultValue;
		}
	}

	/**
	 * 将其他类型返回int
	 * 
	 * @param number
	 * @return
	 */
	public static int intValue(Object number) {
		return intValue(number, 0);
	}

	/**
	 * 将其他类型返回int
	 * 
	 * @param number
	 * @param defaultValue number不为数字时的返回值
	 * @return
	 */
	public static int intValue(Object number, int defaultValue) {
		if (number == null) return defaultValue;
		try {
			return parse(number).intValue();
		} catch (ParseException ex) {
			// number无法转换为数字
			return defaultValue;
		}
	}

	/**
	 * 格式化数字number为指定的格式
	 * 
	 * @param number
	 * @param pattern 数字格式
	 * @return
	 */
	public static String format(Object number, String pattern) {
		DecimalFormat numberFormat = new DecimalFormat(pattern);
		try {
			return numberFormat.format(parse(number));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 格式化数字
	 * 
	 * @param number
	 * @param minFractionDigits 最少保留的小数位数
	 * @param maxFractionDigits 最大保留的小数位数
	 * @param groupingUsed 是否使用千分符
	 * @return 格式化后的字符串
	 */
	public static String format(Object number, int minFractionDigits, int maxFractionDigits, boolean groupingUsed) {
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMinimumFractionDigits(minFractionDigits);
		numberFormat.setMaximumFractionDigits(maxFractionDigits);
		numberFormat.setRoundingMode(RoundingMode.HALF_UP);
		numberFormat.setGroupingUsed(groupingUsed);
		try {
			return numberFormat.format(parse(number));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 格式化小数
	 * 
	 * @param number
	 * @param fractionDigits 小数位数
	 * @param groupingUsed 是否使用千分符
	 * @return
	 */
	public static String format(Object number, int fractionDigits, boolean groupingUsed) {
		return format(number, fractionDigits, fractionDigits, groupingUsed);
	}

	/**
	 * 格式化小数
	 * 
	 * @param number
	 * @param fractionDigits 小数位数
	 * @param groupingUsed 是否使用千分符
	 * @return
	 */
	public static String format(double number, int fractionDigits, boolean groupingUsed) {
		return format(number, fractionDigits, fractionDigits, groupingUsed);
	}

	/**
	 * 格式化小数,使用默认千分符
	 * 
	 * @param number
	 * @param fractionDigits 小数位数
	 * @return
	 */
	public static String format(Object number, int fractionDigits) {
		return format(number, fractionDigits, true);
	}

	/** 默认保留的小数位数,2位小数 */
	public static final int DEFAULT_FRACTION_DIGITS = 2;

	/**
	 * 格式化小数,保留2位小数
	 * 
	 * @param number
	 * @param separator 是否保留分隔符
	 * @see #DEFAULT_FRACTION_DIGITS
	 * @return
	 */
	public static String format(Object number, boolean separator) {
		return format(number, DEFAULT_FRACTION_DIGITS, separator);
	}

	/**
	 * 格式化小数,保留2位小数
	 * 
	 * @param number
	 * @see #DEFAULT_FRACTION_DIGITS
	 * @return
	 */
	public static String format(Object number) {
		return format(number, DEFAULT_FRACTION_DIGITS, true);
	}

	/**
	 * 格式化小数,保留2位小数
	 * 
	 * @param number
	 * @see #DEFAULT_FRACTION_DIGITS
	 * @return
	 */
	public static String format(double number) {
		return format(number, DEFAULT_FRACTION_DIGITS, true);
	}

	/**
	 * 
	 * 将int格式化
	 * 
	 * @param number
	 * @param separator 是否使用千分符
	 * @return
	 */
	public static String formatInteger(int number, boolean separator) {
		return format(number, 0, separator);
	}

	/**
	 * 
	 * 将number格式化为整数形式
	 * 
	 * @param number
	 * @param separator 是否使用千分符
	 * @return
	 */
	public static String formatInteger(Object number, boolean separator) {
		return format(number, 0, separator);
	}

	/**
	 * 格式化为整数形式
	 * 
	 * @param number
	 * @return
	 */
	public static String formatInteger(Object number) {
		return formatInteger(number, true);
	}

	/**
	 * 格式化为整数形式
	 * 
	 * @param number
	 * @return
	 */
	public static String formatInteger(int number) {
		return formatInteger(number, true);
	}

	/**
	 * 将byte数字b转换为16进制数字,注意byte数字仅保留2位数字
	 * 
	 * @param b
	 * @return
	 */
	public static String toHexString(byte b) {
		String text = Integer.toHexString(b & 0xFF);
		return StringHelper.prefix(text, 2, "0");
	}

	/**
	 * 将bytes数组转换为16进制字符串，每个byte数字仅保留2位数字
	 * 
	 * @param bytes
	 * @return
	 */
	public static String toHexString(byte[] bytes) {
		StringBuffer buffer = new StringBuffer();
		for (byte b : bytes) {
			buffer.append(toHexString(b));
		}
		return buffer.toString();
	}

	/**
	 * a+b,null视为0,此方法最少保留2位精度
	 * 
	 * @param a
	 * @param b
	 * @return a+b
	 */
	public static BigDecimal add(BigDecimal a, BigDecimal b) {
		if (a == null) return b;
		if (b == null) return a;
		int scale = Math.max(a.scale(), b.scale());
		BigDecimal r = a.add(b);
		if (scale < 2) {
			r.setScale(Math.max(scale, 2), RoundingMode.HALF_UP);
		}
		return r;
	}

	/**
	 * a+b,,此方法保留scale位精度
	 * 
	 * @param a
	 * @param b
	 * @param scale
	 * @return
	 */
	public static BigDecimal add(BigDecimal a, BigDecimal b, int scale) {
		if (a == null) return b;
		if (b == null) return a;
		BigDecimal r = a.add(b);
		return r.setScale(scale, RoundingMode.HALF_UP);
	}

	/**
	 * a-b,null视为0,此方法最少保留2位精度
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
		if (a == null) return b.negate();
		if (b == null) return a;
		int scale = Math.max(a.scale(), b.scale());
		BigDecimal r = a.subtract(b);
		if (scale < 2) {
			return r.setScale(Math.max(scale, 2), RoundingMode.HALF_UP);
		}
		return r;
	}

	/**
	 * a-b,此方法保留scale位精度
	 * 
	 * @param a
	 * @param b
	 * @param scale
	 * @return
	 */
	public static BigDecimal subtract(BigDecimal a, BigDecimal b, int scale) {
		if (a == null) return b.negate();
		if (b == null) return a;
		BigDecimal r = a.subtract(b);
		return r.setScale(scale, RoundingMode.HALF_UP);
	}

	/**
	 * a*b,null视为0,此方法最少保留2位精度
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
		if (a == null || b == null) return BigDecimal.ZERO;
		int scale = a.scale() + b.scale();
		BigDecimal r = a.multiply(b);
		if (scale < 2) {
			return r.setScale(Math.max(scale, 2), RoundingMode.HALF_UP);
		}
		return r;
	}

	/**
	 * a*b,null视为0,此方法保留scale位精度
	 * 
	 * @param a
	 * @param b
	 * @param scale
	 * @return
	 */
	public static BigDecimal multiply(BigDecimal a, BigDecimal b, int scale) {
		if (a == null || b == null) return BigDecimal.ZERO;
		BigDecimal r = a.multiply(b);
		return r.setScale(scale, RoundingMode.HALF_UP);
	}

	/**
	 * a/b,null视为0,此方法最少保留2位精度
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static BigDecimal divide(BigDecimal a, BigDecimal b) {
		if (a == null) a = BigDecimal.ZERO;
		if (b == null) b = BigDecimal.ZERO;
		int scale = Math.max(a.scale(), b.scale());
		return a.divide(b, Math.max(scale, 2), RoundingMode.HALF_UP);
	}

	/**
	 * a/b,null视为0,此方法保留scale位精度
	 * 
	 * @param a
	 * @param b
	 * @param scale
	 * @return
	 */
	public static BigDecimal divide(BigDecimal a, BigDecimal b, int scale) {
		if (a == null) a = BigDecimal.ZERO;
		if (b == null) b = BigDecimal.ZERO;
		return a.divide(b, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 比较a和b两个数字的大小，精度为scale
	 * 
	 * @param a
	 * @param b
	 * @param scale
	 * @return a.compareTo(b)
	 */
	public static int compare(BigDecimal a, BigDecimal b, int scale) {
		if (a == null) a = BigDecimal.ZERO;
		if (b == null) b = BigDecimal.ZERO;
		BigDecimal aCopy = a.setScale(scale, BigDecimal.ROUND_HALF_UP);
		BigDecimal bCopy = b.setScale(scale, BigDecimal.ROUND_HALF_UP);
		return aCopy.compareTo(bCopy);
	}

	/**
	 * 比较a和b两个数字的大小，小数位数为2
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static int compare(BigDecimal a, BigDecimal b) {
		return NumberHelper.compare(a, b, 2);
	}

	/**
	 * 比较a和b两个数字的大小，精度为scale
	 * 
	 * @param a
	 * @param b
	 * @param scale
	 * @return a.compareTo(b)
	 */
	public static int compare(Number a, Number b, int scale) {
		if (a == null) a = 0;
		if (b == null) b = 0;
		String aStr = NumberHelper.format(a, scale, false);
		String bStr = NumberHelper.format(b, scale, false);
		// if (aStr.length() == bStr.length()) {
		// return aStr.compareTo(bStr);
		// }
		// return aStr.length() > bStr.length() ? 1 : -1;
		return compare(new BigDecimal(aStr), new BigDecimal(bStr), scale);
	}

	/**
	 * 取a和b之间的小者
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Number> T min(T a, T b) {
		if (a instanceof Comparable) {
			Comparable<T> comparable = (Comparable<T>) a;
			return comparable.compareTo(b) == -1 ? a : b;
		}
		return a.doubleValue() < b.doubleValue() ? a : b;
	}

	/**
	 * 取a和b之间的大者
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Number> T max(T a, T b) {
		if (a instanceof Comparable) {
			Comparable<T> comparable = (Comparable<T>) a;
			return comparable.compareTo(b) == 1 ? a : b;
		}
		return a.doubleValue() > b.doubleValue() ? a : b;
	}

	public static void main(String[] args) {
		// System.out.println(isNumber(Long.TYPE));
		// System.out.println(isNumber(Long.class));
		// System.out.println(new BigDecimal("1.00").divide(new BigDecimal("3.00000"), RoundingMode.HALF_UP));

	}
}
