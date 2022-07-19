package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;
    Map<T, Integer> indexBook;
    int pin;
    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public UnionBySizeCompressingDisjointSets() {
        pointers = new ArrayList<>();
        indexBook = new HashMap<>();
        pin = 0;
    }

    @Override
    public void makeSet(T item) {
        indexBook.put(item, pin);
        pointers.add(-1);
        pin++;
    }

    @Override
    public int findSet(T item) {
        if (!indexBook.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        int index = indexBook.get(item);
        while (index >= 0 && pointers.get(index) >= 0) {
            index = pointers.get(index);
        }
        // path compression
        int dadIndex = indexBook.get(item);
        while (dadIndex >= 0 && pointers.get(dadIndex) >= 0) {
            int sonPosition = dadIndex;
            dadIndex = pointers.get(dadIndex);
            pointers.set(sonPosition, index);
        }
        return index;
    }

    @Override
    public boolean union(T item1, T item2) {
        if (!indexBook.containsKey(item1) || !indexBook.containsKey(item2)) {
            throw new IllegalArgumentException();
        }
        int dadOne = findSet(item1);
        int dadTwo = findSet(item2);
        boolean toBeOrNot = false;
        if (dadOne != dadTwo) {
            toBeOrNot = true;
            if (-1 * pointers.get(dadOne) >= -1 * pointers.get(dadTwo)) {
                pointers.set(dadOne, pointers.get(dadOne) + pointers.get(dadTwo));
                pointers.set(dadTwo, dadOne);
            } else {
                pointers.set(dadTwo, pointers.get(dadOne) + pointers.get(dadTwo));
                pointers.set(dadOne, dadTwo);
            }
        }
        return toBeOrNot;
    }
}
