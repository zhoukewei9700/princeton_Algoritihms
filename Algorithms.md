#                                                       Algorithms

## Stack and queues

#### stack: LIFO——last in first out

##### data structure:

```java
public class StackOfStrings {
    public StackOfStrings();
    public void push(String item);
    public String pop();
    public boolean isEmpty();
    public int size();
}
```

###### 用链表实现：

```java
public class LinkedStackOfStrings {
    private class Node {
        String item;
        Node next;
    }
    private Node first = null;
    public booolean isEmpty(){
        return first = null;
    }
    public void push(String item){
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
    }
    public String pop(){
        String item = first.item;
        first = first.next;
        return item;
    }
}
```

###### 用数组实现：

```java
//栈容量确定
//速度快
public class FixedCapacityStackOfStrings {
    private String[] = s;
    private int N = 0;
    public FixedCapacityStackOfStrings(int capacity){s = new String[capacity];}
    public boolean isEmpty(){return N==0;}
    public void push(String item){s[N++] = item;}
    public String pop(){return s[--N];}
}
```

**overflow and underflow**: 

- overflow: use resizing array for array implementation.
- underflow: throw exception if pop from an empty stack.

###### 扩缩容数组（满了则扩大两倍容量）

```java
public class ResizingArrayStackOfStrings() {
    private String[] = s;
    private int N = 0;
    public ResizingArrayStackOfStrings()
    { s = new String[1]; }
    public void push(String item){
        if(N==s.length){resize(2*s.length);}
        s[N++] = item;
    }
    private void resize(int capacity){
        String[] copy = new String[capacity];
        for(int i=0;i<N;i++){
            copy[i] = s[i];
        }
        s=copy;
    }
}
```

shrink操作不能选择在数组只有一半满的时候缩小，因为可能存在反复插入弹出的操作。而是选择在数组之一1/4满时将数组大减半

#### queue: FIFO——first in first out

![image-20230725200923624](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20230725200923624.png)

###### 链表实现：

```java
public class LinkedQueueOfStrings {
    private Node first,last;
    private class Node {
        String item;
        Node next;
    }
    public boolean isEmpty(){
        return first==null;
    }
    public void enqueue(String item){
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        if(isEmpty()) first = last;
        else oldlast.next = last;
    }
    public Stirng dequeue() {
        String item  = first.item;
        first = first.next;
        if(isEmpty()) last = null;
        return item;
    }
}
```

#### Iterator

```java
//Iterable 接口
public interface Iterable<Item> {
    Iterator<Item> iterator();
}
//Iterator 接口
public interface Iterator<Item> {
    boolean hasNext();
    Item next();
    void remove(); //optional: use at your own risk
}

//stack iterator:Linked-list implementation
public class Stack<Item> implements Iterable<Item> {
    //......
    public Iterator<Item> iterator(){
        return new ListIterator();
    }
    private class ListIterator implements Iterator<Item>
    {
        private Node current = first;
        public boolean hasNext(){return current != null;}
        public void remove(){}
        public Item next(){
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}

```

#### Bag

![image-20230726110926740](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20230726110926740.png)

#### *为什么Java不支持泛型数组

```java
List<String>[] ls = new ArrayList<String> [10];  //not allowed
List<String>[] ls = new ArrayList[10];           //allowed
```

why?

```java
List<String>[] lsa = new List<String>[10]; // Not really allowed.
Object o = lsa;
Object[] oa = (Object[]) o;
List<Integer> li = new ArrayList<Integer>();
li.add(new Integer(3));
oa[1] = li; // Unsound, but passes run time store check
String s = lsa[1].get(0); // Run-time error: ClassCastException.
```

由于JVM泛型的擦除机制，在运行时JVM是不知道泛型信息的，所以可以给oa[1]赋上一个[ArrayList](https://so.csdn.net/so/search?q=ArrayList&spm=1001.2101.3001.7020)<Integer>而不会出现ArrayStoreException，但是在取出数据的时候却要做一次类型转换，所以就会出现ClassCastException，如果可以进行泛型数组的声明，上面说的这种情况在编译期将不会出现任何的警告和错误，只有在运行时才会出错。而对泛型数组的声明进行限制，对于这样的情况，可以在编译期提示代码有类型安全问题，比没有任何提示要强很多。

基于以上的原因，Java不支持声明泛型数组，更确切地表达是：**数组的类型不可以是类型变量，除非是采用通配符的方式**

```java
List<?>[] lsa = new List<?>[10]; // OK, array of unbounded wildcard type.
Object o = lsa;
Object[] oa = (Object[]) o;
List<Integer> li = new ArrayList<Integer>();
li.add(new Integer(3));
oa[1] = li; // Correct.
String s = (String) lsa[1].get(0); // Run time error, but cast is explicit.
```

因为对于通配符的方式，最后取出数据是要做显式的类型转换的，所以并不会存在上一个例子的问题。

[参考博客](https://blog.csdn.net/orzlzro/article/details/7017435?spm=1001.2101.3001.6650.1&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-1-7017435-blog-122693960.235%5Ev38%5Epc_relevant_default_base&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-1-7017435-blog-122693960.235%5Ev38%5Epc_relevant_default_base&utm_relevant_index=2)



## Sorting

**Q:** Howcan sort() know how to compare data of type Double, String, and java.io.File without any information about the type of an item's key.

**A:** use Callbacl. Callback = reference to executable code.

- Client passes array of objects to sort() function.
- The sort() function calls back object's compareTo() method as needed.  

implementing callbacks.

- java: interfaces
- C: function pointers
- C++: class-type functors
- C#: delegates
- Python, Perl, ML, Javascripts: first-class functions

如何实现不同类型的sorting

![image-20230731163849997](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20230731163849997.png)

![image-20230731163904934](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20230731163904934.png)

![image-20230731163920421](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20230731163920421.png)

### selection sort

在第 i 次迭代中。找到剩余元素中最小的元素的索引

交换a[i] 和 a[min]

```java
public class Selection
{
    public static void sort(Comparable[] a)
    {
        int N = a.length;
        for(int i=0;i<N;i++)
        {
            int min=i;
            for(int j=i+1;j<N;j++)
                if(less(a[j],a[min]))
                    min=j;
            exch(a,i,min);
        }
    }
    
    private static boolean less(Comparable v,Comparable w)
    {}
    
    private static void exch(Comparable[] a,int i,int j)
    {}
}
```

### insertion sort

in iteration i, swap a[i] with each larger entry to its left

**partially sorted** : number of inversions is <= c N

For partially-sorted arrays, insertion sort runs in linear time, because number of exchanges equals the number of inversions.

```java
public class Insertion
{
    public static void sort(Comparable[] a)
    {
        int N = a.length;
        for(int i=0;i<N;i++)
        {
           for(int j=i;j>0;j--)
               if(less(a[j],a[j-1]))
                   exch(a,j,j-1);
            	else
                    break;
        }
    }
    
    private static boolean less(Comparable v,Comparable w)
    {}
    
    private static void exch(Comparable[] a,int i,int j)
    {}
}
```

### shell sort

move entries several position at a time

增量选择3X+1增量性能较好

```java
public class Shell
{
	public static void sort(Comparable[] a)
    {
        int N = a.length();
        int h = 1;
        while(h<N/3) `h = 3*h+1;
        while(h>=1):
        {
            for(int i=h;i<N;i++){
                for(int j=i;j>=h&&less(a[j],a[j-h]);j-=h){
                    exch(a,j,j-h);
                }
            }
            j/=3;
        }
    }
    private static boolean less(Comparable v,Comparable w)
    {}
    
    private static void exch(Comparable[] a,int i,int j)
    {}
}
```

### shuffle

i从0到n增加，每次从0~i生成一个随机数r

交换a[i]和a[r]

### Convex hull

![image-20230801104236795](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20230801104236795.png)

**graham scan**: 

- 选择y坐标最小的点 p
-  按照以 p 为起点的极较从小到大的顺序将其他所有的点进行排序
- 直接舍弃那些无法产生逆时针旋转的点

### Merge Sort

**basic plan**: 

- Divide array into two halves
- Recursively sort each half
- Merge two halves

```java
Private static void merge(Comparable[] a,Comparable[] aux, int lo, int mid, int hi)
{
    assert isSorted(a,lo,mid);        //precondition: a[lo...mid]   sorted
    assert isSorted(a,mid+1,hi);      //precondition: a[mid+1...hi] sorted
    
    for(int k = lo; k <= hi; k++)
    {
        aux[k] = a[k];                //copy
    }
    
    int i = lo, j = mid + 1;
    for(int k = lo;k <= hi;k++)
    {
             if(j > mid)              a[k] = aux[j++];
        else if(j > hi)               a[k] = aux[i++];
        else if(less(aux[j],aux[i]))  a[k] = aux[j++];
        else                          a[k] = aux[i++];
    }
    assert isSorted(a,lo,hi);
}

private static void sort(Comparable[] a,Comparable[] aux,int lo,int hi)
{
    if(hi<=lo)return;
    int mid = lo+(hi-lo)/2;
    sort(a,aux,lo,mid);
    sort(a,aux,mid+1,hi);
    if(!less(a[mid+1],a[mid]))return;
    merge(a,aux,lo,mid,hi);
}
```

#### bottom-up mergesort

不需要递归，代码容易编写

```java
public class MergeBU
{
    private static Comparable[] aux;
    private static void merge(Comparable[] a,int lo,int mid,int hi){}
    
    public static void sort(Comparable[] a)
    {
        int N = a.length;
        aux = new Comparablbe[N];
        for(int sz = 1;sz<N;sz = sz+sz)
            for(int lo=0;lo<N-sz;lo+=sz+sz)
                merge(a,lo,lo+sz-1,Math.min(lo+sz+sz-1,N-1));
    }
}
```

### Quick Sort

该方法的基本思想是：

- 1．先从数列中取出一个数作为基准数。
- 2．分区过程，将比这个数大的数全放到它的右边，小于或等于它的数全放到它的左边。
- 3．再对左右区间重复第二步，直到各区间只有一个数。 
- ![image-20230809101213744](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20230809101213744.png)

```java
private static int partition(Comparable[] a,int lo,int hi)
{
    int i=lo,j=hi+1;
    while(true){
        while(less(a[++i],a[lo]))      //find item on left to swap
            if(i==hi)break;
        while(less(a[lo],a[--j]))      //find item on right to swap
            if(j==lo)break;
        if(i>=j) break;                //check if pointer cross
        exch(a,i,j);                   //swap
    }
    exch(a,lo,j);                      //swap with partitioning item
    return j;                          //return index of item now known to be in place
}

public static void sort(Comparable[] a)
{
    StdRandom.shuffle(a);
    sort(a,0,a.length-1);
}

private static void sort(Comparable[a],int lo,int hi)
{
    if(hi<=lo)return;
    int j = partition(a,lo,hi);
    sort(a,lo,j-1);
    sort(a,j+1,hi);
}
```

![image-20230809103851562](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20230809103851562.png)

## 

## Priority Queue

![image-20230922110758880](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20230922110758880.png)

#### Binary Heap

![image-20230922111244591](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20230922111244591.png)

```java
public class MaxPQ{
    private Key[] pq;
    private int N;
    
    public MaxPQ(int capacity){
        pq = (Key[]) new Comparable[capacity+1];
    }
    
    public boolean isEmpty(){return N==0;}
    
    private void swim(int k){
        while(k>1&&less(k/2,k)){
            exch(k,k/2);
            k = k/2;
        }
    }
    
    public void insert(Key x){
        pq[++N] = x;
        swim(N);
    }
    
    private void sink(int k){
        while(2*k<=N){
            int j = 2*k;
            if(j<N&&less(j,j+1))j++;
            if(!less(k,j))break;
            exch(k,j);
            k = j
        }
    }
    
    public Key delMax(){
        Key max = pq[1];
        exch(1,N--);
        sink(1);
        pq[N+1] = null;
        return max;
    }
}
```

