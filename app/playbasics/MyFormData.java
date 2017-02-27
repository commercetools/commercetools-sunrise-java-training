package playbasics;

import play.data.validation.Constraints;

public class MyFormData {

    @Constraints.Required
    private String name;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
