package com.example.filemanager.entity;

import android.provider.BaseColumns;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public final class UserContract {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String pwd;
    @Generated(hash = 1522857799)
    public UserContract(Long id, String name, String pwd) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
    }
    @Generated(hash = 366495567)
    public UserContract() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPwd() {
        return this.pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

}
