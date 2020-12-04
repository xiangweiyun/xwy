package com.xwy.common.base;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Id;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 实体基类
 * 
 * @author xiangwy
 * @date: 2020-12-01 15:42:29
 * @Copyright: Copyright (c) 2006 - 2020
 * @Company: Xwy科技股份有限公司
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseEntity implements Serializable {
	/**
	 *
	 */
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	/** 主键ID. */
	public static final String ID = "id";
	/** 创建人. */
	public static final String CREATE_BY = "createBy";
	/** 创建时间. */
	public static final String CREATE_TIME = "createDate";
	/** 更新时间. */
	public static final String UPDATE_TIME = "updateDate";
	/** 更新人. */
	public static final String UPDATE_BY = "updateBy";
	/** 备注. */
	public static final String REMARKS = "remarks";
	/** 删除标识. */
	public static final String DEL_FLAG = "delFlag";
	/**
	 * 删除标记（0：正常；1：删除；）
	 */
	public static final String DEL_FLAG_NORMAL = "0";
	public static final String DEL_FLAG_DELETE = "1";

	@Id
	@Column(name = "ID")
	@ApiModelProperty(value = "主键")
	@TableId(value = "ID", type = IdType.ID_WORKER)
	private Long id; // 主键Id

	@Column(name = "CREATE_BY", updatable = false)
	@ApiModelProperty(value = "创建人")
	@TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
	private Long createBy; // 创建人

	@Column(name = "CREATE_TIME", updatable = false)
	@ApiModelProperty(value = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@TableField(value = "CREATE_DATE", fill = FieldFill.INSERT)
	private LocalDateTime createDate; // 创建日期

	@Column(name = "UPDATE_TIME")
	@ApiModelProperty(value = "更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@TableField(value = "UPDATE_DATE", fill = FieldFill.INSERT_UPDATE, update = "now()")
	private LocalDateTime updateDate; // 更新日期

	@Column(name = "UPDATE_BY")
	@ApiModelProperty(value = "更新人")
	@TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
	private Long updateBy; // 更新人

	@TableField("REMARKS")
	private String remarks; // 备注

	@TableField("DEL_FLAG")
	private String delFlag; // 删除标记（0：正常；1：删除；2：审核）

	public BaseEntity() {
		super();
		this.delFlag = DEL_FLAG_NORMAL;
	}

	public String getDelFlag() {
		if ("".equals(delFlag)) {
			delFlag = DEL_FLAG_NORMAL;
		}
		return delFlag;
	}

}
