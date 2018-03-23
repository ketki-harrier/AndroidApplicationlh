package com.lifecyclehealth.lifecyclehealth.dto;

/**
 * Created by vaibhavi on 23-01-2018.
 */

public class NotificationFilterDto {

    private boolean IncludeArchived;
    private boolean Episode_Level;

    public boolean isIncludeArchived() {
        return IncludeArchived;
    }

    public void setIncludeArchived(boolean includeArchived) {
        IncludeArchived = includeArchived;
    }

    public boolean isEpisode_Level() {
        return Episode_Level;
    }

    public void setEpisode_Level(boolean episode_Level) {
        Episode_Level = episode_Level;
    }
}
