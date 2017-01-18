package models;

import com.commercetools.sunrise.shoppingcart.CartBean;

public class ShopCartBean extends CartBean {

    private BulkyGoodsBean bulkyGoods;

    public BulkyGoodsBean getBulkyGoods() {
        return bulkyGoods;
    }

    public void setBulkyGoods(final BulkyGoodsBean bulkyGoods) {
        this.bulkyGoods = bulkyGoods;
    }
}
