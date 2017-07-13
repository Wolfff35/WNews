package com.wolff.wnews.model;

import java.io.Serializable;

/**
 * Created by wolff on 07.07.2017.
 */

public class WChannelGroup implements Serializable {
    private static final long serialVersionUID = 1054051468057804396L;
    private long id;
    private String name;

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
}
