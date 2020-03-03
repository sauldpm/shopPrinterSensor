package POJO.JSON;
public class Productos {
    private String id;
    private String name;
    private String sku;
    private int qty;
    private double price;
    private double productoTotal;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setProductoTotal(double productoTotal) {
        this.productoTotal = productoTotal;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSku() {
        return sku;
    }

    public int getQty() {
        return qty;
    }

    public double getPrice() {
        return price;
    }

    public double getProductoTotal() {
        return productoTotal;
    }
}
