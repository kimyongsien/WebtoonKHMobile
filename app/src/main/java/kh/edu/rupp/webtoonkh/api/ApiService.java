package kh.edu.rupp.webtoonkh.api;

import java.util.List;

import kh.edu.rupp.webtoonkh.model.Webtoon;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import kh.edu.rupp.webtoonkh.model.Chapter;
import retrofit2.http.Query;
import kh.edu.rupp.webtoonkh.model.ChapterPage;
public interface ApiService {

    @Headers({
            "apikey: sb_publishable_Bm3C45HHyQ__ANEInW1mew_dUK3CWPF",
            "Authorization: Bearer sb_publishable_Bm3C45HHyQ__ANEInW1mew_dUK3CWPF"
    })

    @GET("webtoon?select=*")
    Call<List<Webtoon>> getWebtoons();
    @Headers({
            "apikey: sb_publishable_Bm3C45HHyQ__ANEInW1mew_dUK3CWPF",
            "Authorization: Bearer sb_publishable_Bm3C45HHyQ__ANEInW1mew_dUK3CWPF"
    })
    @GET("chapter?select=*&order=chapter_number.asc")
    Call<List<Chapter>> getChaptersByWebtoon(
            @Query("webtoon_id") String webtoonId
    );
    @Headers({
            "apikey: sb_publishable_Bm3C45HHyQ__ANEInW1mew_dUK3CWPF",
            "Authorization: Bearer sb_publishable_Bm3C45HHyQ__ANEInW1mew_dUK3CWPF"
    })
    @GET("chapter_page?select=*&order=page_order.asc")
    Call<List<ChapterPage>> getPagesByChapter(
            @Query("chapter_id") String chapterId
    );
}