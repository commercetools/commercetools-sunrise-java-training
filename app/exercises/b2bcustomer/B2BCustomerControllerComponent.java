package exercises.b2bcustomer;

import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.customers.queries.CustomerQuery;

/**
 * In this exercise we are going to learn how to modify a CTP request before it has been executed.
 * We are going to display the {@link CustomerGroup} in "my personal details" page and for that we need to expand the reference.
 * Notice that in order to assign some customer group to our customers, we have enabled the possibility to register a B2B customer
 * via the sign up form by checking the option "Do you represent a company?"
 *
 * Step 1: Register this component to {@link controllers.myaccount.MyPersonalDetailsController}
 * Step 2: Implement the missing hook
 *   Hook: For every {@link CustomerQuery} to be executed, expand the reference {@code customerGroup}
 *
 * How to check:
 * - Go to sign up page and create a new account, checking "Do you represent a company?"
 * - On the next page, you should read "Welcome back {your name} (Group: B2B)"
 */
public class B2BCustomerControllerComponent implements ControllerComponent {

}