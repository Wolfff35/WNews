package com.wolff.wnews.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wolff on 07.07.2017.
 */

public class WChannel implements Serializable{
    private static final long serialVersionUID = 1053051468057804396L;
    private long id;
    private long idGroup;
    private String name;
    private String link;
    private String title;
    private String description;
    private Date pubDate;

    private String language;
    private String image;
    private String rating;

    private long menu_items_all;
    private long menu_items_read;


    public long getMenu_items_all() {
        return menu_items_all;
    }

    public void setMenu_items_all(long menu_items_all) {
        this.menu_items_all = menu_items_all;
    }

    public long getMenu_items_read() {
        return menu_items_read;
    }

    public void setMenu_items_read(long menu_items_unread) {
        this.menu_items_read = menu_items_unread;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(long idGroup) {
        this.idGroup = idGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

     public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

     public void setImage(String image) {
        this.image = image;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


    /*
    language        – Язык канала.
    copyright       – Авторские права на канал.
    managingEditor  – Адрес электронной почты редактора данного канала.
    webMaster       – Адрес электронной почты администратора сайта, на котором расположен канал.
    pubDate         – Дата публикации содержания в канале.
    lastBuildDate   – Дата последнего изменения содержания в канале.
    category        – Позволяет добавлять одну или несколько категорий, к которым принадлежит канал.
    generator       – Программа-генератор, которая создала канал.
    docs            – Ссылки на документацию в формате RSS ленты.
    cloud           – Обеспечивает процесс регистрации в «облако», которое будет использоваться для уведомления об обновлениях.
    ttl             – Время жизни канала в кэше в минутах.
    image           – Файл изображения, которое будет отображаться в канале.
    rating          – PICS рейтинга канала.
    textInput       – Текстовое поле ввода, которое позволяет пользователям реагировать на канал.
    skipHours       – Сообщает агрегаторам (программам читающим RSS-ленты), в какое время мы их не хотим видеть.
    skipDays        – Сообщает агрегаторам, в какие дни они не должны нас беспокоить.
 */
}


