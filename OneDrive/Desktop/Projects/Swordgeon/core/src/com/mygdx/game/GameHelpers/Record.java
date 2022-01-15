package com.mygdx.game.GameHelpers;

/**
 * Used to gather and transfer Record data to the database
 */
public class Record {
    private float time;
    private String name;

    /**
     * creates a record to be added to the database
     * @param time time in milliseconds of the record
     * @param name name of the record holder
     */
    public Record(float time, String name) {
        this.time = time;
        this.name = name;
    }

    /**
     * creates a record with default values name = 'AAA' and time = 600f
     */
    public Record(){
        name = "AAA";
        time = 600f;
    }

    /**
     * formats the time to be displayed on the RecordScreen
     * @return formatted time Screen
     */
    public String getTimeString(){
        int minutes = (int) time / 60;
        int seconds = (int) time - minutes * 60;
        int milliseconds = (int) (time % 1 * 100);
        return String.format("%02d:%02d.%02d", minutes, seconds, milliseconds);
    }

    public float getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Record{" +
                "time=" + time +
                ", name='" + name + '\'' +
                '}';
    }
}
