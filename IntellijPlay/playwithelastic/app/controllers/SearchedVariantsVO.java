package controllers;

public class SearchedVariantsVO{

    private Long id;
    private String title;
    private String price;
    private String sku;
    private String barcode;
    private Long inventoryQuantity;
    private Long scannedCount;
    private String metaField;
    private String compareAtPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Long getInventoryQuantity() {
        return inventoryQuantity;
    }

    public void setInventoryQuantity(Long inventoryQuantity) {
        this.inventoryQuantity = inventoryQuantity;
    }

    public Long getScannedCount() {
        return scannedCount;
    }

    public void setScannedCount(Long scannedCount) {
        this.scannedCount = scannedCount;
    }

    public String getMetaField() {
        return metaField;
    }

    public void setMetaField(String metaField) {
        this.metaField = metaField;
    }

    public String getCompareAtPrice() {
        return compareAtPrice;
    }

    public void setCompareAtPrice(String compareAtPrice) {
        this.compareAtPrice = compareAtPrice;
    }

    @Override
    public String toString() {
        return "SearchedVariantsVO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", sku='" + sku + '\'' +
                ", barcode='" + barcode + '\'' +
                ", inventoryQuantity=" + inventoryQuantity +
                ", scannedCount=" + scannedCount +
                ", metaField='" + metaField + '\'' +
                ", compareAtPrice='" + compareAtPrice + '\'' +
                '}';
    }
}

