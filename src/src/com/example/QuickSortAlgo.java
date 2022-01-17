package src.com.example;

import java.util.List;
import java.util.concurrent.RecursiveAction;
public class QuickSortAlgo<T extends Comparable> extends RecursiveAction{
    private List<T> list;
    private int left;
    private int right;

    public QuickSortAlgo(List<T> list)
    {
        this.list = list;
        this.left = 0;
        this.right = list.size() - 1;
    }

    public QuickSortAlgo(List<T> list, int left, int right)
    {
        this.list = list;
        this.left = left;
        this.right = right;
    }

    protected void compute()
    {
        if(left < right)
        {
            int pivotIndex = left + ((right - left)/2);

            pivotIndex = partition(pivotIndex);

            invokeAll(new QuickSortAlgo(list, left, pivotIndex - 1), new QuickSortAlgo(list, pivotIndex + 1, right));
        }
    }

    private int partition(int pivotIndex)
    {
        T pivotValue = list.get(pivotIndex);
        swap(pivotIndex, right);

        int storeIndex = left;

        for(int i = left; i < right; ++i)
        {
            if(list.get(i).compareTo(pivotValue) < 0)
            {
                swap(i, storeIndex);
                ++storeIndex;
            }    
        }

        swap(storeIndex, right);
        return storeIndex;
        
    }
    
    private void swap(int i, int j)
    {
        if(i != j)
        {
            T iValue = list.get(i);

            list.set(i, list.get(j));
            list.set(j, iValue);
        }
    }

}