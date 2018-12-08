package com.colossus.common.eclipselink.domain;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 统一定义id的entity基类.
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略.
 * @author chendilin
 */
//JPA 基类的标识
@MappedSuperclass
public abstract class IdEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected String id;
	//创建时间
	protected Date createTime;
	//更新时间
	protected Date updateTime;

	@Id
    @Column(name = "id",length=32)
//	@UuidGenerator(name = "system-uuid")
	@GeneratedValue(generator="system-uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		if(StringUtils.isEmpty(id)){
			id = null;
		}
		this.id = id;
	}
	
	@Column(name="create_time",insertable=true,updatable=false)
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name="update_time",insertable=true,updatable = true)
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@PreUpdate
	@PrePersist
	public void updateTime(){
		if(this.createTime==null){
			this.createTime = new Date();
		}
		updateTime = new Date();
	}
}