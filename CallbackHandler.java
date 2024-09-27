import java.util.concurrent.*;
import java.util.HashMap;

public class CallbackHandler {

    private HashMap<String, Callable<?>> dict;

    public CallbackHandler()
    {
        dict = new HashMap<>();
    }

    public void addCallback(String key, Callable<?> function)
    {
        if(function != null)
            dict.put(key, function);
    }

    @SuppressWarnings("unchecked")
    public <T> T callback(String key) throws CallbackException
    {
        try
        {
            return (T)dict.get(key).call();
        }
        catch(Exception e)
        {
            throw new CallbackException(e);
        }
    }
}
