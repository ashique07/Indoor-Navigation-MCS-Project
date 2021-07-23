package com.example.indoornavigation;

public class TrackLocationModel {

    private String x;
    private String y;
    private String z;
    private String roomNumber;
    private String distance;

    TrackLocationModel(String x, String y, String z, String roomNumber, String distance)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.roomNumber = roomNumber;
        this.distance = distance;
    }

    public String getX()
    {
        return x;
    }
    public String getY()
    {
        return y;
    }
    public String getZ()
    {
        return z;
    }
    public String getRoomNumber()
    {
        return roomNumber;
    }
    public String getDistance()
    {
        return distance;
    }

    public String toString()
    {
        return x + " " + y + " " + z + " " + roomNumber + " " + distance;
    }
}