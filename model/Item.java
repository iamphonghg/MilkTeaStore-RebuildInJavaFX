package model;

public class Item {
    private Integer noInList;
    private String name;
    private String type;
    private Integer priceM;
    private Integer priceL;
    private String status;
    private Integer promo;

    public Item(Integer noInList, String name, String type, Integer priceM, Integer priceL, String status,
                Integer promo) {
        this.noInList = noInList;
        this.name = name;
        this.type = type;
        this.priceM = priceM;
        this.priceL = priceL;
        this.status = status;
        this.promo = promo;
    }

    public Integer getNoInList() {
        return noInList;
    }
    public void setNoInList(Integer noInList) {
        this.noInList = noInList;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public Integer getPriceM() {
        return priceM;
    }
    public void setPriceM(Integer priceM) {
        this.priceM = priceM;
    }

    public Integer getPriceL() {
        return priceL;
    }
    public void setPriceL(Integer priceL) {
        this.priceL = priceL;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPromo() {
        return promo;
    }
    public void setPromo(Integer promo) {
        this.promo = promo;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", priceM=" + priceM +
                ", priceL=" + priceL +
                ", status='" + status + '\'' +
                ", promo=" + promo +
                '}';
    }
}
