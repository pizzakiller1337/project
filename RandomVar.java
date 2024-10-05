import java.util.*;

public class RandomVar {
    public List<Integer> idArray = new ArrayList<>();
    public int[] arr = new int[36];
    public void randVar() {
        int j=0;
        String outArr ="";
        for (int i = 0; i < 36; i++) {
            j++;
            if (j<=10)
            {
                arr[i]=j;
                outArr+=arr[i]+" ";
            }
            else
            {
                j=0; i--;
            }
        }
        System.out.println(outArr);

        // random
        Random random = new Random();

        for (int i=0; i<36;i++)
        {
            int k = random.nextInt(0,36);
            int buf = arr[i];
            arr[i]=arr[k];
            arr[k]=buf;
        }
        outArr="";
        for (int i=0; i<36;i++)
        {
            outArr += arr[i] + " ";

        }
        System.out.println(outArr);
        MyDataBase myDataBase = new MyDataBase();
    }
}
