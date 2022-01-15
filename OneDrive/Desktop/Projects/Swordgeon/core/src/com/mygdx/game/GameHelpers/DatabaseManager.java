package com.mygdx.game.GameHelpers;


import java.sql.*;

/**
 * Deals with the serialization of records and runtime configurations to the SQLite database
 */
public class DatabaseManager {

    /**
     * Establishes a connection to the database
     * @return the Connection object if it was loaded successfully
     */
    public static Connection connect(){
        Connection conn = null;
        try {
            // create a connection to the database
            String url = "jdbc:sqlite:swordgeon.db";
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            CrashLogHandler.logSevere("There has been a problem connecting to the database: \n", e.getMessage());
        }
        return conn;
    }

    /**
     * Creates tables for the records and runtime configurations. Fills the records table with dummy data until the user
     * creates a new record by completing the level
     */
    public static void createTables(){
        for(int i = 0; i < 4; i++){
            String sql = "CREATE TABLE IF NOT EXISTS records" + (i + 1) + "(\n"
                    + "id INTEGER PRIMARY KEY,\n"
                    + "name TEXT NOT NULL, \n"
                    + "time REAL NOT NULL \n"
                    + ");";
            runSql(sql);
            for(int j = 0; j < 5; j++){
                insert(new Record(), i + 1);
            }
        }
        String sql = "CREATE TABLE IF NOT EXISTS runtime_configs (\n" +
                "record_name TEXT NOT NULL, \n" +
                "sound_level INT NOT NULL, \n" +
                "sfx_level INT NOT NULL);";
        runSql(sql);
        if(isRuntimeConfigsEmpty()){
            sql = "INSERT INTO runtime_configs(record_name, sound_level, sfx_level) \n"
                    + "VALUES ('AAA', 50, 50);";
            runSql(sql);
        }
        EventLogHandler.log("Database tables have been created");
    }

    /**
     * Determines if the runtime_configs table has any data
     * @return if the runtime_configs table has data
     */
    private static boolean isRuntimeConfigsEmpty(){
        String sql = "SELECT * FROM runtime_configs;";
        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            conn.close();
            //rs.next() returns if there was is a result from the ResultSet
            return !rs.next();
        } catch (SQLException e){
            CrashLogHandler.logSevere("There has been an issue determining if the runtime configuration is empty. ",
                    e.getMessage());
            //The application will be closed before this could ever return
            return false;
        }
    }

    /**
     * Loads the runtime config data from the database and loads it to the RuntimeConfigs class
     */
    public static void loadRuntimeConfigData(){
        String sql = "SELECT record_name, sfx_level, sound_level FROM runtime_configs;";
        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            //columnIndexes start at 1. Order follows that of the SQL statement
            RuntimeConfigs.setName(rs.getString(1));
            RuntimeConfigs.setSfxVolume(rs.getInt(2));
            RuntimeConfigs.setMusicVolume(rs.getInt(3));
            EventLogHandler.log("Runtime config data has been loaded");
            conn.close();
        } catch (SQLException e){
            CrashLogHandler.logSevere("There has been an issue loading runtime data. ", e.getMessage());
        }
    }

    /**
     * Adds a swordgeon.db file to the directory if one does not exist
     */
    public static void createDatabase() {
        try {
            Connection conn = connect();
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                EventLogHandler.log("A new database has been created.");
                EventLogHandler.log("The driver is " + meta.getDriverName());
                conn.close();
            }
        } catch (SQLException e) {
            CrashLogHandler.logSevere("There has been an issue creating the database: \n", e.getMessage());
        }
    }

    /**
     * Gets the record data from the database
     * @param level level to gather the data from
     * @param num number of records to return
     * @return the specified number of records from the specified level
     * @see Record
     */
    public static Record[] getTopLevelData(int level, int num){
        String sql = "SELECT * FROM records" + level + "\n"
                + "ORDER BY \n"
                + "time ASC \n";
        Record[] records = new Record[num];
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            // loop through the result set
            int i = 0;
            while (rs.next() && i < num) {
                records[num - i - 1] = new Record(rs.getFloat("time"), rs.getString("name"));
                i++;
            }
            conn.close();
        } catch (SQLException e) {
            CrashLogHandler.logSevere("There has been an issue getting high score data: \n", e.getMessage());
        }
        return records;
    }

    /**
     * Adds a new Record to the SQL database
     * @param record record to add to the database
     * @param level determines which record table the record should be added to
     */
    public static void insert(Record record, int level){
        String sql = "INSERT INTO records" + level + "(name, time) VALUES(?,?);";
        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, record.getName());
            pstmt.setFloat(2, record.getTime());
            pstmt.executeUpdate();
            conn.close();
            EventLogHandler.log("A new record has been added to the database");
        } catch (SQLException e) {
            CrashLogHandler.logSevere("There has been an issue inserting high score data into the database. \n",
                    e.getMessage());
        }
    }

    /**
     * gets the current data from RuntimeConfigs and updates it within the runtime_Configs database
     * @see RuntimeConfigs
     */
    public static void updateRuntimeConfigs(){
        String sql = "UPDATE runtime_configs \n" +
                "SET record_name = ?, sound_level = ?, sfx_level = ?;";
        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            //parameterIndexes start at 1
            pstmt.setString(1, RuntimeConfigs.getName());
            pstmt.setInt(2, RuntimeConfigs.getMusicVolume());
            pstmt.setInt(3, RuntimeConfigs.getSfxVolume());
            pstmt.executeUpdate();
            conn.close();
            EventLogHandler.log("Runtime configs have been updated in the database");
        } catch(SQLException e){
            CrashLogHandler.logSevere("There has been an issue updating runtime configurations. ", e.getMessage());
        }
    }

    /**
     * Runs SQL statement and adds the changes to the database
     * @param sql String sql statement to run
     */
    private static void runSql(String sql){
        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            conn.close();
        } catch (SQLException e) {
            CrashLogHandler.logSevere("There has been an issue running a SQL statement \n", e.getMessage());
        }
    }

}
