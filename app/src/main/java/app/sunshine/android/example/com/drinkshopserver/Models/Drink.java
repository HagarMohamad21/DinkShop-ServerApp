package app.sunshine.android.example.com.drinkshopserver.Models;

public class Drink {
    private String ID;
    private String Name;
    private String Link;
    private String MenuId;
    private String Price;

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public Drink() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }
}
