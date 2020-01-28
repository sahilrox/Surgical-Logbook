package com.example.surgicallogbook;

import com.google.firebase.database.Exclude;

public class Upload {
    private String name, url, key;



    public Upload() {
    }

    public Upload(String name, String url) {

        if(name.trim().equals("")) {
            name = "No Name";
        }
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Exclude
    public String getKey() {
        return this.key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
