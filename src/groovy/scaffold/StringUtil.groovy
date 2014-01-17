// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold

import org.apache.commons.lang.RandomStringUtils
import java.util.regex.Pattern


/**
 * This is the place for various string things
 * @author jwarner
 */
class StringUtil {

    public static String formatUrlName(String title) {
        def maxLen = 40
        def name = title.toLowerCase().replace(" ", "-").replace("/", "-")
        name = name.replaceAll(/[^a-z0-9\-]/, "").replaceAll(/-+/, "-")
        if (name.size() > maxLen)
           name = name.take(maxLen).replaceFirst(/-[a-z0-9]*$/, "")
        return name
    }

    public static Integer toInt(String intStr) {  //Safe str to int conversion
        return intStr?.isInteger() ? intStr.toInteger() : null
    }

    public static Integer toInt(String intStr, int defaultVal) {
        return toInt(intStr) ?: defaultVal
    }

    public static String toCamelCase(String dashStr) { //ex: "ready-set-go" ==> "readySetGo"
        return dashStr.replaceAll(/\-(.)/, { it[1].toUpperCase() })
    }

    public static String toDash(String camel) { //example: "readySet! Go" -> "ready-set-go"
        def joint = ~/[^A-Za-z0-9]|(?<=[a-z])(?=[A-Z])/
        def dashes = ~/-+/
        return camel?.trim().replaceAll(joint, "-").replaceAll(dashes, "-").toLowerCase()
    }

    public static Boolean listContains(String list, String item) { //ex: listContains("emu,ox", "ox") ==> true
        def tokens = list?.toLowerCase().tokenize(',')
        return tokens?.contains(item?.toLowerCase())
    }

    /**
     * We have a pattern of putting * on BOTH sides only when the query is sufficiently
     *  long to make search faster.
     * @param query
     * @return
     */
    public static String prepQueryString(String query) {
        def newQuery
        if (query.length() < 3) {
            newQuery = query + "*"
        }
        else {
            newQuery = "*" + query + "*"
        }
        return newQuery
    }

    public static String getRandomString(int len) {
        String charset = (('A'..'Z') + ('a'..'z') + ('0'..'9')).join()
        return RandomStringUtils.random(len, charset.toCharArray())
    }

    public static String[] commaListToArray(String val) {
        return val.split(',').collect { it.trim() }
    }

    public static String stripRegex(String str, String regex) {
        def pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
        def processedStr = str.replaceAll(pattern, "")
        return processedStr
    }

    public static String limitString(String str, int len) {
        if (str.size() > len) {
            return str[0..len-1]
        }
        return str
    }
}
