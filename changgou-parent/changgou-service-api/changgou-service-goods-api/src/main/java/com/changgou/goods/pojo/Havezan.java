package com.changgou.goods.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/****
 * @Author:
 * @Description:Havezan构建
 * @Date 2019/6/14 19:13
 *****/
@Table(name="tb_havezan")
public class Havezan implements Serializable{

	@Id
    @Column(name = "username")
	private String username;//用户账号名

    @Column(name = "cmt_id")
	private Integer cmtId;//评价id



	//get方法
	public String getUsername() {
		return username;
	}

	//set方法
	public void setUsername(String username) {
		this.username = username;
	}
	//get方法
	public Integer getCmtId() {
		return cmtId;
	}

	//set方法
	public void setCmtId(Integer cmtId) {
		this.cmtId = cmtId;
	}


}
