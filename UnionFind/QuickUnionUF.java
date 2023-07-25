package UnionFind;

public class QuickUnionUF {

  private int[] id;
  private int[] sz;

  public QuickUnionUF(int N) {
    for (int i = 0; i < N; i++) {
      id[i] = i;
      sz[i] = 1;
    }
  }

  /**
   * chase parent pointers until reach root(depth of i array accesses)
   * @param i
   * @return root value
   */
  private int root(int i) {
    while (i != id[i]) {
      id[i] = id[id[i]]; //path compression
      i = id[i];
    }
    return i;
  }

  /**
   * check if p and q have same root(depth of p and q array accesses)
   * @param p
   * @param q
   * @return
   */
  public boolean connected(int p, int q) {
    return root(p) == root(q);
  }

  /**
   * change root of p to point to root of q(depth of p and q array accesses)
   * @param p
   * @param q
   */
  public void union(int p, int q) {
    int i = root(p);
    int j = root(q);
    if (i == j) return;
    if (sz[i] < sz[j]) {
      id[i] = j;
      sz[j] += sz[i];
    } else {
      id[j] = i;
      sz[i] += sz[j];
    }
  }
}
