package minesim;

public class StockInfo {
    private int stock = 0;
    private String name;

    public StockInfo(String name, int stock) {
        this.name = name;
        this.stock = stock;
    }

    /**
     * Get name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get stock
     * @return stock
     */
    public int getStock() {
        return stock;
    }

}
