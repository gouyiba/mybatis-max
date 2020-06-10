package com.rabbit.core.injector;

import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

/**
 * this class by created wuyongfei on 2020/5/10 21:00
 **/
public abstract class RabbitAbstractMethod {
    protected static final Log logger = LogFactory.getLog(RabbitAbstractMethod.class);
    protected Configuration configuration;
    protected LanguageDriver languageDriver;
    protected MapperBuilderAssistant builderAssistant;

    public RabbitAbstractMethod() {
    }

    public void inject(MapperBuilderAssistant builderAssistant, Class<?> mapperClass, Class<?> modelClass) {
        this.configuration = builderAssistant.getConfiguration();
        this.builderAssistant = builderAssistant;
        this.languageDriver = this.configuration.getDefaultScriptingLanguageInstance();
        this.injectMappedStatement(mapperClass, modelClass);
    }

    private boolean hasMappedStatement(String mappedStatement) {
        return this.configuration.hasStatement(mappedStatement, false);
    }

//    protected String sqlLogicSet(TableInfo table) {
//        return "SET " + table.getLogicDeleteSql(false, true);
//    }
//
//    protected String sqlSet(boolean logic, boolean ew, TableInfo table, boolean judgeAliasNull, String alias, String prefix) {
//        String sqlScript = table.getAllSqlSet(logic, prefix);
//        if (judgeAliasNull) {
//            sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", alias), true);
//        }
//
//        if (ew) {
//            sqlScript = sqlScript + "\n";
//            sqlScript = sqlScript + SqlScriptUtils.convertIf(SqlScriptUtils.unSafeParam("ew.sqlSet"), String.format("%s != null and %s != null", "ew", "ew.sqlSet"), false);
//        }
//
//        sqlScript = SqlScriptUtils.convertSet(sqlScript);
//        return sqlScript;
//    }

//    protected String sqlComment() {
//        return SqlScriptUtils.convertChoose(String.format("%s != null and %s != null", "ew", "ew.sqlComment"), SqlScriptUtils.unSafeParam("ew.sqlComment"), "");
//    }
//
//    protected String sqlSelectColumns(TableInfo table, boolean queryWrapper) {
//        String selectColumns = "*";
//        if (table.getResultMap() == null || table.getResultMap() != null && table.isInitResultMap()) {
//            selectColumns = table.getAllSqlSelect();
//        }
//
//        return !queryWrapper ? selectColumns : SqlScriptUtils.convertChoose(String.format("%s != null and %s != null", "ew", "ew.sqlSelect"), SqlScriptUtils.unSafeParam("ew.sqlSelect"), selectColumns);
//    }
//
//    protected String sqlCount() {
//        return SqlScriptUtils.convertChoose(String.format("%s != null and %s != null", "ew", "ew.sqlSelect"), SqlScriptUtils.unSafeParam("ew.sqlSelect"), "1");
//    }
//
//    protected String sqlSelectObjsColumns(TableInfo table) {
//        return SqlScriptUtils.convertChoose(String.format("%s != null and %s != null", "ew", "ew.sqlSelect"), SqlScriptUtils.unSafeParam("ew.sqlSelect"), table.getAllSqlSelect());
//    }
//
//    protected String sqlWhereByMap(TableInfo table) {
//        String sqlScript;
//        if (table.isLogicDelete()) {
//            sqlScript = SqlScriptUtils.convertChoose("v == null", " ${k} IS NULL ", " ${k} = #{v} ");
//            sqlScript = SqlScriptUtils.convertForeach(sqlScript, "cm", "k", "v", "AND");
//            sqlScript = SqlScriptUtils.convertIf(sqlScript, "cm != null and !cm.isEmpty", true);
//            sqlScript = sqlScript + "\n" + table.getLogicDeleteSql(true, false);
//            sqlScript = SqlScriptUtils.convertWhere(sqlScript);
//            return sqlScript;
//        } else {
//            sqlScript = SqlScriptUtils.convertChoose("v == null", " ${k} IS NULL ", " ${k} = #{v} ");
//            sqlScript = SqlScriptUtils.convertForeach(sqlScript, "cm", "k", "v", "AND");
//            sqlScript = SqlScriptUtils.convertWhere(sqlScript);
//            sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null and !%s", "cm", "cm.isEmpty"), true);
//            return sqlScript;
//        }
//    }
//
//    protected String sqlWhereEntityWrapper(boolean newLine, TableInfo table) {
//        String sqlScript;
//        if (table.isLogicDelete()) {
//            sqlScript = table.getAllSqlWhere(true, true, "ew.entity.");
//            sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", "ew.entity"), true);
//            sqlScript = sqlScript + "\n" + table.getLogicDeleteSql(true, false) + "\n";
//            String normalSqlScript = SqlScriptUtils.convertIf(String.format("AND ${%s}", "ew.sqlSegment"), String.format("%s != null and %s != '' and %s", "ew.sqlSegment", "ew.sqlSegment", "ew.nonEmptyOfNormal"), true);
//            normalSqlScript = normalSqlScript + "\n";
//            normalSqlScript = normalSqlScript + SqlScriptUtils.convertIf(String.format(" ${%s}", "ew.sqlSegment"), String.format("%s != null and %s != '' and %s", "ew.sqlSegment", "ew.sqlSegment", "ew.emptyOfNormal"), true);
//            sqlScript = sqlScript + normalSqlScript;
//            sqlScript = SqlScriptUtils.convertChoose(String.format("%s != null", "ew"), sqlScript, table.getLogicDeleteSql(false, false));
//            sqlScript = SqlScriptUtils.convertWhere(sqlScript);
//            return newLine ? "\n" + sqlScript : sqlScript;
//        } else {
//            sqlScript = table.getAllSqlWhere(false, true, "ew.entity.");
//            sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", "ew.entity"), true);
//            sqlScript = sqlScript + "\n";
//            sqlScript = sqlScript + SqlScriptUtils.convertIf(String.format(SqlScriptUtils.convertIf(" AND", String.format("%s and %s", "ew.nonEmptyOfEntity", "ew.nonEmptyOfNormal"), false) + " ${%s}", "ew.sqlSegment"), String.format("%s != null and %s != '' and %s", "ew.sqlSegment", "ew.sqlSegment", "ew.nonEmptyOfWhere"), true);
//            sqlScript = SqlScriptUtils.convertWhere(sqlScript) + "\n";
//            sqlScript = sqlScript + SqlScriptUtils.convertIf(String.format(" ${%s}", "ew.sqlSegment"), String.format("%s != null and %s != '' and %s", "ew.sqlSegment", "ew.sqlSegment", "ew.emptyOfWhere"), true);
//            sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", "ew"), true);
//            return newLine ? "\n" + sqlScript : sqlScript;
//        }
//    }
//
//    protected String filterTableFieldInfo(List<TableFieldInfo> fieldList, Predicate<TableFieldInfo> predicate, Function<TableFieldInfo, String> function, String joiningVal) {
//        Stream<TableFieldInfo> infoStream = fieldList.stream();
//        return predicate != null ? (String)infoStream.filter(predicate).map(function).collect(Collectors.joining(joiningVal)) : (String)infoStream.map(function).collect(Collectors.joining(joiningVal));
//    }

    protected String optlockVersion() {
        return "<if test=\"et instanceof java.util.Map\"> AND ${et.MP_OPTLOCK_VERSION_COLUMN}=#{et.MP_OPTLOCK_VERSION_ORIGINAL}</if>";
    }

//    protected MappedStatement addSelectMappedStatementForTable(Class<?> mapperClass, String id, SqlSource sqlSource, TableInfo table) {
//        String resultMap = table.getResultMap();
//        return null != resultMap ? this.addMappedStatement(mapperClass, id, sqlSource, SqlCommandType.SELECT, (Class)null, resultMap, (Class)null, new NoKeyGenerator(), (String)null, (String)null) : this.addSelectMappedStatementForOther(mapperClass, id, sqlSource, table.getEntityType());
//    }

    protected MappedStatement addSelectMappedStatementForOther(Class<?> mapperClass, String id, SqlSource sqlSource, Class<?> resultType) {
        return this.addMappedStatement(mapperClass, id, sqlSource, SqlCommandType.SELECT, (Class) null, (String) null, resultType, new NoKeyGenerator(), (String) null, (String) null);
    }

    protected MappedStatement addInsertMappedStatement(Class<?> mapperClass, Class<?> parameterType, String id, SqlSource sqlSource, KeyGenerator keyGenerator, String keyProperty, String keyColumn) {
        return this.addMappedStatement(mapperClass, id, sqlSource, SqlCommandType.INSERT, parameterType, (String) null, Integer.class, keyGenerator, keyProperty, keyColumn);
    }

    protected MappedStatement addDeleteMappedStatement(Class<?> mapperClass, String id, SqlSource sqlSource) {
        return this.addMappedStatement(mapperClass, id, sqlSource, SqlCommandType.DELETE, (Class) null, (String) null, Integer.class, new NoKeyGenerator(), (String) null, (String) null);
    }

    protected MappedStatement addUpdateMappedStatement(Class<?> mapperClass, Class<?> parameterType, String id, SqlSource sqlSource) {
        return this.addMappedStatement(mapperClass, id, sqlSource, SqlCommandType.UPDATE, parameterType, (String) null, Integer.class, new NoKeyGenerator(), (String) null, (String) null);
    }

    /**
     * 注入MappedStatement
     *
     * @param mapperClass
     * @param id
     * @param sqlSource
     * @param sqlCommandType
     * @param parameterType
     * @param resultMap
     * @param resultType
     * @param keyGenerator
     * @param keyProperty
     * @param keyColumn
     * @return
     */
    protected MappedStatement addMappedStatement(Class<?> mapperClass, String id, SqlSource sqlSource, SqlCommandType sqlCommandType, Class<?> parameterType, String resultMap, Class<?> resultType, KeyGenerator keyGenerator, String keyProperty, String keyColumn) {
        String statementName = mapperClass.getName() + "." + id;
        if (this.hasMappedStatement(statementName)) {
            logger.warn("[" + statementName + "] Has been loaded by XML or SqlProvider or Mybatis's Annotation, so ignoring this injection for [" + this.getClass() + "]");
            return null;
        } else {
            boolean isSelect = false;
            if (sqlCommandType == SqlCommandType.SELECT) {
                isSelect = true;
            }

            return this.builderAssistant.addMappedStatement(id, sqlSource, StatementType.PREPARED, sqlCommandType, (Integer) null, (Integer) null, (String) null, parameterType, resultMap, resultType, (ResultSetType) null, !isSelect, isSelect, false, keyGenerator, keyProperty, keyColumn, this.configuration.getDatabaseId(), this.languageDriver, (String) null);
        }
    }

    public abstract void injectMappedStatement(Class<?> mapperClass, Class<?> modelClass);
}
