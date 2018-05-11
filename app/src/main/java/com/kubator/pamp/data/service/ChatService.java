package com.kubator.pamp.data.service;

import com.kubator.pamp.data.model.base.ListResponse;
import com.kubator.pamp.data.model.message.MessageResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by shonliu on 12/14/17.
 */

public interface ChatService {

    @GET("/chat/{id}")
    Observable<ListResponse<MessageResponse>> getMessages(@Path("id") String _chatID, @Query("page") int page, @Query("limit") int limit);
}
