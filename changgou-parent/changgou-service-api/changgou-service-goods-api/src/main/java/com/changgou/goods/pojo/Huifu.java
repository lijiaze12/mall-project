package com.changgou.goods.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/****
 * @Author:
 * @Description:Huifu构建
 * @Date 2019/6/14 19:13
 *****/
@Table(name="tb_huifu")
public class Huifu implements Serializable{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Integer id;//主键id

    @Column(name = "huifu_msg")
	private String huifuMsg;//回复内容

    @Column(name = "username")
	private String username;//用户账号名

    @Column(name = "cmt_id")
	private Integer cmtId;//评价id

    @Column(name = "is_show")
	private String isShow;//0:显示 1:隐藏

    @Column(name = "add_time")
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	private Date addTime;//记录生成时间

    @Column(name = "upd_time")
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	private Date updTime;//记录更新时间



	//get方法
	public Integer getId() {
		return id;
	}

	//set方法
	public void setId(Integer id) {
		this.id = id;
	}
	//get方法
	public String getHuifuMsg() {
		return huifuMsg;
	}

	//set方法
	public void setHuifuMsg(String huifuMsg) {
		this.huifuMsg = huifuMsg;
	}
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
	//get方法
	public String getIsShow() {
		return isShow;
	}

	//set方法
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	//get方法
	public Date getAddTime() {
		return addTime;
	}

	//set方法
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	//get方法
	public Date getUpdTime() {
		return updTime;
	}

	//set方法
	public void setUpdTime(Date updTime) {
		this.updTime = updTime;
	}


	@Override
	public String toString() {
		return "Huifu{" +
				"id=" + id +
				", huifuMsg='" + huifuMsg + '\'' +
				", username='" + username + '\'' +
				", cmtId=" + cmtId +
				", isShow='" + isShow + '\'' +
				", addTime=" + addTime +
				", updTime=" + updTime +
				'}';
	}
}
