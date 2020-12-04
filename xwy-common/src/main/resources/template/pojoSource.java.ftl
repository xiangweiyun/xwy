package ${package.Entity};

<#assign dateFlag = false />
<#list table.fields as field>
    <#if "createBy" != field.propertyName && "createDate" != field.propertyName && "updateBy" != field.propertyName && "updateDate" != field.propertyName && "remarks" != field.propertyName && "delFlag" != field.propertyName>
        <#if field.propertyType == "LocalDateTime">
            <#assign dateFlag = true />
        </#if>
    </#if>
</#list>
import com.xwy.common.base.BaseEntity;
<#if dateFlag>
import java.time.LocalDateTime;
</#if>
import javax.persistence.Column;
<#if swagger2>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>

<#if entityLombokModel>
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
</#if>

/**
 * <p>
 * ${table.comment!}
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if entityLombokModel>
@Data
    <#if superEntityClass??>
@EqualsAndHashCode(callSuper = true)
    <#else>
@EqualsAndHashCode(callSuper = false)
    </#if>
@Accessors(chain = true)
</#if>
<#if table.convert>
@TableName("${table.name}")
</#if>
<#if swagger2>
@ApiModel(value="${entity}对象", description="${table.comment!}")
</#if>
public class ${entity} extends BaseEntity {
    private static final long serialVersionUID = 1L;
    <#if entityColumnConstant>
    <#list table.fields as field>
    <#if "id" != field.propertyName && "createBy" != field.propertyName && "createDate" != field.propertyName && "updateBy" != field.propertyName && "updateDate" != field.propertyName && "remarks" != field.propertyName && "delFlag" != field.propertyName>
    /** ${field.comment}. */
    public static final String ${field.name?upper_case} = "${field.propertyName}";
    </#if>
    </#list>
	</#if>
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
	<#if "id" != field.propertyName && "createBy" != field.propertyName && "createDate" != field.propertyName && "updateBy" != field.propertyName && "updateDate" != field.propertyName && "remarks" != field.propertyName && "delFlag" != field.propertyName>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>

    <#if field.comment!?length gt 0>
    /**
     * ${field.comment}
     */
    @Column(name = "${field.name?upper_case}")
    <#if swagger2>
    @ApiModelProperty(value = "${field.comment}")
    </#if>
    </#if>
    <#-- 普通字段 -->
    <#if field.fill??>
    <#-- -----   存在字段填充设置   ----->
        <#if field.convert>
    @TableField(value = "${field.name}", fill = FieldFill.${field.fill})
        <#else>
    @TableField(fill = FieldFill.${field.fill})
        </#if>
    <#elseif field.convert>
    @TableField("${field.name}")
    </#if>
<#-- 乐观锁注解 -->
    <#if (versionFieldName!"") == field.name>
    @Version
    </#if>
<#-- 逻辑删除注解 -->
    <#if (logicDeleteFieldName!"") == field.name>
    @TableLogic
    </#if>
    private ${field.propertyType} ${field.propertyName};
    </#if>
</#list>
<#------------  END 字段循环遍历  ---------->
}
