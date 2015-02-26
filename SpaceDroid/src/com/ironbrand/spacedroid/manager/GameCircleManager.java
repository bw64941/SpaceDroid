package com.ironbrand.spacedroid.manager;

import java.util.EnumSet;

import android.app.Activity;

import com.amazon.ags.api.AGResponseCallback;
import com.amazon.ags.api.AGResponseHandle;
import com.amazon.ags.api.AmazonGamesCallback;
import com.amazon.ags.api.AmazonGamesClient;
import com.amazon.ags.api.AmazonGamesFeature;
import com.amazon.ags.api.AmazonGamesStatus;
import com.amazon.ags.api.achievements.AchievementsClient;
import com.amazon.ags.api.achievements.UpdateProgressResponse;
import com.amazon.ags.api.leaderboards.LeaderboardsClient;
import com.amazon.ags.api.leaderboards.SubmitScoreResponse;

public class GameCircleManager {

    private static final String TAG = GameCircleManager.class.getName();
    private static int GAMECIRCLE_BEGINNER_ACHIEVMENT = 200;
    private static int GAMECIRCLE_INTERMEDIATE_ACHIEVMENT = 600;
    private static int GAMECIRCLE_EXPERT_ACHIEVMENT = 1000;
    private static int GAMECIRCLE_MASTER_ACHIEVMENT = 2000;

    private static String GAMECIRCLE_BEGINNER_ID = "Beginner";
    private static String GAMECIRCLE_INTERMEDIATE_ID = "Intermediate";
    private static String GAMECIRCLE_EXPERT_ID = "Expert";
    private static String GAMECIRCLE_MASTER_ID = "Master";

    private static String GAMECIRCLE_LEADERBOARD_ID = "Leaderboard";

    private static boolean isBeginnerAcheived = false;
    private static boolean isIntermediateAcheived = false;
    private static boolean isExpertAcheived = false;
    private static boolean isMasterAcheived = false;

    private static AmazonGamesClient agsClient;
    private static AchievementsClient acClient;
    private static LeaderboardsClient lbClient;
    private static final EnumSet<AmazonGamesFeature> myGameFeatures = EnumSet.of(AmazonGamesFeature.Achievements, AmazonGamesFeature.Leaderboards);

    private static AmazonGamesCallback callback = new AmazonGamesCallback() {
	@Override
	public void onServiceNotReady(AmazonGamesStatus status) {
//	    Log.d(GameCircleManager.TAG, "AMAZON GAME SERVICE NOT READY" + status.values());
	}

	@Override
	public void onServiceReady(AmazonGamesClient amazonGamesClient) {
//	    Log.d(GameCircleManager.TAG, "AMAZON GAME SERVICE READY");
	    agsClient = amazonGamesClient;
	    lbClient = agsClient.getLeaderboardsClient();
	    acClient = agsClient.getAchievementsClient();
	    // ready to use GameCircle
	}
    };

    /**
     * Initialize GameCirle Services
     */
    public static void initializeAmazonGameCirle(Activity activity) {
	AmazonGamesClient.initialize(activity, callback, myGameFeatures);
    }

    /**
     * Release GameCircle Services
     */
    public static void releaseAmazonGameCircle() {
	if (agsClient != null) {
	    AmazonGamesClient.release();
	}
    }

    /**
     * Update Achievement
     */
    public static void updateAchievement(String acheivementName, float percentage) {
	// Replace YOUR_ACHIEVEMENT_ID with an actual achievement ID from your
	// game.
	// acClient = agsClient.getAchievementsClient();
	AGResponseHandle<UpdateProgressResponse> handle = acClient.updateProgress(acheivementName, percentage);

	// Optional callback to receive notification of success/failure.
	handle.setCallback(new AGResponseCallback<UpdateProgressResponse>() {

	    @Override
	    public void onComplete(UpdateProgressResponse result) {
		if (result.isError()) {
		    // Add optional error handling here. Not strictly required
		    // since retries and on-device request caching are
		    // automatic.
		} else {
		    // Continue game flow.
		}
	    }
	});
    }

    /**
     * Evaluate Achievements
     */
    public static void evaluateAchievements(int score) {
	if (score >= GameCircleManager.GAMECIRCLE_BEGINNER_ACHIEVMENT && isBeginnerAcheived == false) {
	    GameCircleManager.updateAchievement(GameCircleManager.GAMECIRCLE_BEGINNER_ID, 100);
	    isBeginnerAcheived = true;
	}

	if (score >= GameCircleManager.GAMECIRCLE_INTERMEDIATE_ACHIEVMENT && isIntermediateAcheived == false) {
	    GameCircleManager.updateAchievement(GameCircleManager.GAMECIRCLE_INTERMEDIATE_ID, 100);
	    isIntermediateAcheived = true;
	}

	if (score >= GameCircleManager.GAMECIRCLE_EXPERT_ACHIEVMENT && isExpertAcheived == false) {
	    GameCircleManager.updateAchievement(GameCircleManager.GAMECIRCLE_EXPERT_ID, 100);
	    isExpertAcheived = true;
	}

	if (score >= GameCircleManager.GAMECIRCLE_MASTER_ACHIEVMENT && isMasterAcheived == false) {
	    GameCircleManager.updateAchievement(GameCircleManager.GAMECIRCLE_MASTER_ID, 100);
	    isMasterAcheived = false;
	}
    }

    /**
     * Show Acheivements Overlay
     */
    public static void showAcheivementsOverlay() {
	acClient.showAchievementsOverlay();
    }

    /**
     * Show Leaderboard Overlay
     */
    public static void showLeaderboardOverlay() {
	// lbClient = agsClient.getLeaderboardsClient();
	lbClient.showLeaderboardsOverlay();
    }

    /**
     * Submit Score to Leader Board
     */
    public static void submitScoreToLeaderboard(long score) {
	lbClient = agsClient.getLeaderboardsClient();
	AGResponseHandle<SubmitScoreResponse> handle = lbClient.submitScore(GAMECIRCLE_LEADERBOARD_ID, score);

	// Optional callback to receive notification of success/failure.
	handle.setCallback(new AGResponseCallback<SubmitScoreResponse>() {

	    @Override
	    public void onComplete(SubmitScoreResponse result) {
		if (result.isError()) {
		    // Add optional error handling here. Not strictly required
		    // since retries and on-device request caching are
		    // automatic.
		} else {
		    // Continue game flow.
		}
	    }
	});
    }
}
