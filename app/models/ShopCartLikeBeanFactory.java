package models;

import bulkygoods.BulkyGoodsComponent;
import com.commercetools.sunrise.common.utils.MoneyContext;
import com.commercetools.sunrise.shoppingcart.CartLikeBean;
import com.commercetools.sunrise.shoppingcart.CartLikeBeanFactory;
import io.sphere.sdk.carts.CartLike;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CompletionException;

public class ShopCartLikeBeanFactory extends CartLikeBeanFactory {
    @Override
    public CartLikeBean create(final CartLike<?> cartLike) {
        final ShopCartLikeBean bean = new ShopCartLikeBean();
        try {
            BeanUtils.copyProperties(bean, super.create(cartLike));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw new CompletionException(e);
        }

        cartLike.getCustomLineItems().stream()
                .filter(cl -> BulkyGoodsComponent.BULKY_FEE_SLUG.equals(cl.getSlug()))
        .findAny()
        .ifPresent(bulkyGoodCustomLineItem -> {
            final BulkyGoodsBean bulkyGoodsBean = new BulkyGoodsBean();
            final MoneyContext moneyContext = MoneyContext.of(cartLike.getCurrency(), userContext.locale());
            bulkyGoodsBean.setPrice(moneyContext.formatOrNull(bulkyGoodCustomLineItem.getTotalPrice()));
            bean.setBulkyGoods(bulkyGoodsBean);
        });
        return bean;
    }
}
