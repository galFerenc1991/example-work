package com.ferenc.pamp.presentation.utils;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by
 * Ferenc on 2017.11.29..
 */

public abstract class DateManager {

    public static final String DATE_FORMAT_SERVER_PROFILE = "yyyy-MM-dd";
    public static final String DATE_FORMAT_SERVER_DATE_AND_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_READABLE = "dd MMM yyyy";

    public static final String FORMAT_RIDE_TIME = "HH:mm";
    public static final String FORMAT_RIDE_DATE = "dd MMM ''yy";
    public static final String FORMAT_MY_RIDE_DAY_AND_MONTH = "MMM dd";
    public static final String FORMAT_MY_RIDES_HEADER = "MMMM dd";
    public static final String FORMAT_RIDE_DATE_TIME = "dd MMM ''yy, HH:mm";
    public static final String FORMAT_OPPORTUNITY_DATE = "HH:mm, dd MMM ''yy";
    public static final String FORMAT_CREATOD_BIRTHDAY = "MMM dd, yyyy";

    public static final int RELATIVE_TODAY = 31;
    public static final int RELATIVE_YESTERDAY = 32;
    public static final int RELATIVE_TOMORROW = 33;
    public static final int RELATIVE_DATE = 34;


    public static Calendar getServerMyRideCalendar(String serverDate) {
        SimpleDateFormat sdfOut = new SimpleDateFormat(DATE_FORMAT_SERVER_DATE_AND_TIME, Locale.getDefault());
        Calendar result = Calendar.getInstance();
        try {
            result.setTime(sdfOut.parse(serverDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean isPreviousDay(Calendar date1, Calendar date2) {
        Calendar cal1 = (Calendar) date1.clone();
        Calendar cal2 = (Calendar) date2.clone();
        cal2.add(Calendar.DAY_OF_MONTH, 1);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isNextDay(Calendar date1, Calendar date2) {
        Calendar cal1 = (Calendar) date1.clone();
        Calendar cal2 = (Calendar) date2.clone();
        cal2.add(Calendar.DAY_OF_MONTH, -1);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isSameDay(Calendar date1, Calendar date2) {
        return date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)
                && date1.get(Calendar.DAY_OF_YEAR) == date2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isDatesDifferent(Calendar date1, Calendar date2) {
        return date1.get(Calendar.YEAR) != date2.get(Calendar.YEAR)
                || date1.get(Calendar.DAY_OF_YEAR) != date2.get(Calendar.DAY_OF_YEAR);
    }

    public static String getMyRideDateAndMonth(Calendar date1, Calendar date2) {
        boolean isSameDay = isSameDay(date1, date2);
        SimpleDateFormat sdfOut = new SimpleDateFormat(FORMAT_MY_RIDE_DAY_AND_MONTH, Locale.getDefault());
        if (isSameDay) return sdfOut.format(date1.getTime());
        else
            return String.format("%s - %s", sdfOut.format(date1.getTime()), sdfOut.format(date2.getTime()));
    }

    public static String getMyRideTime(Calendar date1, Calendar date2) {
        SimpleDateFormat sdfOut = new SimpleDateFormat(FORMAT_RIDE_TIME, Locale.getDefault());
        return String.format("%s - %s", sdfOut.format(date1.getTime()), sdfOut.format(date2.getTime()));
    }

    public static int getRelativeDate(String serverTime) {
        Calendar todayCalendar = Calendar.getInstance();
        Calendar dateCalendar = getServerMyRideCalendar(serverTime);
        if (isSameDay(todayCalendar, dateCalendar)) return RELATIVE_TODAY;
        if (isNextDay(todayCalendar, dateCalendar)) return RELATIVE_TOMORROW;
        if (isPreviousDay(todayCalendar, dateCalendar)) return RELATIVE_YESTERDAY;
        return RELATIVE_DATE;
    }

    public static boolean isBeforeNow(Calendar first) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, -1);   // correlation
        return first.getTime().before(now.getTime());
    }

    public static boolean startDateIsBeforeEndDate(Calendar _startDate, Calendar _endDate) {
//        _startDate.add(Calendar.MINUTE, -1);   // correlation
        return _startDate.getTime().before(_endDate.getTime());
    }

    public static boolean isAfterNow(Calendar first) {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.SECOND, 0);    // correlation
        return first.getTime().after(now.getTime());
    }

    public static class DateConverter {
        private String srcPattern;
        private String dstPattern;
        private Locale locale = Locale.ENGLISH;

        private String date;
        private Calendar calendar;

        public DateConverter(String date) {
            this.date = date;
        }

        public DateConverter(Calendar calendar) {
            this.calendar = calendar;
        }

        public DateConverter setSrcPattern(String srcPattern) {
            this.srcPattern = srcPattern;
            return this;
        }

        public DateConverter setDstPattern(String dstPattern) {
            this.dstPattern = dstPattern;
            return this;
        }

        public String toString() {
            SimpleDateFormat outFormat = new SimpleDateFormat(dstPattern, locale);
            if (calendar == null) {
                if (!TextUtils.isEmpty(date)) {
                    SimpleDateFormat inFormat = new SimpleDateFormat(srcPattern, locale);
                    try {
                        Date day = inFormat.parse(date);
                        if (day == null) {
                            return "Not match patterns";
                        } else {
                            return outFormat.format(day);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return "Parse error";
                    }
                } else {
                    return "Not specified";
                }
            } else {
                return outFormat.format(calendar.getTime());
            }
        }

        public Calendar toCalendar() {
            if (calendar == null) {
                SimpleDateFormat outFormat = new SimpleDateFormat(dstPattern, locale);
                try {
                    if (!TextUtils.isEmpty(date)) {
                        Date day = outFormat.parse(date);
                        if (day == null) {
                            Log.w("System.err", "Not match patterns");
                            return Calendar.getInstance();
                        } else {
                            calendar = Calendar.getInstance();
                            calendar.setTime(day);
                            return calendar;
                        }
                    } else {
                        Log.w("System.err", "No specified string date");
                        return Calendar.getInstance();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    return Calendar.getInstance();
                }
            } else {
                return calendar;
            }
        }
    }

}
