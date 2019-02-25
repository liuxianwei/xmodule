package com.penglecode.xmodule.common.mybatis.codegen;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.penglecode.xmodule.common.support.BaseModel;
import com.penglecode.xmodule.common.util.ClassUtils;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.common.util.StringUtils;


/**
 * 自定义的基于NySQL数据库的Mybatis Generator Plugin
 * 
 * @author pengpeng
 * @date 2018年2月9日 下午1:02:05
 */
public class CustomMysqlGeneratorPlugin extends PluginAdapter {

	/** 默认的表名别名 */
	private static final String DEFAULT_TABLE_ALIAS_NAME = "a";
	
	protected String serializable;
	
	protected Set<String> baseMappers = new HashSet<String>();
	
	protected String mybatisUtilsClass;
	
	protected Boolean mergeableXmlMapper;
	
	public CustomMysqlGeneratorPlugin() {
		super();
	}

	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public void setProperties(Properties properties) {
		super.setProperties(properties);
		this.serializable = properties.getProperty("modelSerializableClass", "java.io.Serializable");
		this.mybatisUtilsClass = properties.getProperty("mybatisUtilsClass");
		this.mergeableXmlMapper = Boolean.valueOf(properties.getProperty("mergeableXmlMapper", "false"));
		String baseMappers = properties.getProperty("baseMappers");
		if(!StringUtils.isEmpty(baseMappers)) {
			for (String baseMapper : baseMappers.split(",")) {
				this.baseMappers.add(baseMapper);
			}
		}
	}

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		makeSerializable(topLevelClass, introspectedTable); //加入 XxxModel implements BaseModel {} 声明
		return true;
	}
	
	@Override
	public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
		try {
			java.lang.reflect.Field field = sqlMap.getClass().getDeclaredField("isMergeable");
			field.setAccessible(true);
			field.setBoolean(sqlMap, mergeableXmlMapper);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 重写Mapper生成方法
	 */
	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		String mapperAnnotationString = introspectedTable.getTableConfiguration().getProperty("mapperAnnotations");
		String[] mapperAnnotations = null;
		if(!StringUtils.isEmpty(mapperAnnotationString)) {
			mapperAnnotations = mapperAnnotationString.split(",");
		}
		// 获取实体类
		FullyQualifiedJavaType entityType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
		// import接口
		for (String baseMapper : baseMappers) {
			baseMapper = baseMapper.trim();
			if(!StringUtils.isEmpty(baseMapper)) {
				interfaze.addImportedType(new FullyQualifiedJavaType(baseMapper));
				interfaze.addSuperInterface(new FullyQualifiedJavaType(baseMapper + "<" + entityType.getShortName() + ">"));
			}
		}
		// import实体类
		interfaze.addImportedType(entityType);
		
		if(mapperAnnotations != null && mapperAnnotations.length > 0) {
			for(String mapperAnnotation : mapperAnnotations) {
				mapperAnnotation = mapperAnnotation.trim();
				if(!StringUtils.isEmpty(mapperAnnotation)) {
					FullyQualifiedJavaType annotation = new FullyQualifiedJavaType(mapperAnnotation);
					interfaze.addImportedType(annotation);
					interfaze.addAnnotation("@" + annotation.getShortName());
				}
			}
		}
		
		return true;
	}
	
	/**
	 * 重写sqlMapDocumentGenerated，让生成BaseMybatisMapper接口对应的映射文件
	 */
	@Override
	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
		XmlElement rootElement = document.getRootElement();
		//rootElement.addElement(createNewLineElement());
		//rootElement.addElement(createBaseColumnListElement(introspectedTable)); //创建<sql id="xxxBaseColumnList"/>
		rootElement.addElement(createNewLineElement());
		rootElement.addElement(createInsertModelElement(introspectedTable)); //创建<insert id="insertModel"/>
		rootElement.addElement(createNewLineElement());
		rootElement.addElement(createUpdateModelByIdElement(introspectedTable)); //创建<update id="updateModelById"/>
		rootElement.addElement(createNewLineElement());
		rootElement.addElement(createDynamicUpdateModelByIdElement(introspectedTable)); //创建<update id="dynamicUpdateModelById"/>
		rootElement.addElement(createNewLineElement());
		rootElement.addElement(createDeleteModelByIdElement(introspectedTable)); //创建<delete id="deleteModelById"/>
		rootElement.addElement(createNewLineElement());
		rootElement.addElement(createSelectModelByIdElement(introspectedTable)); //创建<select id="selectModelById"/>
		rootElement.addElement(createNewLineElement());
		rootElement.addElement(createSelectModelByExampleElement(introspectedTable)); //创建<select id="selectModelByExample"/>
		rootElement.addElement(createNewLineElement());
		rootElement.addElement(createSelectModelListByIdsElement(introspectedTable)); //创建<select id="selectModelListByIds"/>
		rootElement.addElement(createNewLineElement());
		rootElement.addElement(createSelectAllModelListElement(introspectedTable)); //创建<select id="selectAllModelList"/>
		rootElement.addElement(createNewLineElement());
		rootElement.addElement(createCountAllModelListElement(introspectedTable)); //创建<select id="countAllModelList"/>
		rootElement.addElement(createNewLineElement());
		rootElement.addElement(createSelectModelListByExampleElement(introspectedTable)); //创建<select id="selectModelPageListByExample"/>
		rootElement.addElement(createNewLineElement());
		rootElement.addElement(createSelectModelPageListByExampleElement(introspectedTable)); //创建<select id="selectModelPageListByExample"/>
		rootElement.addElement(createNewLineElement());
		rootElement.addElement(createCountModelPageListByExampleElement(introspectedTable)); //创建<select id="countModelPageListByExample"/>
		return true;
	}
	
	@Override
	public boolean clientDeleteByPrimaryKeyMethodGenerated(org.mybatis.generator.api.dom.java.Method method,
			Interface interfaze, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientInsertMethodGenerated(org.mybatis.generator.api.dom.java.Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientSelectByPrimaryKeyMethodGenerated(org.mybatis.generator.api.dom.java.Method method,
			Interface interfaze, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(org.mybatis.generator.api.dom.java.Method method,
			Interface interfaze, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(org.mybatis.generator.api.dom.java.Method method,
			Interface interfaze, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(org.mybatis.generator.api.dom.java.Method method,
			Interface interfaze, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientInsertSelectiveMethodGenerated(org.mybatis.generator.api.dom.java.Method method,
			Interface interfaze, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientSelectAllMethodGenerated(org.mybatis.generator.api.dom.java.Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean sqlMapResultMapWithoutBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean sqlMapResultMapWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean sqlMapBaseColumnListElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return false;
	}
	
	public TextElement createNewLineElement() {
		return new TextElement(" ");
	}
	
	public XmlElement createBaseColumnListElement(IntrospectedTable introspectedTable) {
		CustomXmlElement sqlElement = new CustomXmlElement("sql");
		String domainName = introspectedTable.getTableConfiguration().getDomainObjectName();
		domainName = Character.toLowerCase(domainName.charAt(0)) + domainName.substring(1);
		String id = domainName + "BaseColumnList";
		sqlElement.addAttribute(new Attribute("id", id));
		sqlElement.addElement(new TextElement(buildBaseColumnList4Select(introspectedTable)));
		return sqlElement;
	}
	
	public XmlElement createInsertModelElement(IntrospectedTable introspectedTable) {
		CustomXmlElement element = new CustomXmlElement("insert");
		element.addAttribute(new Attribute("id", "insertModel"));
		element.addAttribute(new Attribute("parameterType", introspectedTable.getTableConfiguration().getDomainObjectName()));
		element.addAttribute(new Attribute("statementType", "PREPARED"));
		
		IntrospectedColumn pkColumn = getPrimaryKeyColumn(introspectedTable, 0);
		if(pkColumn.isAutoIncrement()) {
			element.addAttribute(new Attribute("keyProperty", pkColumn.getJavaProperty()));
			element.addAttribute(new Attribute("useGeneratedKeys", "true"));
		}
		
		String insertModelColumnsJson = introspectedTable.getTableConfiguration().getProperty("insertModelColumns");
		Set<String> insertModelColumns = new LinkedHashSet<String>();
		if(!StringUtils.isEmpty(insertModelColumnsJson)) {
			List<String> columnNameList = JsonUtils.json2Object(insertModelColumnsJson, new TypeReference<List<String>>() {});
			if(!CollectionUtils.isEmpty(columnNameList)) {
				columnNameList.add(0, pkColumn.getActualColumnName().toLowerCase()); //加入主键列
				for(String columnName : columnNameList) {
					insertModelColumns.add(columnName); //去重
				}
			}
		}
		
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		List<IntrospectedColumn> insertColumns = new ArrayList<IntrospectedColumn>();
		
		if(!CollectionUtils.isEmpty(insertModelColumns)) { //指定了插入字段?
			for(IntrospectedColumn column : allColumns) {
				for(String columnName : insertModelColumns) {
					if(column.getActualColumnName().toLowerCase().equals(columnName.toLowerCase())) {
						insertColumns.add(column);
						break;
					}
				}
			}
		} else { //全字段插入
			insertColumns.addAll(allColumns);
		}
		
		IntrospectedColumn column = null;
		
		StringBuilder sb = new StringBuilder();
		OutputUtilities.javaIndent(sb, 1);
		sb.append("INSERT INTO " + introspectedTable.getTableConfiguration().getTableName().toLowerCase() + "(");
		OutputUtilities.newLine(sb);
		for(int i = 0, len = insertColumns.size(); i < len; i++) {
			column = insertColumns.get(i);
			OutputUtilities.javaIndent(sb, 3);
			sb.append(column.getActualColumnName().toLowerCase());
			if(i != len - 1) {
				sb.append(",");
			}
			OutputUtilities.newLine(sb);
		}
		OutputUtilities.javaIndent(sb, 2);
		sb.append(") VALUES (");
		OutputUtilities.newLine(sb);
		for(int i = 0, len = insertColumns.size(); i < len; i++) {
			column = insertColumns.get(i);
			OutputUtilities.javaIndent(sb, 3);
			sb.append("#{" + column.getJavaProperty() + ", jdbcType=" + getJdbcTypeName(column) + "}");
			if(i != len - 1) {
				sb.append(",");
			}
			OutputUtilities.newLine(sb);
		}
		OutputUtilities.javaIndent(sb, 2);
		sb.append(")");
		element.addElement(new TextElement(sb.toString()));
		return element;
	}
	
	public XmlElement createUpdateModelByIdElement(IntrospectedTable introspectedTable) {
		IntrospectedColumn pkColumn = getPrimaryKeyColumn(introspectedTable, 0);
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		List<IntrospectedColumn> updateColumns = new ArrayList<IntrospectedColumn>();
		
		String updateModelColumnsJson = introspectedTable.getTableConfiguration().getProperty("updateModelColumns");
		List<String> updateModelColumns = null;
		if(!StringUtils.isEmpty(updateModelColumnsJson)) {
			updateModelColumns = JsonUtils.json2Object(updateModelColumnsJson, new TypeReference<List<String>>() {});
		}
		
		for(IntrospectedColumn column : allColumns) {
			if(!column.equals(pkColumn)) { //排除pkColumn
				if(!CollectionUtils.isEmpty(updateModelColumns)) { //指定了更新字段?
					for(String columnName : updateModelColumns) {
						if(column.getActualColumnName().toLowerCase().equals(columnName.toLowerCase())) {
							updateColumns.add(column);
							break;
						}
					}
				} else { //不指定更新字段，则全字段更新
					updateColumns.add(column);
				}
			}
		}
		
		String dynamicUpdateColumnStr = introspectedTable.getTableConfiguration().getProperty("dynamicUpdateColumn");
		boolean dynamicUpdateColumn = true;
		if("false".equals(dynamicUpdateColumnStr)) {
			dynamicUpdateColumn = false;
		}
		
		return createUpdateModelByIdElement(introspectedTable, "updateModelById", updateColumns, dynamicUpdateColumn);
	}
	
	public XmlElement createDynamicUpdateModelByIdElement(IntrospectedTable introspectedTable) {
		IntrospectedColumn pkColumn = getPrimaryKeyColumn(introspectedTable, 0);
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		List<IntrospectedColumn> updateColumns = new ArrayList<IntrospectedColumn>();
		for(IntrospectedColumn column : allColumns) {
			if(!column.equals(pkColumn)) { //排除pkColumn
				updateColumns.add(column);
			}
		}
		
		return createUpdateModelByIdElement(introspectedTable, "dynamicUpdateModelById", updateColumns, true);
	}
	
	protected XmlElement createUpdateModelByIdElement(IntrospectedTable introspectedTable, String statementId, List<IntrospectedColumn> updateColumns, boolean dynamicUpdateColumn) {
		IntrospectedColumn pkColumn = getPrimaryKeyColumn(introspectedTable, 0);
		
		CustomXmlElement element = new CustomXmlElement("update");
		element.addAttribute(new Attribute("id", statementId));
		element.addAttribute(new Attribute("parameterType", introspectedTable.getTableConfiguration().getDomainObjectName()));
		element.addAttribute(new Attribute("statementType", "PREPARED"));
		
		IntrospectedColumn updateColumn = null;
		
		StringBuilder sb = new StringBuilder();
		OutputUtilities.javaIndent(sb, 1);
		sb.append("UPDATE " + introspectedTable.getTableConfiguration().getTableName().toLowerCase() + " " + DEFAULT_TABLE_ALIAS_NAME);
		OutputUtilities.newLine(sb);
		OutputUtilities.javaIndent(sb, 1);
		OutputUtilities.xmlIndent(sb, 1);
		sb.append("     SET " + DEFAULT_TABLE_ALIAS_NAME + "." + pkColumn.getActualColumnName().toLowerCase() + " = " + "#{" + pkColumn.getJavaProperty() + ", jdbcType=" + getJdbcTypeName(pkColumn) + "}" );
		OutputUtilities.newLine(sb);
		for(int i = 0, len = updateColumns.size(); i < len; i++) {
			updateColumn = updateColumns.get(i);
			if(dynamicUpdateColumn) {
				OutputUtilities.javaIndent(sb, 3);
				if(!StringUtils.isEmpty(mybatisUtilsClass)) {
					sb.append("<if test=\"@" + mybatisUtilsClass + "@isNotEmpty(" + updateColumn.getJavaProperty() + ")\">");
				} else {
					sb.append("<if test=\"" + updateColumn.getJavaProperty() + " != null\">");
				}
				OutputUtilities.newLine(sb);
			}
			
			OutputUtilities.javaIndent(sb, 1);
			sb.append("           ");
			sb.append(",");
			sb.append(DEFAULT_TABLE_ALIAS_NAME + "." + updateColumn.getActualColumnName().toLowerCase() + " = " + "#{" + updateColumn.getJavaProperty() + ", jdbcType=" + getJdbcTypeName(updateColumn) + "}");
			
			OutputUtilities.newLine(sb);
			
			if(dynamicUpdateColumn) {
				OutputUtilities.javaIndent(sb, 3);
				sb.append("</if>");
				OutputUtilities.newLine(sb);
			}
		}
		sb.append("         WHERE " + DEFAULT_TABLE_ALIAS_NAME + "." + pkColumn.getActualColumnName().toLowerCase() + " = " + "#{" + pkColumn.getJavaProperty() + ", jdbcType=" + getJdbcTypeName(pkColumn) + "}");
		element.addElement(new TextElement(sb.toString()));
		return element;
	}
	
	public XmlElement createDeleteModelByIdElement(IntrospectedTable introspectedTable) {
		IntrospectedColumn pkColumn = getPrimaryKeyColumn(introspectedTable, 0);
		CustomXmlElement element = new CustomXmlElement("delete");
		element.addAttribute(new Attribute("id", "deleteModelById"));
		element.addAttribute(new Attribute("parameterType", pkColumn.getFullyQualifiedJavaType().getFullyQualifiedNameWithoutTypeParameters()));
		element.addAttribute(new Attribute("statementType", "PREPARED"));
		
		StringBuilder sb = new StringBuilder();
		OutputUtilities.javaIndent(sb, 1);
		sb.append("DELETE FROM " + introspectedTable.getTableConfiguration().getTableName().toLowerCase() + " WHERE " + pkColumn.getActualColumnName().toLowerCase() + " = " + "#{" + pkColumn.getJavaProperty() + ", jdbcType=" + getJdbcTypeName(pkColumn) + "}");
		element.addElement(new TextElement(sb.toString()));
		return element;
	}
	
	public XmlElement createSelectModelByIdElement(IntrospectedTable introspectedTable) {
		IntrospectedColumn pkColumn = getPrimaryKeyColumn(introspectedTable, 0);
		CustomXmlElement element = new CustomXmlElement("select");
		element.addAttribute(new Attribute("id", "selectModelById"));
		element.addAttribute(new Attribute("parameterType", pkColumn.getFullyQualifiedJavaType().getFullyQualifiedNameWithoutTypeParameters()));
		element.addAttribute(new Attribute("statementType", "PREPARED"));
		element.addAttribute(new Attribute("resultType", introspectedTable.getTableConfiguration().getDomainObjectName()));
		
		StringBuilder sb = new StringBuilder();
		OutputUtilities.javaIndent(sb, 1);
		sb.append("SELECT ");
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		IntrospectedColumn column = null;
		for(int i = 0, len = allColumns.size(); i < len; i++) {
			column = allColumns.get(i);
			if(i == 0) {
				sb.append("");
			} else {
				sb.append("       ");
				OutputUtilities.javaIndent(sb, 2);
			}
			sb.append(getSelectColumnName(column) + "\t" + column.getJavaProperty());
			if(i != len - 1) {
				sb.append(",");
			}
			OutputUtilities.newLine(sb);
		}
		OutputUtilities.javaIndent(sb, 2);
		sb.append("  FROM " + introspectedTable.getTableConfiguration().getTableName().toLowerCase() + " " + DEFAULT_TABLE_ALIAS_NAME);
		OutputUtilities.newLine(sb);
		OutputUtilities.javaIndent(sb, 2);
		sb.append(" WHERE " + DEFAULT_TABLE_ALIAS_NAME + "." + pkColumn.getActualColumnName().toLowerCase() + " = " + "#{" + pkColumn.getJavaProperty() + ", jdbcType=" + getJdbcTypeName(pkColumn) + "}");
		element.addElement(new TextElement(sb.toString()));
		return element;
	}
	
	public XmlElement createSelectModelByExampleElement(IntrospectedTable introspectedTable) {
		Map<String,WhereConditionOperator> exampleQueryWhereColumnMap =  getExampleQueryWhereColumnMap(introspectedTable);
		
		CustomXmlElement element = new CustomXmlElement("select");
		element.addAttribute(new Attribute("id", "selectModelByExample"));
		element.addAttribute(new Attribute("parameterType", introspectedTable.getTableConfiguration().getDomainObjectName()));
		element.addAttribute(new Attribute("statementType", "PREPARED"));
		element.addAttribute(new Attribute("resultType", introspectedTable.getTableConfiguration().getDomainObjectName()));
		
		StringBuilder sb = new StringBuilder();
		OutputUtilities.javaIndent(sb, 1);
		sb.append("SELECT ");
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		IntrospectedColumn column = null;
		for(int i = 0, len = allColumns.size(); i < len; i++) {
			column = allColumns.get(i);
			if(i == 0) {
				sb.append("");
			} else {
				sb.append("       ");
				OutputUtilities.javaIndent(sb, 2);
			}
			sb.append(getSelectColumnName(column) + "\t" + column.getJavaProperty());
			if(i != len - 1) {
				sb.append(",");
			}
			OutputUtilities.newLine(sb);
		}
		OutputUtilities.javaIndent(sb, 2);
		sb.append("  FROM " + introspectedTable.getTableConfiguration().getTableName().toLowerCase() + " " + DEFAULT_TABLE_ALIAS_NAME);
		OutputUtilities.newLine(sb);
		OutputUtilities.javaIndent(sb, 2);
		sb.append(" WHERE 1=1");
		OutputUtilities.newLine(sb);
		//IF where条件
		boolean allocatedWhereCondition = !exampleQueryWhereColumnMap.isEmpty();
		for(int i = 0, len = allColumns.size(); i < len; i++) {
			column = allColumns.get(i);
			WhereConditionOperator columnOp = exampleQueryWhereColumnMap.get(column.getActualColumnName().toLowerCase());
			if(allocatedWhereCondition && columnOp == null) { //如果指定了where条件列
				continue;
			}
			OutputUtilities.javaIndent(sb, 2);
			if(!StringUtils.isEmpty(mybatisUtilsClass)) {
				sb.append("<if test=\"@" + mybatisUtilsClass + "@isNotEmpty(" + column.getJavaProperty() + ")\">");
			} else {
				sb.append("<if test=\"" + column.getJavaProperty() + " != null\">");
			}
			OutputUtilities.newLine(sb);
			
			OutputUtilities.javaIndent(sb, 1);
			sb.append("       AND ");
			applyExampleWhereCondition(sb, columnOp, column, "");
			OutputUtilities.newLine(sb);
			
			OutputUtilities.javaIndent(sb, 2);
			sb.append("</if>");
			OutputUtilities.newLine(sb);
		}
		element.addElement(new TextElement(sb.toString()));
		return element;
	}
	
	public XmlElement createSelectModelListByIdsElement(IntrospectedTable introspectedTable) {
		IntrospectedColumn pkColumn = getPrimaryKeyColumn(introspectedTable, 0);
		CustomXmlElement element = new CustomXmlElement("select");
		element.addAttribute(new Attribute("id", "selectModelListByIds"));
		element.addAttribute(new Attribute("parameterType", "java.util.List"));
		element.addAttribute(new Attribute("statementType", "PREPARED"));
		element.addAttribute(new Attribute("resultType", introspectedTable.getTableConfiguration().getDomainObjectName()));
		
		StringBuilder sb = new StringBuilder();
		OutputUtilities.javaIndent(sb, 1);
		sb.append("SELECT ");
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		IntrospectedColumn column = null;
		for(int i = 0, len = allColumns.size(); i < len; i++) {
			column = allColumns.get(i);
			if(i == 0) {
				sb.append("");
			} else {
				sb.append("       ");
				OutputUtilities.javaIndent(sb, 2);
			}
			sb.append(getSelectColumnName(column) + "\t" + column.getJavaProperty());
			if(i != len - 1) {
				sb.append(",");
			}
			OutputUtilities.newLine(sb);
		}
		OutputUtilities.javaIndent(sb, 2);
		sb.append("  FROM " + introspectedTable.getTableConfiguration().getTableName().toLowerCase() + " " + DEFAULT_TABLE_ALIAS_NAME);
		OutputUtilities.newLine(sb);
		OutputUtilities.javaIndent(sb, 2);
		sb.append(" WHERE " + DEFAULT_TABLE_ALIAS_NAME + "." + pkColumn.getActualColumnName().toLowerCase() + " in ");
		OutputUtilities.newLine(sb);
		OutputUtilities.javaIndent(sb, 2);
		sb.append("<foreach collection=\"list\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\">");
		OutputUtilities.newLine(sb);
		OutputUtilities.javaIndent(sb, 3);
		sb.append("#{item}");
		OutputUtilities.newLine(sb);
		OutputUtilities.javaIndent(sb, 2);
		sb.append("</foreach>");
		OutputUtilities.newLine(sb);
		OutputUtilities.javaIndent(sb, 2);
		sb.append(" ORDER BY " + DEFAULT_TABLE_ALIAS_NAME + "." + pkColumn.getActualColumnName().toLowerCase() + " ASC");
		element.addElement(new TextElement(sb.toString()));
		return element;
	}
	
	public XmlElement createSelectAllModelListElement(IntrospectedTable introspectedTable) {
		IntrospectedColumn pkColumn = getPrimaryKeyColumn(introspectedTable, 0);
		CustomXmlElement element = new CustomXmlElement("select");
		element.addAttribute(new Attribute("id", "selectAllModelList"));
		element.addAttribute(new Attribute("parameterType", "java.util.Map"));
		element.addAttribute(new Attribute("statementType", "PREPARED"));
		element.addAttribute(new Attribute("resultType", introspectedTable.getTableConfiguration().getDomainObjectName()));
		
		StringBuilder sb = new StringBuilder();
		OutputUtilities.javaIndent(sb, 1);
		sb.append("SELECT ");
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		IntrospectedColumn column = null;
		for(int i = 0, len = allColumns.size(); i < len; i++) {
			column = allColumns.get(i);
			if(i == 0) {
				sb.append("");
			} else {
				sb.append("       ");
				OutputUtilities.javaIndent(sb, 2);
			}
			sb.append(getSelectColumnName(column) + "\t" + column.getJavaProperty());
			if(i != len - 1) {
				sb.append(",");
			}
			OutputUtilities.newLine(sb);
		}
		OutputUtilities.javaIndent(sb, 2);
		sb.append("  FROM " + introspectedTable.getTableConfiguration().getTableName().toLowerCase() + " " + DEFAULT_TABLE_ALIAS_NAME);
		OutputUtilities.newLine(sb);
		OutputUtilities.javaIndent(sb, 2);
		sb.append(" WHERE 1=1");
		OutputUtilities.newLine(sb);
		OutputUtilities.javaIndent(sb, 2);
		sb.append(" ORDER BY " + DEFAULT_TABLE_ALIAS_NAME + "." + pkColumn.getActualColumnName().toLowerCase() + " ASC");
		element.addElement(new TextElement(sb.toString()));
		return element;
	}
	
	public XmlElement createCountAllModelListElement(IntrospectedTable introspectedTable) {
		CustomXmlElement element = new CustomXmlElement("select");
		element.addAttribute(new Attribute("id", "countAllModelList"));
		element.addAttribute(new Attribute("parameterType", "java.util.Map"));
		element.addAttribute(new Attribute("statementType", "PREPARED"));
		element.addAttribute(new Attribute("resultType", "java.lang.Integer"));
		
		StringBuilder sb = new StringBuilder();
		OutputUtilities.javaIndent(sb, 1);
		sb.append("SELECT count(*)");
		OutputUtilities.newLine(sb);
		OutputUtilities.javaIndent(sb, 2);
		sb.append("  FROM " + introspectedTable.getTableConfiguration().getTableName().toLowerCase() + " " + DEFAULT_TABLE_ALIAS_NAME);
		OutputUtilities.newLine(sb);
		OutputUtilities.javaIndent(sb, 2);
		sb.append(" WHERE 1=1");
		element.addElement(new TextElement(sb.toString()));
		return element;
	}
	
	public XmlElement createSelectModelListByExampleElement(IntrospectedTable introspectedTable) {
		return createSelectModelListByExampleElement(introspectedTable, "selectModelListByExample");
	}
	
	public XmlElement createSelectModelPageListByExampleElement(IntrospectedTable introspectedTable) {
		return createSelectModelListByExampleElement(introspectedTable, "selectModelPageListByExample");
	}
	
	protected XmlElement createSelectModelListByExampleElement(IntrospectedTable introspectedTable, String statementId) {
		Map<String,WhereConditionOperator> exampleQueryWhereColumnMap =  getExampleQueryWhereColumnMap(introspectedTable);
		
		CustomXmlElement element = new CustomXmlElement("select");
		element.addAttribute(new Attribute("id", statementId));
		element.addAttribute(new Attribute("parameterType", "java.util.Map"));
		element.addAttribute(new Attribute("statementType", "PREPARED"));
		element.addAttribute(new Attribute("resultType", introspectedTable.getTableConfiguration().getDomainObjectName()));
		
		StringBuilder sb = new StringBuilder();
		OutputUtilities.javaIndent(sb, 1);
		sb.append("SELECT ");
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		IntrospectedColumn column = null;
		for(int i = 0, len = allColumns.size(); i < len; i++) {
			column = allColumns.get(i);
			if(i == 0) {
				sb.append("");
			} else {
				sb.append("       ");
				OutputUtilities.javaIndent(sb, 2);
			}
			sb.append(getSelectColumnName(column) + "\t" + column.getJavaProperty());
			if(i != len - 1) {
				sb.append(",");
			}
			OutputUtilities.newLine(sb);
		}
		OutputUtilities.javaIndent(sb, 2);
		sb.append("  FROM " + introspectedTable.getTableConfiguration().getTableName().toLowerCase() + " " + DEFAULT_TABLE_ALIAS_NAME);
		OutputUtilities.newLine(sb);
		OutputUtilities.javaIndent(sb, 2);
		sb.append(" WHERE 1=1");
		OutputUtilities.newLine(sb);
		//IF where条件
		boolean allocatedWhereCondition = !exampleQueryWhereColumnMap.isEmpty();
		for(int i = 0, len = allColumns.size(); i < len; i++) {
			column = allColumns.get(i);
			WhereConditionOperator columnOp = exampleQueryWhereColumnMap.get(column.getActualColumnName().toLowerCase());
			if(allocatedWhereCondition && columnOp == null) { //如果指定了where条件列
				continue;
			}
			OutputUtilities.javaIndent(sb, 2);
			if(!StringUtils.isEmpty(mybatisUtilsClass)) {
				sb.append("<if test=\"example != null and @" + mybatisUtilsClass + "@isNotEmpty(example." + column.getJavaProperty() + ")\">");
			} else {
				sb.append("<if test=\"example != null and example." + column.getJavaProperty() + " != null\">");
			}
			OutputUtilities.newLine(sb);
			
			OutputUtilities.javaIndent(sb, 1);
			sb.append("       AND ");
			applyExampleWhereCondition(sb, columnOp, column, "example.");
			OutputUtilities.newLine(sb);
			
			OutputUtilities.javaIndent(sb, 2);
			sb.append("</if>");
			OutputUtilities.newLine(sb);
		}
		//IF order by条件
		OutputUtilities.javaIndent(sb, 2);
		if(!StringUtils.isEmpty(mybatisUtilsClass)) {
			sb.append("<if test=\"sort != null and @" + mybatisUtilsClass + "@isNotEmpty(sort.orders)\">");
		} else {
			sb.append("<if test=\"sort != null and sort.orders != null\">");
		}
		OutputUtilities.newLine(sb);
		
		OutputUtilities.javaIndent(sb, 1);
		sb.append("     ORDER BY ");
		
		sb.append("<foreach collection=\"sort.orders\" index=\"index\" item=\"item\" open=\"\" separator=\",\" close=\"\">");
		sb.append("#{item.property} #{item.direction}");
		sb.append("</foreach>");
		
		OutputUtilities.newLine(sb);
		
		OutputUtilities.javaIndent(sb, 2);
		sb.append("</if>");
		element.addElement(new TextElement(sb.toString()));
		return element;
	}
	
	public XmlElement createCountModelPageListByExampleElement(IntrospectedTable introspectedTable) {
		Map<String,WhereConditionOperator> exampleQueryWhereColumnMap =  getExampleQueryWhereColumnMap(introspectedTable);
		
		CustomXmlElement element = new CustomXmlElement("select");
		element.addAttribute(new Attribute("id", "countModelPageListByExample"));
		element.addAttribute(new Attribute("parameterType", "java.util.Map"));
		element.addAttribute(new Attribute("statementType", "PREPARED"));
		element.addAttribute(new Attribute("resultType", "java.lang.Integer"));
		
		StringBuilder sb = new StringBuilder();
		OutputUtilities.javaIndent(sb, 1);
		sb.append("SELECT count(*)");
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		IntrospectedColumn column = null;
		OutputUtilities.newLine(sb);
		OutputUtilities.javaIndent(sb, 2);
		sb.append("  FROM " + introspectedTable.getTableConfiguration().getTableName().toLowerCase() + " " + DEFAULT_TABLE_ALIAS_NAME);
		OutputUtilities.newLine(sb);
		OutputUtilities.javaIndent(sb, 2);
		sb.append(" WHERE 1=1");
		//IF where条件
		boolean allocatedWhereCondition = !exampleQueryWhereColumnMap.isEmpty();
		for(int i = 0, len = allColumns.size(); i < len; i++) {
			column = allColumns.get(i);
			WhereConditionOperator columnOp = exampleQueryWhereColumnMap.get(column.getActualColumnName().toLowerCase());
			if(allocatedWhereCondition && columnOp == null) { //如果指定了where条件列
				continue;
			}
			OutputUtilities.newLine(sb);
			OutputUtilities.javaIndent(sb, 2);
			if(!StringUtils.isEmpty(mybatisUtilsClass)) {
				sb.append("<if test=\"example != null and @" + mybatisUtilsClass + "@isNotEmpty(example." + column.getJavaProperty() + ")\">");
			} else {
				sb.append("<if test=\"example != null and example." + column.getJavaProperty() + " != null\">");
			}
			OutputUtilities.newLine(sb);
			
			OutputUtilities.javaIndent(sb, 1);
			sb.append("       AND ");
			applyExampleWhereCondition(sb, columnOp, column, "example.");
			OutputUtilities.newLine(sb);
			
			OutputUtilities.javaIndent(sb, 2);
			sb.append("</if>");
		}
		element.addElement(new TextElement(sb.toString()));
		return element;
	}
	
	/**
	 * 
	 * @param sql
	 * @param op
	 * @param column
	 * @param paramDomain - 参数域例如：example.
	 */
	protected void applyExampleWhereCondition(StringBuilder sql, WhereConditionOperator op, IntrospectedColumn column, String paramDomain) {
		if(WhereConditionOperator.LIKE.equals(op)) {
			sql.append(DEFAULT_TABLE_ALIAS_NAME + "." + column.getActualColumnName().toLowerCase() + " like CONCAT('%', " + "#{" + paramDomain + column.getJavaProperty() + ", jdbcType=" + getJdbcTypeName(column) + "}, '%')");
		} else {
			sql.append(DEFAULT_TABLE_ALIAS_NAME + "." + column.getActualColumnName().toLowerCase() + " = " + "#{" + paramDomain + column.getJavaProperty() + ", jdbcType=" + getJdbcTypeName(column) + "}");
		}
	}
	
	protected Map<String,WhereConditionOperator> getExampleQueryWhereColumnMap(IntrospectedTable introspectedTable) {
		Map<String,WhereConditionOperator> exampleQueryWhereColumnMap = new LinkedHashMap<String,WhereConditionOperator>();
		String exampleQueryWhereColumns = introspectedTable.getTableConfiguration().getProperty("exampleQueryWhereColumns");
		if(!StringUtils.isEmpty(exampleQueryWhereColumns)) {
			Map<String,String> exampleQueryWhereColumnMap1 = JsonUtils.json2Object(exampleQueryWhereColumns, new TypeReference<Map<String,String>>(){});
			if(exampleQueryWhereColumnMap1 != null) {
				String value = null;
				for(Map.Entry<String,String> entry : exampleQueryWhereColumnMap1.entrySet()) {
					value = entry.getValue();
					if(value != null) {
						WhereConditionOperator op = WhereConditionOperator.getOperator(value.toLowerCase());
						if(op != null) {
							exampleQueryWhereColumnMap.put(entry.getKey().toLowerCase(), op);
						}
					}
				}
			}
		}
		return exampleQueryWhereColumnMap;
	}
	
	protected String buildBaseColumnList4Select(IntrospectedTable introspectedTable) {
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		StringBuilder sb = new StringBuilder();
		IntrospectedColumn column = null;
		for(int i = 0, len = allColumns.size(); i < len; i++) {
			column = allColumns.get(i);
			if(i == 0) {
				OutputUtilities.javaIndent(sb, 1);
			} else {
				OutputUtilities.javaIndent(sb, 2);
			}
			sb.append(getSelectColumnName(column) + "\t" + column.getJavaProperty());
			if(i != len - 1) {
				sb.append(",");
				OutputUtilities.newLine(sb);
			}
		}
		return sb.toString();
	}
	
	protected IntrospectedColumn getPrimaryKeyColumn(IntrospectedTable introspectedTable, int index) {
		return introspectedTable.getPrimaryKeyColumns().get(index);
	}
	
	protected String getSelectColumnName(IntrospectedColumn column) {
		String selectColumnString = DEFAULT_TABLE_ALIAS_NAME + "." + column.getActualColumnName().toLowerCase();
		if(Types.TIMESTAMP == column.getJdbcType()) {
			return "DATE_FORMAT(" + selectColumnString + ", '%Y-%m-%d %T')"; //mysql
		} else if (Types.DATE == column.getJdbcType()) {
			return "DATE_FORMAT(" + selectColumnString + ", '%Y-%m-%d')"; //mysql
		}
		return selectColumnString;
	}
	
	protected String getJdbcTypeName(IntrospectedColumn column) {
		String jdbcTypeName = column.getJdbcTypeName();
		if(String.class.getName().equals(column.getFullyQualifiedJavaType().getFullyQualifiedNameWithoutTypeParameters()) 
				&& (Types.DATE == column.getJdbcType() || Types.TIMESTAMP == column.getJdbcType())) {
			jdbcTypeName = "VARCHAR";
		}
		return jdbcTypeName;
	}

	@Override
	public boolean sqlMapBlobColumnListElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean sqlMapSelectAllElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return false;
	}

	protected void makeSerializable(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		String importedType = serializable;
		String superInterface = serializable;
		Class<?> clazz = ClassUtils.forName(superInterface);
		if(clazz.equals(BaseModel.class)) {
			superInterface = clazz.getSimpleName() + "<" + topLevelClass.getType().getShortName() + ">";
		}
		topLevelClass.addImportedType(new FullyQualifiedJavaType(importedType));
		topLevelClass.addSuperInterface(new FullyQualifiedJavaType(superInterface));
		
		Field field = new Field();
		field.setFinal(true);
		field.setInitializationString("1L");
		field.setName("serialVersionUID");
		field.setStatic(true);
		field.setType(new FullyQualifiedJavaType("long"));
		field.setVisibility(JavaVisibility.PRIVATE);
		field.addJavaDocLine(" ");
		context.getCommentGenerator().addFieldComment(field, introspectedTable);
		topLevelClass.getFields().add(0, field);
	}

	protected IntrospectedColumn getIntrospectedColumnByName(IntrospectedTable introspectedTable, String fieldName) {
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		if(allColumns != null) {
			for(IntrospectedColumn column : allColumns) {
				if(column.getJavaProperty().equals(fieldName)) {
					return column;
				}
			}
		}
		return null;
	}
	
}
