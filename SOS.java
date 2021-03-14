import java.util.Scanner;
import java.util.ArrayList;

class SOS{

    static class Move{//it will be used to hundle the players move
        int row;
        int col;
        char move;
    } 

    static class Node{
        char[][] board = new char[3][3];
        int cost; 
		int pos;
        ArrayList<Node> children = new ArrayList<Node>();
        Node parent;
        boolean player;
		int iter;
		Node nextMove;
    };
	int maxWins,minWins;
	static private ArrayList <Node> leafs = new ArrayList<Node>();
    private boolean isMax = true;
    //private boolean isO = false;
    
    static private ArrayList <Node> tree = new ArrayList<Node>();
    //private int[] visited_array = new int[];
   
    public Boolean isMoveLeft(char board[][]){
        for (int i=0; i < 3; i++){
            for (int j=0; j<3; j++){
                if (board[i][j] == '-'){
                    return true;
                }
            }
        }
        return false;
    }

    public int evaluate(char board[][]){

        // check rows
        for(int row=0; row<3; row++){
            if (board[row][0]=='S' && board[row][1]=='O' && board[row][2]=='S'){
                return 1;
			}
 
        }

        // check columns
        for(int col=0; col<3; col++){
            if (board[0][col]=='S' && board[1][col]=='O' && board[2][col]=='S'){
                return 1;
            }
        }

        //check diagonals left to right
        if (board[0][0]=='S' && board[1][1]=='O' && board[2][2]=='S'){
            return 1;
        }

        // check diagonals right to left
        if (board[2][0]=='S' && board[1][1]=='O' && board[0][2]=='S'){
            return 1;
        }
        return 0;
    }

    public void DFS(Node state){
		int i , j;
		boolean isLeaf;
        
		while(state.pos < 9){	
			if(state.pos == 3 || state.pos == 5 ){
				state.pos++;
				continue;
			}
			Node node = new Node();
			for(int k = 0;k < 3;k++){
				for(int l = 0;l < 3;l++){
					node.board[k][l] = state.board[k][l];
				}
			}
			i = state.pos / 3;
			j = state.pos % 3;
			//System.out.println("i , j = " + i + " " + j);
			isLeaf = false;
            if (state.board[i][j] == '-'){
                if(state.iter == 0){ //iter show us how many times we have changed a specific empty slot of a node
					node.board[i][j] = 'S';//if it is 0 we haven't yet so we put an s and and form a new node
					node.parent = state;//if it is 1 we have done the s child so now we do the o
				}else if(state.iter == 1){//if it is 2 we change the slot and repeat 
					node.board[i][j] = 'O';
					node.parent = state;
				}else{
					state.iter = 0;
					state.pos++;
					continue;
				}
				
				if(evaluate(node.board) == 1){
					if(state.player){
						node.cost = 1;
						maxWins++;
						node.player = !state.player;
						leafs.add(node); //leafs is an array that will contain all the terminal nodes which minimax
						isLeaf = true;	//will use to start moving up the tree informing the nodes
					}else{				// only these nodes will have assigned costs before minimax is called
						node.cost = -1;
						minWins++;
						node.player = !state.player;
						leafs.add(node);
						isLeaf = true;
					}
				}else if(!isMoveLeft(node.board)){
					node.cost = 0;
					node.player = !state.player;
					leafs.add(node);
					isLeaf = true;
				}
				state.children.add(node);
				//tree.add(node);
				state.iter++;
				//printBoard(node.board);
				
				//printBoard(node.board);
				//System.out.println("first tree");
				
				if(!isLeaf){
					node.player = !state.player;
					DFS(node);
					
				}
				
            }else{
				state.pos++;
			}
			
		}
            
        
    }

    public void minimax(){
        //depth is current depth in game tree
        // returns the maximun value max can obtain
		
		ArrayList<Node> parents = new ArrayList<Node>();
        
        // MAX's turn
        //if (isMax==true){
			
            // go through all the cells
			//System.out.println("size = "+ leafs.size());
        for(int i=0; i<leafs.size(); i++){ 
			//System.out.println("out");
			
			if(leafs.get(i).parent != null){//we parse all the terminal nodes
				//System.out.println("in");
				//Node par = leafs.get(i).parent;
				if(!parents.contains(leafs.get(i).parent)){
					//System.out.println("in2");
					parents.add(leafs.get(i).parent);
				}
				if(leafs.get(i).parent.player){
					for(int j=0; j<leafs.get(i).parent.children.size(); j++){
						//Node child = par.children.get(j);
						if(leafs.get(i).parent.cost == 1){ //here if there is already a 1 that means that a shorter path
							continue;						//to victory exists and we should keep it as it is
						}
						if(leafs.get(i).parent.cost <= leafs.get(i).parent.children.get(j).cost){ //otherwise we change the cost of the
							leafs.get(i).parent.cost = leafs.get(i).parent.children.get(j).cost;// parent and inform the next move field
							leafs.get(i).parent.nextMove = leafs.get(i).parent.children.get(j);//accordingly
							//best = Math.max(best, minimax(board,depth + 1,!isMax));
						
						}
					}
				}else{
					for(int j=0; j<leafs.get(i).parent.children.size(); j++){//the same here but for min
						//Node child = par.children.get(j);
						if(leafs.get(i).parent.cost == -1){
							continue;
						}
						if(leafs.get(i).parent.cost >= leafs.get(i).parent.children.get(j).cost){
							leafs.get(i).parent.cost = leafs.get(i).parent.children.get(j).cost;
							leafs.get(i).parent.nextMove = leafs.get(i).parent.children.get(j);
							//best = Math.max(best, minimax(board,depth + 1,!isMax));
						
						}
					}
				}
			}
		}
		
			
        //}
		leafs = parents;
		if(leafs.size() != 0){ 
			minimax();
		}
    }

   

    public void printBoard(char board[][]){
        for (int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                System.out.print(" " + board[i][j] + " ");
            }
            System.out.println();

        }
		System.out.println();
    }
	public void printTree(ArrayList<Node> tree){
		for(int i = 0; i < tree.size(); i++){
			System.out.println("cost of a tree node is "+ tree.get(i).cost);
			printBoard(tree.get(i).board);
		}
	}
	public Node findcur(Node cur,char[][] board){//this method compares the current board and node to find to which node
		int count = 0,k,l;							//has the players move led us
		for(int i = 0;i < cur.children.size();i++){
			
			for(int j = 0;j < 9;j++){
				k = j/3;
				l = j%3;
				if(cur.children.get(i).board[k][l] == board[k][l] ){
					count++;
				}else{
					count = 0;
					break;
				}
				
			}
			if(count == 9){
				return cur.children.get(i);
			}
		}
		return null;
	}

    public static void main(String[] args){
        SOS game = new SOS();
        // computer start?
		Node root = new Node();
		Node cur  = new Node();//a node that will show us the current node on which we are
        char board[][]={{'-','-','-'},{'O','-','S'},{'-','-','-'}};//a board that will show us the current board
		System.out.println("Please wait for the game to load.This migth take a minute");
		root.board = board;//{{'-','-','-'},{'O','-','S'},{'-','-','O'}};
		root.parent = null;
		root.player = true;
		cur = root;
		//game.tree.add(root);
		
        char move; int row; int col;
        Scanner input = new Scanner(System.in);
        game.DFS(root);
		//game.printTree(tree);
		
		
		game.minimax();
		//game.printTree(tree);
		//System.out.println("number of leafs = "+ leafs.size());
		//System.out.println("root cost = "+ root.cost);
		//System.out.println("maxWins = "+ game.maxWins + " minWins = " + game.minWins);
		game.printBoard(board);
		System.out.println("Ready.The computer already made it's move.it's your turn");
        while(true){

          //  game.isMax = false;
            
            Move computerMove = new Move();
			
			
			cur = cur.nextMove;//given that during minimax we didn't only updated the cost but also saved the node to which
			board = cur.board;//the best cost move leads now we have nothing to do but use that node to inform the current node
								//and that node's board to inform the current board
            //computer's turn
            game.isMax = true;
            /*computerMove = game.playComputer(board);
            board[computerMove.row][computerMove.col] = computerMove.move;
            game.printBoard(board);*/
             game.printBoard(board);
			 if(game.evaluate(board) == 1){
				 System.out.println("Game over.Computer won");
				 break;
			 }else if(!game.isMoveLeft(board)){
				 System.out.println("Game over.It's a draw");
				 break;
			 }
            // player's turn
            System.out.print("Player enter your move. Type O or S: ");
            move = input.next().charAt(0);
            char move1 = Character.toUpperCase(move);

            System.out.print("Player enter the row: ");
            row = input.nextInt();

            System.out.print("Player enter the col: "); 
            col = input.nextInt();

            board[row][col] = move1;
            game.printBoard(board); 
			if(game.evaluate(board) == 1){
				 System.out.println("Game over.Player won");
				 break;
			 }else if(!game.isMoveLeft(board)){
				 System.out.println("Game over.It's a draw");
				 break;
			 }
			cur = game.findcur(cur,board);

        }
        input.close();
    }

}