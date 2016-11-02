package com.lexing.common.utils;

import android.content.Context;
import android.support.annotation.StyleRes;
import android.text.SpannableStringBuilder;
import android.text.style.TextAppearanceSpan;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: yanhao(amosbake@gmail.com)
 * Date : 2015-12-01
 * Time: 14:15
 * 字符串操作工具包
 *
 * @version 1.2
 */
public final class StringUtils {
    private final static String Regex_Email = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
    private final static String Regex_Phone = "^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";
    private final static String Regex_Zipcode = "^[1-9]\\d{5}(?!\\d)$";
    private final static String Regex_Integer = "^-?[1-9]\\d*$";
    private final static String Regex_Float = "^[-+]?[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";
    private final static String Regex_QQ = "^[1-9][0-9]{4,}$";
    private final static String Regex_IP = "^((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))$";
    private final static String Regex_Url = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    private final static String Regex_Vehicle_Lisence =  "^[京津晋冀蒙辽吉黑沪苏浙皖闽赣鲁豫鄂湘粤桂琼川贵云藏陕甘青宁新渝]?[A-Z][A-HJ-NP-Z0-9学挂港澳练]{5}$";
    private static final String[] num_lower = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(CharSequence input) {
        if (input == null || "".equals(input) || "null".equalsIgnoreCase(input.toString()))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     */
    public static boolean isEmail(CharSequence email) {
        return !isEmpty(email) && Pattern.matches(Regex_Email, email);
    }

    /**
     * 判断是不是一个合法的手机号码
     */
    public static boolean isPhone(CharSequence phoneNum) {
        return !isEmpty(phoneNum) && Pattern.matches(Regex_Phone, phoneNum);
    }

    /**
     * 判断是不是一个合法的邮编
     */
    public static boolean isZipCode(CharSequence zipcode) {
        return !isEmpty(zipcode) && Pattern.matches(Regex_Zipcode, zipcode);
    }


    /**
     * 判断一个字符串是不是数字
     */
    public static boolean isNumber(CharSequence numStr) {
        return !isEmpty(numStr) && (Pattern.matches(Regex_Integer, numStr) || Pattern.matches(Regex_Float, numStr));
    }

    /**
     * 判断一个字符串是不是整数
     */
    public static boolean isInteger(CharSequence numStr) {
        return !isEmpty(numStr) && Pattern.matches(Regex_Integer, numStr);
    }

    /**
     * 判断一个字符串是不是浮点数
     */
    public static boolean isFloat(CharSequence numStr) {
        return !isEmpty(numStr) && Pattern.matches(Regex_Float, numStr);
    }

    /**
     * 判断一个字符串是不是QQ号
     */
    public static boolean isQQ(CharSequence qq) {
        return !isEmpty(qq) && Pattern.matches(Regex_QQ, qq);
    }
    /**
     * 判断一个字符串是不是车牌号
     */
    public static boolean isVehicleNumber(String value) {
        return !isEmpty(value) && Pattern.matches(Regex_Vehicle_Lisence, value);
    }

    /**
     * byte[]数组转换为16进制的字符串。
     *
     * @param data 要转换的字节数组。
     * @return 转换后的结果。
     */
    public static String byteArrayToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            int v = b & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.getDefault());
    }

    /**
     * 16进制表示的字符串转换为字节数组。
     *
     * @param s 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] d = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
            d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return d;
    }



    /**
     * 获取随机字符串
     *
     * @param length
     */
    public static String getRandomString(int length) {
        String base = "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 简单URL格式检查
     *
     * @param pInput 要检查的字符串
     * @return boolean   返回检查结果
     */
    public static boolean isUrl(String pInput) {
        if (pInput == null) {
            return false;
        }
        Pattern p = Pattern.compile(Regex_Url);
        Matcher matcher = p.matcher(pInput);
        return matcher.matches();
    }

    /**
     * 半角转全角
     *
     * @param input 需要转换的字符串
     * @return 转换后的字符串
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 分转换为带两位小数的元
     * @param cent 分
     * @return 元
     */
    public static String toYuan(int cent){
        return String.format(Locale.CHINA,"%d.%d%d",cent/100, cent%100/10, cent%10);
    }

    /**
     * 分转换为带两位小数的元
     * @param cent 分
     * @return 元
     */
    public static String toYuanTrim(int cent){
        String yuan = String.format(Locale.CHINA,"%d.%d",cent/100,cent%100);
        if (yuan.endsWith("0")) yuan = yuan.substring(0, yuan.length() - 1);
        if (yuan.endsWith(".")) yuan = yuan.substring(0, yuan.length() - 1);
        return yuan;
    }

    /**
     * 转换文件大小为以B/KB/MB/GB为单位
     *
     * @param fileSize fileSize in B
     */
    public static String formatFileSize(long fileSize) {
        if(fileSize == 0){
            return "0KB";
        }
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = df.format((double) fileSize / 1024) + "KB";
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format((double) fileSize / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileSize / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 给字符串打码，对于邮箱类型字符串，将@前字符的后一半打吗，对于非邮箱类型字符串，将中间一半字符打码
     *
     * @param src 待打码的字符串
     * @return 打好码的字符串
     */
    public static String mask(String src) {
        if (isEmpty(src)) {
            return src;
        }
        int l;
        StringBuilder sb = new StringBuilder();
        if ((l = src.indexOf("@")) != -1) {  //邮箱
            sb.append(src.substring(0, l / 2));
            for (int index = 0; index < l - l / 2; index++) {
                sb.append("*");
            }
            sb.append(src.substring(l, src.length()));
            return sb.toString();
        } else {
            if (src.length() == 1) {
                return "*";
            }
            int length = src.length();
            int start = (int) Math.ceil((double) length / 4);
            int end = (int) Math.ceil((double) length * 3 / 4);
            sb.append(src.substring(0, start));
            for (int index = 0; index < end - start; index++) {
                sb.append("*");
            }
            sb.append(src.substring(end, length));
            return sb.toString();
        }
    }

    /**
     * 判断字符串是否ip
     */
    public static boolean isIp(String ip) {
        return !isEmpty(ip) && Pattern.matches(Regex_IP, ip);
    }

    public static String num2ChineseChar(int num) {
        char[] numArray = {'零', '一', '二', '三', '四', '五', '六', '七', '八', '九', '十'};
        String[] units = {"", "十", "百", "千", "万", "十万", "百万", "千万", "亿",
                "十亿", "百亿", "千亿", "万亿"};
        char[] val = String.valueOf(num).toCharArray();
        int len = val.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            String m = val[i] + "";
            int n = Integer.valueOf(m);
            boolean isZero = n == 0;
            String unit = units[(len - 1) - i];
            if (isZero) {
                if (val.length > 1 && '0' == val[i - 1]) {
                    // not need process if the last digital bits is 0
                } else {
                    // no unit for 0
                    sb.append(numArray[n]);
                }
            } else {
                sb.append(numArray[n]);
                sb.append(unit);
            }
        }
        if (sb.length() > 2 && sb.charAt(0) == '一' && sb.charAt(1) == '十') {
            sb.deleteCharAt(0);
        }
        if (sb.length() > 1 && sb.charAt(sb.length() - 1) == '零') {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 比较版本大小 APP-123.apk,apk-124.apk
     *
     * @return 0相等，大于0大，小于0小
     */
    public static int compareVersion(String source, String target) {
        if (source == null && target == null)
            return 0;
        else if (source == null)
            return -1;
        else if (target == null)
            return 1;
        String[] arrOld = source.split("[^a-zA-Z0-9]+"),
                arrNew = target.split("[^a-zA-Z0-9]+");
        int i1, i2, i3;
        for (int i = 0; i <= Math.min(arrOld.length, arrNew.length); i++) {
            if (i == arrOld.length)
                return i == arrNew.length ? 0 : -1;
            else if (i == arrNew.length)
                return 1;
            try {
                i1 = Integer.parseInt(arrOld[i]);
            } catch (Exception x) {
                i1 = Integer.MAX_VALUE;
            }
            try {
                i2 = Integer.parseInt(arrNew[i]);
            } catch (Exception x) {
                i2 = Integer.MAX_VALUE;
            }
            if (i1 != i2) {
                return i1 - i2;
            }
            i3 = arrOld[i].compareTo(arrNew[i]);
            if (i3 != 0)
                return i3;
        }
        return 0;
    }

    /**为指定文本加载样式**/
    public static SpannableStringBuilder format(Context context, String text, @StyleRes int style){
        SpannableStringBuilder _builder = new SpannableStringBuilder(text);
        _builder.setSpan(new TextAppearanceSpan(context,style),0,text.length(),0);
        return _builder;
    }

    /**获取中文的大写*/
    public static String getUpCase(int position) {
        return num_lower[position];
    }

    public static String join(String[] array, String sep) {
        if (array == null) {
            return null;
        }

        int arraySize = array.length;
        int sepSize = 0;
        if (sep != null && !sep.equals("")) {
            sepSize = sep.length();
        }

        int bufSize = (arraySize == 0 ? 0 : ((array[0] == null ? 16 : array[0].length()) + sepSize) * arraySize);
        StringBuilder buf = new StringBuilder(bufSize);

        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                buf.append(sep);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }
}
