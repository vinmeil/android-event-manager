package com.fit2081.a2.schemas;

public class Event {
    private String eventId;
    private String eventName;
    private String eventCategoryId;
    private String ticketsAvailable;
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
