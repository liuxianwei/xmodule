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

@Service("${serviceBeanName}")
public class ${serviceImplClassName} implements ${serviceClassName} {

	@Autowired
	private ${modelClassName}Mapper ${serviceClassNameLower}Mapper;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void create${modelClassName}(${modelClassName} parameter) {
		Assert.notNull(parameter, "参数不能为空");
		${serviceClassNameLower}Mapper.insertModel(parameter);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void update${modelClassName}(${modelClassName} parameter) {
		Assert.notNull(parameter, "参数不能为空");
		${serviceClassNameLower}Mapper.updateModelById(parameter);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void delete${modelClassName}ById(${modelIdClassName} id) {
		Assert.notNull(id, "id不能为空");
		${serviceClassNameLower}Mapper.deleteModelById(id);
	}

	@Override
	public ${modelClassName} get${modelClassName}ById(${modelIdClassName} id) {
		return ModelDecodeUtils.decodeModel(${serviceClassNameLower}Mapper.selectModelById(id));
	}

	@Override
	public List<${modelClassName}> get${modelClassName}ListByPage(${modelClassName} condition, Page page, Sort sort) {
		List<${modelClassName}> dataList = ModelDecodeUtils.decodeModel(${serviceClassNameLower}Mapper.selectModelPageListByExample(condition, sort, new RowBounds(page.getOffset(), page.getLimit())));
    	page.setTotalRowCount(${serviceClassNameLower}Mapper.countModelPageListByExample(condition));
		return dataList;
	}

	@Override
	public List<${modelClassName}> getAll${modelClassName}List() {
		return ModelDecodeUtils.decodeModel(${serviceClassNameLower}Mapper.selectAllModelList());
	}

}