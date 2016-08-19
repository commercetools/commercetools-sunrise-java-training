package models;

import bulkygoods.BulkyGoodsComponent;
import com.commercetools.sunrise.common.utils.MoneyContext;
import com.commercetools.sunrise.shoppingcart.CartBean;
import com.commercetools.sunrise.shoppingcart.CartBeanFactory;
import io.sphere.sdk.carts.Cart;

public class ShopCartBeanFactory extends CartBeanFactory {
    @Override
    public CartBean create(final Cart cart) {
        final ShopCartBean bean = new ShopCartBean();
        initialize(bean, cart);
        return bean;
    }

    protected void initialize(final ShopCartBean bean, final Cart cart) {
        super.initialize(bean, cart);
        cart.getCustomLineItems().stream()
                .filter(cl -> BulkyGoodsComponent.BULKY_FEE_SLUG.equals(cl.getSlug()))
                .findAny()
                .ifPresent(bulkyGoodCustomLineItem -> {
                    final BulkyGoodsBean bulkyGoodsBean = new BulkyGoodsBean();
                    final MoneyContext moneyContext = MoneyContext.of(cart.getCurrency(), userContext.locale());
                    bulkyGoodsBean.setPrice(moneyContext.formatOrNull(bulkyGoodCustomLineItem.getTotalPrice()));
                    bean.setBulkyGoods(bulkyGoodsBean);
                });
    }
}
