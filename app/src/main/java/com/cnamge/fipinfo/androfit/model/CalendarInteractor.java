package com.cnamge.fipinfo.androfit.model;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

public class CalendarInteractor {
    private static final String CALENDAR_ACCOUNT_NAME = "andro_fit";
    private static final String CALENDAR_NAME         = "AndroFit Events";

    private long calendarId;
    private Activity activity;

    public CalendarInteractor(Activity activity) {
        this.activity = activity;
        this.calendarId = -1;
    }

    public void deleteCalendarEvent(Session session) {
        long eventId = session.getCalendarEventId();

        if (this.activity.checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED
        || this.getCalendarId() == -1 || eventId == -1) {
            return;
        }

        this.activity.getContentResolver().delete(
            CalendarContract.Events.CONTENT_URI,
            CalendarContract.Events._ID + " = ? AND " + CalendarContract.Calendars._ID + " = ? ",
            new String[]{Long.toString(eventId), Long.toString(this.getCalendarId())}
        );
    }

    public void saveCalendarEvent(Session session) {
        if (this.getCalendarId() == -1) {
            return;
        }

        long eventId = -1;
        if (session.getCalendarEventId() == -1) {
            eventId = this.createEvent(session);
            if (eventId == -1) {
                return;
            }
        } else {
            if (this.activity.checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            Cursor cursor = this.activity.getContentResolver().query(
                CalendarContract.Events.CONTENT_URI,
                new String[]{CalendarContract.Events._ID},
                CalendarContract.Events._ID + " = ? ",
                new String[]{Long.toString(session.getCalendarEventId())},
                null
            );
            if (cursor != null && cursor.moveToFirst()) {
                eventId = cursor.getLong(0);
                cursor.close();
                this.updateEventId(session, eventId);
            }
        }
        session.setCalendarEventId(eventId);
        session.save();
    }

    private long createEvent(Session session) {
        if (this.activity.checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return -1;
        }

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.CALENDAR_ID, this.getCalendarId());
        values.put(CalendarContract.Events.TITLE, session.getName());
        values.put(CalendarContract.Events.EVENT_LOCATION, session.getLocation());
        values.put(CalendarContract.Events.DTSTART, session.getBeginDate());
        values.put(CalendarContract.Events.DTEND, session.getEndDate());
        values.put(CalendarContract.Events.DESCRIPTION, session.getDescription());
        values.put(CalendarContract.Events.RRULE, "FREQ=DAILY;COUNT=20;BYDAY=MO,TU,WE,TH,FR;WKST=MO");
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Paris");
        values.put(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PUBLIC);
        values.put(CalendarContract.Events.SELF_ATTENDEE_STATUS, CalendarContract.Events.STATUS_CONFIRMED);
        values.put(CalendarContract.Events.ALL_DAY, 2);
        values.put(CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS, 1);
        values.put(CalendarContract.Events.GUESTS_CAN_MODIFY, 1);
        values.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

        Uri uri = this.activity.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
        if (uri == null || uri.getLastPathSegment() == null) {
            return -1;
        }

        return Long.valueOf(uri.getLastPathSegment());
    }

    private void updateEventId(Session session, long eventId) {
        if (this.activity.checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED
            || eventId == -1) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.TITLE, session.getName());
        values.put(CalendarContract.Events.EVENT_LOCATION, session.getLocation());
        values.put(CalendarContract.Events.DTSTART, session.getBeginDate());
        values.put(CalendarContract.Events.DTEND, session.getEndDate());
        values.put(CalendarContract.Events.CALENDAR_ID, this.getCalendarId());
        values.put(CalendarContract.Events.DESCRIPTION, session.getDescription());

        this.activity.getContentResolver().update(
            CalendarContract.Events.CONTENT_URI,
            values,
            CalendarContract.Events._ID + " = ? AND " + CalendarContract.Calendars._ID + " = ? ",
            new String[]{Long.toString(eventId), Long.toString(this.getCalendarId())}
        );
    }

    private long getCalendarId() {
        if (this.calendarId > -1) {
            return this.calendarId;
        }

        if (this.activity.checkSelfPermission(Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED
            && this.activity.checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return -1;
        }

        String[] projection = new String[]{CalendarContract.Calendars._ID};
        String selection = String.format("%s = ? AND %s = ?", CalendarContract.Calendars.ACCOUNT_NAME, CalendarContract.Calendars.ACCOUNT_TYPE);
        String[] selArgs = new String[]{CALENDAR_ACCOUNT_NAME, CalendarContract.ACCOUNT_TYPE_LOCAL};

        Cursor cursor = this.activity.getContentResolver().query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            selection,
            selArgs,
            null
        );

        long calendarId = -1;
        if (cursor != null && cursor.moveToFirst()) {
            calendarId = cursor.getLong(0);
            cursor.close();
        } else {
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDAR_ACCOUNT_NAME);
            values.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
            values.put(CalendarContract.Calendars.NAME, CALENDAR_NAME);
            values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDAR_NAME);
            values.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
            values.put(CalendarContract.Calendars.SYNC_EVENTS, 1);

            Uri.Builder builder = CalendarContract.Calendars.CONTENT_URI.buildUpon();
            builder.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CalendarInteractor.CALENDAR_ACCOUNT_NAME);
            builder.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
            builder.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true");
            Uri uri = this.activity.getContentResolver().insert(builder.build(), values);
            if (uri != null && uri.getLastPathSegment() != null) {
                calendarId = Long.parseLong(uri.getLastPathSegment());
            }
        }
        this.calendarId = calendarId;
        return this.calendarId;
    }
}
