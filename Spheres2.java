import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
 

public class Spheres2{
	 class node{
		int cost;
		char[] inst = new char[2*n + 1];
		ArrayList <node> children = new ArrayList<node>();
		node parent;
		int gap;
		boolean searched;
		
		}
	
	int n;
	int min_cost = 100;
	int ptr = 0,count = 0;
	
	ArrayList <node> tree = new ArrayList<node>();
	ArrayList <char[]> results = new ArrayList<char[]>();
	//int current_node = 0;
	
	
	
	//checks if the black block are in front!
	private boolean found_solution(node x){
		int i;
		int m_count = 0,a_count = 0;
		for(i=0;i<=2*n;i++){
			if(x.inst[i] == 'm'){
				m_count++;
			}else if(x.inst[i] == 'a'){
				a_count++;
			}
			
			if(m_count == n && a_count == 0 && x.inst[2*n] != '-'){
				return true;
			}
		}
		return false;
	}
	
	private void find_children(node t){
		int i,j;
		//Random rand = new Random();
		//System.out.println("gap is "+ t.gap);
		if(t.gap - n < 0){
			for(i=0;i<=t.gap + n;i++){
				if(i == t.gap){
					continue;
				}
				char[] temp = new char[2*n+1];
				temp = (t.inst).clone();
				temp[i] = t.inst[t.gap];
				temp[t.gap] = t.inst[i];
				/*for(j = 0;j<2*n+1;j++){
					System.out.print(t.inst[j]);
					
				}
				System.out.println();
				for(j = 0;j<2*n+1;j++){
					System.out.print(temp[j]);
					
				}
				System.out.println();*/
				node new_node = new node();
				new_node.cost = t.cost + Math.abs(t.gap - i);
				new_node.inst = temp;
				new_node.parent = t;
				new_node.gap = i;
				new_node.searched = false;
				if(search_if_exists(new_node)){
					t.children.add(new_node);
					tree.add(new_node);
				}
				
			}
			
			t.searched = true;
		}else{
			for(i=t.gap - n;i<=2*n;i++){
				if(i == t.gap){
					continue;
				}
				char[] temp = new char[2*n+1];
				temp = (t.inst).clone();
				temp[i] = t.inst[t.gap];
				temp[t.gap] = t.inst[i];
				/*for(j = 0;j<2*n+1;j++){
					System.out.print(t.inst[j]);
					
				}
				System.out.println();
				for(j = 0;j<2*n+1;j++){
					System.out.print(temp[j]);
					
				}
				System.out.println();*/
				node new_node = new node();
				new_node.cost = t.cost + Math.abs(t.gap - i);
				new_node.inst = temp;
				new_node.parent = t;
				new_node.gap = i;
				new_node.searched = false;
				if(search_if_exists(new_node)){
					t.children.add(new_node);
					tree.add(new_node);
				}
				
			}
			t.searched = true;
		}
			
				
	}
	private boolean search_if_exists(node x){
		int i,j,equals = 0;
		for(i=0;i<tree.size();i++){
			for(j=0;j<2*n+1;j++){
				if(tree.get(i).inst[j] == x.inst[j]){
					equals++;
				}
			}
			if(equals == 2*n+1){
				
				return false;
			}
			equals = 0;
		}
		return true;
	}
	
	private void Astar(){
		
		int i;
		count++;
		node temp = new node();
		for(i=0;i<tree.size();i++){
			
			temp = tree.get(i);
			if(temp.searched){
				continue;
			}else{
				if(temp.cost + heuristic(temp) < min_cost){
					min_cost = temp.cost + heuristic(temp);
					ptr = i;
				}
			}
		}
		min_cost = 100;
		if(found_solution(tree.get(ptr))){
			System.out.println("found it");
			System.out.println("final cost = " + tree.get(ptr).cost);
			show_result(tree.get(ptr));
			//return tree.get(ptr);
		}else{
			find_children(tree.get(ptr));
			//if(count<10){
			Astar();
			//}
		}
	}
	private int heuristic(node cur){
		int m_count = 0,a_count = 0;
		for(int i = 0;i < n;i++){
			if(cur.inst[i] == 'm'){
				m_count++;
				if(m_count == n){
					return a_count * n;
				}
				
			}else if(cur.inst[i] == 'a'){
				a_count++;
			}else{
				continue;
			}
		}
		return -1;
	}
	
	private void show_result(node out){
		int i;
		if(out.parent == null){
			results.add(out.inst);
			
			for(i = results.size() - 1; i >= 0;i--){
				System.out.println(results.get(i));
			}
		}else{
			results.add(out.inst);
			show_result(out.parent);
		}
	}
	public void init(node root){
		int i;
		int n_err = 0,s_err = 0,m_count,a_count,gap_count;
		root.cost = 0;
		
		root.parent = null;
		root.searched = false;
		System.out.println("enter the number of white and black spheres");
		Scanner scant = new Scanner(System.in);
		int num;
		while(n_err == 0){
			num = scant.nextInt();
			if(num > 0){
				n = num;
				n_err++;
			}else{
				System.out.println("invalid input.Enter a positive integer");
			}
		}
			
		System.out.println("enter initial distribution of the spheres,ex.ma-ma");
		String arr = "";
		while(s_err == 0){
			m_count = 0;
			a_count = 0;
			gap_count = 0;
			arr = scant.next();
			if(arr.length()!= 2*n+1){
				System.out.println("invalid input.The size of your input must be the same as the one you declared above.");
				continue;
			}
			for(i=0;i<=2*n;i++){
				if(arr.charAt(i) == 'm'){
					m_count++;
				}else if(arr.charAt(i) == 'a'){
					a_count++;
				}else if(arr.charAt(i) == '-'){
					gap_count++;
				}else{
					System.out.println("invalid input.Enter only 'm','a' and '-' " );
				}
			
			}
				if(m_count == n && a_count == n && gap_count == 1){
					s_err++;
				}else{
					System.out.println("invalid input.The size of your input must be the same as the one you declared above.");
					System.out.println("With the same number of 'm' and 'a' and with only one '-' ");
				}
			
		}
		//System.out.println("wait");
		char[] ready = new char[2*n + 1];
		for(i = 0; i<arr.length();i++){
			ready[i] = arr.charAt(i);
			//System.out.println(ready[i]);
			if(arr.charAt(i) == '-'){
				root.gap = i;
			}
		}
		root.inst = ready;
		tree.add(root);
		
	}
	
	
	public static void main(String[] args){
		int i;
		ai_1b goal = new ai_1b();
		ai_1b.node in_node = new ai_1b().new node();
		
		goal.init(in_node);
		
		goal.Astar();
		
	}
	
}