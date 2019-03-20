package com.es.chatapp.Fragments;

import com.es.chatapp.Notifications.MyResponse;
import com.es.chatapp.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAATN25yZ8:APA91bH7W1th8gHy0d5UsKjPLkV0P6NrwXWEfoRECEJLc9wsKiaEvbZcUg5N1AlBd-NPEboGwGWr96-IgY4RW9lPxh2zONgjxIJkN-WShVJu_G1LFrWqa2034h6yXLdnecbzoTNrU0lc"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
