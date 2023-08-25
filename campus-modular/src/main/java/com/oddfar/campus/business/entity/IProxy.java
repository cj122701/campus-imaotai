package com.oddfar.campus.business.entity;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * I茅台商品对象 i_shop
 *
 * @author oddfar
 * @date 2023-07-02
 */
@Data
@TableName("i_proxy")
public class IProxy {
    private static final long serialVersionUID = 1L;

    /**
     * 省份
     */
    @TableId(value = "province_name")
    private String provinceName;

    /**
     * 省份ID
     */
    @TableField(value  ="province_id")
    private String provinceId;


    public IProxy() {
    }

    public IProxy(String provinceName, JSONObject jsonObject) {
        this.provinceName = provinceName;
        this.provinceId = jsonObject.getString("provinceId");

    }



}
