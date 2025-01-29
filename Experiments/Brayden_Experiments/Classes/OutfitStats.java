package Classes;

import java.util.Date;

public class OutfitStats extends ConvertsToJSON{
    private Integer outfit_tracked_stats_id;
    private Integer outfit_id;
    private Date date_worn;
    private Float high_temp;
    private Float low_temp;

    public OutfitStats(Integer outfit_tracked_stats_id, Integer outfit_id, Date date_worn, Float high_temp, Float low_temp) {
        this.outfit_tracked_stats_id = outfit_tracked_stats_id;
        this.outfit_id = outfit_id;
        this.date_worn = date_worn;
        this.high_temp = high_temp;
        this.low_temp = low_temp;
    }

    public Integer getOutfit_tracked_stats_id() {
        return outfit_tracked_stats_id;
    }

    public Integer getOutfit_id() {
        return outfit_id;
    }

    public Date getDate_worn() {
        return date_worn;
    }

    public Float getHigh_temp() {
        return high_temp;
    }

    public Float getLow_temp() {
        return low_temp;
    }

    @Override
    public String toJSON() {
        return "{" +
                "\"outfit_tracked_stats_id\": \"" + escapeJSON(outfit_tracked_stats_id.toString()) + "\"," +
                "\"outfit_id\": \"" + escapeJSON(outfit_id.toString()) + "\"," +
                "\"date_worn\": \"" + (date_worn != null ? dateFormat.format(date_worn) : null) + "\"," +
                "\"high_temp\": \"" + escapeJSON(high_temp.toString()) + "\"" +
                "\"low_temp\": \"" + escapeJSON(low_temp.toString()) + "\"" +
                "}";
    }
}
