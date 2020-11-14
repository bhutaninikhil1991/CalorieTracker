package controllers;

import Service.*;
import calorieapp.HTTPSingleResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import models.User;
import utils.HelperUtils;

import javax.inject.Inject;
import java.util.HashMap;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/auth")
public class AuthenticationController {

    @Inject
    protected final UserService userService;

    /**
     * constructor
     *
     * @param userService
     */
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * register user
     *
     * @param object
     * @return HTTPSingleResponse
     */
    @Post("/register")
    public HTTPSingleResponse registerUser(@Body String object) {
        HTTPSingleResponse response = new HTTPSingleResponse();
        HashMap<String, Object> map = new HashMap<>();
        JsonObject userObject = new JsonParser().parse(object).getAsJsonObject();
        String emailAddress = userObject.get("username").getAsString();
        String password = userObject.get("password").getAsString();
        try {
            if (userService.findUserByEmailAddress(emailAddress) == null) {
                User usr = userService.registerUser(emailAddress, password);
                map.put(String.valueOf(usr.getId()), usr);
                response.success = true;
                response.data = map;
            } else {
                response.success = false;
                response.errorMessage = "user already exists";
            }
        } catch (Exception ex) {
            response.success = false;
            response.errorMessage = "unable to register user";
            HelperUtils.logErrorMessage(response.errorMessage, ex);
        }
        return response;
    }
}
