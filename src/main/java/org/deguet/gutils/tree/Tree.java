package org.deguet.gutils.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deguet.gutils.string.Blanks;


/**
 * Immutable tree with parent to child direction and order between the children.
 * @author joris
 *
 * @param <T>
 */
public class Tree<T extends Serializable> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7096953078168849844L;

	private final T content;
	
	public final Tree<T>[] subtrees;
	
	/**
	 * Builds a leaf. A tree with a content but without any siblings.
	 * @param c is the content of this specific leaf.
	 */
	public Tree(T c){
		this.content = c;
		this.subtrees = new Tree[0];
	}
	
	/**
	 * Build a tree with subtrees as children.
	 * @param content is the tree node content
	 * @param sibl the subtrees of this new tree.
	 */
	public Tree(T content,Tree<T>... sibl){
		this.content = content;
		this.subtrees = sibl;
	}
	
	/**
	 * Builds a Tree from its content and its siblings.
	 * @param content
	 * @param sibl
	 */
	public Tree(T content,List<Tree<T>> sibl){
		this.content = content;
		Tree<T>[] temp = new Tree[sibl.size()];
		for (int i = 0 ; i < temp.length ; i++){
			temp[i] = sibl.get(i);
		}
		this.subtrees = temp;
	}
	
	/**
	 * Returns the iteration of paths to leaves in the tree.
	 * We recall that leaves are nodes that do not have children
	 * @return an iterable object that goes over leaf paths from the left to the 
	 * right
	 */
	public List<T> leaves(){
		if (this.subtrees.length == 0){
			ArrayList<T> result = new ArrayList<T>();
			result.add(this.content);
			return result;
		}
		else {
			ArrayList<T> result = new ArrayList<T>();
			for (int index = 0; index < subtrees.length; index++){
				Tree<T> sub = subtrees[index];
				result.addAll(sub.leaves());
			}
			return result;
		}
	}
	
	/**
	 * Returns the iteration of paths to leaves in the tree.
	 * We recall that leaves are nodes that do not have children
	 * @return an iterable object that goes over leaf paths from the left to the 
	 * right
	 */
	public List<int[]> pathsToleaves(){
		if (this.subtrees.length == 0){
			ArrayList<int[]> result = new ArrayList<int[]>();
			result.add(new int[]{});
			return result;
		}
		else {
			ArrayList<int[]> result = new ArrayList<int[]>();
			for (int index = 0; index < subtrees.length; index++){
				Tree<T> sub = subtrees[index];
				for (int[] elt : sub.pathsToleaves()){
					int[] expanded = new int[elt.length+1];
					System.arraycopy(elt, 0, expanded, 1, elt.length);
					expanded[0] = index;
					result.add(expanded);
				}
			}
			return result;
		}
	}
	
	/**
	 * Return a string representation of this tree.
	 * This is a dot representation to visualize with the graphviz tools.
	 */
	@Override
	public String toString(){
		//return this.toDot();
		return this.toString(0);
	}
	
	/**
	 * Return the ith children(subtree) of the tree.
	 * @param i the index of the desired subtree
	 * @return the subtree.
	 */
	public Tree<T> siblingAt(int i){
		return subtrees[i];
	}
	
	/**
	 * Returns the subtree of the current tree at the specified path.
	 * @param path an array of integer indicating a descending path in the tree.
	 * @return the subtree located on the branch corresponding to the path
	 * @throws IllegalArgumentException
	 */
	public Tree<T> subTreeAt(int[] path) throws IllegalArgumentException{
		Tree<T> current = this;
		for (int i = 0 ; i < path.length; i++){
			int selector = path[i];
			//if (path[i]<0 || path[i]>=current.numberOfSiblings()) throw new IllegalArgumentException();
			current = current.siblingAt(selector);
		}
		return current;
	}
	
	public T elementAt(int[] path) throws IllegalArgumentException{
		return this.subTreeAt(path).getContent();
	}
	
	/**
	 * Recursive method to give a correctly indented representation of the tree.
	 * @param indent the level of indentation which should correspond to the depth.
	 * @return
	 */
	private String toString(int indent){
		StringBuilder s = new StringBuilder();
		s.append(Blanks.blanks(1*indent)+this.content+"\n");
		for (Tree<T> sub : this.subtrees){
			s.append(sub.toString(indent+1));
		}
		return s.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + Arrays.hashCode(subtrees);
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tree other = (Tree) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (!Arrays.equals(subtrees, other.subtrees))
			return false;
		return true;
	}
	
	public String toLine(){
		if (subtrees.length == 0){
			return this.content.toString();
		}
		else{
			String result = this.content.toString();
			for (Tree<T> sub : subtrees){
				result += "<"+sub.toLine()+">";
			}
			return result;
		}
	}
	
	/**
	 * 
	 * @return the object of this node.
	 */
	public T getContent(){
		return this.content;
	}
	
	/**
	 * Returns the element toString after replacement of newlines
	 * @return
	 */
	public String getStringContent(){
		return this.content.toString().replaceAll("\\n", "NL");
	}
	
	/**
	 * Add a subtree in an immutable fashion.
	 * This means it returns a new tree but does not modify the one you called the method on.
	 * @param subtree
	 */
	public Tree<T> addSub(Tree<T> subtree){
		Tree<T>[] newSiblings = new Tree[this.subtrees.length+1];;
		System.arraycopy(this.subtrees, 0, newSiblings, 0, subtrees.length);
		newSiblings[this.subtrees.length] = subtree;
		return new Tree<T>(this.getContent(),newSiblings);
	}
	
	public Tree<T> add(T elt){
		return this.addSub(new Tree<T>(elt));
	}
	
	// TODO
	public Tree<T> addSubAt(int[] path, Tree<T> subtree){
		if (path.length == 0){
			return this.addSub(subtree);
		}
		else{
			Tree<T>[] newSiblings = new Tree[this.subtrees.length+1];
			System.arraycopy(this.subtrees, 0, newSiblings, 0, subtrees.length);
			newSiblings[this.subtrees.length] = subtree;
			return new Tree<T>(this.getContent(),newSiblings);
		}
		
	}
	
	/**
	 * Returns a new tree with the provided children added.
	 * They are added at the right of the lst of siblings.
	 * @param subtree
	 */
	public Tree<T> addSubs(List<Tree<T>> subs){
		List<Tree<T>> siblings = new ArrayList<Tree<T>>();
		for (Tree<T> sibling : this.subtrees){siblings.add(sibling);}
		siblings.addAll(subs);
		return new Tree<T>(this.getContent(),siblings);
	}

	/**
	 * Returns a XML representation of this tree using the toString method.
	 * for node<s content.
	 * @return
	 */
	public String toXML(){
		return this.toXML(0);
	}
	
	/**
	 * The number of siblings of this tree.
	 * @return The number of subtrees in this tree.
	 */
	public int numberOfSiblings(){
		return this.subtrees.length;
	}
	
	private String toXML(int level){
		if (this.subtrees.length ==0){
			return Blanks.blanks(2*level)+""+this.getStringContent()+"\n";
		}
		else{
			String result = "";
			result += Blanks.blanks(2*level)+"<"+this.getStringContent()+">\n";
			for (Tree<T> sub : this.subtrees){
				result += sub.toXML(level+1);
			}
			result += Blanks.blanks(2*level)+"</"+this.getStringContent()+">\n";
			return result;
		}
	}
	
	/**
	 * Produce a annotated dot version where all leaves should have the same index to be aligned
	 * @param depth
	 * @return
	 */
	private String toLinks(int depth, String prefix){
		StringBuilder sb = new StringBuilder();
		sb.append("\n \""+dotLabel(this.getContent())+prefix+"\"");
		for (int i = 0 ; i < subtrees.length ; i++){
			Tree<T> sub = subtrees[i];
			String tag1 = dotLabel(this.getContent());
			String tag2 = dotLabel(sub.getContent());
			sb.append("\n   \""+tag1+prefix+"\"  [label=\""+tag1+"\"] ");
			String leafOptions = "";
			if (sub.subtrees.length == 0){
				leafOptions =",shape=diamond";
				sb.append("\n subgraph leaves  {rank=same \""+tag2+(prefix+"."+i)+"\"}");
			}
			sb.append("\n   \""+tag2+(prefix+"."+i)+"\"  [label=\""+tag2+"\""+leafOptions+"] ");
			sb.append("\n   \""+tag1+prefix+"\" -> \""+tag2+(prefix+"."+i)+ "\"");
			sb.append(sub.toLinks(depth+1,prefix+"."+i));
		}
		return sb.toString();
	}
	
	// used in toLinks
	private String dotLabel(T object){
		String result = object.toString();
		result = (result.equals("\n")?"NLine":result);
		result = result.replaceAll("\t", "TAB");
		result = result.replaceAll("\n", "NL");
		result = result.replaceAll("\\\"", "''");
		result = result.replaceAll("\"", "''");
		return result;
	}
	
	/**
	 * Returns a dot compatible version of the tree.
	 * @return
	 */
	public String toDot(){
		return "digraph tree   \n{\nrankdir=RL"+ this.toLinks(0, "") +"\n}";
	}
	
	public int depth(){
		int result = 1;
		for(Tree<T> t : subtrees){
			int subDepth = t.depth() + 1;
			if (t.depth() > result) result = subDepth;
		}
		return result;
	}
}

