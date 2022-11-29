public class LAB {
public static void main (String args[])
{
    double[] array = {1,2,3,4,5,6,7,8,9,10};

    System.out.println(max(array));
    System.out.println(sum(array));
    System.out.println(ave(array));
}
    public static double max(double[] arr) {
        return max(arr,0);
    }

    public static double max(double[] arr, int count) {
        if (count == arr.length - 1) return arr[count];
        return Math.max(arr[count], max(arr, count + 1));
    }

    public static double sum (double[] arr) {
        return sum(arr, 0);
    }
    
    public static double sum (double[] arr, int count){
        if (count == arr.length - 1) return arr[count];
        return arr[count] + sum(arr, count + 1);
    }

    public static double ave(double[] arr) {
        return sum(arr) / arr.length;
    }
}
