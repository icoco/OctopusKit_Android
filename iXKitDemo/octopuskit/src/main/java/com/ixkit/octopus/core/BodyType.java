package com.ixkit.octopus.core;

/**
 * Created by icoco on 14/01/2019.
 */

public enum BodyType {
     json("json"),form("form"),raw("raw"), binary("binary"),none("none");

    BodyType(String name){

    }

    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
