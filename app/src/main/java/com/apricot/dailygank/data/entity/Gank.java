package com.apricot.dailygank.data.entity;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Table;

import java.util.Date;

/**
 * Created by Apricot on 2016/5/10.
 */
@Table("ganks")
public class Gank extends BaseEntity{
    @Column("url")
    public String url;
    @Column("type")
    public String type;
    @Column("desc")
    public String desc;
    @Column("who")
    public String who;
    @Column("used")
    public boolean used;
    @Column("createdAt")
    public Date createdAt;
    @Column("updatedAt")
    public Date updatedAt;
    @Column("publishedAt")
    public Date publishedAt;
}
