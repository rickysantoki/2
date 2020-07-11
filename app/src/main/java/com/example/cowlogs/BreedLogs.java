package com.example.cowlogs;

public class BreedLogs {
    private int id;
    private String breed_id;
    private String weight;
    private String age;
    private String condition;
    private String Start;
    private int breedType;

   public static final String TABLE_NAME = "CowEntries";
   public static final String COLUMN_ID = "_id";
    public static final String COLUMN_BREED_ID = "breed_id";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_CONDITION = "condition";
    public static final String COLUMN_START = "Start";
    public static final String COLUMN_BREED_INDEX = "breed_index";


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "breed_id" + " TEXT,"
                    + "weight" + " TEXT,"
                    + "age"+ " TEXT,"
                    + "condition"+ " TEXT,"
                    + "Start" + " TEXT,"
                    + "Breed_Index"+ " INTEGER)";



    public BreedLogs() {

    }

    public BreedLogs( String breedid, String w, String a, String c, String startview) {
        this.breed_id = breedid;
        this.weight = w;
        this.age = a;
        this.condition = c;
        this.Start = startview;


    }

    public int getBreedType() {
        return breedType;
    }

    public void setBreedType(int breedType) {
        this.breedType = breedType;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getbreedId() {
        return breed_id;
    }
    public void setbreedId(String breedid) {
        this.breed_id = breedid;
    }

    public String getweight() {
        return weight;
    }
    public void setweight(String w) {
        this.weight = w;
    }

    public String getage() {
        return age;
    }
    public void setage(String a) {
        this.age = a;
    }

    public String getcondition() {
        return condition;
    }
    public void setcondition(String c) {
        this.condition = c;
    }

    public String getStart() {
        return Start;
    }
    public void setStart(String startview) {
        this.Start = startview;
    }

    public String format() {
        return breed_id + " " + weight + " " + age + " " + condition+ " " + Start;
    }

    @Override
    public String toString() {
        return "BreedLogs{" +
                "breed_id='" + breed_id + '\'' +
                ", weight='" + weight + '\''+
                ", age='" + age + '\''+
                ", condition='" + condition + '\''+
                ", start='" + Start + '\''
                + '}';
    }
}
