package exercises.creditcardfee;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.AddPayment;
import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.PaymentDraft;
import io.sphere.sdk.payments.PaymentDraftBuilder;
import io.sphere.sdk.payments.commands.PaymentCreateCommand;
import io.sphere.sdk.payments.commands.PaymentDeleteCommand;
import io.sphere.sdk.utils.MoneyImpl;
import org.junit.Test;
import utils.WithSphereClient;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.function.Function;

import static com.commercetools.sunrise.it.TestFixtures.withCart;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class CreditCardFeeControllerComponentIntegrationTest extends WithSphereClient {

    private static final CurrencyUnit CURRENCY = Monetary.getCurrency("EUR");

    @Test
    public void checkPaymentsAreExpanded() throws Exception {
//        final PaymentDraft paymentDraft = PaymentDraftBuilder.of(MoneyImpl.ofCents(540, CURRENCY)).build();
//        withPayment(sphereClient, paymentDraft, payment -> {
//            withCart(sphereClient, CartDraft.of(CURRENCY), cart -> {
//                final CartUpdateCommand baseCommand = CartUpdateCommand.of(cart, AddPayment.of(payment));
//                final CartUpdateCommand commandAfterComponent = component().onCartUpdateCommand(baseCommand);
//                final Cart cartWithPayment = sphereClient.executeBlocking(commandAfterComponent);
//                assertThat(cartWithPayment.getPaymentInfo().getPayments())
//                        .extracting(Reference::getObj)
//                        .isNotNull();
//                return cartWithPayment;
//            });
//            return payment;
//        });
    }

    private CreditCardFeeControllerComponent component() {
        return app.injector().instanceOf(CreditCardFeeControllerComponent.class);
    }

    private static void withPayment(final BlockingSphereClient client, final PaymentDraft paymentDraft, final Function<Payment, Payment> test) {
        final Payment payment = client.executeBlocking(PaymentCreateCommand.of(paymentDraft));
        final Payment paymentAfterTest = test.apply(payment);
        client.executeBlocking(PaymentDeleteCommand.of(paymentAfterTest));
    }
}