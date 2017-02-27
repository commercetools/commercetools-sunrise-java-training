package playbasics;

import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;

/**
 * Access http://localhost:9000/playbasics
 */
public class MyController extends Controller {

    private final FormFactory formFactory;

    @Inject
    public MyController(final FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result show1() {
        return ok("Hello World!");
    }

    public Result show2(final String name) {
        return ok("Hello " + name + "!");
    }

    public Result show3(final String name) {
        final String nameSession = session("name");
        session("name", name);
        return ok("Hello " + name + "! Bye " + (nameSession != null ? nameSession : "stranger"));
    }

    public Result show4(final String name) {
        final Http.Cookie nameCookie = request().cookie("name");
        response().setCookie(Http.Cookie.builder("name", name).build());
        return ok("Hello " + name + "! Bye " + (nameCookie != null ? nameCookie.value() : "stranger"));
    }

    public Result show5() {
        final Form<MyFormData> form = formFactory.form(MyFormData.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest("Form had errors: " + form.errorsAsJson());
        } else {
            return ok("Hello " + form.get().getName() + "!");
        }
    }
}
