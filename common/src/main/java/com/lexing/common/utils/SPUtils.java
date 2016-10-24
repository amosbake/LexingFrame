package com.lexing.common.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;

public class SPUtils {

    private static SharedPreferences spDefault;
    private static SharedPreferences.Editor editorDefault;
    
    private static HashMap<String, SharedPreferences> spAppointedMap = new HashMap<>();
    private static HashMap<String, SharedPreferences.Editor> editorAppointedMap = new HashMap<>();

    public static void setParamAppointed(Context context , String key, Object object, String spName){
        
        String type = object.getClass().getSimpleName();
        SharedPreferences.Editor editorExtra = getEditorExtra(context, spName);
        if("String".equals(type)){  
        	editorExtra.putString(key, (String)object);
        }  
        else if("Integer".equals(type)){  
        	editorExtra.putInt(key, (Integer)object);
        }  
        else if("Boolean".equals(type)){  
        	editorExtra.putBoolean(key, (Boolean)object);
        }  
        else if("Float".equals(type)){  
        	editorExtra.putFloat(key, (Float)object);
        }  
        else if("Long".equals(type)){  
        	editorExtra.putLong(key, (Long)object);
        }  
          
        editorExtra.commit();  
    }  

    public static Object getParamAppointed(Context context , String key, Object defaultObject, String spName){
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = getSpExtra(context, spName);
        
        if("String".equals(type)){  
            return sp.getString(key, (String)defaultObject);
        }  
        else if("Integer".equals(type)){  
            return sp.getInt(key, (Integer)defaultObject);
        }  
        else if("Boolean".equals(type)){  
            return sp.getBoolean(key, (Boolean)defaultObject);
        }  
        else if("Float".equals(type)){  
            return sp.getFloat(key, (Float)defaultObject);
        }  
        else if("Long".equals(type)){  
            return sp.getLong(key, (Long)defaultObject);
        }  
          
        return null;  
    }  

    public static void setParamDefault(Context context , String key, Object object){
        
        String type = object.getClass().getSimpleName();
        getEditorDefault(context);
        if("String".equals(type)){  
        	editorDefault.putString(key, (String)object);
        }  
        else if("Integer".equals(type)){  
        	editorDefault.putInt(key, (Integer)object);
        }  
        else if("Boolean".equals(type)){  
        	editorDefault.putBoolean(key, (Boolean)object);
        }  
        else if("Float".equals(type)){  
        	editorDefault.putFloat(key, (Float)object);
        }  
        else if("Long".equals(type)){  
        	editorDefault.putLong(key, (Long)object);
        }  
          
        editorDefault.commit();  
    }  

    public static void removeParam(Context context, String key){
        getEditorDefault(context);
        editorDefault.remove(key);
        editorDefault.commit();
    }

    public static void clearParam(Context context){
        getEditorDefault(context);
        editorDefault.clear();
        editorDefault.commit();
    }

    public static void removeParamAppointed(Context context, String key, String spName){
        SharedPreferences.Editor editor = getEditorExtra(context, spName);
        editor.remove(key);
        editor.commit();
    }

    public static void clearParamAppointed(Context context, String spName){
        SharedPreferences.Editor editor = getEditorExtra(context, spName);
        editorDefault.clear();
        editorDefault.commit();
    }

    public static Object getParamDefault(Context context , String key, Object defaultObject){
        String type = defaultObject.getClass().getSimpleName();
        getEditorDefault(context);
        
        if("String".equals(type)){  
            return spDefault.getString(key, (String)defaultObject);
        }  
        else if("Integer".equals(type)){  
            return spDefault.getInt(key, (Integer)defaultObject);
        }  
        else if("Boolean".equals(type)){  
            return spDefault.getBoolean(key, (Boolean)defaultObject);
        }  
        else if("Float".equals(type)){  
            return spDefault.getFloat(key, (Float)defaultObject);
        }  
        else if("Long".equals(type)){  
            return spDefault.getLong(key, (Long)defaultObject);
        }  

        return null;
    }

    private static SharedPreferences.Editor getEditorExtra(Context context, String spName){
        SharedPreferences.Editor spEditor = editorAppointedMap.get(spName);
        if(spEditor == null){
            SharedPreferences sp = getSpExtra(context, spName);
            spEditor = sp.edit();
            editorAppointedMap.put(spName, spEditor);
        }
        return spEditor;
    }

    private static SharedPreferences getSpExtra(Context context, String spName){
        SharedPreferences sp = spAppointedMap.get(spName);
        if(sp == null){
            sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
            spAppointedMap.put(spName, sp);
        }
        return sp;
    }

    private static void getEditorDefault(Context context){
        if(spDefault == null){
        	spDefault = PreferenceManager.getDefaultSharedPreferences(context);
        	
        }
        if(editorDefault == null){
        	editorDefault = spDefault.edit();  
        }
    }
    
    public static boolean defaultHasKey(Context context, final String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).contains(
                key);
    }

    public static boolean appointedHasKey(Context context, final String key, String spName) {
        return getSpExtra(context, spName).contains(
                key);
    }
}  