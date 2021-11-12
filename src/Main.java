
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

class Distance {

    private int i;
    private int dist;

    Distance(int i, int dist) {
        this.i = i;
        this.dist = dist;
    }

    public int getI() {
        return i;
    }

    public int getDist() {
        return dist;
    }

    @Override
    public String toString() {
        return i + "";
    }
}

class DistanceComparator implements Comparator<Distance> {

    public int compare(Distance d1, Distance d2) {
        if (d1.getDist() < d2.getDist()) {
            return -1;
        } else {
            return 0;
        }
    }
}

class Edge {

    private int s;
    private int d;
    private int w;

    Edge(int s, int d, int w) {
        this.s = s;
        this.d = d;
        this.w = w;
    }

    public int getS() {
        return s;
    }

    public int getD() {
        return d;
    }

    public int getW() {
        return w;
    }
}

class Graph {

    private int v;
    private LinkedList<Edge> adj[];

    public Graph(int v) {
        this.v = v;
        adj = new LinkedList[v];
        for (int i = 0; i < v; i++) {
            adj[i] = new LinkedList<>();
        }
    }

    public void addEdge(int v, int u, int w) {
        Edge e = new Edge(v, u, w);
        adj[v].add(e);
    }

    public LinkedList<Edge>[] getAdj() {
        return adj;
    }
}

public class Main {

    public static void main(String[] args) {
        int n = 5;
        Graph g = new Graph(n);

        g.addEdge(0, 1, 15);
        g.addEdge(1, 0, 15);
        g.addEdge(0, 2, 11);
        g.addEdge(2, 0, 11);

        g.addEdge(1, 2, 12);
        g.addEdge(2, 1, 12);
        g.addEdge(1, 3, 14);
        g.addEdge(3, 1, 14);
        g.addEdge(1, 4, 1);
        g.addEdge(4, 1, 1);

        g.addEdge(2, 3, 3);
        g.addEdge(3, 2, 3);

        g.addEdge(3, 4, 2);
        g.addEdge(4, 3, 2);

        dijkstra(g, n, 0);
        floydWarshall(g, n);
    }

    public static void dijkstra(Graph g, int n, int s) {
        int[] dist = new int[n];
        int[] prev = new int[n];
        PriorityQueue<Distance> q = new PriorityQueue(n, new DistanceComparator());
        dist[s] = 0;
        for (int i = 0; i < n; i++) {
            if (i != s) {
                dist[i] = 1000;
            }
            prev[i] = -1;
            q.add(new Distance(i, dist[i]));
        }
        soutDist(dist);
        soutPrev(prev);
        System.out.println(Arrays.toString(q.toArray()) + "\n");

        while (!q.isEmpty()) {
            int u = q.remove().getI();
            for (int i = 0; i < g.getAdj()[u].size(); i++) {
                int alt = dist[u] + g.getAdj()[u].get(i).getW();
                if (alt < dist[g.getAdj()[u].get(i).getD()]) {
                    Object[] distAr = q.toArray();
                    Distance[] distArr = new Distance[q.size()];
                    for (int j = 0; j < distArr.length; j++) {
                        distArr[j] = (Distance) distAr[j];
                    }
                    Distance temp = new Distance(-1, -1);
                    for (int j = 0; j < distArr.length; j++) {
                        if (distArr[j].getI() == g.getAdj()[u].get(i).getD()) {
                            temp = distArr[j];
                        }
                    }
                    q.remove(temp);
                    dist[g.getAdj()[u].get(i).getD()] = alt;
                    prev[g.getAdj()[u].get(i).getD()] = u;
                    q.add(new Distance(g.getAdj()[u].get(i).getD(), alt));
                }
            }

            soutDist(dist);
            soutPrev(prev);
            System.out.println(Arrays.toString(q.toArray()) + "\n");
        }
    }

    public static void soutDist(int[] dist) {
        System.out.print("[");
        for (int i = 0; i < dist.length; i++) {
            if (dist[i] == 1000) {
                System.out.print("∞");
            } else {
                System.out.print(dist[i]);
            }
            if (i != dist.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.print("]\n");
    }

    public static void soutPrev(int[] prev) {
        System.out.print("[");
        for (int i = 0; i < prev.length; i++) {
            if (prev[i] == -1) {
                System.out.print("nil");
            } else {
                System.out.print(prev[i]);
            }
            if (i != prev.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.print("]\n");
    }

    public static void floydWarshall(Graph g, int n) {
        int[][] arr = new int[n][n];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                arr[i][j] = Integer.MAX_VALUE;
            }
        }

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (i == j) {
                    arr[i][j] = 0;
                }
            }
        }

        LinkedList<Edge> adj[] = g.getAdj();
        for (int i = 0; i < adj.length; i++) {
            for (int j = 0; j < adj[i].size(); j++) {
                arr[adj[i].get(j).getS()][adj[i].get(j).getD()] = adj[i].get(j).getW();
            }
        }
        soutMatrix(arr);

        for (int k = 0; k < arr.length; k++) {
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr.length; j++) {
                    if (arr[i][j] > (arr[i][k] + arr[k][j]) && ((arr[i][k]) != Integer.MAX_VALUE && ((arr[k][j]) != Integer.MAX_VALUE))) {
                        arr[i][j] = (arr[i][k] + arr[k][j]);
                    }
                }
            }
            soutMatrix(arr);
        }
    }

    public static void soutMatrix(int[][] arr) {
        for (int x = 0; x < arr.length; x++) {
            System.out.print("|");
            for (int y = 0; y < arr[x].length; y++) {
                if (arr[x][y] != Integer.MAX_VALUE) {
                    System.out.print(arr[x][y]);
                } else {
                    System.out.print("∞");
                }
                if (y != arr[x].length - 1) {
                    System.out.print("\t");
                }
            }
            System.out.println("|");
        }
        System.out.println("");
    }
}
