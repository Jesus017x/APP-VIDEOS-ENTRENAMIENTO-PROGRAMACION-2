package com.example.app_videos.notificationPackage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAJHCXpbw:APA91bEOOJuNwDAzRiF1tku91AoYmwcXxpTD1HrQKiyCR8Ct8NG66OwKTzZjsoPJoUHmXgCi8jXrahvhyZg_Pt0ehebrJAwjgqDtLNe8OetTvbZ1q5ow95W5InVk-P39o3DHZtKBmOWT"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);
}
