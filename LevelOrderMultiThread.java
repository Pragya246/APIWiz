import java.util.*;
import java.util.concurrent.*;

public class LevelOrderMultiThread {

    static class Graph {
        Map<Integer, String> nodes = new HashMap<>();
        Map<Integer, List<Integer>> adj = new HashMap<>();

        void addNode(int key, String name) {
            nodes.put(key, name);
            adj.putIfAbsent(key, new ArrayList<>());
        }

        void addEdge(int u, int v) {
            adj.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            adj.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
        }

        List<Integer> getNeighbors(int node) {
            return adj.getOrDefault(node, Collections.emptyList());
        }

        String getName(int node) {
            return nodes.get(node);
        }
    }

    public static void parallelLevelBFS(Graph graph, int start) throws InterruptedException {
        Set<Integer> visited = ConcurrentHashMap.newKeySet();
        visited.add(start);

        Queue<Integer> currentLevel = new ConcurrentLinkedQueue<>();
        currentLevel.add(start);

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        while (!currentLevel.isEmpty()) {
            Queue<Integer> nextLevel = new ConcurrentLinkedQueue<>();
            List<Callable<Void>> tasks = new ArrayList<>();
            for (Integer node : currentLevel) {
                tasks.add(() -> {
                    System.out.println(graph.getName(node));
                    for (Integer neighbor : graph.getNeighbors(node)) {
                        if (visited.add(neighbor)) {
                            nextLevel.add(neighbor);
                        }
                    }
                    return null;
                });
            }
            executor.invokeAll(tasks);
            currentLevel = nextLevel;
        }

        executor.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        sc.nextLine();
        Graph graph = new Graph();

        for(int i = 0; i < N; i++) {
            String[] parts = sc.nextLine().split(":");
            int key = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            graph.addNode(key, name);
        }

        int M = sc.nextInt();
        sc.nextLine();
        for(int i = 0; i < M; i++) {
            String[] edges = sc.nextLine().split(":");
            int u = Integer.parseInt(edges[0].trim());
            int v = Integer.parseInt(edges[1].trim());
            graph.addEdge(u, v);
        }
        parallelLevelBFS(graph, 1);
        System.out.println(N);
    }
}
