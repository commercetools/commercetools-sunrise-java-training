package models;

import bulkygoods.BulkyGoodsComponent;
import com.commercetools.sunrise.common.utils.MoneyContext;
import com.commercetools.sunrise.shoppingcart.CartBean;
import com.commercetools.sunrise.shoppingcart.CartBeanFactory;
import io.sphere.sdk.carts.Cart;

import javax.annotation.Nullable;

public class ShopCartLikeBeanFactory extends CartBeanFactory {

    @Override
    public CartBean create(@Nullable final Cart cart) {
        final ShopCartLikeBean bean = new ShopCartLikeBean();
        initialize(bean, cart);
        return bean;
    }

    protected void initialize(final ShopCartLikeBean bean, final Cart cart) {
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
