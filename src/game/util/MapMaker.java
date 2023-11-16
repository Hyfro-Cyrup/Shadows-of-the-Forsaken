/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.util;

import game.model.Attack;
import game.model.DungeonTile;
import game.model.Entity;
import game.model.Enemy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;

/**
 * Singleton class used to create new randomized dungeon maps, which are 2d arrays of DungeonTile objects
 */
public final class MapMaker {
    // Map Generation Parameters
    // everything's up here for easy tweaking and so there's no magic numbers
    private static final int WIDTH = 20;
    private static final int HEIGHT = 16;
    /**
     * vaguely how large the map can branch out to be
     */
    private static final int ITERATIONS = 20; 
    /**
     * The probability a given node contains a non-creature entity.
     * Higher makes the ladder and key appear earlier
     */
    private static final double ENTITY_PROB = 0.3;
    /**
     * The probability a given node contains a creature.
     * Higher makes more densely-packed creatures.
     */
    private static final double CREATURE_PROB = 0.2;
    /**
     * The probability of an additional creature in a tile, given one already exists
     * Hence, the probability of 2 or 3 creatures is CP*SCP and CP*SCP^2 respectively
     */
    private static final double SUBSEQUENT_CREATURE_PROB = 0.3;
    /**
     * Clustering coefficient. At 0, no 2x2 rooms appear. 
     * Higher makes more clusters like that.
     * Keep lower than you think. 
     */
    private static final double NEIGHBOR_PROB = 0.05; 
    /**
     * How many iterations must be done before non-creature entities appear.
     */
    private static final int ENTITY_START = 5; 
    /**
     * How many iterations must be done before creatures can appear.
     */
    private static final int CREATURE_START = 1; 
    /**
     * Minimum count of tiles in the final map.
     */
    private static final int MIN_TILES = 40; 
    /**
     * Minimum allowable distance between the key and door, in tiles.
     */
    private static final int MIN_DISTANCE = 15;
    /**
     * Minimum number of combats between key and ladder
     */
    private static final int MIN_COMBATS = 2;
    /**
     * List of enemy entities from which to populate the grid
     */
    private static final List<Enemy> ENEMIES = Arrays.asList(
            new Enemy("Skeleton", "Boney guy", "/resources/Skeleton.png",
                    30, 2, 0, 1, new Attack[]{Attack.SLASH, Attack.QUICK_STRIKE}, 
                    new float[]{1.5f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f})
    );
    
    
    /**
     * Populates a grid of DungeonTile objects via an infection-like simulation.
     * @param w Width of the array
     * @param h Height of the array
     * @param iterations Number of times to run the simulation
     * @param probability The chance a given tile can "infect" its neighbors
     * @return a (`w` x `h`) array of DungeonTiles
     */
    private static DungeonTile[][] coolMapGenerator(int w, int h, int iterations, double probability)
    {       
        List<Entity> Objects = new ArrayList<>();
        Objects.add(new Entity("Ladder", "The exit beckons.", "dummy/path"));
        Objects.add(new Entity("Key", "It shines with hope.", "dummy/path"));
        
        DungeonTile[][] map = new DungeonTile[w][h];
        ArrayList<List<Integer>> cursor = new ArrayList<>();
        cursor.add(Arrays.asList(w/2, h/2));
        
        // track which cells are filled
        ArrayList<List<Integer>> visited = new ArrayList<>();
        
        // modeled after influence diffusion in a network. 
        // each tile has a chance to infect its neighbors.
        for (int i = 0; i < iterations; i++)
        {
            ArrayList<List<Integer>> nextCursor = new ArrayList<>();
            ArrayList<Entity> contents = new ArrayList<>();
            
            // mark tiles beforehand to avoid conflict
            for (List<Integer> tile : cursor)
            {
                visited.add(tile);
            }
            
            for (List<Integer> tile : cursor) 
            {
                contents.clear();
                // fill in dungeon tile
                if ((i > ENTITY_START) && (!Objects.isEmpty()) && (Math.random() < ENTITY_PROB))
                {
                    // add an object
                    int index = (int) Math.floor(Math.random()*Objects.size());
                    contents.add(Objects.get(index));
                    Objects.remove(index);
                    
                }
                else if ((i > CREATURE_START) && Math.random() < CREATURE_PROB)
                {
                    Entity entity = ENEMIES.get((int) Math.floor(Math.random()*ENEMIES.size()));
                    contents.add(entity);
                    for (int j = 0; j < 2; j++)
                    {
                        if ((Math.random() < SUBSEQUENT_CREATURE_PROB))
                        {
                            entity = ENEMIES.get((int) Math.floor(Math.random()*ENEMIES.size()));
                            contents.add(entity);
                        }
                        else
                        {
                            break;
                        }
                    }
                    
                }
                
                map[tile.get(0)][tile.get(1)] = new DungeonTile(new ArrayList<>(contents), false);               
                
                // propagate the next generation
                int[][] points = {
                {tile.get(0) + 1, tile.get(1)}, // East
                {tile.get(0), tile.get(1) -1},  // North
                {tile.get(0) -1, tile.get(1)},  // West
                {tile.get(0), tile.get(1) + 1}, // South
                };
                
                int[][] corners = {
                    {tile.get(0) + 1, tile.get(1) - 1}, // NE
                    {tile.get(0) - 1, tile.get(1) - 1}, // NW
                    {tile.get(0) - 1, tile.get(1) + 1}, // SW
                    {tile.get(0) + 1, tile.get(1) + 1}, // SE
                };
                
                
                for(int j = 0; j < 4; j++)
                {
                    int[] p = points[j];
                    if ((p[0] >= 0) && (p[0] < w) && (p[1] >= 0) && (p[1] < h) &&
                            !(visited.contains(Arrays.asList(p[0], p[1]))))
                    {
                        // reduce certain probabilities to make more linear
                        var prob = probability;
                        var ccw = corners[j];   // counter-clockwise neighbor
                        if (visited.contains(Arrays.asList(ccw[0], ccw[1])))
                        {
                            prob *= NEIGHBOR_PROB;
                        }
                        var cw = corners[j > 0 ? j - 1 : j + 3]; // clockwise neighbor
                        if (visited.contains(Arrays.asList(cw[0], cw[1])))
                        {
                            prob *= NEIGHBOR_PROB;
                        }
                        
                        
                        // infect next cell with some chance
                        if (Math.random() < prob)
                        {
                            nextCursor.add(Arrays.asList(p[0], p[1]));
                        }
                    }
                }
            }
            
            cursor = nextCursor;
        }
        return map;
    }
    
    
    /**
     * Creates a new dungeon map from scratch.
     * @return a 2d array of DungeonTile objects
     */
    public static DungeonTile[][] newMap()
    {
        DungeonTile[][] map = coolMapGenerator(WIDTH, HEIGHT, ITERATIONS, 0.6);
        while (!mapIsValid(map))
        {
            map = coolMapGenerator(20, 16, 20, 0.6);
        }
        return map;
    }
    
    private static Boolean mapIsValid(DungeonTile[][] map)
    {
        int count = 0;
        for (DungeonTile[] col : map){
            for (DungeonTile tile : col){
                if (tile != null){
                    count += 1;
                }
            }
        }
        if (count < MIN_TILES)
        {
            return false;
        }
        
        // Check for key and ladder placement
        int[] keyPos = null, ladderPos = null;
        for (int i = 0; i < map.length; i++)
        {
            for (int j = 0; j < map[i].length; j++) 
            {
                if ((map[i][j] != null))
                {
                    if (map[i][j].containsKey())
                    {
                        keyPos = new int[]{i, j};
                    }
                    else if (map[i][j].containsLadder())
                    {
                        ladderPos = new int[]{i, j};
                    }
                }
            }
        }
        if ((keyPos == null) || (ladderPos == null))
        {
            return false;
        }
        
        // check distance between key and ladder
        if (AStar(map, keyPos, ladderPos) < MIN_DISTANCE)
        {
            return false;
        }
        
        // check number of combats between key and ladder
        if (combatsRequired(map, keyPos, ladderPos) < MIN_COMBATS)
        {
            return false;
        }
        
        
        return true;
    }
    
    public static int test(DungeonTile[][] map)
    {
        int[] keyPos = null, ladderPos = null;
        for (int i = 0; i < map.length; i++)
        {
            for (int j = 0; j < map[i].length; j++) 
            {
                if ((map[i][j] != null))
                {
                    if (map[i][j].containsKey())
                    {
                        keyPos = new int[]{i, j};
                    }
                    else if (map[i][j].containsLadder())
                    {
                        ladderPos = new int[]{i, j};
                    }
                }
            }
        }
        
        return AStar(map, keyPos, ladderPos);
    }
    
    private static class AStarNode
    {
        private enum Status
        {
            UNVISITED, 
            CLOSED, 
            OPEN
        }
        public int[] position;
        public AStarNode prevNode;
        public int costSoFar;
        public double heuristic;
        public double estimatedTotalCost;
        public Status status = Status.UNVISITED;
        
        public AStarNode(int[] position)
        {
            this.position = position;
        }
    }
    
    private static class AStarComp implements Comparator<AStarNode>
    {
        @Override
        public int compare(AStarNode node1, AStarNode node2) 
        {
            // Compare nodes based on their estimated total cost.
            // A lower cost should have a higher priority.
            return Double.compare(node1.costSoFar, node2.costSoFar);
        }
    }
    
    /**
     * Implements the A* pathfinding algorithm to find the length of the shortest path between two DungeonTiles
     * @param map The DungeonTile array to search through
     * @param start The coordinates of the first DungeonTile
     * @param end The coordinates of the second DungeonTile
     * @return an int representing the length of the path. -1 signifies no path exists, but one always should under the MapMaker construction
     */
    private static int AStar(DungeonTile[][] map, int[] start, int[] end)
    {
        Function<int[], Integer> cost = position -> 1;
        
        return AStar(map, start, end, cost);
    }
    
    
    private static int AStar(DungeonTile[][] map, int[] start, int[] end, Function<int[], Integer> costToEnter)
    {
        PriorityQueue<AStarNode> openNodes = new PriorityQueue<>(new AStarComp());
        AStarNode startNode = new AStarNode(start);
        openNodes.add(startNode);
        List<AStarNode> closedNodes = new LinkedList<>();
        AStarNode current;
        
        while (!openNodes.isEmpty())
        {
            current = openNodes.poll();
            
            // exit as soon as the goal node is found
            if (Arrays.equals(current.position, end))
            {
                int cost = 0;
                while (!Arrays.equals(current.position, start))
                {
                    cost += 1;
                    current = current.prevNode;
                }
                return cost;
            }
            
            // loop through the neighbors
            int[][] neighbors = {
                {current.position[0] + 1, current.position[1]},
                {current.position[0], current.position[1] + 1},
                {current.position[0], current.position[1] - 1},
                {current.position[0] - 1, current.position[1]},
            };
            for (int[] n : neighbors)
            {
                // ensure position is valid on the map
                if (-1 < n[0] && n[0] < map.length &&
                    -1 < n[1] && n[1] < map[0].length &&
                    map[n[0]][n[1]] != null)
                {
                    int csf = current.costSoFar + costToEnter.apply(n); // cost-so-far of this neighbor
                    double est;    // heuristic of this neighbor
                    
                    // if neighbor is in openNodes or closedNodes, don't recalculate heuristic
                    AStarNode next;
                    next = closedNodes.stream().filter(e -> Arrays.equals(e.position, n)).findFirst().orElse(null);
                    if (next != null) // node is in closedNodes
                    {
                        // if the new route is longer than a previously found route, ignore it
                        if (next.costSoFar <= csf)
                        {
                            continue;
                        }
                        
                        // if new route is the new shortest route, we'll have to recalculate any subsequent nodes
                        closedNodes.remove(next);
                        
                        // reuse previously calculated heuristic
                        est = next.heuristic;
                    }
                    else
                    {
                        next = openNodes.stream().filter(e -> Arrays.equals(e.position, n)).findFirst().orElse(null);
                        if (next != null) // node is in openNodes
                        {
                            // if the new route is longer than a previously found route, ignore it
                            if (next.costSoFar <= csf)
                            {
                                continue;
                            }
                            
                            // reuse previously calculated heuristic
                            est = next.heuristic;
                        }
                        else // node is not in openNodes or closedNodes
                        {
                            // there's no previously calculated heuristic to use, so calculate one
                            est = Math.pow(n[0] - end[0], 2) + Math.pow(n[1] - end[1], 2);
                            next = new AStarNode(n);
                            next.heuristic = est;
                        }
                    }
                    // if node was not ignored, overwrite data
                    next.costSoFar = csf;
                    next.estimatedTotalCost = csf + est;
                    next.prevNode = current;

                    // add to open list if it's not there already
                    if (!openNodes.stream().anyMatch(e -> Arrays.equals(e.position, n)))
                    {
                        openNodes.add(next);
                    }
                }
            }
            // outside for loop
            // we're done with the current node's neighbors, so close it
            closedNodes.add(current);
        }
        // outside while loop
        // if we got here, we exhausted A* without finding the goal node, so no such 
        // path exists. This should never happen if the map was made by MapMaker
        
        return -1;
    }
    
    /**
     * Determines whether the player can traverse from one node to another without having a combat encounter.
     * Implementation is A* with a different function determining edge weights
     * @param map The DungeonTile array to traverse
     * @param start indices of the start node
     * @param end indices of the end node
     * @return the minimum number of combats between the start and end
     */
    private static int combatsRequired(DungeonTile[][] map, int[] start, int[] end)
    {
        Function<int[], Integer> cost = position -> map[position[0]][position[1]].containsEnemy() ? 1 : 0;
        
        return AStar(map, start, end, cost);
    }
    
    /**
     * Private MapMaker constructor ensures that it will not be instantiated
     */
    private MapMaker()
    {
        throw new UnsupportedOperationException("MapMaker should not be instantiated");
    }
}
