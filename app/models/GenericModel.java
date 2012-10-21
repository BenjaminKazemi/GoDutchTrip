package models;

import com.google.common.collect.ImmutableList;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import play.db.jpa.Model;
import util.annotation.IgnoreGSon;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 9/22/12
 * Time: 5:16 PM
 * To change this template use File | Settings | File Templates.
 */

public abstract class GenericModel extends Model {
    public String toJson() {
        return new GsonBuilder().setExclusionStrategies(new MyExclusionStrategy()).create().toJson( this );
    }

    public String toParams(String objName) throws IllegalAccessException, UnsupportedEncodingException {
        String params = "";
        Iterator<Field> fields = ImmutableList.copyOf( getClass().getFields() ).iterator();
        String klassName = objName;
        if( klassName == null ) {
            klassName = getClass().getSimpleName().toLowerCase();
        }
        while( fields.hasNext() ) {
            Field tmp = fields.next();
            if( tmp.get( this ) != null ) {
                Object value = tmp.get(this);
                if( value instanceof Date) {
                    value = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format( value );
                    params += klassName + "." + tmp.getName() + "=" + URLEncoder.encode( value.toString(), Charset.defaultCharset().toString() );
                } else if( value instanceof List) {
                    List list = (List)value;
                    int j = 0;
                    for( Object item : list ) {
                        params += tmp.getName() + "[" + j++ + "]=" + URLEncoder.encode( item.toString(), Charset.defaultCharset().toString() );
                        if( j < list.size() ) {
                            params += "&";
                        }
                    }
                } else if( value instanceof GenericModel ) {
                    params += ((GenericModel)value).toParams(klassName + "." + tmp.getName());
                } else {
                    params += klassName + "." + tmp.getName() + "=" + URLEncoder.encode( value.toString(), Charset.defaultCharset().toString() );
                }
                if( fields.hasNext() ) {
                    params += "&";
                }
            }
        }

        return params;
    }

    public class MyExclusionStrategy implements ExclusionStrategy {
        private MyExclusionStrategy() {
        }

        public boolean shouldSkipClass(Class<?> clazz) {
            return clazz.getAnnotation(IgnoreGSon.class) != null;
        }

        public boolean shouldSkipField(FieldAttributes f) {
            return f.getAnnotation(IgnoreGSon.class) != null;
        }
    }
}
