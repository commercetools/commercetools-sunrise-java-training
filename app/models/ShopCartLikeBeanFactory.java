package models;

import bulkygoods.BulkyGoodsComponent;
import com.commercetools.sunrise.common.utils.MoneyContext;
import com.commercetools.sunrise.shoppingcart.CartLikeBean;
import com.commercetools.sunrise.shoppingcart.CartLikeBeanFactory;
import io.sphere.sdk.carts.CartLike;

public class ShopCartLikeBeanFactory extends CartLikeBeanFactory {
    @Override
    public CartLikeBean create(final CartLike<?> cartLike) {
        final ShopCartLikeBean bean = new ShopCartLikeBean();
        initialize(bean, cartLike);
        return bean;
    }

    protected void initialize(final ShopCartLikeBean bean, final CartLike<?> cartLike) {
        super.initialize(bean, cartLike);
        cartLike.getCustomLineItems().stream()
                .filter(cl -> BulkyGoodsComponent.BULKY_FEE_SLUG.equals(cl.getSlug()))
                .findAny()
                .ifPresent(bulkyGoodCustomLineItem -> {
                    final BulkyGoodsBean bulkyGoodsBean = new BulkyGoodsBean();
                    final MoneyContext moneyContext = MoneyContext.of(cartLike.getCurrency(), userContext.locale());
                    bulkyGoodsBean.setPrice(moneyContext.formatOrNull(bulkyGoodCustomLineItem.getTotalPrice()));
                    bean.setBulkyGoods(bulkyGoodsBean);
                });
    }
}
