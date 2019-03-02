package ${packageName};

<#list jdkImports as ipm>
import ${ipm};
</#list>

<#list thirdImports as ipm>
import ${ipm};
</#list>

<#list projectImports as ipm>
import ${ipm};
</#list>

/**
 * ${modelName}服务
 * 
 * @author 	${author}
 * @date	${date}
 */
public interface ${serviceClassName} {

	/**
	 * 创建${modelName}
	 * @param parameter
	 */
	public void create${modelClassName}(${modelClassName} parameter);
	
	/**
	 * 根据ID更新${modelName}
	 * @param parameter
	 */
	public void update${modelClassName}(${modelClassName} parameter);
	
	/**
	 * 根据ID删除${modelName}
	 * @param id
	 */
	public void delete${modelClassName}ById(${modelIdClassName} id);
	
	/**
	 * 根据ID获取${modelName}
	 * @param id
	 * @return
	 */
	public ${modelClassName} get${modelClassName}ById(${modelIdClassName} id);
	
	/**
	 * 根据条件查询${modelName}列表(排序、分页)
	 * @param condition		- 查询条件
	 * @param page			- 分页参数
	 * @param sort			- 排序参数
	 * @return
	 */
	public List<${modelClassName}> get${modelClassName}ListByPage(${modelClassName} condition, Page page, Sort sort);
	
	/**
	 * 获取所有${modelName}列表
	 * @return
	 */
	public List<${modelClassName}> getAll${modelClassName}List();
	
}