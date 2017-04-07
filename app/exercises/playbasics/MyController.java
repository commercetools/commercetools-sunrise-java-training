package exercises.playbasics;

import play.Configuration;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Access http://localhost:9000/playbasics/show1 (or show2, show3, show4, show5)
 */
public class MyController extends Controller {

    private final FormFactory formFactory;
    private final String pageTitle;

    @Inject
    public MyController(final FormFactory formFactory, final Configuration configuration) {
        this.formFactory = formFactory;
        this.pageTitle = configuration.getString("playbasics.pageTitle", "Hello you!");
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
        return ok(formWithMessage(flash("message")));
    }

    public Result process5() {
        final Form<MyFormData> form = formFactory.form(MyFormData.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(formWithMessage("Form had errors: " + form.errorsAsJson()));
        } else {
            flash("message", "Hello " + form.get().getName() + "!");
            return redirect(exercises.playbasics.routes.MyController.show5());
        }
    }

    private Html formWithMessage(@Nullable final String message) {
        return new Html(
                "<html>"
                + "  <head><title>" + pageTitle + "</title></head>"
                + "  <body>"
                + "    <p>Type your name</p>"
                + "    <p class=\"message\">" + Optional.ofNullable(message).orElse("") + "</p>"
                + "    <form action=\"" + exercises.playbasics.routes.MyController.process5().url() + "\" method=\"post\">"
                + "      <input name=\"name\" type=\"text\" />"
                + "      <button type=\"submit\">Submit</button>"
                + "    </form>"
                + "  </body>"
                + "</html>");
    }
}
