package barqsoft.footballscores.service;

import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;


import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;


/**
 * Created by oleplus on 10/4/15.
 */


public class FootScoresWigetRemoveViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}


class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String LOG_TAG = "WidgetRemoteViewsFactory";
    private Cursor mCursor = null;
    private Context mContext;
    private int mAppWidgetId;

    private static final String[] SCORE_COLUMNS = {
            DatabaseContract.scores_table.DATE_COL,
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
    };

    public static final int COL_DATE = 0;
    public static final int COL_HOME = 1;
    public static final int COL_AWAY = 2;
    public static final int COL_HOME_GOALS = 3;
    public static final int COL_AWAY_GOALS = 4;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    public void onCreate() {
    }

    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    public int getCount() {
        return mCursor.getCount();
    }

    public void onDataSetChanged() {

        Uri dateUri = DatabaseContract.scores_table.buildScoreWithDate();


        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = mformat.format(new Date());

        ContentResolver cr = mContext.getContentResolver();

        final long identityToken = Binder.clearCallingIdentity();

        mCursor = cr.query(DatabaseContract.BASE_CONTENT_URI, SCORE_COLUMNS, null, new String[]{todayDate}, null);
        //mCursor = cr.query(dateUri, SCORE_COLUMNS, null, new String[]{todayDate}, null);

        Binder.restoreCallingIdentity(identityToken);
    }

    public RemoteViews getViewAt(int position) {

        final int itemId = R.layout.list_item_score_widget;
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), itemId);

        if (mCursor.moveToPosition(position)) {

            rv.setTextViewText(R.id.home_name, mCursor.getString(COL_HOME));
            rv.setTextViewText(R.id.away_name, mCursor.getString(COL_AWAY));
            rv.setTextViewText(R.id.home_score, mCursor.getString(COL_HOME_GOALS));
            rv.setTextViewText(R.id.away_score, mCursor.getString(COL_AWAY_GOALS));
        }

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
