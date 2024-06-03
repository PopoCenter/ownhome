package com.tencent.wxcloudrun.util;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tencent.wxcloudrun.vo.CustomerListVoItem;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
public class ChangeChinesePinyinUtil {
    /**
     * 获取姓名全拼和首字母
     * @author 于公成
     * @param  chinese 汉语名称
     * @return fullPinyin : 全拼        simplePinyin ： 首字母  groupPinyin：微信用户组第一个字母
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public static Map<String, String> changeChinesePinyin(String chinese) throws BadHanyuPinyinOutputFormatCombination {
        Map<String, String> pinyin = new HashMap<String, String>();

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        StringBuffer fullPinyin = new StringBuffer();
        StringBuffer simplePinyin = new StringBuffer();
        StringBuffer firstPinyin = new StringBuffer();

        char[] chineseChar = chinese.toCharArray();
        for (int i = 0; i < chineseChar.length; i++) {
            String[] str = null;
            try {
                str = PinyinHelper.toHanyuPinyinStringArray(chineseChar[i],
                        format);
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
            if (str != null) {
                fullPinyin = fullPinyin.append(str[0].toString());
                simplePinyin = simplePinyin.append(str[0].charAt(0));

            }
            if (str == null) {
                String regex = "^[0-9]*[a-zA-Z]*+$";
                Pattern pattern = Pattern.compile(regex);
                Matcher m = pattern.matcher(String.valueOf(chineseChar[i]));
                if (m.find()) {
                    fullPinyin = fullPinyin.append(chineseChar[i]);
                    simplePinyin = simplePinyin.append(chineseChar[i]);
                }
            }
        }
        String[] name = PinyinHelper.toHanyuPinyinStringArray(chineseChar[0],format);
        firstPinyin=firstPinyin.append(name[0].charAt(0));
        pinyin.put("fullPinyin", fullPinyin.toString());
        pinyin.put("simplePinyin", simplePinyin.toString().toUpperCase());
        pinyin.put("groupPinyin", firstPinyin.toString().toUpperCase());
        return pinyin;
    }
    /**
     * 按拼音首字母分组
     * @author 于公成
     * @param list
     * @return
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public static Map<String, Object> getCodeGroup(List<String> list) throws BadHanyuPinyinOutputFormatCombination {
        Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
        //按字母排序
        Collections.sort(list, com);
        //输出26个字母
        Map<String, Object> map = new TreeMap<String, Object>();
        for(int i=1;i<=26;i++){
            String word = String. valueOf((char) (96 + i)). toUpperCase();
            //循环找出首字母一样的数据
            List<String> letter = new ArrayList<String>();
            for (String str : list) {
                String code=changeChinesePinyin(str).get("groupPinyin");
                if(word.equals(code)) {
                    letter.add(str);
                }
                System.out.println(str);
            }
            map.put(word, letter);
        }
        System.out.println(map);
        return map;
    }

    /**
     * @author 于公成
     * 按用户拼音首字母分组
     * @param list
     * @return
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public static Map<String, List<CustomerListVoItem>> getUserCodeGroup(List<CustomerListVoItem> list) throws BadHanyuPinyinOutputFormatCombination {
        //Collections工具类的sort()方法对list集合元素排序　
        Collections.sort(list,new Comparator<CustomerListVoItem>(){
            @Override
            public int compare(CustomerListVoItem o1, CustomerListVoItem o2) {
                //获取中文环境
                Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
                return com.compare(o1.getName(),o2.getName());
            }
        });

        //输出26个字母
        Map<String, List<CustomerListVoItem>> map = new TreeMap<String, List<CustomerListVoItem>>();
        for(int i=1;i<=26;i++){
            String word = String. valueOf((char) (96 + i)). toUpperCase();
            //循环找出首字母一样的数据
            List<CustomerListVoItem> letter = new ArrayList<CustomerListVoItem>();
            for (CustomerListVoItem str : list) {

                String code=changeChinesePinyin(str.getName()).get("groupPinyin");
                if(word.equals(code)) {
                    letter.add(str);
                }
                //System.out.println(str);
            }
            map.put(word, letter);
        }
        //System.out.println(map);
        return map;
    }


//    public static void main(String[] args) throws BadHanyuPinyinOutputFormatCombination {
////		System.out.println(changeChinesePinyin("于公成").get("groupPinyin"));
//        List<String> list=new ArrayList<String>();
//        list.add("于公成");
//        list.add("由于工程");
//        list.add("与工程");
//        list.add("王为");
//        list.add("刘汝祥");
//        list.add("阿宝");
//        list.add("阿亮");
//        getCodeGroup(list);
//
//
//
//        //getUserCodeGroup(list);
//    }
}