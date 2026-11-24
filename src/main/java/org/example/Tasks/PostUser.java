package org.example.Tasks;

import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;
import org.example.Models.UserModel;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class PostUser implements Task {

    private final UserModel user;

    public PostUser(UserModel user) { this.user = user; }

    public static Performable withData(UserModel user) {  return instrumented(PostUser.class, user); }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Post.to("/user")
                        .with(requestSpecification -> requestSpecification
                                .contentType(ContentType.JSON)
                                .header("Accept", "application/json")
                                .body(user) // <-- ¡RestAssured convierte el objeto a JSON automáticamente!
                                .log().all()
                        )
        );
        // Guardamos el modelo entero en la memoria
        actor.remember("user", this.user);
    }
}

