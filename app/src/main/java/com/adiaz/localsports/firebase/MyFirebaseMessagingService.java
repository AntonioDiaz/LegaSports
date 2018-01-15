package com.adiaz.localsports.firebase;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.adiaz.localsports.database.LocalSportsDbContract;
import com.adiaz.localsports.database.daos.CompetitionsDAO;
import com.adiaz.localsports.entities.Competition;
import com.adiaz.localsports.entities.Favorite;
import com.adiaz.localsports.sync.retrofit.LocalSportsRestApi;
import com.adiaz.localsports.sync.retrofit.callbacks.CompetitionsAvailableCallback;
import com.adiaz.localsports.sync.retrofit.callbacks.SportsCallback;
import com.adiaz.localsports.sync.retrofit.entities.competition.CompetitionRestEntity;
import com.adiaz.localsports.sync.retrofit.entities.competition.TeamsDeref;
import com.adiaz.localsports.sync.retrofit.entities.sport.SportsRestEntity;
import com.adiaz.localsports.utilities.FavoritesUtils;
import com.adiaz.localsports.utilities.LocalSportsConstants;
import com.adiaz.localsports.utilities.LocalSportsUtils;
import com.adiaz.localsports.utilities.NotificationUtils;
import com.adiaz.localsports.utilities.PreferencesUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.adiaz.localsports.database.LocalSportsDbContract.CompetitionsEntry;


/**
 * Created by adiaz on 10/1/18.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String fcmTopic = PreferencesUtils.queryPreferenceFCMTopic(getApplicationContext());
        if (remoteMessage.getFrom().equals("/topics/" + fcmTopic)) {
            if (remoteMessage.getData().size() > 0) {
                Map<String, String> data = remoteMessage.getData();
                String competitionStr = data.get("competition");
                Gson gson = new Gson();
                CompetitionRestEntity competitionRestEntity = gson.fromJson(competitionStr, CompetitionRestEntity.class);
                Competition competition = CompetitionsDAO.findCompetition(getApplicationContext(), competitionRestEntity.getId());
                if (competition!=null) {
                    CompetitionsDAO.markCompetitionsAsDirty(getApplicationContext(), competition.serverId(), true);
                    //check if competition is favorite to show notification.
                    boolean favoriteCompetition = FavoritesUtils.isFavoriteCompetition(getApplicationContext(), competition.serverId());
                    if (favoriteCompetition) {
                        showNotificationCompetition(getApplicationContext(), competition);
                    } else {
                        for (TeamsDeref teamsDeref : competitionRestEntity.getTeamsAffectedByLastUpdateDeref()) {
                            String teamName = teamsDeref.getName();
                            boolean favoriteTeam = FavoritesUtils.isFavoriteTeam(getApplicationContext(), competition.serverId(), teamName);
                            if (favoriteTeam) {
                                showNotificationTeam(getApplicationContext(), competition, teamName);
                            }
                        }
                    }
                } else {
                    Long idTownSelect = PreferencesUtils.queryPreferenceTownId(this);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(LocalSportsConstants.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    LocalSportsRestApi localSportsRestApi = retrofit.create(LocalSportsRestApi.class);
                    Call<List<CompetitionRestEntity>> callCompetitions = localSportsRestApi.competitionsQuery(idTownSelect);
                    callCompetitions.enqueue(new CompetitionsAvailableCallback(getApplicationContext(), null));
                    Call<List<SportsRestEntity>> callSports = localSportsRestApi.sportsQuery(idTownSelect);
                    callSports.enqueue(new SportsCallback(getApplicationContext(), null));
                }
            }
        }
    }

    private void showNotificationTeam(Context context, Competition competition, String teamName) {
        if (LocalSportsUtils.isShowNotification(context)) {
            NotificationUtils.remindUpdatedTeam(context, competition, teamName);
        }
    }

    private void showNotificationCompetition(Context context, Competition competition) {
        if (LocalSportsUtils.isShowNotification(context)) {
            NotificationUtils.remindUpdatedCompetition(context, competition);
        }
    }
}
