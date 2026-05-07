package kh.edu.rupp.webtoonkh.api;

import java.util.List;

import kh.edu.rupp.webtoonkh.model.Webtoon;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ApiService {

    @Headers({
            "apikey: sb_publishable_Bm3C45HHyQ__ANEInW1mew_dUK3CWPF",
            "Authorization: Bearer sb_publishable_Bm3C45HHyQ__ANEInW1mew_dUK3CWPF"
    })

    @GET("webtoon?select=*")
    Call<List<Webtoon>> getWebtoons();
}