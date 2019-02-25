import java.util.*;

/* L: means new learnings */


public class ChainReaction{
    
    public static void main(String[] args){
	int current_player = 1;
	Scanner scanner = new Scanner (System.in);     /* L: how to take input from console */
	String prompt_msg = "";
	boolean engage = false;

	ChainReactionGame game = new ChainReactionGame();
	/* L: use instance methods of separate class rather than writing whole logic 
	   in this class which strictly demands every method to be static. */
	game.start_new_board();
	
	while(true){
	    String player_ref = "PL-"+Integer.toString(current_player);   /* L: type castings in Java */
	    prompt_msg = player_ref+". Choose box in the matrix:\n";
	    System.out.println(prompt_msg);
	    String[] player_prompt = scanner.next().split(",");
	    int row = Integer.parseInt(player_prompt[0]);
	    int col = Integer.parseInt(player_prompt[1]);
	    String[] box_values = game.game_matrix[row][col].split(",");
	    if(box_values[0].equals("NULL") || box_values[0].equals(player_ref)){
		game.take_position(box_values, player_ref, row, col);
	    }
	    else{
		System.out.println("position at:("+row+","+col+")"+" already taken by "+box_values[0]+". please choose a differnt one. \n");
		/* TODO: how do you put place holders in string like in python? */
		continue;
	    }
	    System.out.print("\033\143");  /* this is to clear the console */
	    game.display_board();
	    current_player = current_player+1;
	    if(current_player > game.no_players){
		current_player = current_player%game.no_players;
		engage = true;   /* make engage true after one cycle is completed */
	    }
	    if(!engage){
		/* don't do game finish checks before engage */
		continue;
	    }
	    String checks = game.do_game_finish_checks();
	    if(checks.equals("")){
		continue;
	    }
	    else{
		System.out.println("Game won by:"+checks);
		break;
	    }
	}
    }
}


class ChainReactionGame{
    
    String[][] game_matrix = new String[16][8];
    int no_players = 2;
    

    public void start_new_board(){
	for(int i=0; i<this.game_matrix.length; i++){
	    for(int j=0; j<this.game_matrix[0].length; j++){
		this.game_matrix[i][j] = "NULL,0";
	    }
	}
    }


    public void take_position(String[] box_values, String player_ref, int row, int col){
	/* method is used to occupy the square by the player(player_ref). 
	   It occupies the square till its max limit and then expands into adjacent squares. */
	int no_of_new_balls = Integer.parseInt(box_values[1])+1;
	this.game_matrix[row][col] = player_ref+","+no_of_new_balls;
	ArrayList<int[]> adj_squares = this.get_adjacent_squares(row, col);
	if(adj_squares.size() == no_of_new_balls){
	    /* check to see if square has reached max limit */
	    this.expand_chain(row, col, player_ref, adj_squares);
	}
    }


    public void expand_chain(int row, int col, String player_ref, ArrayList<int[]> adj_squares){
	/* this method is used to make the chain reaction happen through adjacent squares by recursively taking positions */
	this.game_matrix[row][col] = "NULL,0";
	for(int val[]:adj_squares){
	    String[] box_values = this.game_matrix[val[0]][val[1]].split(",");
	    this.take_position(box_values, player_ref, val[0], val[1]);
	}
    }


    public ArrayList<int[]> get_adjacent_squares(int row, int col){
	/* method to return the adjacent squares for a particular given square */
	int no_rows = this.game_matrix.length;
	int no_cols = this.game_matrix[0].length;
	ArrayList<int[]> adj_squares = new ArrayList<int[]>();   /* how list of list works */
	if(col-1 >= 0){
	    /* left adjacent square */
	    int vals[] = new int[2];
	    vals[0] = row;
	    vals[1] = col-1;
	    adj_squares.add(vals);
	}
	if(row-1 >= 0){
	    /* upper adjacent square */
	    int vals[] = new int[2];
	    vals[0] = row-1;
	    vals[1] = col;
	    adj_squares.add(vals);
	}
	if(col+1 <= no_cols-1){
	    /* right adjacent square */
	    int vals[] = new int[2];
	    vals[0] = row;
	    vals[1] = col+1;
	    adj_squares.add(vals);
	}
	if(row+1 <= no_rows-1){
	    /* down adjacent square */
	    int vals[] = new int[2];
	    vals[0] = row+1;
	    vals[1] = col;
	    adj_squares.add(vals);
	}
	return adj_squares;
    }

    
    public String do_game_finish_checks(){
	/* method to do checks if the game has finished */
	String game_player = "NULL";
	for(int i=0; i<this.game_matrix.length; i++){
	    	for(int j=0; j<this.game_matrix[0].length; j++){
		    String[] box_values = this.game_matrix[i][j].split(",");
		    if(!box_values[0].equals("NULL") && !game_player.equals("NULL") && !box_values[0].equals(game_player)){
			return "";
		    }
		    if(!box_values[0].equals("NULL")){
			game_player = box_values[0];
		    }
		}
	}
	return game_player;
    }


    public void display_board(){
	String final_value = "";
	    for(int i=0; i<this.game_matrix.length; i++){
	    	for(int j=0; j<this.game_matrix[0].length; j++){
		    final_value = final_value + this.game_matrix[i][j]+ "|";
		}
		final_value = final_value + "\n";
	}
	System.out.println(final_value);
    }
    
}
