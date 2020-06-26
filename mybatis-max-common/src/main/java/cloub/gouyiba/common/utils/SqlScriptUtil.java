package cloub.gouyiba.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @ClassName SqlScriptUtil SQL脚本转换工具
 * @Author duxiaoyu
 * @Date 2020/5/12 18:07
 * @Version 1.0
 */
public class SqlScriptUtil implements Serializable {

    /**
     * 转换if标签
     *
     * @param test
     * @param sqlScript
     * @return
     */
    public static String convertIf(final String test, final String sqlScript) {
        return String.format("\n<if test=\"%s\">\n%s\n</if>", test, sqlScript);
    }

    /**
     * 转换where标签
     *
     * @param sqlScript
     * @return
     */
    public static String convertWhere(final String sqlScript) {
        return String.format("\n<where>\n%s\n</where>", sqlScript);
    }

    /**
     * 转换trim标签
     *
     * @param prefix
     * @param sufffix
     * @param prefixOverrides
     * @param suffixOverrides
     * @return
     */
    public static String convertTrim(final String prefix,
                                     final String sufffix,
                                     final String prefixOverrides,
                                     final String suffixOverrides,
                                     final String sqlScript) {
        StringBuffer stringBuffer = new StringBuffer("\n<trim");
        if (StringUtils.isNotBlank(prefix)) {
            stringBuffer.append(String.format(" prefix=\"%s\"", prefix));
        }
        if (StringUtils.isNotBlank(sufffix)) {
            stringBuffer.append(String.format(" sufffix=\"%s\"", sufffix));
        }
        if (StringUtils.isNotBlank(prefixOverrides)) {
            stringBuffer.append(String.format(" prefixOverrides=\"%s\"", prefixOverrides));
        }
        if (StringUtils.isNotBlank(suffixOverrides)) {
            stringBuffer.append(String.format(" suffixOverrides=\"%s\"", suffixOverrides));
        }
        stringBuffer.append(">");
        if (StringUtils.isNotBlank(sqlScript)) {
            stringBuffer.append(sqlScript);
        }
        return stringBuffer.append("\n</trim>").toString();
    }

    /**
     * 转换foreach标签
     *
     * @param collection
     * @param item
     * @param index
     * @param open
     * @param separator
     * @param close
     * @param sqlScript
     * @return
     */
    public static String convertForeach(final String collection,
                                        final String item,
                                        final String index,
                                        final String open,
                                        final String separator,
                                        final String close,
                                        final String sqlScript) {
        StringBuffer stringBuffer = new StringBuffer("\n<foreach");
        if (StringUtils.isNotBlank(collection)) {
            stringBuffer.append(String.format(" collection=\"%s\"", collection));
        }
        if (StringUtils.isNotBlank(item)) {
            stringBuffer.append(String.format(" item=\"%s\"", item));
        }
        if (StringUtils.isNotBlank(index)) {
            stringBuffer.append(String.format(" index=\"%s\"", index));
        }
        if (StringUtils.isNotBlank(open)) {
            stringBuffer.append(String.format(" open=\"%s\"", open));
        }
        if (StringUtils.isNotBlank(separator)) {
            stringBuffer.append(String.format(" separator=\"%s\"", separator));
        }
        if (StringUtils.isNotBlank(close)) {
            stringBuffer.append(String.format(" close=\"%s\"", close));
        }
        stringBuffer.append(">");
        if (StringUtils.isNotBlank(sqlScript)) {
            stringBuffer.append(sqlScript);
        }
        return stringBuffer.append("\n</foreach>").toString();
    }

    /**
     * 安全入参#{}
     *
     * @param param
     * @return
     */
    public static String safeParam(final String param) {
        return new StringBuffer("#{").append(param).append("}").toString();
    }

    /**
     * 非安全入参${}
     *
     * @param param
     * @return
     */
    public static String unSafeParam(final String param) {
        return new StringBuffer("${").append(param).append("}").toString();
    }
}
