package it.unibo.trummervocalization.main;

public class Configuration {

    // Use commented values to work with flights db
    public static final boolean COMPLETE_TREE = true;
    public static final int N_REFINEMENTS = 3;
    public static final int N_SIGNIFICANT_DIGITS = 2;
    // public static final int N_SIGNIFICANT_DIGITS = 1;
    public static final double ST_DEV_FACTOR = 0.1;
    // public static final double ST_DEV_FACTOR = 0.5;
    public static final double P_RANGE_FACTOR = 0.1;
    // public static final double P_RANGE_FACTOR = 0.5;
    public static final long MAX_CHILDREN = 25;
    public static final long MAX_GROUPS = 1500;
    public static final long CHARACTER_MILLIS = 80;
    public static final String CUBE_NAME = "sales_fact_1997";
    // public static final String CUBE_NAME = "flights";
    public static final String CUBE_XML = "FoodMart.xml";
    // public static final String CUBE_XML = "FlightDelays.xml";
    public static final String OUTPUT_PATH = "outputs";
    public static final String INPUT_ERROR = "Input query is not supported by the vocalization system.";
    public static final String NO_ENTRY_ERROR = "Input query doesn't match any database record.";
    public static final String TOO_FINE_ERROR = "Aggregation level is too fine for the vocalization system, go up to a coarser level.";

}
