package com.rndtechnosoft.foodcart.Item;

public class ItemCart {

    private String MenuId;
    private String MenuName;
    private String MenuQuantity;
    private String MenuPrice;
    private String CatType;

    public ItemCart(String menuId, String menuName, String menuQuantity, String catType, String menuPrice) {
        MenuId = menuId;
        MenuName = menuName;
        MenuQuantity = menuQuantity;
        catType = catType;
        MenuPrice = menuPrice;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    public String getMenuName() {
        return MenuName;
    }

    public String getCatType() {
        return CatType;
    }

    public void setCatType(String catType) {
        CatType = catType;
    }

    public void setMenuName(String menuName) {
        MenuName = menuName;
    }

    public String getMenuQuantity() {
        return MenuQuantity;
    }

    public void setMenuQuantity(String menuQuantity) {
        MenuQuantity = menuQuantity;
    }

    public String getMenuPrice() {
        return MenuPrice;
    }

    public void setMenuPrice(String menuPrice) {
        MenuPrice = menuPrice;
    }
}
