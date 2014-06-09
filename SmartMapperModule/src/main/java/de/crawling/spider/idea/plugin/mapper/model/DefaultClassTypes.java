package de.crawling.spider.idea.plugin.mapper.model;

/**
 * Created by sscheffler on 09.06.14.
 */
enum DefaultClassTypes {
    BOOLEAN("boolean", Boolean.class.getCanonicalName(), "true"),
    CHAR("char", Character.class.getCanonicalName(),"'a'"),
    BYTE("byte", Byte.class.getCanonicalName(), "Byte.valueOf(\"98\")"),
    SHORT("short", Short.class.getCanonicalName(), "Short.valueOf(\"0\")"),
    INTEGER("int", Integer.class.getCanonicalName(), "1"),
    LONG("long", Long.class.getCanonicalName(), "1L"),
    FLOAT("float", Float.class.getCanonicalName(), "1.0f"),
    DOUBLE("double", Double.class.getCanonicalName(), "1.0d");

    private final String primName;
    private final String complName;
    private final String defaultValue;

    DefaultClassTypes(String primName, String complName, String defaultValue) {
        this.primName = primName;
        this.complName = complName;
        this.defaultValue = defaultValue;
    }

    public String getPrimName() {
        return primName;
    }

    public String getComplName() {
        return complName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public static DefaultClassTypes fromValue(String str){
        for(DefaultClassTypes type : DefaultClassTypes.values()){
            if(type.getPrimName().equals(str) || type.complName.equals(str)){
                return type;
            }
        }
        return null;
    }

}
