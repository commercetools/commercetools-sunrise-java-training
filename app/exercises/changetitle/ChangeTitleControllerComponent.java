package exercises.changetitle;


import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import com.commercetools.sunrise.framework.viewmodels.PageData;

/**
 * In this exercise we are going to learn how to modify a mutable instance such as {@link PageData} via hooks.
 * We are just going to change the title of our shop (or anything you prefer) to something different.
 * For example, you can try changing the title (found inside {@code header}) to "My Shop".
 *
 * Step 1: Register this on your favorite controller (for example {@link controllers.productcatalog.ProductDetailController})
 * Step 2: Implement the missing hook
 *   Hook: When the {@link PageData} is ready for rendering the template, change its title to "My Shop"
 */
public class ChangeTitleControllerComponent implements ControllerComponent {

}