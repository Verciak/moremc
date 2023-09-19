package net.moremc.api.helper;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataHelper {

    public static String getTimeToString(long time, boolean minusCurrentTime) {
        if (minusCurrentTime) time -= System.currentTimeMillis();
        if (time < 1L) {
            return "< 1s";
        }

        StringBuilder builder = new StringBuilder();
        long[] units = new long[] {
                TimeUnit.MILLISECONDS.toDays(time),
                TimeUnit.MILLISECONDS.toHours(time) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(time)),
                TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)),
                TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
        };
        if (units[0] > 0L) {
            builder.append(units[0]).append("d").append(" ");
        }
        if (units[1] > 0L) {
            builder.append(units[1]).append("h").append(" ");
        }
        if (units[2] > 0L) {
            builder.append(units[2]).append("m").append(" ");
        }
        if (units[3] > 0L) {
            builder.append(units[3]).append("s").append(" ");
        }

        return builder.length() > 0 ? builder.toString().trim() : time + "ms";
    }
    public static String getCringeTimeToString(long time) {
        long d = Math.abs(time - System.currentTimeMillis()) / 1000;
        int days = Math.toIntExact(d / 86400);
        d -= days * 86400L;

        int hours = Math.toIntExact(d / 3600) % 24;
        d -= hours * 3600;

        int minutes = Math.toIntExact(d / 60) % 60;
        d -= minutes * 60;

        int seconds = Math.toIntExact(d % 60);
        d -= seconds % 60;

        int ms = Math.toIntExact(d * 1000);
        if (days == 0 && minutes == 0 && hours == 0 && seconds == 0)
            return "<1";

        return (days > 0 ? days + " dni " : "") + (hours > 0 ? hours + " godz " : "") +
                (minutes > 0 ? minutes + " min " : "") + (seconds > 0 ? seconds + " sek " : "") + (minutes < 1 && hours < 1 && days < 1 ? (ms > 0 ? ms + " ms " : "") : "");
    }

    public static String getTimeToString(long time) {
        return getTimeToString(time, true);
    }
    public static long parseDateDiff(final String time, final boolean future) {
        try {
            final Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?", 2);
            final Matcher m = timePattern.matcher(time);
            int years = 0;
            int months = 0;
            int weeks = 0;
            int days = 0;
            int hours = 0;
            int minutes = 0;
            int seconds = 0;
            boolean found = false;
            while (m.find()) {
                if (m.group() != null && !m.group().isEmpty()) {
                    for (int i = 0; i < m.groupCount(); ++i) {
                        if (m.group(i) != null && !m.group(i).isEmpty()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        continue;
                    }
                    if (m.group(1) != null && !m.group(1).isEmpty()) {
                        years = Integer.parseInt(m.group(1));
                    }
                    if (m.group(2) != null && !m.group(2).isEmpty()) {
                        months = Integer.parseInt(m.group(2));
                    }
                    if (m.group(3) != null && !m.group(3).isEmpty()) {
                        weeks = Integer.parseInt(m.group(3));
                    }
                    if (m.group(4) != null && !m.group(4).isEmpty()) {
                        days = Integer.parseInt(m.group(4));
                    }
                    if (m.group(5) != null && !m.group(5).isEmpty()) {
                        hours = Integer.parseInt(m.group(5));
                    }
                    if (m.group(6) != null && !m.group(6).isEmpty()) {
                        minutes = Integer.parseInt(m.group(6));
                    }
                    if (m.group(7) == null) {
                        break;
                    }
                    if (m.group(7).isEmpty()) {
                        break;
                    }
                    seconds = Integer.parseInt(m.group(7));
                    break;
                }
            }
            if (!found) {
                return -1L;
            }
            final Calendar c = new GregorianCalendar();
            if (years > 0) {
                c.add(1, years * (future ? 1 : -1));
            }
            if (months > 0) {
                c.add(2, months * (future ? 1 : -1));
            }
            if (weeks > 0) {
                c.add(3, weeks * (future ? 1 : -1));
            }
            if (days > 0) {
                c.add(5, days * (future ? 1 : -1));
            }
            if (hours > 0) {
                c.add(11, hours * (future ? 1 : -1));
            }
            if (minutes > 0) {
                c.add(12, minutes * (future ? 1 : -1));
            }
            if (seconds > 0) {
                c.add(13, seconds * (future ? 1 : -1));
            }
            final Calendar max = new GregorianCalendar();
            max.add(1, 10);
            if (c.after(max)) {
                return max.getTimeInMillis();
            }
            return c.getTimeInMillis();
        }
        catch (Exception e) {
            return -1L;
        }
    }
}
