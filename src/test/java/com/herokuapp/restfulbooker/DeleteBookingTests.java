package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DeleteBookingTests extends BaseTest {
    @Test
    public void partialUpdateBookingTest() {
        Response responseCreate = createBooking();
        responseCreate.print();

        int bookingid = responseCreate.jsonPath().getInt("bookingid");

        Response responseDelete = RestAssured.given(spec).auth().preemptive().basic("admin", "password123")
                .delete("/booking/" + bookingid);
        responseDelete.print();

        Assert.assertEquals(responseDelete.getStatusCode(), 201, "Status code should be 201");

        Response responseGet = RestAssured.get("/booking/" + bookingid);
        responseGet.print();

        Assert.assertEquals(responseGet.getBody().asString(), "Not Found", "Should be not found");
    }
}
