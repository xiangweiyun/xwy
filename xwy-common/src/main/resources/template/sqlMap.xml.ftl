<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
    PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">
    <!-- BaseResultMap和表字段一致，不允许改动 -->
    <resultMap id="BaseResultMap" type="${package.Entity}.${entity}">
    <#list table.fields as field>
        <result column="${field.name?upper_case}" property="${field.propertyName}" />
    </#list>
    </resultMap>

    <!-- BaseResultMap和表字段一致，不允许改动 -->
    <sql id="base_column">
    <#list table.commonFields as field>
        ${alias}${field.name?upper_case},
	</#list>
        ${table.fieldNames}
    </sql>	
</mapper>