package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class GetBookingTest {

    //Some test fails because this is a public API playground so people mess with the fields.
    @Test
    public void bookingTest() {
        // Get response with booking
        Response response = RestAssured.get("https://restful-booker.herokuapp.com/booking/5");
        response.print();

        // Verify the response is 200
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");

        // Verify all fields
        SoftAssert softAssert = new SoftAssert();
        String actualFirstName = response.jsonPath().getString("firstname");
        softAssert.assertEquals(actualFirstName, "Jim", "First name should be Jim");

        String actualLastName = response.jsonPath().getString("lastname");
        softAssert.assertEquals(actualLastName, "Ericsson", "Last name should be Ericsson");

        int price = response.jsonPath().getInt("totalprice");
        softAssert.assertEquals(price, 362, "Price should be 362");

        boolean depositPaid = response.jsonPath().getBoolean("depositpaid");
        softAssert.assertTrue(depositPaid, "Deposit paid should be true");


        String actualCheckin = response.jsonPath().getString("bookingdates.checkin");
        softAssert.assertEquals(actualCheckin, "2017-12-22", "Checkin is not expected");

        String actualCheckout = response.jsonPath().getString("bookingdates.checkout");
        softAssert.assertEquals(actualCheckout, "2020-05-03", "Checkout is not expected");

        String actualAdittionalNeeds = response.jsonPath().getString("additionalneeds");
        softAssert.assertEquals(actualAdittionalNeeds, "Breakfast", "Breakfast is expected");

        softAssert.assertAll();

    }
}
