package com.adiaz.localsports.sync.retrofit;

import com.adiaz.localsports.sync.retrofit.entities.competition.CompetitionRestEntity;
import com.adiaz.localsports.sync.retrofit.entities.competitiondetails.CompetitionDetails;
import com.adiaz.localsports.sync.retrofit.entities.issue.Issue;
import com.adiaz.localsports.sync.retrofit.entities.match.MatchRestEntity;
import com.adiaz.localsports.sync.retrofit.entities.town.TownRestEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by toni on 20/04/2017.
 */

public interface LocalSportsRestApi {

/*	@GET("/server/competitions/")
	Call<List<CompetitionRestEntity>> competitionsQuery();*/

	@GET("/server/towns/")
	Call<List<TownRestEntity>> townsQuery();

	@GET("/server/search_competitions/")
	Call<List<CompetitionRestEntity>> competitionsQuery(@Query("idTown")Long idTown);

	@GET("/server/matches/{idCompetition}")
	Call<List<MatchRestEntity>> matchesQuery(@Path("idCompetition")Long idCompetition);

	@GET("/server/competitiondetails/{idCompetition}")
	Call<CompetitionDetails> competitionDetailsQuery(@Path("idCompetition")Long idCompetition);

	@POST ("/server/issues")
	Call<Long> addIssue(@Body Issue issue);
}
