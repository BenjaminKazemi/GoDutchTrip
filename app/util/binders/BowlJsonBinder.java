package util.binders;

import models.Bowl;
import play.data.binding.Global;
import play.data.binding.TypeBinder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 9/22/12
 * Time: 6:42 PM
 * To change this template use File | Settings | File Templates.
 */

@Global
public class BowlJsonBinder implements TypeBinder<Bowl> {
    @Override
    public Object bind(String name, Annotation[] annotations, String value, Class actualClass, Type genericType) throws Exception {
        Bowl b = new Bowl();
        return b;
    }
}
