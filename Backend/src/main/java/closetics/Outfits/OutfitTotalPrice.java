package closetics.Outfits;

public class OutfitTotalPrice {
    private Outfit outfit;
    private double totalPrice;

    public OutfitTotalPrice(Outfit outfit, double totalPrice) {
        this.outfit = outfit;
        this.totalPrice = totalPrice;
    }

    public Outfit getOutfit() {
        return outfit;
    }

    public void setOutfit(Outfit outfit) {
        this.outfit = outfit;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
