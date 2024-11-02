package com.event_manager.schemas;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "events")
public class Event {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uniqueEventID")
    @NonNull
    private int uniqueID;

    @ColumnInfo(name = "eventID")
    private String eventId;

    @ColumnInfo(name = "eventName")
    private String eventName;

    public int getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(int uniqueID) {
        this.uniqueID = uniqueID;
    }

    @ColumnInfo(name = "eventCategoryID")
    private String eventCategoryId;

    @ColumnInfo(name = "ticketsAvailable")
    private String ticketsAvailable;

    @ColumnInfo(name = "isEventActive")
    private boolean isEventActive;

    public Event(String eventId, String eventName, String eventCategoryId, String ticketsAvailable, boolean isEventActive) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventCategoryId = eventCategoryId;
        this.ticketsAvailable = ticketsAvailable;
        this.isEventActive = isEventActive;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventCategoryId() {
        return eventCategoryId;
    }

    public void setEventCategoryId(String eventCategoryId) {
        this.eventCategoryId = eventCategoryId;
    }

    public String getTicketsAvailable() {
        return ticketsAvailable;
    }

    public void setTicketsAvailable(String ticketsAvailable) {
        this.ticketsAvailable = ticketsAvailable;
    }

    public boolean isEventActive() {
        return isEventActive;
    }

    public void setEventActive(boolean eventActive) {
        isEventActive = eventActive;
    }
}
