package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class GetBookingTest extends BaseTest {

    //Some test fails because this is a public API playground so people mess with the fields.
    //@Test
    public void bookingTest() {
        // Create booking
        Response responseCreate = createBooking();
        responseCreate.print();

        //Set path parameter
        spec.pathParam("bookingId", responseCreate.jsonPath().getInt("bookingid"));

        // Get response with booking
        Response response = RestAssured.given(spec).get("/booking/{bookingId}");
        response.print();

        // Verify the response is 200
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");

        // Verify all fields
        SoftAssert softAssert = new SoftAssert();
        String actualFirstName = response.jsonPath().getString("firstname");
        softAssert.assertEquals(actualFirstName, "Mina", "First response is not expected");

        String actualLastName = response.jsonPath().getString("lastname");
        softAssert.assertEquals(actualLastName, "Cebarolla", "Last name response is not expected");

        int price = response.jsonPath().getInt("totalprice");
        softAssert.assertEquals(price, 90, "Price should be 90");

        boolean depositPaid = response.jsonPath().getBoolean("depositpaid");
        softAssert.assertFalse(depositPaid, "Deposit paid should be false");


        String actualCheckin = response.jsonPath().getString("bookingdates.checkin");
        softAssert.assertEquals(actualCheckin, "2024-03-25", "Checkin is not expected");

        String actualCheckout = response.jsonPath().getString("bookingdates.checkout");
        softAssert.assertEquals(actualCheckout, "2024-03-27", "Checkout is not expected");

        String actualAdittionalNeeds = response.jsonPath().getString("additionalneeds");
        softAssert.assertEquals(actualAdittionalNeeds, "Breakfast", "Breakfast is expected");

        softAssert.assertAll();

    }

    @Test
    public void getBookingXMLTest() {
        // Create booking
        Response responseCreate = createBooking();
        responseCreate.print();

        //Set path parameter
        spec.pathParam("bookingId", responseCreate.jsonPath().getInt("bookingid"));

        // Get response with booking
        Header xml = new Header("Accept","application/xml");
        spec.header(xml);


        Response response = RestAssured.given(spec).get("/booking/{bookingId}");
        response.print();

        // Verify the response is 200
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");

        // Verify all fields
        SoftAssert softAssert = new SoftAssert();
        String actualFirstName = response.xmlPath().getString("booking.firstname");
        softAssert.assertEquals(actualFirstName, "Mina", "First response is not expected");

        String actualLastName = response.xmlPath().getString("booking.lastname");
        softAssert.assertEquals(actualLastName, "Cebarolla", "Last name response is not expected");

        int price = response.xmlPath().getInt("booking.totalprice");
        softAssert.assertEquals(price, 90, "Price should be 90");

        boolean depositPaid = response.xmlPath().getBoolean("booking.depositpaid");
        softAssert.assertFalse(depositPaid, "Deposit paid should be false");


        String actualCheckin = response.xmlPath().getString("booking.bookingdates.checkin");
        softAssert.assertEquals(actualCheckin, "2024-03-25", "Checkin is not expected");

        String actualCheckout = response.xmlPath().getString("booking.bookingdates.checkout");
        softAssert.assertEquals(actualCheckout, "2024-03-27", "Checkout is not expected");

        String actualAdittionalNeeds = response.xmlPath().getString("booking.additionalneeds");
        softAssert.assertEquals(actualAdittionalNeeds, "Breakfast", "Breakfast is expected");

        softAssert.assertAll();

    }
}
