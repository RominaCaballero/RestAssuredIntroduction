package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class CreateBookingTests extends BaseTest{

    //@Test
    public void createBookingTest() {
        // Create JSON body

        Response response = createBooking();

        response.print();
        // Verifications
        // Verify the response is 200
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");

        // Verify all fields
        SoftAssert softAssert = new SoftAssert();
        String actualFirstName = response.jsonPath().getString("booking.firstname");
        softAssert.assertEquals(actualFirstName, "Mina", "First name should be Mina");

        String actualLastName = response.jsonPath().getString("booking.lastname");
        softAssert.assertEquals(actualLastName, "Cebarolla", "Last name should be Cebarolla");

        int price = response.jsonPath().getInt("booking.totalprice");
        softAssert.assertEquals(price, 90, "Price should be 90");

        boolean depositPaid = response.jsonPath().getBoolean("booking.depositpaid");
        softAssert.assertFalse(depositPaid, "Deposit paid should be False");


        String actualCheckin = response.jsonPath().getString("booking.bookingdates.checkin");
        softAssert.assertEquals(actualCheckin, "2024-03-25", "Checkin is not expected");

        String actualCheckout = response.jsonPath().getString("booking.bookingdates.checkout");
        softAssert.assertEquals(actualCheckout, "2024-03-27", "Checkout is not expected");

        softAssert.assertAll();
    }

    @Test
    public void createBookingWithPOJOTest() {
        // Create body using POJOs
        Bookingdates bookingdates = new Bookingdates("2020-03-25", "2020-03-27");
        Booking booking = new Booking("Olga", "Shyshkin", 200, false, bookingdates, "Baby crib");

        // Get response
        Response response = RestAssured.given(spec).contentType(ContentType.JSON).body(booking)
                .post("/booking");
        response.print();
        Bookingid bookingid = response.as(Bookingid.class);

        // Verifications
        // Verify response 200
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200, but it's not");

        System.out.println("Request booking : " + booking.toString());
        System.out.println("Response booking: " + bookingid.getBooking().toString());

        // Verify All fields
        Assert.assertEquals(bookingid.getBooking().toString(), booking.toString());
    }

}
