package graphs.minspantrees;

import disjointsets.DisjointSets;
// import disjointsets.QuickFindDisjointSets;
import disjointsets.UnionBySizeCompressingDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        // return new QuickFindDisjointSets<>();
        /*
        Disable the line above and enable the one below after you've finished implementing
        your `UnionBySizeCompressingDisjointSets`.
         */
        return new UnionBySizeCompressingDisjointSets<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
        // Here's some code to get you started; feel free to change or rearrange it if you'd like.

        // sort edges in the graph in ascending weight order
        List<E> edges = new ArrayList<>(graph.allEdges());
        edges.sort(Comparator.comparingDouble(E::weight));

        // Kruskal's algorithm
        List<V> vertices = new ArrayList<>(graph.allVertices());
        DisjointSets<V> disjointSets = createDisjointSets();
        Collection<E> treeEdges = new HashSet<>();

        for (V island : vertices) {
            disjointSets.makeSet(island);
        }
        for (E bridge : edges) {
            if (disjointSets.findSet(bridge.from()) != disjointSets.findSet(bridge.to())) {
                disjointSets.union(bridge.from(), bridge.to());
                treeEdges.add(bridge);
            }
        }

        // Check connection
        boolean connection = true;
        if (!vertices.isEmpty()) {
            int root = disjointSets.findSet(vertices.get(0));
            for (V vertex : vertices) {
                int leaf = disjointSets.findSet(vertex);
                if (leaf != root) {
                    connection = false;
                }
            }
        }

        // Output
        if (connection) {
            return new MinimumSpanningTree.Success<>(treeEdges);
        } else {
            return new MinimumSpanningTree.Failure<>();
        }
    }
}
