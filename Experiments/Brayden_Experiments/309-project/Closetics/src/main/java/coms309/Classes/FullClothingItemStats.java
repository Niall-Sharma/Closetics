package coms309.Classes;

import java.util.Arrays;

public class FullClothingItemStats extends ConvertsToJSON{
    private Integer item_id;
    private ClothingItemStats[] combine_item_stats;
    private Integer total_times_worn;
    private Float average_high_temp;
    private Float average_low_temp;
    private Integer[] times_worn_each_month = new Integer[12];
    private Integer times_worn_this_year;
    private Integer times_worn_this_month;

    public FullClothingItemStats(Integer item_id, ClothingItemStats[] combine_item_stats) {
        this.item_id = item_id;
        this.combine_item_stats = combine_item_stats;
        calcTotal_times_worn();
    }

    public ClothingItemStats[] getCombine_item_stats() {
        return combine_item_stats;
    }

    public void setCombine_item_stats(ClothingItemStats[] combine_item_stats) {
        this.combine_item_stats = combine_item_stats;
    }

    public Integer getTotal_times_worn() {
        return total_times_worn;
    }

    public void calcTotal_times_worn() {
        total_times_worn = combine_item_stats.length;
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

    public void calcAverage_low_temp(Float average_low_temp) {
        //TODO
    }

    public Integer[] getTimes_worn_each_month() {
        return times_worn_each_month;
    }

    public void calcTimes_worn_each_month(Integer[] times_worn_each_month) {
        //TODO
    }

    public Integer getTimes_worn_this_year() {
        return times_worn_this_year;
    }

    public void calcTimes_worn_this_year() {
        //TODO
    }

    @Override
    public String toJSON() {
        return "{" +
                "\"item_id\": \"" + escapeJSON(item_id.toString()) + "\"," +
                "\"total_times_worn\": \"" + escapeJSON(total_times_worn.toString()) + "\"," +
                "\"average_high_temp\": \"" + escapeJSON(average_high_temp.toString()) + "\"" +
                "\"average_low_temp\": \"" + escapeJSON(average_low_temp.toString()) + "\"" +
                "\"times_worn_each_month\": \"" + escapeJSON(Arrays.toString(times_worn_each_month)) + "\"" +
                "\"times_worn_this_year\": \"" + escapeJSON(times_worn_this_year.toString()) + "\"" +
                "\"times_worn_this_month\": \"" + escapeJSON(times_worn_this_month.toString()) + "\"" +
                "}";
    }
}
