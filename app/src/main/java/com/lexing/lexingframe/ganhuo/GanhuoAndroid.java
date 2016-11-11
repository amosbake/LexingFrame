package com.lexing.lexingframe.ganhuo;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Author: mopel
 * Date : 2016/10/9
 */
public class GanhuoAndroid {
    public GanhuoAndroid() {
    }

    public GanhuoAndroid(String id, Date createDate, Date publishDate, String desc, List<String> imgs, String source, String type, String url, String author, boolean used) {
        this.id = id;
        this.createDate = createDate;
        this.publishDate = publishDate;
        this.desc = desc;
        this.imgs = imgs;
        this.source = source;
        this.type = type;
        this.url = url;
        this.author = author;
        this.used = used;
    }

    @SerializedName("_id")
    String id;
    @SerializedName("createdAt")
    Date createDate;
    @SerializedName("publishedAt")
    Date publishDate;
    @SerializedName("desc")
    String desc;
    @SerializedName("images")
    List<String> imgs;
    @SerializedName("source")
    String source;
    @SerializedName("type")
    String type;
    @SerializedName("url")
    String url;
    @SerializedName("who")
    String author;
    @SerializedName("used")
    boolean used;

    @Override
    public String toString() {
        return "GanhuoAndroid{" +
            "id='" + id + '\'' +
            ", createDate=" + createDate +
            ", publishDate=" + publishDate +
            ", desc='" + desc + '\'' +
            ", imgs=" + imgs +
            ", source='" + source + '\'' +
            ", type='" + type + '\'' +
            ", url='" + url + '\'' +
            ", author='" + author + '\'' +
            ", used=" + used +
            '}';
    }
}
