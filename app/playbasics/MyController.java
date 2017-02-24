package playbasics;

import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

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
        final String oldName = session("name");
        session("name", name);
        return ok("Hello " + name + "! Bye " + oldName);
    }

    public Result show4() {
        final Form<MyFormData> form = formFactory.form(MyFormData.class);
        if (form.hasErrors()) {
            return badRequest("Form had errors: " + form.errorsAsJson());
        } else {
            return ok("Hello " + form.get().getName() + "!");
        }
    }
}
