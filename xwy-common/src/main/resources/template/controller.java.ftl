package ${package.Controller};


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

import com.xwy.common.utils.DataformResult;
import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 /**
 * ${table.comment}控制器.
 * 
 * @author ${author}
 * @date: ${date}
 * @Copyright: Copyright (c) 2020
 * @Company: Xwy科技股份有限公司
 * @Version: V1.0
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("/${table.entityPath}")
@Api(tags = "${table.comment}")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>
	private final Logger logger = LoggerFactory.getLogger(${table.controllerName}.class);
	@Autowired
    private ${table.serviceName} ${table.entityPath}Service;
    
    @ApiOperation(value = "${table.comment}-新增", notes = "${table.comment}-新增")
    @PostMapping("/save")
    public DataformResult<String> save(@RequestBody ${entity} ${table.entityPath}) {
        if (null == ${table.entityPath}.getId()) {
            ${table.entityPath}Service.save(${table.entityPath});
        } else {
            ${table.entityPath}Service.updateById(${table.entityPath});
        }
        return DataformResult.success();
    }

    @ApiOperation(value = "${table.comment}-删除", notes = "${table.comment}-刪除")
    @PostMapping("/remove/{id}")
    public DataformResult<String> removeById(@PathVariable("id") Long id) {
        ${table.entityPath}Service.removeById(id);
        return DataformResult.success();
    }

    @ApiOperation(value = "${table.comment}-根据ID获取", notes = "${table.comment}-根据ID获取")
    @GetMapping("/{id}")
    public DataformResult<${entity}> getById(@PathVariable("id") Long id) {
        ${entity} ${table.entityPath} = ${table.entityPath}Service.getById(id);
        return DataformResult.success(${table.entityPath});
    }
}
</#if>
