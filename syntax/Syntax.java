package syntax;

import java.lang.reflect.Method;
import relation.*;

public class Syntax {
    
    Method method;
    String request;
    String value;

    public void setRequest(String request) {
        this.request = request;
    }
    public String getValue() {
        return value;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRequest() {
        return request;
    }
    
    public Method getMethod() {
        return method;
    }

    public Relation execute(Relation relation) throws Exception {
        return (Relation) method.invoke(relation, value);
    }

    public Syntax(String request, Method fonction) {
        setRequest(request);
        setMethod(fonction);
    }

    public Syntax(String request, Method fonction, String value) {
        setRequest(request);
        setMethod(fonction);
        setValue(value);
    }
}