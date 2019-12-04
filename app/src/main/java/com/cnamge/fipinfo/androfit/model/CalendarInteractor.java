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
    private static final String EVENTS_TIMEZONE       = "Europe/Paris";
    private static final int REMINDERS_MINUTES        = 60;

    private long calendarId;
    private Activity activity;

    public CalendarInteractor(Activity activity) {
        this.activity = activity;
        this.calendarId = -1;
    }

    public void deleteCalendarEvent(Session session) {
        long eventId = session.getCalendarEventId();

        if (eventId == -1 || this.activity.checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        this.activity.getContentResolver().delete(
            CalendarContract.Events.CONTENT_URI,
            CalendarContract.Events._ID + " = ? ",
            new String[]{Long.toString(eventId)}
        );
    }

    public void saveCalendarEvent(Session session) {
        long eventId;

        if (session.getCalendarEventId() == -1) {
            eventId = this.createEvent(session);
            if (eventId == -1) {
                return;
            }
        } else {
            if (this.activity.checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            eventId = session.getCalendarEventId();

            Cursor cursor = this.activity.getContentResolver().query(
                CalendarContract.Events.CONTENT_URI,
                new String[]{CalendarContract.Events._ID},
                CalendarContract.Events._ID + " = ? ",
                new String[]{Long.toString(eventId)},
                null
            );
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    this.updateEvent(session, eventId);
                } else {
                    eventId = this.createEvent(session);
                    if (eventId == -1) {
                        cursor.close();
                        return;
                    }
                }
                cursor.close();
            }
        }
        session.setCalendarEventId(eventId);
        session.save();
    }

    private long createEvent(Session session) {
        long calendarId = this.getCalendarId();

        if (calendarId == -1 || this.activity.checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return -1;
        }

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.CALENDAR_ID, calendarId);
        values.put(CalendarContract.Events.TITLE, session.getName());
        values.put(CalendarContract.Events.EVENT_LOCATION, session.getLocation());
        values.put(CalendarContract.Events.DTSTART, session.getBeginDate());
        values.put(CalendarContract.Events.DTEND, session.getEndDate());
        values.put(CalendarContract.Events.DESCRIPTION, session.getDescription());
        values.put(CalendarContract.Events.EVENT_TIMEZONE, EVENTS_TIMEZONE);
        values.put(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PUBLIC);
        values.put(CalendarContract.Events.SELF_ATTENDEE_STATUS, CalendarContract.Events.STATUS_CONFIRMED);
        values.put(CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS, 1);
        values.put(CalendarContract.Events.GUESTS_CAN_MODIFY, 1);
        values.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

        Uri uri = this.activity.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
        if (uri == null || uri.getLastPathSegment() == null) {
            return -1;
        }
        long eventId = Long.valueOf(uri.getLastPathSegment());
        this.createEventReminder(eventId);

        return eventId;
    }

    private void updateEvent(Session session, long eventId) {
        if (this.activity.checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED
            || eventId == -1) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.TITLE, session.getName());
        values.put(CalendarContract.Events.EVENT_LOCATION, session.getLocation());
        values.put(CalendarContract.Events.DTSTART, session.getBeginDate());
        values.put(CalendarContract.Events.DTEND, session.getEndDate());
        values.put(CalendarContract.Events.DESCRIPTION, session.getDescription());

        this.activity.getContentResolver().update(
            CalendarContract.Events.CONTENT_URI,
            values,
            CalendarContract.Events._ID + " = ? ",
            new String[]{Long.toString(eventId)}
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
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                calendarId = cursor.getLong(0);
            }
            cursor.close();
        }
        if (calendarId != -1) {
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

    private void createEventReminder(long eventId) {
        if (this.activity.checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.MINUTES, REMINDERS_MINUTES);
        values.put(CalendarContract.Reminders.EVENT_ID, eventId);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        this.activity.getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, values);
    }
}
