package com.vonader.tender_info_platform.utils;

import java.util.regex.Pattern;

public class Validator {
    /**
     * 邮箱正则表达式
     * 支持常见邮箱格式，包括：
     * - 字母、数字及特殊字符(_+&*-)组成的用户名
     * - 支持多级域名（如xxx.xxx@xxx.xxx.xxx）
     * - 顶级域名长度限制为2-7个字母
     */
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    /**
     * 预编译正则表达式，提升性能-m
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * 校验邮箱格式是否合法
     *
     * @param email 待校验的邮箱地址
     * @return 邮箱格式合法返回true，否则返回false
     * @throws IllegalArgumentException 当输入的邮箱为null时抛出
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("邮箱地址不能为null");
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
}
