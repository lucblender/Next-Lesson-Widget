package com.lucblender.lucasbonvin.widgettest;

import java.io.File;

public class PathItem {
    
    private String path;
    private boolean selected;
    
    public PathItem(String path, boolean selected)
    {
        this.path = path;
        this.selected = selected;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getPath() {
        return path;
    }

    public boolean isPathValid() {
        File file = new File(path);
        return file.exists();
    }

    public boolean getSelected(){
        return selected;
    }
}
