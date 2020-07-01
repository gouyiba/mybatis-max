package cloud.gouyiba.core.parse;

import cloud.gouyiba.common.utils.ClassUtils;
import cloud.gouyiba.common.utils.StringUtils;
import cloud.gouyiba.core.annotation.*;
import cloud.gouyiba.core.bean.TableFieldInfo;
import cloud.gouyiba.core.bean.TableInfo;
import cloud.gouyiba.core.enumation.MySqlColumnType;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Transient;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * mybatis-max ParseClass2TableInfo
 * <p>
 * this class by created wuyongfei on 2020/5/30 16:20
 **/
public final class ParseClass2TableInfo {

    private static final Logger logger = LoggerFactory.getLogger(ParseClass2TableInfo.class);

    // mybatis-max-tag
    protected static final String TAG = "Mybatis-Max";

    /**
     * Global TableInfo Cache
     */
    private static final Map<Class<?>, TableInfo> TABLE_INFO_CACHE = new ConcurrentHashMap<>();

    /**
     * add TableInfo cache
     *
     * @param clazz     bean.class
     * @param tableInfo After initialization tableInfo
     */
    private static void addTableInfoCache(Class<?> clazz, TableInfo tableInfo) {
        if (clazz != null && !Objects.isNull(tableInfo)) {
            TABLE_INFO_CACHE.put(ClassUtils.getUserClass(clazz), tableInfo);
        }
    }

    /**
     * get TableInfo cache
     *
     * @param clazz bean.class
     * @return TableInfo
     */
    public static TableInfo getTableInfo(Class<?> clazz) {
        return TABLE_INFO_CACHE.get(ClassUtils.getUserClass(clazz));
    }

    /**
     * get all TableInfo cache
     *
     * @return Map<Class < ?> , TableInfo>
     */
    public static Map<Class<?>, TableInfo> getAllTableInfo() {
        return TABLE_INFO_CACHE;
    }

    /**
     * remove TableInfo cache
     *
     * @param clazz bean.class
     */
    private static void removeTableInfoCache(Class<?> clazz) {
        TABLE_INFO_CACHE.remove(ClassUtils.getUserClass(clazz));
    }

    /**
     * clear all TableInfo cache
     */
    private static void clearTableInfoCache() {
        TABLE_INFO_CACHE.clear();
    }

    /**
     * parse Class to TableInfo and Global cache
     *
     * @return TableInfo
     * @author duxiaoyu
     */
    public static TableInfo parseClazzToTableInfo(Class<?> clazz) {
        // 查找Class是否已存在缓存中
        TableInfo tableInfo = getTableInfo(clazz);
        if (tableInfo != null) {
            return tableInfo;
        }

        Map<String, TableFieldInfo> tbFieldMap = new HashMap<>();

        logger.info("{} - start parse tableInfo ......", TAG);
        tableInfo = new TableInfo();

        String tableName; // 数据库表名

        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getAnnotation(Table.class);
            tableName = table.value();
        } else {
            // 自动解析对应的数据库表名称，默认按照驼峰转下划线格式进行转换，如: GoodsInfo -> goods_info
            String className = clazz.getSimpleName();
            tableName = StringUtils.camelToUnderline(StringUtils.firstToLowerCase(className));
        }
        tableInfo.setTableName(tableName);

        //tableInfo.setId(1);// 表默认主键，暂时没有用到的地方，如果后面有需要用到的地方，再启用

        logger.info("{} - parse result db.tableName ==> {}", TAG, tableName);

        // 解析实例中的字段
        List<Field> fieldList = getClassFields(clazz);

        for (Field item : fieldList) {
            if (item.isAnnotationPresent(Transient.class)) {
                continue;
            }
            TableFieldInfo tbField = new TableFieldInfo();

            String columnName = "";// 数据库表字段名
            String propertyName = "";// bean属性名

            // 未标注 @Column 或 @Id
            if (!item.isAnnotationPresent(Column.class) && !item.isAnnotationPresent(Id.class)) {
                // 自动解析字段，默认按照驼峰转下划线格式进行转换，如: salePrice -> sale_price
                propertyName = item.getName();
                columnName = StringUtils.camelToUnderline(propertyName);
                tbField.setJdbcType(ParseClass2TableInfo.getColumnType(item.getGenericType()));
                // 设置解析后的字段内容
                tbField.setField(item);
                tbField.setColumnName(columnName);
                tbField.setPropertyName(propertyName);
                Class<?> clazzFieldType = item.getType();
                tbField.setPropertyType(clazzFieldType);
                tbFieldMap.put(propertyName, tbField);
                continue;
            }

            // 标注 @Column 或 @Id
            Column column = item.getAnnotation(Column.class);
            Id id = item.getAnnotation(Id.class);
            if (!Objects.isNull(column)) {
                // 判断是否是数据库表字段，如果不是，直接忽略不进行解析
                if (!column.isTableColumn()) {
                    continue;
                }
                columnName = column.value();

                // 字段对应的数据类型：此处在设置数据库字段类型时，如果没有指定，会拿默认的数据库类型varchar，
                // 但是这个情况是不符合实际情况的，如果是枚举类型，其实对应的应该不是varchar，此处问题不大，所以暂时先不做处理
                tbField.setJdbcType(column.jdbcType());
                if (MySqlColumnType.VARCHAR.equals(column.jdbcType())) {
                    tbField.setJdbcType(ParseClass2TableInfo.getColumnType(item.getGenericType()));
                }

                tbField.setTypeHandler(column.typeHandler());
            } else if (!Objects.isNull(id)) {
                columnName = id.value();
                // 设置table的主键字段
                tableInfo.setPrimaryKey(item);

                tbField.setJdbcType(id.jdbcType());
                if (MySqlColumnType.VARCHAR.equals(id.jdbcType())) {
                    tbField.setJdbcType(ParseClass2TableInfo.getColumnType(item.getGenericType()));
                }
            }

            // 判断 @column 是否设置了对应的数据库表字段名称，如果没有设置，自动解析字段
            // 默认按照驼峰转下划线格式进行转换，如: salePrice -> sale_price
            if (StringUtils.isBlank(columnName)) {
                propertyName = item.getName();
                columnName = StringUtils.camelToUnderline(propertyName);
            }
            // 设置解析后的字段内容
            tbField.setField(item);
            tbField.setColumnName(columnName);
            tbField.setPropertyName(propertyName);
            Class<?> clazzFieldType = item.getType();
            tbField.setPropertyType(clazzFieldType);
            tbFieldMap.put(propertyName, tbField);
        }

        // 解析实例中填充方法
        Map<Class<? extends Annotation>, Method> fillMap = new HashMap<>();
        List<Method> classMethods = getClassMethods(clazz);
        classMethods.stream()
                .filter(method -> ObjectUtils.isNotEmpty(method.getAnnotation(Create.class))).findFirst()
                .ifPresent(method -> fillMap.computeIfAbsent(Create.class, key -> method));
        classMethods.stream()
                .filter(method -> ObjectUtils.isNotEmpty(method.getAnnotation(Update.class))).findFirst()
                .ifPresent(method -> fillMap.computeIfAbsent(Update.class, key -> method));
        tableInfo.setFillMethods(fillMap);

        logger.info("{} - complete parse tableInfo ......", TAG);

        tableInfo.setColumnMap(tbFieldMap);

        // cache TableInfo
        addTableInfoCache(ClassUtils.getUserClass(clazz), tableInfo);
        return tableInfo;
    }

    /**
     * 获取此类及基类方法
     *
     * @param parameterType
     * @return
     */
    private static List<Method> getClassMethods(Class parameterType) {
        List<Method> methodList = new ArrayList(Arrays.asList(parameterType.getDeclaredMethods()));
        if (ObjectUtils.isEmpty(parameterType.getSuperclass())) {
            return methodList;
        }
        methodList.addAll(getClassMethods(parameterType.getSuperclass()));
        return methodList;
    }

    /**
     * 获取此类及基类字段
     *
     * @param parameterType
     * @return
     */
    private static List<Field> getClassFields(Class parameterType) {
        List<Field> fieldList = new ArrayList(Arrays.asList(parameterType.getDeclaredFields()));
        if (ObjectUtils.isEmpty(parameterType.getSuperclass())) {
            return fieldList;
        }
        fieldList.addAll(getClassFields(parameterType.getSuperclass()));
        return fieldList;
    }

    /**
     * 获取数据库中字段的数据类型
     *
     * @param type 类型
     * @return MySqlColumnType
     */
    public static MySqlColumnType getColumnType(Type type) {
        Class<?> clazz = null;
        if (type instanceof Class<?>) {
            // 判断具体的数据类型
            clazz = (Class<?>) type;
            if (Integer.class.isAssignableFrom(clazz)) {
                return MySqlColumnType.INTEGER;
            } else if (String.class.isAssignableFrom(clazz)) {
                return MySqlColumnType.VARCHAR;
            } else if (Double.class.isAssignableFrom(clazz)) {
                return MySqlColumnType.DOUBLE;
            } else if (Float.class.isAssignableFrom(clazz)) {
                return MySqlColumnType.FLOAT;
            } else if (BigDecimal.class.isAssignableFrom(clazz)) {
                return MySqlColumnType.DECIMAL;
            } else if (Date.class.isAssignableFrom(clazz)) {
                return MySqlColumnType.DATE;
            } else if (Long.class.isAssignableFrom(clazz)) {
                return MySqlColumnType.BIGINT;
            } else if (Boolean.class.isAssignableFrom(clazz)) {
                return MySqlColumnType.TINYINT;
            } else if (Short.class.isAssignableFrom(clazz)) {
                return MySqlColumnType.INTEGER;
            } else if (Character.class.isAssignableFrom(clazz)) {
                return MySqlColumnType.CHAR;
            } else if (LocalDate.class.isAssignableFrom(clazz)) {
                return MySqlColumnType.DATE;
            } else if (LocalDateTime.class.isAssignableFrom(clazz)) {
                return MySqlColumnType.TIMESTAMP;
            } else if (Timestamp.class.isAssignableFrom(clazz)) {
                return MySqlColumnType.TIMESTAMP;
            }
        }
        // TODO: rebuild...
        // 默认返回枚举在数据库中对应的TINYINT类型
        return MySqlColumnType.TINYINT;
    }
}
