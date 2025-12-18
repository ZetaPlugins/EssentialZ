package com.zetaplugins.essentialz.util;

/**
 * Utility class for converting between Minecraft ticks and time strings.
 */
public final class TimeConverter {
    private TimeConverter () {}

    /**
     * Converts Minecraft ticks to a time string in "HH:MM" format.
     * @param ticks the time in Minecraft ticks
     * @return the equivalent time as a string
     */
    public static String ticksToTimeString(long ticks) {
        // Each Minecraft hour = 1000 ticks
        long totalMinutes = (ticks * 60) / 1000; // convert ticks to minutes
        long hours = (totalMinutes / 60 + 6) % 24; // Minecraft day starts at 6 AM
        long minutes = totalMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    /**
     * Converts a time string to Minecraft ticks.
     * Supported formats:
     * - "HH:MM" (24-hour format)
     * - "H[H]am" or "H[H]pm" (12-hour format)
     * - "NNNticks" (direct tick value)
     * @param timeStr the time string to convert
     * @return the equivalent time in ticks
     * @throws NumberFormatException if the format is invalid
     */
    public static int timeStringToTicks(String timeStr) throws NumberFormatException {
        timeStr = timeStr.toLowerCase();
        if (timeStr.endsWith("ticks")) {
            return Integer.parseInt(timeStr.replace("ticks", "").trim());
        } else if (timeStr.contains(":")) {
            String[] parts = timeStr.split(":");
            int hours = Integer.parseInt(parts[0].trim());
            int minutes = Integer.parseInt(parts[1].trim());
            return ((hours + 18) % 24) * 1000 + (minutes * 1000 / 60);
        } else if (timeStr.endsWith("am") || timeStr.endsWith("pm")) {
            boolean isPM = timeStr.endsWith("pm");
            int hours = Integer.parseInt(timeStr.replace("am", "").replace("pm", "").trim());
            if (isPM && hours != 12) hours += 12;
            if (!isPM && hours == 12) hours = 0;
            return ((hours + 18) % 24) * 1000;
        } else {
            throw new NumberFormatException("Invalid time format");
        }
    }
}
