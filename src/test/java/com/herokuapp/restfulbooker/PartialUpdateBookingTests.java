package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.function.BinaryOperator;

public class PartialUpdateBookingTests extends BaseTest {
    @Test
    public void partialUpdateBookingTest() {
        Response responseCreate = createBooking();
        responseCreate.print();

        int bookingid = responseCreate.jsonPath().getInt("bookingid");

        JSONObject body = new JSONObject();
        body.put("firstname", "Nani");

        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin","2024-03-26");
        bookingDates.put("checkout","2024-03-28");

        body.put("bookingdates", bookingDates);

        Response responseUpdate = RestAssured.given().auth().preemptive().basic("admin", "password123").contentType(ContentType.JSON).body(body.toString())
                .patch("https://restful-booker.herokuapp.com/booking/" + bookingid);
        responseUpdate.print();

        // Verify all fields
        SoftAssert softAssert = new SoftAssert();
        String actualFirstName = responseUpdate.jsonPath().getString("firstname");
        softAssert.assertEquals(actualFirstName, "Nani", "First name should be Nani");

        String actualLastName = responseUpdate.jsonPath().getString("lastname");
        softAssert.assertEquals(actualLastName, "Cebarolla", "Last name should be Cebarolla");

        int price = responseUpdate.jsonPath().getInt("totalprice");
        softAssert.assertEquals(price, 90, "Price should be 90");

        boolean depositPaid = responseUpdate.jsonPath().getBoolean("depositpaid");
        softAssert.assertFalse(depositPaid, "Deposit paid should be False");


        String actualCheckin = responseUpdate.jsonPath().getString("bookingdates.checkin");
        softAssert.assertEquals(actualCheckin, "2024-03-26", "Checkin is not expected");

        String actualCheckout = responseUpdate.jsonPath().getString("bookingdates.checkout");
        softAssert.assertEquals(actualCheckout, "2024-03-28", "Checkout is not expected");

        softAssert.assertAll();
    }
}
