package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;

public class BaseTest {
    protected Response createBooking() {
        // Create JSON body
        JSONObject body  = new JSONObject();
        body.put("firstname", "Mina");
        body.put("lastname", "Cebarolla");
        body.put("totalprice",90);
        body.put("depositpaid", false);

        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin","2024-03-25");
        bookingDates.put("checkout","2024-03-27");
        body.put("bookingdates", bookingDates);

        // Get response
        Response response = RestAssured.given().contentType(ContentType.JSON).
                body(body.toString()).post("https://restful-booker.herokuapp.com/booking");
        return response;
    }
}
