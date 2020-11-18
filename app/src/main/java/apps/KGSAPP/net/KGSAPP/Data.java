package apps.KGSAPP.net.KGSAPP;

import java.io.Serializable;

/**
 * Created by yunseokchoi on 2017. 4. 19..
 */

public class Data implements Serializable {

    boolean isboolean;
    String str;
    String strArray[];
    int i;
    int intArray[];

    public Data(boolean isboolean, String str, String[] strArray, int i, int[] intArray)
    {
        this.isboolean = isboolean;
        this.str = str;
        this.strArray = strArray;
        this.i = i;
        this.intArray = intArray;
    }

}
