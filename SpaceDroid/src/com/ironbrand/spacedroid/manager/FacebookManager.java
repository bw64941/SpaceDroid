//package com.ironbrand.spacedroid.manager;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.Activity;
//import android.os.Bundle;
//
//import com.facebook.HttpMethod;
//import com.facebook.Request;
//import com.facebook.RequestAsyncTask;
//import com.facebook.Response;
//import com.facebook.Session;
//import com.facebook.Session.Builder;
//import com.facebook.Session.OpenRequest;
//import com.facebook.Session.StatusCallback;
//import com.facebook.SessionLoginBehavior;
//import com.facebook.SessionState;
//import com.facebook.model.GraphUser;
//
//public class FacebookManager {
//
//    private static final String TAG = FacebookManager.class.getName();
//    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
//
//    private static boolean sUserLoggedIn;
//    private static String sFirstName;
//    private static String FB_SHARE_CAPTION = "SpaceDroid for Android";
//    private static String FB_SHARE_LINK = "https://play.google.com/store/apps/details?id=com.ironbrand.spacedroid";
//    private static String FB_SHARE_DESCRIPTION = "Click to download!";
//
//    /**
//     * Opens an active Facebook Login
//     * 
//     * @param pActivity
//     * @param pAllowLoginUI
//     * @param pCallback
//     * @param pPermissions
//     * @return
//     */
//    private static Session openActiveSession(final Activity pActivity, final boolean pAllowLoginUI, final StatusCallback pCallback, final List<String> pPermissions) {
//	final OpenRequest openRequest = new OpenRequest(pActivity).setPermissions(pPermissions).setCallback(pCallback);
//	openRequest.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
//	final Session session = new Builder(pActivity.getBaseContext()).build();
//	if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState()) || pAllowLoginUI) {
//	    Session.setActiveSession(session);
//	    session.openForPublish(openRequest);
//	    return session;
//	}
//	return null;
//    }
//
//    public static void checkUserLoggedIn() {
//	ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {
//	    @Override
//	    public void run() {
//		openActiveSession(ResourcesManager.getInstance().activity, false, new Session.StatusCallback() {
//		    @Override
//		    public void call(Session session, SessionState state, Exception exception) {
//			if (session.isOpened()) {
//			    Request.newMeRequest(session, new Request.GraphUserCallback() {
//				@Override
//				public void onCompleted(GraphUser user, Response response) {
//				    if (user != null) {
//					sFirstName = user.getFirstName();
//					FacebookManager.sUserLoggedIn = true;
//				    } else {
//					FacebookManager.sUserLoggedIn = false;
//				    }
//				}
//			    }).executeAsync();
//			}
//		    }
//		}, PERMISSIONS);
//	    }
//	});
//    }
//
//    private static void loginAndPost(final String pData) {
//	ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {
//	    @Override
//	    public void run() {
//		openActiveSession(ResourcesManager.getInstance().activity, true, new Session.StatusCallback() {
//		    @Override
//		    public void call(Session session, SessionState state, Exception exception) {
//			if (session.isOpened()) {
//			    Request.newMeRequest(session, new Request.GraphUserCallback() {
//				@Override
//				public void onCompleted(GraphUser user, Response response) {
//				    if (user != null) {
//					sFirstName = user.getFirstName();
//					final Session.OpenRequest openRequest;
//					openRequest = new Session.OpenRequest(ResourcesManager.getInstance().activity);
//					openRequest.setPermissions(PERMISSIONS);
//					sUserLoggedIn = true;
//					post(user.getFirstName(), pData);
//				    } else {
//					sUserLoggedIn = false;
//				    }
//				}
//			    }).executeAsync();
//			}
//		    }
//		}, PERMISSIONS);
//	    }
//	});
//    }
//
//    private static void post(final String pFirstName, final String pData) {
//	ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {
//	    @Override
//	    public void run() {
//		Bundle params = new Bundle();
//		params.putString("name", pFirstName + pData);
//		params.putString("caption", FB_SHARE_CAPTION);
//		params.putString("description", FB_SHARE_DESCRIPTION);
//		params.putString("link", FB_SHARE_LINK);
//		// params.putString("picture",
//		// "https://lh5.ggpht.com/Sbm0k7LJkf7aHdgYiJynZ3MwiGHWbzVSJhXsrvF8MZ4Kkv2dweahf7htsCtl1ce7I7dDWfw=s85");
//		// params.putString("picture",
//		// "https://lh4.ggpht.com/iuC9GXRteJrMui69AzaXeuRmmYn5LIKEQ-kLqUze1Jc2Vsidxybrx568MOybkfeG0jxc=s85");
//		JSONObject actions = new JSONObject();
//		try {
//		    actions.put("name", "Get");
//		    actions.put("link", FB_SHARE_LINK);
//		} catch (Exception e) {
//		}
//		;
//		params.putString("actions", actions.toString());
//		Request.Callback callback = new Request.Callback() {
//		    @Override
//		    public void onCompleted(Response response) {
//			try {
//			    JSONObject graphResponse = response.getGraphObject().getInnerJSONObject();
//			    @SuppressWarnings("unused")
//			    String postID = null;
//			    try {
//				postID = graphResponse.getString("id");
//			    } catch (JSONException e) {
//			    }
//			} catch (NullPointerException e) {
//			}
//		    }
//		};
//		Request request = new Request(Session.getActiveSession(), "me/feed", params, HttpMethod.POST, callback);
//		RequestAsyncTask task = new RequestAsyncTask(request);
//		task.execute();
//	    }
//	});
//
//    }
//
//    public static void postScore(final String pScore) {
//	if (sUserLoggedIn) {
//	    post(sFirstName, " just scored " + pScore + " playing SpaceDroid for Android!");
//	} else {
//	    loginAndPost(" just scored " + pScore + " playing SpaceDroid for Android!");
//	}
//    }
//
//    public static void postLevelCompletion(final String pLevel) {
//	if (sUserLoggedIn) {
//	    post(sFirstName, " has completed Level " + pLevel + " in SpaceDroid for Android!");
//	} else {
//	    loginAndPost(" has completed Level " + pLevel + " in SpaceDroid for Android!");
//	}
//    }
//
//}
