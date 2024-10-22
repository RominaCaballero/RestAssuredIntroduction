package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class UpdateBookingTests extends BaseTest{

    @Test
    public void createBookingTest() {
        // Create booking
        Response responseCreate = createBooking();
        responseCreate.print();

        //Get bookingID of new booking
        int bookingid = responseCreate.jsonPath().getInt("bookingid");

        // Create JSON body
        JSONObject body  = new JSONObject();
        body.put("firstname", "Olga");
        body.put("lastname", "Cebarolla");
        body.put("totalprice",125);
        body.put("depositpaid", true);

        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin","2024-03-25");
        bookingDates.put("checkout","2024-03-27");
        body.put("bookingdates", bookingDates);

        //Update booking
        Response responseUpdate = RestAssured.given().auth().preemptive().basic("admin", "password123").contentType(ContentType.JSON).body(body.toString())
                .put("https://restful-booker.herokuapp.com/booking/" + bookingid);
        responseUpdate.print();

        Assert.assertEquals(responseUpdate.getStatusCode(), 200, "Status code should be 200");

        // Verify all fields
        SoftAssert softAssert = new SoftAssert();
        String actualFirstName = responseUpdate.jsonPath().getString("firstname");
        softAssert.assertEquals(actualFirstName, "Olga", "First name should be Olga");

        String actualLastName = responseUpdate.jsonPath().getString("lastname");
        softAssert.assertEquals(actualLastName, "Cebarolla", "Last name should be Cebarolla");

        int price = responseUpdate.jsonPath().getInt("totalprice");
        softAssert.assertEquals(price, 125, "Price should be 125");

        boolean depositPaid = responseUpdate.jsonPath().getBoolean("depositpaid");
        softAssert.assertTrue(depositPaid, "Deposit paid should be True");


        String actualCheckin = responseUpdate.jsonPath().getString("bookingdates.checkin");
        softAssert.assertEquals(actualCheckin, "2024-03-25", "Checkin is not expected");

        String actualCheckout = responseUpdate.jsonPath().getString("bookingdates.checkout");
        softAssert.assertEquals(actualCheckout, "2024-03-27", "Checkout is not expected");

        softAssert.assertAll();
    }

}
