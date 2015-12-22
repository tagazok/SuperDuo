package barqsoft.footballscores;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import barqsoft.footballscores.service.FootScoresWigetRemoveViewsService;

public class FootScoresWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        final int N = appWidgetIds.length;

        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.football_scores_widget);

            Intent intent = new Intent(context, FootScoresWigetRemoveViewsService.class);

            rv.setRemoteAdapter(appWidgetId, R.id.listview_scores_widget, intent);

            rv.setEmptyView(R.id.listview_scores_widget, R.id.empty_view);

            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
    }
}


