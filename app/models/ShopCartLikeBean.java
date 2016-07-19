package models;

import com.commercetools.sunrise.shoppingcart.CartLikeBean;

public class ShopCartLikeBean extends CartLikeBean {
    private BulkyGoodsBean bulkyGoods;

    public BulkyGoodsBean getBulkyGoods() {
        return bulkyGoods;
    }

    public void setBulkyGoods(final BulkyGoodsBean bulkyGoods) {
        this.bulkyGoods = bulkyGoods;
    }
}
