package com.changgou.goods.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/****
 * @Author:
 * @Description:Comment构建
 * @Date 2019/6/14 19:13
 *****/
@Table(name="tb_comment")
public class Comment implements Serializable{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Integer id;//主键id

    @Column(name = "cmt_msg")
	private String cmtMsg;//评价内容

    @Column(name = "username")
	private String username;//用户账号名

    @Column(name = "spu_id")
	private String spuId;//商品的id

    @Column(name = "cmt_star")
	private String cmtStar;//评价星级

    @Column(name = "cmt_zannum")
	private Integer cmtZannum;//点赞次数

    @Column(name = "cmt_huifunum")
	private Integer cmtHuifunum;//回复次数

    @Column(name = "images")
	private String images;//商品图片列表

    @Column(name = "is_show")
	private String isShow;//0:显示 1:隐藏

    @Column(name = "add_time")
	private Date addTime;//记录生产时间

    @Column(name = "upd_time")
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
	public String getCmtMsg() {
		return cmtMsg;
	}

	//set方法
	public void setCmtMsg(String cmtMsg) {
		this.cmtMsg = cmtMsg;
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
	public String getSpuId() {
		return spuId;
	}

	//set方法
	public void setSpuId(String spuId) {
		this.spuId = spuId;
	}
	//get方法
	public String getCmtStar() {
		return cmtStar;
	}

	//set方法
	public void setCmtStar(String cmtStar) {
		this.cmtStar = cmtStar;
	}
	//get方法
	public Integer getCmtZannum() {
		return cmtZannum;
	}

	//set方法
	public void setCmtZannum(Integer cmtZannum) {
		this.cmtZannum = cmtZannum;
	}
	//get方法
	public Integer getCmtHuifunum() {
		return cmtHuifunum;
	}

	//set方法
	public void setCmtHuifunum(Integer cmtHuifunum) {
		this.cmtHuifunum = cmtHuifunum;
	}
	//get方法
	public String getImages() {
		return images;
	}

	//set方法
	public void setImages(String images) {
		this.images = images;
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


}
