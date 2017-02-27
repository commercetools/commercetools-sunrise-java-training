package b2bcustomer;

import com.commercetools.sunrise.framework.components.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.requests.CustomerQueryHook;
import io.sphere.sdk.customers.queries.CustomerQuery;

/**
 * In this exercise we are going
 */
public class B2BCustomerControllerComponent implements ControllerComponent, CustomerQueryHook {

    @Override
    public CustomerQuery onCustomerQuery(final CustomerQuery customerQuery) {
        return customerQuery.plusExpansionPaths(customer -> customer.customerGroup());
    }
}