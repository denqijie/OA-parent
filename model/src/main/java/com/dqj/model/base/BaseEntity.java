package com.dqj.model.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO) // IdType.AUTO 主键自动增长
    private Long id;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableLogic // MP封装的逻辑刪除注解
    @TableField("is_deleted")
    private Integer isDeleted;

    @TableField(exist = false) //   exist = false 表示表里面可允许没有对应字段
    private Map<String,Object> param = new HashMap<>();
}
