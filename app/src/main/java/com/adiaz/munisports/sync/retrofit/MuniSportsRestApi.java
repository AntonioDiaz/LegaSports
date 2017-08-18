package com.adiaz.munisports.sync.retrofit;

import com.adiaz.munisports.sync.retrofit.entities.town.TownRestEntity;
import com.adiaz.munisports.sync.retrofit.entities.competition.CompetitionRestEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by toni on 20/04/2017.
 */

public interface MuniSportsRestApi {

/*	@GET("/server/competitions/")
	Call<List<CompetitionRestEntity>> competitionsQuery();*/

	@GET("/server/towns/")
	Call<List<TownRestEntity>> townsQuery();

	@GET("/server/search_competitions/")
	Call<List<CompetitionRestEntity>> competitionsQuery(@Query("idTown")Long idTown);

}
