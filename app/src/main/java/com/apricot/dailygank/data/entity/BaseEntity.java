package com.apricot.dailygank.data.entity;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;

import java.io.Serializable;

/**
 * Created by Apricot on 2016/5/10.
 */
public class BaseEntity implements Serializable{
    @PrimaryKey(PrimaryKey.AssignType.AUTO_INCREMENT)
    @Column("_id")
    public int id;
    
}
