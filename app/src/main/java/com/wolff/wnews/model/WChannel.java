package com.wolff.wnews.model;

import java.util.Date;

/**
 * Created by wolff on 07.07.2017.
 */

public class WChannel {
    private long id;
    private long idGroup;
    private String name;
    private String link;
    private String title;
    private String description;
    private Date pubDate;

    private String language;
    private String copyright;
    private String managingEditor;
    private String webMaster;
    private String lastBuildDate;
    private String category;
    private String generator;
    private String docs;
    private String cloud;
    private int ttl;
    private String image;
    private String rating;
    private String textInput;
    private String skipHours;
    private String skipDays;

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

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getManagingEditor() {
        return managingEditor;
    }

    public void setManagingEditor(String managingEditor) {
        this.managingEditor = managingEditor;
    }

    public String getWebMaster() {
        return webMaster;
    }

    public void setWebMaster(String webMaster) {
        this.webMaster = webMaster;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public String getDocs() {
        return docs;
    }

    public void setDocs(String docs) {
        this.docs = docs;
    }

    public String getCloud() {
        return cloud;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public String getImage() {
        return image;
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

    public String getTextInput() {
        return textInput;
    }

    public void setTextInput(String textInput) {
        this.textInput = textInput;
    }

    public String getSkipHours() {
        return skipHours;
    }

    public void setSkipHours(String skipHours) {
        this.skipHours = skipHours;
    }

    public String getSkipDays() {
        return skipDays;
    }

    public void setSkipDays(String skipDays) {
        this.skipDays = skipDays;
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


