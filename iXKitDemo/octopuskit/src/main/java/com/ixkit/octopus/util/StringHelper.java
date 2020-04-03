package com.ixkit.octopus.util;

/**
 *
 */


import android.net.UrlQuerySanitizer;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {

    public static String listToSequence(Collection<? extends Object> l, String delim) {
        StringBuffer sb = new StringBuffer();
        boolean addDelim = false;
        for (Object o : l) {
            if (addDelim) {
                sb.append(delim);
            }
            addDelim = true;
            sb.append(o);
        }
        return sb.toString();
    }

    public static String listToMethodSequence(Collection<? extends Object> l, String delim, String methodName) {
        StringBuffer sb = new StringBuffer();

        Method method = null;
        boolean first = true;

        try {
            for (Object o : l) {
                if (first) {
                    method = o.getClass().getMethod(methodName);
                    first = false;
                } else {
                    sb.append(delim);
                }
                sb.append(method.invoke(o));
            }

        } catch (Exception e) {
            throw new RuntimeException("Fehler beim erzeugen der Id Liste", e);
        }
        return sb.toString();
    }

    public static String cleanToLowerCaseAndNumbers(String s) {
        s = s.toLowerCase();
        return s.replaceAll("[^a-zäöüéèà0-9]","");
    }

    public static boolean isDouble(String s) {
        if (empty(s)) {
            return false;
        }
        final String decimalPattern = "^[-+]?\\d+(\\.{0,1}(\\d+?))?$";
        return Pattern.matches(decimalPattern, s);
    }

    public static boolean empty(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static boolean isNotEmpty(String s) { return ! empty(s); }

    public static String duration(long millis) {
        return String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(millis),
                (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
    }

    public static String getYoutubeIframe(String youtubeVideoLink) {
        if (StringHelper.empty(youtubeVideoLink)) {
            return "";
        }

        String youtubeId = getYoutubeId(youtubeVideoLink);

        if (StringHelper.empty(youtubeId)) {
            return "";
        }

        return "<iframe width=\"375\" height=\"243\" frameborder=\"0\" allowfullscreen=\"\" src=\"https://www.youtube.com/embed/"
                + youtubeId
                + "?controls=0&rel=0&amp;hd=1\"></iframe>";
    }

    private static String getYoutubeId(String youtubeVideoLink) {
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youtubeVideoLink);

        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    public static String getExcelColumnName(int number) {
        String converted = "";

        while (number >= 0) {
            int remainder = number % 26;
            converted = (char) (remainder + 'A') + converted;
            number = (number / 26) - 1;
        }

        return converted;
    }

    public static int getExcelColumnNumber(String columnName) {
        columnName = columnName.trim();
        if (StringHelper.empty(columnName)) {
            throw new IllegalStateException("Column Name is not valid.");
        }

        StringBuffer buff = new StringBuffer(columnName);
        char chars[] = buff.reverse().toString().toLowerCase().toCharArray();

        int retVal = 0;
        int multiplier = 0;

        for (int i = 0; i < chars.length; i++) {
            //retrieve ascii value of character, subtract 96 so number corresponds to place in alphabet. ascii 'a' = 97
            multiplier = chars[i] - 96;
            //mult the number by 26^(position in array)
            retVal += multiplier * Math.pow(26, i);
        }
        return retVal - 1;
    }

    public static String formatThousandSplitString(String zahl) {
        return formatThousandSplitLong(Long.parseLong(zahl));
    }

    public static String formatThousandSplitLong(long zahl) {
        DecimalFormat df = new DecimalFormat("###,000");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator('\'');
        df.setDecimalFormatSymbols(dfs);
        return df.format(zahl);
    }

    public static void validateLength(String s, int maxLength, String name) {
        if (isNotEmpty(s) && s.length() > maxLength) {
            throw new IllegalStateException("Feld " + name + " darf maximal " + maxLength + " Zeichen lang sein");
        }
    }

    public static String stripExtension (String str) {
        if (str == null) return null;

        int pos = str.lastIndexOf(".");
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    public static boolean containsUmlaut(String input) {
        return input.contains("ü") ||
                input.contains("ö") ||
                input.contains("ä") ||
                input.contains("ß") ||
                input.contains("Ü") ||
                input.contains("Ö") ||
                input.contains("Ä")      ||
                input.contains("&#252;") ||
                input.contains("&#220;") ||
                input.contains("&#228;") ||
                input.contains("&#196;") ||
                input.contains("&#246;") ||
                input.contains("&#214;") ||
                input.contains("&#223;");
    }

    public static String replaceUmlaut(String input) {
        return input
                .replaceAll("ü", "ue")
                .replaceAll("ö", "oe")
                .replaceAll("ä", "ae")
                .replaceAll("ß", "ss")
                .replaceAll("Ü", "Ue")
                .replaceAll("Ö", "Oe")
                .replaceAll("Ä", "Ae");
    }

    public static String removeHtml(String html) {
        html = html.replaceAll("\\<.*?>", "");
        html = html.replaceAll("&nbsp;", "");
        html = html.replaceAll("&amp;", "");
        return html;
    }

    public static boolean isValidEmailAddress(String email) {
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        return  pattern.matcher(email).matches();
    }

    public static String getExtension (final String str) {
        if (str == null) { return null; }

        int pos = str.lastIndexOf(".");
        if (pos == -1) {
            return str;
        }

        return str.substring(pos + 1);
    }

    /**
     * https://github.com/shopware/shopware/blob/3f07aecb571673aad98dfc50f69502fe94653607/engine/Shopware/Bundle/MediaBundle/Strategy/Md5Strategy.php
     * Bilder für Shopware 5 Encode damit wir sie in der Logistik anzeigen können
     * DAS 22.02.2016
     * @param url
     * @return
     */
    public static String encodeShopware5Url(String url) {
        if (empty(url)) { return null; }
        String md5 = "";
        try {
            md5 = md5ThePhpWay(url);
            return getFolderFromMd5(0, md5) +  getFolderFromMd5(2, md5) + getFolderFromMd5(4, md5);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String md5ThePhpWay(String input) throws NoSuchAlgorithmException {
        String result = input;
        if(input != null) {
            MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
            md.update(input.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            result = hash.toString(16);
            while(result.length() < 32) { //40 for SHA-1
                result = "0" + result;
            }
        }
        return result;
    }

    private static String getFolderFromMd5(int folderStart, String md5) {
        return md5.substring(folderStart, folderStart + 2).replace("ad", "g0") + "/";
    }

    public static String getShortString(String input, int width) {
        if (input != null && input.length() > width) {
            return input.substring(0, width - 3) + "...";
        }
        return input;
    }

    public static String removeBracketsWithContent(String s) {
        return s.replaceAll("\\(.*?\\) ?", "");
    }

    public static boolean isEmpty(String value){
        return (null == value ) || ("".equals(value));
    }
    public static String safe(String value){
        if (null == value){
            return "";
        }
        return value;
    }

    public static boolean equal(String a, String b){
        String s_a = safe(a);
        return s_a.equals(b);
    }
    public static String replaceHtmlTag(String buf){
        if (StringHelper.isEmpty(buf)){
            return buf;
        }

        return  buf.replaceAll("<br/>","\n");
    }

    public static String getUrlParameter(String url, String key){
        UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(url);
        if (1>0) {
            return sanitizer.getValue(key);
        }
        List<UrlQuerySanitizer.ParameterValuePair> list =  sanitizer.getParameterList();
        for (UrlQuerySanitizer.ParameterValuePair pair : list) {

        }
        return "";
    }

}
