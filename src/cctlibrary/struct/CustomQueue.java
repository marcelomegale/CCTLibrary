package cctlibrary.struct;

import java.util.ArrayList;
import java.util.List;

/**
 * A queue is an ordered set of items from which you can remove items of the 
 * beginning and insert items at the end.
 */
public class CustomQueue<T> {
    
    private static final int EMPTY_INDEX = -1;
    
    private static final int FIRST_INDEX = 0; 
    
    private List<T> items;

    public CustomQueue() {
        this.items = new ArrayList();
    }
    
    public void insert(T object) {
        this.items.add(object);
    }
    
    public T remove() {
        if (!this.items.isEmpty()) {
            return this.items.remove(FIRST_INDEX);
        }
        
        return null;
    }
    
    public boolean remove(T object) {
        return this.items.remove(object);
    }
    
    public int getSize() {
        return this.items.size();
    }
    
    public T get(int index) {
        if (index > EMPTY_INDEX && index < this.items.size()) {
            return this.items.get(index);
        }
        return null;
    }
    
    public void set(int index, T object) {
        if (index > EMPTY_INDEX && index < this.items.size()) {
            this.items.set(index, object);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        
        if (this.items.size() > 0) {
            for (int i = 0; i < this.items.size(); i++) {
                T item = this.items.get(i);
                sb.append(item.toString());
                sb.append("\n");
            }
        }
        
        return sb.toString();
    }

}
