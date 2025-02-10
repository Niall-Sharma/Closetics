package coms309.Classes;

import java.time.Year;
import java.util.Arrays;
import java.util.Date;

public class FullOutfitStats extends ConvertsToJSON {
    private Integer outfit_id;
    private OutfitStats[] combine_outfit_stats;
    private Integer total_times_worn;
    private Float average_high_temp;
    private Float average_low_temp;
    private Integer[] times_worn_each_month = new Integer[12];
    private Integer times_worn_this_year;
    private Integer times_worn_this_month;

    public FullOutfitStats(Integer outfit_id, OutfitStats[] combine_outfit_stats) {
        this.outfit_id = outfit_id;
        this.combine_outfit_stats = combine_outfit_stats;
        calcTotal_times_worn();
    }

    public OutfitStats[] getCombine_outfit_stats() {
        return combine_outfit_stats;
    }

    public void setCombine_outfit_stats(OutfitStats[] combine_outfit_stats) {
        this.combine_outfit_stats = combine_outfit_stats;
    }

    public Integer getTotal_times_worn() {
        return total_times_worn;
    }

    public void calcTotal_times_worn() {
        total_times_worn = combine_outfit_stats.length;
    }

    public Float getAverage_high_temp() {
        return average_high_temp;
    }

    public void calcAverage_high_temp() {
        //TODO
    }

    public Float getAverage_low_temp() {
        return average_low_temp;
    }

    public void calcAverage_low_temp() {
        //TODO
    }

    public Integer[] getTimes_worn_each_month() {
        return times_worn_each_month;
    }

    public void calcTimes_worn_each_month() {
        //TODO
    }

    public Integer getTimes_worn_this_year() {
        return times_worn_this_year;
    }

    public void calcTimes_worn_this_year() {
        Year thisYear = Year.now();
        int counter = 0;

        for (OutfitStats worn_outfit : combine_outfit_stats) {
            if (worn_outfit.getDate_worn().getYear() == thisYear.getValue()) {
                counter++;
            }
            times_worn_this_year = counter;
        }
    }

    public Integer getTimes_worn_this_month() {
        return times_worn_this_month;
    }

    public void calcTimes_worn_this_month() {
        //TODO
    }

    @Override
    public String toJSON() {
        return "{" +
                "\"outfit_id\": \"" + escapeJSON(outfit_id.toString()) + "\"," +
                "\"total_times_worn\": \"" + escapeJSON(total_times_worn.toString()) + "\"," +
                "\"average_high_temp\": \"" + escapeJSON(average_high_temp.toString()) + "\"" +
                "\"average_low_temp\": \"" + escapeJSON(average_low_temp.toString()) + "\"" +
                "\"times_worn_each_month\": \"" + escapeJSON(Arrays.toString(times_worn_each_month)) + "\"" +
                "\"times_worn_this_year\": \"" + escapeJSON(times_worn_this_year.toString()) + "\"" +
                "\"times_worn_this_month\": \"" + escapeJSON(times_worn_this_month.toString()) + "\"" +
                "}";
    }
}