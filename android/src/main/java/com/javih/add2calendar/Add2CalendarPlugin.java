package com.javih.add2calendar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** Add2CalendarPlugin */
public class Add2CalendarPlugin implements MethodCallHandler {
    private static final String AGENDA_URI_BASE = "content://com.android.calendar/events";
    private final Registrar mRegistrar;

    public Add2CalendarPlugin(Registrar registrar) {
        mRegistrar = registrar;
    }

    /** Plugin registration. */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter.javih.com/add_2_calendar");
        channel.setMethodCallHandler(new Add2CalendarPlugin(registrar));
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("add2Cal")) {
            try {
              int eventId = insert((int) call.argument("id"), (String) call.argument("title"), (String) call.argument("desc"), (String) call.argument("location"), (long) call.argument("startDate"), (long) call.argument("endDate"), (boolean) call.argument("allDay"));
                result.success(eventId);
            } catch (NullPointerException e) {
                result.error("Exception ocurred in Android code", e.getMessage(), 0);
            }
        } else if (call.method.equals("removeFromCal")) {
            try {
                remove((int) call.argument("id"));
                result.success(true);
            } catch (NullPointerException e) {
                result.error("Exception ocurred in Android code", e.getMessage(), false);
            }
        }
        else {
            result.notImplemented();
        }
    }

    @SuppressLint("NewApi")
    public int insert(int id, String title, String desc, String loc, long start, long end, boolean allDay) {
        Context context = getActiveContext();
//        Intent intent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);
//        intent.putExtra(CalendarContract.Events._ID, id);
//        intent.putExtra(CalendarContract.Events.CALENDAR_ID, id);
//        intent.putExtra(CalendarContract.Events.TITLE, title);
//        intent.putExtra(CalendarContract.Events.DESCRIPTION, desc);
//        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, loc);
//        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, start);
//        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);
//        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, allDay);
//       context.startActivity(intent);
//            int calId = tryParse(CalendarContract.Events._ID);
//
//       return calId;
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, start);
        values.put(CalendarContract.Events.DTEND, end);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.ALL_DAY, allDay);
        values.put(CalendarContract.Events.DESCRIPTION, desc);
        values.put(CalendarContract.Events.EVENT_LOCATION, loc);
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Amsterdam");
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

            // get the event ID that is the last element in the Uri
            long eventID = Long.parseLong(uri.getLastPathSegment());
            return (int) eventID;
    }

    public static Integer tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @SuppressLint("NewApi")
    public void remove(int id) {
        Context context = getActiveContext();
//        Intent intent = new Intent(Intent.ACTION_EDIT, CalendarContract.Events.CONTENT_URI);
//        intent.putExtra(CalendarContract.Events.CALENDAR_ID, id);
//        context.startActivity(intent);
        ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.DELETED, 1);
        Uri eventUri = ContentUris
                .withAppendedId(CalendarContract.Events.CONTENT_URI, id);
        int rows = context.getContentResolver().delete(eventUri, null, null);
        Log.i("DEBUG_TAG", "Rows deleted: " + rows);
    }

    private Context getActiveContext() {
        return (mRegistrar.activity() != null) ? mRegistrar.activity() : mRegistrar.context();
    }
}
