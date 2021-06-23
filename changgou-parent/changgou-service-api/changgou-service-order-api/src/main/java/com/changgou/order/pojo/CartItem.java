package com.changgou.order.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/****
 * @Author:admin
 * @Description:CartItem构建
 * @Date 2019/6/14 19:13
 *****/
@Table(name="tb_cart_item")
public class CartItem implements Serializable{

	@Id
    @Column(name = "id")
	private String id;//

    @Column(name = "cart_sku_id")
	private Long cartSkuId;//cart

    @Column(name = "username")
	private String username;//用户名

    @Column(name = "num")
	private Integer num;//购买数量

    @Column(name = "create_time")
	private Date createTime;//

    @Column(name = "update_time")
	private Date updateTime;//



	//get方法
	public String getId() {
		return id;
	}

	//set方法
	public void setId(String id) {
		this.id = id;
	}
	//get方法
	public Long getCartSkuId() {
		return cartSkuId;
	}

	//set方法
	public void setCartSkuId(Long cartSkuId) {
		this.cartSkuId = cartSkuId;
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
	public Integer getNum() {
		return num;
	}

	//set方法
	public void setNum(Integer num) {
		this.num = num;
	}
	//get方法
	public Date getCreateTime() {
		return createTime;
	}

	//set方法
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	//get方法
	public Date getUpdateTime() {
		return updateTime;
	}

	//set方法
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


}
