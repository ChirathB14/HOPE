package com.example.artists.model;

import android.net.Uri;

public class Event {
    private String eventId;
    private String eventName;
    private String eventDate;
    private String eventVenue;
    private String eventDescription;
    private String eventCategory;
    private String eventContactName;
    private String eventContactNumber;
    private String eventContactEmail;
    private String eventContactNIC;
    private String eventCvrImgUrl;



    public Event(){

    }

    public Event(String eventId, String eventName, String eventDate,
                 String eventVenue, String eventDescription,
                 String eventCategory, String eventContactName,
                 String eventContactNumber, String eventContactEmail,
                 String eventContactNIC) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventVenue = eventVenue;
        this.eventDescription = eventDescription;
        this.eventCategory = eventCategory;
        this.eventContactName = eventContactName;
        this.eventContactNumber = eventContactNumber;
        this.eventContactEmail = eventContactEmail;
        this.eventContactNIC = eventContactNIC;
    }

    public Event(String id, String name, String venue) {
        this.eventId = id;
        this.eventName = name;
        this.eventVenue = venue;
    }

    public Event(String eventId, String event_name, String event_date, String event_venue,
                 String event_description, String event_category, String event_contact_name,
                 String event_contact_number, String event_contact_mail, String event_contact_nic,
                 String uri) {
        this.eventId = eventId;
        this.eventName = event_name;
        this.eventDate = event_date;
        this.eventVenue = event_venue;
        this.eventDescription = event_description;
        this.eventCategory = event_category;
        this.eventContactName = event_contact_name;
        this.eventContactNumber = event_contact_number;
        this.eventContactEmail = event_contact_mail;
        this.eventContactNIC = event_contact_nic;
        this.eventCvrImgUrl = uri;
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

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventContactName() {
        return eventContactName;
    }

    public void setEventContactName(String eventContactName) {
        this.eventContactName = eventContactName;
    }

    public String getEventContactNumber() {
        return eventContactNumber;
    }

    public void setEventContactNumber(String eventContactNumber) {
        this.eventContactNumber = eventContactNumber;
    }

    public String getEventContactEmail() {
        return eventContactEmail;
    }

    public void setEventContactEmail(String eventContactEmail) {
        this.eventContactEmail = eventContactEmail;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public String getEventContactNIC() {
        return eventContactNIC;
    }

    public void setEventContactNIC(String eventContactNIC) {
        this.eventContactNIC = eventContactNIC;
    }


}
