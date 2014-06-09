package de.crawling.spider.idea.plugin.mapper.model;

/**
 * Created by sscheffler on 09.06.14.
 */
public enum DefaultPrimitiveTypes {
    BOOLEAN("boolean", Boolean.class.getCanonicalName(), "true", Boolean.TYPE),
    CHAR("char", Character.class.getCanonicalName(),"'a'", Character.TYPE),
    BYTE("byte", Byte.class.getCanonicalName(), "Byte.valueOf(\"98\")", Byte.TYPE),
    SHORT("short", Short.class.getCanonicalName(), "Short.valueOf(\"0\")", Short.TYPE),
    INTEGER("int", Integer.class.getCanonicalName(), "1", Integer.TYPE),
    LONG("long", Long.class.getCanonicalName(), "1L", Long.TYPE),
    FLOAT("float", Float.class.getCanonicalName(), "1.0f", Float.TYPE),
    DOUBLE("double", Double.class.getCanonicalName(), "1.0d", Double.TYPE);

    private final String primName;
    private final String complName;
    private final String defaultValue;
    private Class type;

    DefaultPrimitiveTypes(String primName, String complName, String defaultValue, Class type) {
        this.primName = primName;
        this.complName = complName;
        this.defaultValue = defaultValue;
        this.type = type;
    }

    public Class getType() {
        return type;
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

    public static DefaultPrimitiveTypes fromValue(String str){
        for(DefaultPrimitiveTypes type : DefaultPrimitiveTypes.values()){
            if(type.getPrimName().equals(str) || type.complName.equals(str)){
                return type;
            }
        }
        return null;
    }

}
