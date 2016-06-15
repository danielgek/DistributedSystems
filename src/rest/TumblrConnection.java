package rest;

import com.github.scribejava.apis.TumblrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuthService;
import models.User;

/**
 * Created by danielgek on 02/01/16.
 */
public class TumblrConnection {
    private static final String API_APP_KEY = "72bZRYrRpFSVWvq0T8pyP7daDz8rdmMamTovQyakvHcKEhHLyB";
    private static final String API_APP_SECRET = "6tDVLgNsYNbicOzn1gaMPcoIId7FNLNQFOhaJzamtUamha1dKx";

    private OAuthService service;
    private Token requestToken;
    private Token accessToken;

    private User user;

    public TumblrConnection() {

        service = new ServiceBuilder()
                .provider(TumblrApi.class)
                .apiKey(API_APP_KEY)
                .apiSecret(API_APP_SECRET)
                .callback("http://127.0.0.1:8080/tumblrResult")
                .build();
    }

    public String getAuthorizationUrl(){
        requestToken = service.getRequestToken();
        String url = service.getAuthorizationUrl(requestToken);
        return url;
    }

    public User getUser() {
        return user;
    }

    public Token getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(Token requestToken) {
        this.requestToken = requestToken;
    }

    public Token getAccessToken(Verifier verifier){
        return service.getAccessToken(requestToken,verifier);
    }
    public void getUserInfo(){



    }

    public void setUser(User user) {
        this.user = user;
    }
}
