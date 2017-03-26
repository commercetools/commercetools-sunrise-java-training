package exercises.extendjvmsdk;

import io.sphere.sdk.client.HttpRequestIntent;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.http.HttpMethod;
import io.sphere.sdk.http.HttpResponse;

import javax.annotation.Nullable;

public class MySphereRequest implements SphereRequest<String> {

    @Nullable
    @Override
    public String deserialize(final HttpResponse httpResponse) {
        if (httpResponse.getResponseBody() != null) {
            return new String(httpResponse.getResponseBody());
        } else {
            return "no content";
        }
    }

    @Override
    public HttpRequestIntent httpRequestIntent() {
        return HttpRequestIntent.of(HttpMethod.GET, "string-endpoint");
    }
}
