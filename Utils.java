public class Utils
{
    public static int sign(int number)
    {
        if(number < 0)
            return -1;
        else if(number > 0)
            return 1;
            
        return 0;
    }
}