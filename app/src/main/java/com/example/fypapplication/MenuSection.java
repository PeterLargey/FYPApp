package com.example.fypapplication;

import java.util.List;

public class MenuSection {

    private String sectionTitle;
    private List<MenuItem> menuItems;

    public MenuSection(String sectionTitle, List<MenuItem> menuItems){
        this.sectionTitle = sectionTitle;
        this.menuItems = menuItems;
    }


    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    @Override
    public String toString() {
        return "MenuSection{" +
                "sectionTitle='" + sectionTitle + '\'' +
                ", menuItems=" + menuItems.toString() +
                '}';
    }
}
