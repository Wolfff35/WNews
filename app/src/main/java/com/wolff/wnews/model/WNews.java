package com.wolff.wnews.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wolff on 07.07.2017.
 */

public class WNews implements Serializable {
    private static final long serialVersionUID = 1053151468057804396L;
    private String guid;
    private Date pubDate;
    private String title;
    private String description;
    private String link;
    private String author;
    private String category;
    private String comments;
    private String enclosure;
    private String source;

    private long id;
    private String name;
    private long idChannel;
    private boolean isReaded;

    public long getIdChannel() {
        return idChannel;
    }

    public void setIdChannel(long idChannel) {
        this.idChannel = idChannel;
    }

    public boolean isReaded() {
        return isReaded;
    }

    public void setReaded(boolean readed) {
        isReaded = readed;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(String enclosure) {
        this.enclosure = enclosure;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    /*
    guid        – Уникальный идентификатор элемента item.
    pubDate     – Дата публикации элемента.
    title       – Заголовок элемента. В нашем случае он совпадает с заголовком публикуемой записи в интернет-дневнике.
    description – содержит основные данные элемента. В нашем случае это тело записи в блоге.
    link        – Содержит полный URL адрес до страницы, на которой данный элемент представлен максимально подробно.
    author      – Автор этой записи.
    category    – Позволяет поместить элемент в одну или более категорий.
    comments    — Ссылка на страницу, где можно оставлять комментарии к этой записи.
    enclosure   – Может быть использован для описания медиа объекта, прикрепленного к элементу.
    source      – Ссылка на RSS-канал, откуда был взят этот элемент.
            */

}
