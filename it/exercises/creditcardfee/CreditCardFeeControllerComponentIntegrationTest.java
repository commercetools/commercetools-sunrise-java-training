package exercises.creditcardfee;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.AddPayment;
import io.sphere.sdk.client.BlockingSphereClient;
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
    public void checkPaymentsAreAdded() throws Exception {
        final PaymentDraft paymentDraft = PaymentDraftBuilder.of(MoneyImpl.ofCents(540, CURRENCY)).build();
        withPayment(sphereClient, paymentDraft, payment -> {
            final CartDraft cartDraft = CartDraft.of(CURRENCY);
            withCart(sphereClient, cartDraft, cart -> {
                assertThat(cart.getPaymentInfo()).isNull();
                final Cart cartWithPayment = sphereClient.executeBlocking(CartUpdateCommand.of(cart, AddPayment.of(payment)));
                assertThat(cartWithPayment.getPaymentInfo()).isNotNull();
                assertThat(cartWithPayment.getPaymentInfo().getPayments().get(0).getId()).isEqualTo(payment.getId());
                return cartWithPayment;
            });
            return payment;
        });
    }

    private static void withPayment(final BlockingSphereClient client, final PaymentDraft paymentDraft, final Function<Payment, Payment> test) {
        final Payment payment = client.executeBlocking(PaymentCreateCommand.of(paymentDraft));
        final Payment paymentAfterTest = test.apply(payment);
        client.executeBlocking(PaymentDeleteCommand.of(paymentAfterTest));
    }
}