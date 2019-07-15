package cz.mzk.integrity.kramerius_api;

import com.google.gson.JsonObject;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

import java.util.List;
import java.util.Map;

/**
 * Created by holmanj on 16.11.15.
 */
public interface ProcessRemoteApiRetrofit {

    // parameters: 1. uuid je uuid, ka kterém se proces spustí, 2. uuid je název dokumentu do pole "name"
    @POST("/processes")
    Process planProcess(@Query("def") String def, @Body Parameters parameters) throws Exception;

    @GET("/processes/{uuid}")
    Process getProcess(@Path("uuid") String uuid) throws Exception;

    @PUT("/processes/{uuid}?stop") // retrofit does not allow PUT with empty body
    Process stopProcess(@Path("uuid") String uuid, @Body Parameters fakeBody) throws Exception;

    @DELETE("/processes/{uuid}")
    JsonObject deleteProcessLog(@Path("uuid") String uuid) throws Exception;

    @GET("/processes")
    List<Process> listProcesses() throws Exception;

    @GET("/processes")
    List<Process> listProcesses(@Query("resultSize") int pocetProcesu) throws Exception;

    @GET("/processes")
    List<Process> listProcesses(@Query("resultSize") int pocetProcesu, @Query("offset") int offset) throws Exception;

    @GET("/processes")
    List<Process> filterProcesses(@QueryMap Map<String, String> fields) throws Exception;
}
