package minesim.pathfinder;

import java.util.Comparator;

import minesim.World;
import minesim.entities.WorldEntity;
import minesim.entities.items.Transportation;
import minesim.tiles.TileGridManager;
import minesim.tiles.TileNotFoundException;

public class Pathfinder {
	World world;
    TileGridManager tilemanager;
    NodeList nodes;
    private int[][] map;

    public Pathfinder(World world, TileGridManager tilemanager) {
    	this.world = world;
        this.tilemanager = tilemanager;
        map = new int[World.WIDTH][World.HEIGHT];
        nodes = new NodeList();
    }

    public void updateMap() {
        nodes = new NodeList();

		/*Map generation
		  1 means solid
		  0 means air
		  2 means the entity won't fall at the position
		  3 means transportation*/
        for (int i = 0; i < World.WIDTH; i++) {
            for (int j = 0; j < World.HEIGHT; j++) {
                map[i][j] = 0;
            }
        }
        for (int i = 0; i < World.WIDTH; i++) {
            for (int j = 0; j < World.HEIGHT; j++) {
                try {
                    if (tilemanager.getTileAtLocation(i, j).isSolid()) {
                        map[i][j] = 1;
                    }
                } catch (TileNotFoundException e) {
                    // TODO Auto-generated catch block
                    map[i][j] = 0;
                }
            }
        }
        for (int i = 0; i < World.WIDTH - 1; i++) {
            for (int j = 3; j < World.HEIGHT; j++) {
                if ((map[i][j] == 1 || map[i + 1][j] == 1) && map[i][j - 1] == 0 && map[i + 1][j - 1] == 0
                        && map[i][j - 2] == 0 && map[i + 1][j - 2] == 0
                        && map[i][j - 3] == 0 && map[i + 1][j - 3] == 0) {
                    map[i][j - 2] = 2;
                }
            }
        }

        //Nodes generation
        loadTransportation();
        for (int i = 0; i < World.WIDTH - 2; i++) {
            for (int j = 0; j < World.HEIGHT - 3; j++) {
                if (map[i][j] == 2) {
                    if (isEnd(i, j)) {
                        if (i != 0 && j != 0) {
                            // check if entity falls on left edge
                            if (map[i - 1][j] != 1 && map[i - 1][j - 1] != 1 &&
                                    map[i - 1][j + 1] != 1 && map[i - 1][j + 2] != 1 &&
                                    map[i - 1][j + 3] != 1 && map[i][j + 2] != 1 && map[i][j + 3] != 1) {
                                Node newnode = new Node(i - 1, j, 99999, null);
                                if (nodes.getByNode(newnode) != null) {
                                	newnode = nodes.getByNode(newnode);
                                }
                                Node fallpoint = fallNode(i - 1, j);
                                if (fallpoint != null) {
                                	newnode.linked.add(fallpoint);
                                    fallpoint.linked.add(newnode);
                                }
                                nodes.add(newnode);
                                nodes.add(fallpoint);
                                nodes.add(new Node(i, j, 99999, null));
                                // check if entity falls on right edge
                            } else if (map[i + 2][j] != 1 && map[i + 2][j - 1] != 1 &&
                                    map[i + 2][j + 1] != 1 && map[i + 2][j + 2] != 1 &&
                                    map[i + 2][j + 3] != 1 && map[i + 1][j + 2] != 1 && map[i + 1][j + 3] != 1) {
                                Node newnode = new Node(i + 1, j, 99999, null);
                                if (nodes.getByNode(newnode) != null) {
                                	newnode = nodes.getByNode(newnode);
                                }
                                Node fallpoint = fallNode(i + 1, j);
                                if (fallpoint != null) {
                                	newnode.linked.add(fallpoint);
                                	fallpoint.linked.add(newnode);
                                }
                                nodes.add(newnode);
                                nodes.add(fallpoint);
                                nodes.add(new Node(i, j, 99999, null));
                            } else {
                                Node newnode = new Node(i, j, 99999, null);
                                nodes.add(newnode);
                            }
                        } else {
                            Node newnode = new Node(i, j, 99999, null);
                            nodes.add(newnode);
                        }
                    }
                }
            }
        }
        for (Node node : nodes) {
        	Node left = leftNode(node);
        	Node right = rightNode(node);
            node.linked.add(left);
            node.linked.add(right);
            if (left != null) {
            	left.linked.add(node);
            }
            if (right != null) {
            	right.linked.add(node);
            }
        }
        /*
        for (int j = 0; j < 100; j++) {
        	for (int i = 0; i<50; i++) {
        		System.out.print(map[i][j]);
        	}
        	System.out.println();
        }*/
    }

    private Node leftNode(Node node) {
        int x = node.x;
        int y = node.y;
        if (nodes.getByPos(x - 1, y) != null) {
        	return nodes.getByPos(x - 1, y);
        }
        if (x == 0) {
            return null;
        }
        if (map[x - 1][y] == 2) {
            x--;
        } else if (map[x - 1][y + 1] == 2) {
            x--;
            y++;
        } else if (map[x - 1][y - 1] == 2) {
            x--;
            y--;
        } else {
            return null;
        }
        while (nodes.getByPos(x, y) == null) {
            if (x == 0) {
                return null;
            }
            if (map[x - 1][y] == 2) {
                x--;
            } else if (map[x - 1][y + 1] == 2) {
                x--;
                y++;
            } else if (map[x - 1][y - 1] == 2) {
                x--;
                y--;
            } else {
                break;
            }
        }
        if (nodes.getByPos(x, y) == null && nodes.getByPos(x - 1, y) != null) {
            return nodes.getByPos(x - 1, y);
        } else {
            return nodes.getByPos(x, y);
        }
    }
    
    private void loadTransportation(){
    	for(WorldEntity entity: world.getWorldentities()){
    		if (entity instanceof Transportation) {
    			Transportation trans = (Transportation) entity;
    			if (nodes.getByPos(trans.getXpos()/16, trans.getYpos()/16)  == null) {
    				Node newnode = new Node(trans.getXpos()/16, trans.getYpos()/16, 99999, null);
    				Transportation toptrans = trans;
    				Node prevnode = newnode;
    				nodes.add(newnode);
    				map[newnode.x][newnode.y] = 3;
    				while (world.getNearestYabove(Transportation.class, toptrans).isPresent()) {
	    				toptrans = (Transportation)world.getNearestYabove(Transportation.class, toptrans).get();
	    				Node topnode = new Node(toptrans.getXpos()/16, toptrans.getYpos()/16, 99999, null);
	    				Node middle = new Node(toptrans.getXpos()/16, toptrans.getYpos()/16+1, 99999, null);
	    				topnode.linked.add(middle);
	    				middle.linked.add(topnode);
	    				middle.linked.add(prevnode);
	    				prevnode.linked.add(middle);
	    				topnode.type = 1;
	    				prevnode.type = 1;
	    				middle.type = 1;
	    				nodes.add(topnode);
	    				nodes.add(middle);
	    				prevnode = topnode;
	    				map[topnode.x][topnode.y] = 3;
	    				map[middle.x][middle.y] = 3;
    				}
    				Transportation bottrans = trans;
    				prevnode = newnode;
    				while (world.getNearestYbelow(Transportation.class, bottrans).isPresent()) {
	    				bottrans = (Transportation)world.getNearestYbelow(Transportation.class, bottrans).get();
	    				Node botnode = new Node(bottrans.getXpos()/16, bottrans.getYpos()/16, 99999, null);
	    				Node middle = new Node(bottrans.getXpos()/16, bottrans.getYpos()/16-1, 99999, null);
	    				botnode.linked.add(middle);
	    				middle.linked.add(botnode);
	    				middle.linked.add(prevnode);
	    				prevnode.linked.add(middle);
	    				botnode.type = 1;
	    				middle.type = 1;
	    				prevnode.type = 1;
	    				nodes.add(botnode);
	    				nodes.add(middle);
	    				prevnode = botnode;
	    				map[botnode.x][botnode.y] = 3;
	    				map[middle.x][middle.y] = 3;
    				}
    			}
    		}
    	}
    }

 

	private Node rightNode(Node node) {
        int x = node.x;
        int y = node.y;
        if (x == World.WIDTH - 1) {
            return null;
        }
        if (nodes.getByPos(x + 1, y) != null) {
        	return nodes.getByPos(x + 1, y);
        }
        if (map[x + 1][y] == 2) {
            x++;
        } else if (map[x + 1][y + 1] == 2) {
            x++;
            y++;
        } else if (map[x + 1][y - 1] == 2) {
            x++;
            y--;
        } else {
            return null;
        }
        while (nodes.getByPos(x, y) == null) {
            if (x == World.WIDTH - 1) {
                return null;
            }
            if (map[x + 1][y] == 2) {
                x++;
            } else if (map[x + 1][y + 1] == 2) {
                x++;
                y++;
            } else if (map[x + 1][y - 1] == 2) {
                x++;
                y--;
            } else {
                break;
            }
        }
        if (nodes.getByPos(x, y) == null && nodes.getByPos(x + 1, y) != null) {
            return nodes.getByPos(x + 1, y);
        } else {
            return nodes.getByPos(x, y);
        }
    }

    private Node fallNode(int x, int y) {
        while (map[x][y] != 2) {
            y++;
            if (y >= World.HEIGHT) {
            	return null;
            }
        }
        if (nodes.getByPos(x, y) != null) {
        	return nodes.getByPos(x, y);
        }
        return new Node(x, y, 99999, null);
    }

    private Boolean isEnd(int x, int y) {
        int count = 0;
        if (x != 0 && x != World.WIDTH - 3) {
            if (map[x - 1][y] == 2) {
                count++;
            }
            if (y != World.HEIGHT) {
                if (map[x - 1][y + 1] == 2) {
                    count++;
                }
            }
            if (y != 0) {
                if (map[x - 1][y - 1] == 2) {
                    count++;
                }
            }
            if (map[x + 1][y] == 2) {
                count++;
            }
            if (y != World.HEIGHT) {
                if (map[x + 1][y + 1] == 2) {
                    count++;
                }
            }
            if (y != 0) {
                if (map[x + 1][y - 1] == 2) {
                    count++;
                }
            }
        }
        if (count > 1) {
            return false;
        } else {
            return true;
        }
    }

    private Node getClosest(int x, int y) {
        if (map[x][y] == 2 || map[x][y] == 3) {
            if (nodes.contains(new Node(x, y, 99999, null))) {
                return nodes.getByPos(x, y);
            }
            return new Node(x, y, 99999, null);
        }
        if (map[x][y - 1] == 2 || map[x][y - 1] == 3) {
            if (nodes.contains(new Node(x, y - 1, 99999, null))) {
                return nodes.getByPos(x, y - 1);
            }
            return new Node(x, y - 1, 99999, null);
        }
        int i = 1;
        while (map[x][y + i] != 2 && map[x][y + i] != 3) {
            if (map[x][y + 1] == 1) {
                return null;
            }
            i++;
        }
        if (nodes.contains(new Node(x, y + i, 99999, null))) {
            return nodes.getByPos(x, y + i);
        }
        return new Node(x, y + i, 99999, null);
    }

    private String solutionGenerator(Node dest) {
        String s = new String();
        while (dest != null && dest.parent != null) {
        	if (dest.type == 1) {
        		s = "c " + Integer.toString(dest.x) + " " + Integer.toString(dest.y) + " " + s;
        		while (dest.parent.type == 1) {

        			dest = dest.parent;
        			if (dest.parent == null) {
        				break;
        			}
        		}
        		s = "m " + Integer.toString(dest.x) + " " + Integer.toString(dest.y) + " " + s;
                dest = dest.parent;
        	}
        	else if (dest.x == dest.parent.x && dest.y < dest.parent.y) {
                s = "j " + Integer.toString(dest.x) + " " + Integer.toString(dest.y) + " " + s;
                dest = dest.parent;
            } else {
                s = "m " + Integer.toString(dest.x) + " " + Integer.toString(dest.y) + " " + s;
                dest = dest.parent;
            }
        }
        return s;
    }

    public String pathFind(int xpos, int ypos, int xdes, int ydes) {
        NodeList opened = new NodeList();
        NodeList closed = new NodeList();
        updateMap();
        Node dest = getClosest(xdes, ydes);

        if (dest == null) {
            return null;
        }
        Node left = (leftNode(dest));
        if (left != null) {
            dest.linked.add(left);
            left.linked.add(dest);
        }
        Node right = rightNode(dest);
        if (right != null) {
            dest.linked.add(right);
            right.linked.add(dest);
        }
        nodes.add(dest);
        Node start = getClosest(xpos, ypos);
        if (start == null) {
        	return null;
        }
        start.cost = 0;
        if (nodes.contains(start)) {
            start = nodes.getByNode(start);
        } else {
            left = leftNode(start);
            if (left != null) {
                start.linked.add(left);
                left.linked.add(start);
            }

            right = rightNode(start);
            if (right != null) {
                start.linked.add(right);
                right.linked.add(start);
            }
        }
        start.cost = 0;
        opened.add(start);
        nodes.add(start);

        while (!opened.isEmpty()) {
            opened.sort(new Comparator<Node>() {
                public int compare(Node l, Node r) {
                    if (l.cost + Math.abs(l.x - dest.x) < r.cost + Math.abs(r.x - dest.x)) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
            Node open = opened.get(0);
            closed.add(open);
            opened.remove(0);
            for (Node link : open.linked) {

                if (!closed.contains(link)) {
                    int cost = open.cost;
                    if (open.x == link.x) {
                        if (open.y < link.y) {
                        } else if (open.y - link.y <= 5) {
                            cost = cost + 1;
                        } else {
                            cost = 99999;
                        }
                    } else {
                    	cost = cost + Math.abs(open.x - link.x);
                    }
                    if (cost < link.cost) {
                        link.cost = cost;
                        link.parent = open;
                        opened.add(link);
                        if (link.equals(dest)) {
                            return solutionGenerator(dest);
                        }
                    }
                }
            }
        }
        return "";

    }


}
