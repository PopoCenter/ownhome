package com.tencent.wxcloudrun.util;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;


public class CoreStringUtils {
    protected static final transient Logger logger = LoggerFactory.getLogger(CoreStringUtils.class);

    public static String generateRandomStr(int len) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < len; i++) {
            Random random = new Random();
            result.append(chars.charAt(random.nextInt(chars.length() - 1)));
        }
        return result.toString();
    }

    public static String generateRandomCode(int len) {
        String chars = "0123456789";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < len; i++) {
            Random random = new Random();
            result.append(chars.charAt(random.nextInt(chars.length() - 1)));
        }
        return result.toString();
    }

    /**
     * 检验中国的手机号格式
     *
     * @param phone 不含+86/8086等前缀的11位手机号
     * @return 检测通过则返回true
     */
    public static boolean checkChinaMobilePhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            logger.error("手机号不能为空, phone={}", phone);
            return false;
        }

        if (phone.length() != 11) {
            logger.error("手机号位数不正确, phone=\"{}\"", phone);
            return false;
        }

        if (!isNumeric(phone)) {
            logger.error("手机号必须全为数字, phone={}", phone);
            return false;
        }

        if (!StringUtils.startsWithAny(phone, "1")) {
            logger.error("手机号首位非法, phone={}", phone);
            return false;
        }

        return true;
    }

    /**
     * 检查手机号并返回错误信息
     *
     * @param phone
     * @return
     */
    public static String checkChinaMobilePhoneWithMessage(String phone) {
        if (StringUtils.isBlank(phone)) {
            return "手机号不能为空";
        }

        if (phone.length() != 11) {
            return "手机号位数不正确";
        }

        if (!isNumeric(phone)) {
            return "手机号必须全为数字";
        }

        if (!StringUtils.startsWithAny(phone, "1")) {
            return "手机号首两位非法";
        }

        return null;
    }

    /**
     * 验证中国身份证是否合法
     *
     * @param idCard
     * @return
     */
    public static boolean checkChinaIdCard(String idCard) {
        if (idCard.length() == 15) {
            idCard = convertIdCardBy15bit(idCard);
        }

        if (idCard == null) {
            return false;
        }

        return isValidate18IdCard(idCard);
    }

    /**
     * <p>
     * 判断18位身份证的合法性
     * </p>
     * 根据〖中华人民共和国国家标准GB11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。
     * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
     * <p>
     * 顺序码: 表示在同一地址码所标识的区域范围内，对同年、同月、同 日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配 给女性。
     * </p>
     * <p>
     * 1.前1、2位数字表示：所在省份的代码； 2.第3、4位数字表示：所在城市的代码； 3.第5、6位数字表示：所在区县的代码；
     * 4.第7~14位数字表示：出生年、月、日； 5.第15、16位数字表示：所在地的派出所的代码；
     * 6.第17位数字表示性别：奇数表示男性，偶数表示女性；
     * 7.第18位数字是校检码：也有的说是个人信息码，一般是随计算机的随机产生，用来检验身份证的正确性。校检码可以是0~9的数字，有时也用x表示。
     * </p>
     * <p>
     * 第十八位数字(校验码)的计算方法为： 1.将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4
     * 2 1 6 3 7 9 10 5 8 4 2
     * </p>
     * <p>
     * 2.将这17位数字和系数相乘的结果相加。
     * </p>
     * <p>
     * 3.用加出来和除以11，看余数是多少？
     * </p>
     * 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3
     * 2。
     * <p>
     * 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2。
     * </p>
     *
     * @param idCard
     * @return
     */
    private static boolean isValidate18IdCard(String idCard) {
        // 非18位为假
        if (idCard.length() != 18) {
            return false;
        }

        String idCard17 = idCard.substring(0, 17);
        String idCard18Code = idCard.substring(17, 18);
        char c[];

        // 是否都为数字
        if (CoreStringUtils.isNumeric(idCard17)) {
            c = idCard17.toCharArray();
        } else {
            return false;
        }

        int bit[] = convertCharToInt(c);
        int sum17 = getPowerSum(bit);

        // 将和值与11取模得到余数进行校验码判断
        String checkCode = getCheckCodeBySum(sum17);
        if (null == checkCode) {
            return false;
        }

        // 将身份证的第18位与算出来的校码进行匹配，不相等就为假
        return idCard18Code.equalsIgnoreCase(checkCode);
    }

    // 每位加权因子
    private static final int power[] = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 将15位的身份证转成18位身份证
     *
     * @param idCard
     * @return
     */
    private static String convertIdCardBy15bit(String idCard) {
        String idCard17 = null;
        // 非15位身份证
        if (idCard.length() != 15) {
            return null;
        }

        if (CoreStringUtils.isNumeric(idCard)) {
            // 获取出生年月日
            String birthDayStr = idCard.substring(6, 12);
            Date birthDay = CoreDateUtils.parseDate(birthDayStr, "yyMMdd");
            if (birthDay == null) {
                // 身份证日期不合法
                return null;
            }

            Calendar cDay = Calendar.getInstance();
            cDay.setTime(birthDay);
            String year = String.valueOf(cDay.get(Calendar.YEAR));

            idCard17 = idCard.substring(0, 6) + year + idCard.substring(8);

            char c[] = idCard17.toCharArray();
            String checkCode = "";

            // 将字符数组转为整型数组
            int bit[] = convertCharToInt(c);
            int sum17 = getPowerSum(bit);

            // 获取和值与11取模得到余数进行校验码
            checkCode = getCheckCodeBySum(sum17);
            // 获取不到校验位
            if (null == checkCode) {
                return null;
            }

            // 将前17位与第18位校验码拼接
            idCard17 += checkCode;
        } else { // 身份证包含数字
            return null;
        }
        return idCard17;
    }

    private static int[] convertCharToInt(char[] c) throws NumberFormatException {
        int[] a = new int[c.length];
        int k = 0;
        for (char temp : c) {
            a[k++] = Integer.parseInt(String.valueOf(temp));
        }
        return a;
    }

    /**
     * 将和值与11取模得到余数进行校验码判断
     *
     * @param sum17
     * @return 校验位
     */
    private static String getCheckCodeBySum(int sum17) {
        String checkCode = null;
        switch (sum17 % 11) {
            case 10:
                checkCode = "2";
                break;
            case 9:
                checkCode = "3";
                break;
            case 8:
                checkCode = "4";
                break;
            case 7:
                checkCode = "5";
                break;
            case 6:
                checkCode = "6";
                break;
            case 5:
                checkCode = "7";
                break;
            case 4:
                checkCode = "8";
                break;
            case 3:
                checkCode = "9";
                break;
            case 2:
                checkCode = "x";
                break;
            case 1:
                checkCode = "0";
                break;
            case 0:
                checkCode = "1";
                break;
        }
        return checkCode;
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
     *
     * @param bit
     * @return
     */
    private static int getPowerSum(int[] bit) {
        int sum = 0;
        if (power.length != bit.length) {
            return sum;
        }

        for (int i = 0; i < bit.length; i++) {
            for (int j = 0; j < power.length; j++) {
                if (i == j) {
                    sum = sum + bit[i] * power[j];
                }
            }
        }
        return sum;
    }

    /**
     * 检测字符串是否仅包含数字
     *
     * @param s 待测试字符串
     * @return 检测通过返回true
     */
    public static boolean isNumeric(String s) {
        return StringUtils.containsOnly(s, "0123456789");
    }

    public static String uniqueId(String prefix) {
        SecureRandom sec = new SecureRandom();
        byte[] sbuf = sec.generateSeed(8);
        ByteBuffer bb = ByteBuffer.wrap(sbuf);

        long time = System.currentTimeMillis();
        String uniqid = String.format("%s%08x%05x", prefix, time / 1000, time);
        uniqid += "." + String.format("%.8s", "" + bb.getLong() * -1);
        return uniqid;
    }

    /**
     * 字符串打码
     *
     * @param s           待打码字符串
     * @param leftRest    左边预留
     * @param rightRest   右边预留
     * @param mosaic      替换字符
     * @param mosaicCount 如果大于零则按指定数量显示替换字符个数, 否则保持原始长度
     * @return 打码后的字符串
     */
    public static String mask(String s, int leftRest, int rightRest, String mosaic, int mosaicCount) {
        int length = StringUtils.length(s);
        if (leftRest + rightRest >= length) {
            // 超过长度不做打码原样返回
            return s;
        }

        String left = StringUtils.left(s, leftRest);
        String right = StringUtils.right(s, rightRest);

        String middle;
        if (mosaicCount <= 0) {
            middle = StringUtils.repeat(mosaic, length - leftRest - rightRest);
        } else {
            middle = StringUtils.repeat(mosaic, mosaicCount);
        }

        return left + middle + right;
    }

    /**
     * 手机号打码
     *
     * @param phone 11位长度的中国手机号
     * @return 打码后的手机号
     */
    public static String maskChinaMobilePhone(String phone) {
        return mask(phone, 3, 4, "*", 0);
    }

    /**
     * 中文姓名打码
     *
     * @param name 中文姓名
     * @return 打码后的中文姓名
     */
    public static String maskChineseName(String name) {
        if (StringUtils.isNotBlank(name)) {
            return mask(name, 0, name.length() - 1, "*", 0);
        }
        return "*";
    }

    /**
     * 身份证打码
     *
     * @param idCard 身份证号
     * @return 打码后的身份证号
     */
    public static String maskIdCard(String idCard) {
        return mask(idCard, 3, 4, "*", 0);
    }

    /**
     * 银行卡号打码
     *
     * @param bankCard 银行卡号
     * @return 打码后的银行卡号
     */
    public static String maskBankCard(String bankCard) {
        return mask(bankCard, 4, 4, "*", 10);
    }

    /**
     * 昵称或用户名打码
     *
     * @param nickname 昵称
     * @return 打码后的昵称
     */
    public static String maskNickname(String nickname) {
        int length = StringUtils.length(nickname);

        // 4个字符以内展示一半并填充马赛克
        if (length <= 4) {
            return mask(nickname, length / 2, 0, "*", 6);
        } else {
            // 否则展示前2后2并填充马赛克
            return mask(nickname, 2, 2, "*", 4);
        }
    }

    /**
     * 校验用户名是否合法
     * @param username 用户名
     * @return 是否合法
     */
    public static boolean checkUsername(String username) {
        // 长度4到20位
        if (StringUtils.isBlank(username) || username.length() > 20) {
            return false;
        }

        // 纯中文名-长度至少2
        if (Pattern.matches("^[\\u4e00-\\u9fa5]+$", username)) {

            if (username.length() >= 2) {
                return true;
            }
        }

        // 非纯中文长度至少4
        else if (username.length() < 4) {
            return false;
        }

        // 不能为纯数字-手机号
        String phoneFmt = "^\\d*$";
        if (Pattern.matches(phoneFmt, username)) {
            return false;
        }

        // 首位不能是数字
        String firstChar = StringUtils.substring(username, 0, 1);
        if (Pattern.matches(phoneFmt, firstChar)) {
            return false;
        }

        // 只能包含小写字母、大写字母、数字、-、_、中文
        String reg = "^[a-zA-Z0-9_\\-\\u4e00-\\u9fa5]+$";
        return Pattern.matches(reg, username);
    }


    // 中国人姓名规则 如：阿沛·阿旺晋美  塔拉衣白克·赛里克
    private static final Pattern NAME_PATTERN = Pattern.compile("[\\u4E00-\\u9FA5]{1,10}(?:·[\\u4E00-\\u9FA5]{1,10})*");

    /**
     * 校验中国人姓名是否合法
     * @param name 姓名
     * @return 验证结果
     */
    public static boolean checkChineseName(String name) {
        if (StringUtils.containsWhitespace(name)) {
            return false;
        }

        int len = StringUtils.length(name);
        if (len < 2) {
            return false;
        }

        return NAME_PATTERN.matcher(name).matches();
    }

    /**
     * 检查用户名并返回错误信息
     *
     * @param username
     * @return
     */
    public static String checkUsernameWithMessage(String username) {
        // 长度4到20位
        if (StringUtils.isBlank(username)) {
            return "用户名不能为空";
        }

        if (username.length() > 20) {
            return "用户名长度不能超过20位";
        }

        // 纯中文名
        if (Pattern.matches("^[\\u4e00-\\u9fa5]+$", username)) {

            // 长度至少2
            if (username.length() < 2) {
                return "中文用户名长度至少为2位";
            }
        }

        // 非纯中文长度至少4
        else if (username.length() < 4) {
            return "非中文用户名长度至少为4位";
        }

        // 不能为纯数字-手机号
        String phoneFmt = "^\\d*$";
        if (Pattern.matches(phoneFmt, username)) {
            return "用户名不能为纯数字";
        }

        // 首位不能是数字
        String firstChar = StringUtils.substring(username, 0, 1);
        if (Pattern.matches(phoneFmt, firstChar)) {
            return "用户名首位不能为数字";
        }

        // 只能包含小写字母、大写字母、数字、-、_、中文
        String reg = "^[a-zA-Z0-9_\\-\\u4e00-\\u9fa5]+$";
        if (!Pattern.matches(reg, username)) {
            return "用户名只能包含大小写字母、数字、中文、或-、_";
        }
        return null;
    }

    /**
     * 查找第一个非空的字符串
     *
     * @param strings 要查找的字符串列表
     * @return 第一个非空的字符串, 如果未找到则返回null
     */
    public static String firstNotBlank(String... strings) {
        for (String string : strings) {
            if (StringUtils.isNotBlank(string)) {
                return string;
            }
        }
        return null;
    }


    /**
     * 从url中获取参数
     * @param url
     * @return
     */
    public static Map<String, String> convertUrlToMap(String url) {
        Map<String, String> map = Maps.newHashMap();
        String paramsStr = StringUtils.substringAfter(url, "?");
        String[] arrTemp = StringUtils.split(paramsStr, "&");
        for (String str : arrTemp) {
            String[] paramsArray = StringUtils.split(str,"=");
            map.put(paramsArray[0], paramsArray.length == 1 ? StringUtils.EMPTY : paramsArray[1]);
        }
        return map;
    }

}
