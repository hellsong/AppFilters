package com.hellsong.appfilters.module;

import java.util.List;

/**
 * Created by weiruyou on 2015/11/27.
 */
public class FilterSection {
    private String mTitle;
    private List<String> mItems;

    public String getTitle() {
        return mTitle;
    }

    public void setmTitle(String title) {
        this.mTitle = title;
    }

    public List<String> getItems() {
        return mItems;
    }

    public void setItems(List<String> items) {
        this.mItems = items;
    }
}
