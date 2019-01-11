package com.jidn.web.model;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2019/1/10 15:31
 * @Description:
 */
public class News {

    public String newTitle;

    public String newIntroduction;

    public String newImg;

    public String newHref;

    public String newType;

    public String getNewIntroduction() {
        return newIntroduction;
    }

    public void setNewIntroduction(String newIntroduction) {
        this.newIntroduction = newIntroduction;
    }

    public String getNewTitle() {
        return newTitle;
    }

    public void setNewTitle(String newTitle) {
        this.newTitle = newTitle;
    }

    public String getNewImg() {
        return newImg;
    }

    public void setNewImg(String newImg) {
        this.newImg = newImg;
    }

    public String getNewHref() {
        return newHref;
    }

    public void setNewHref(String newHref) {
        this.newHref = newHref;
    }

    public String getNewType() {
        return newType;
    }

    public void setNewType(String newType) {
        this.newType = newType;
    }
}
