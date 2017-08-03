package com.adiaz.legasports.sync.retrofit;

import com.adiaz.legasports.sync.retrofit.entities.CompetitionRestEntity;
import com.adiaz.legasports.sync.retrofit.entities.Town;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by toni on 20/04/2017.
 */

public interface LegaSportsRestApi {

	@GET("/server/competitions/")
	Call<List<CompetitionRestEntity>> competitionsQuery();

	@GET("/server/towns/")
	Call<List<Town>> townsQuery();

}
