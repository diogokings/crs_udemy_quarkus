package com.food.register;

import static io.restassured.RestAssured.given;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

import com.github.database.rider.cdi.api.DBRider;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@QuarkusTest
@QuarkusTestResource(RegisterLifecycleManagerTest.class)
@TestSecurity(authorizationEnabled = false)
public class RestaurantResourceTest {

    @Test
    @DataSet("restaurant-find_all-success.yml")
    public void testFindAllRestaurants() {
        String response = given().contentType(ContentType.JSON)
                .when().get("/restaurants")
                .then().statusCode(200)
                .extract().asString();

        Approvals.verifyJson(response);
    }

}