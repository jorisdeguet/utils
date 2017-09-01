package org.deguet.gutils.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.deguet.gutils.string.Blanks;

/**
 * Trees.
 * @author joris
 *
 * @param <T>
 */
public class MuTree<T extends Serializable> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6582733467760310823L;

	private final T content;
	
	public final List<MuTree<T>> subtrees;
	
	public MuTree(T dname){
		this.content = dname;
		this.subtrees = new ArrayList<MuTree<T>>();
	}
	
	public MuTree(T content, List<MuTree<T>> sibl){
		this.content = content;
		this.subtrees = sibl;
	}

	
	public T getContent(){
		return this.content;
	}
	
	
	/**
	 * Add a subtree in an immutable fashion.
	 * @param subtree
	 */
	public void add(MuTree<T> subtree){
		//System.out.println("Subtree request for " + subtree);
		subtrees.add(subtree);
	}
	
	public void addFirst(MuTree<T> subtree){
		//System.out.println("Subtree request for " + subtree);
		subtrees.add(0,subtree);
	}

	public String toXML(){
		return this.toXML(0);
	}
	
	public Tree<T> toImmutable(){
		List<Tree<T>> sibl = new ArrayList<Tree<T>>();
		for (MuTree<T> sub : this.subtrees){
			sibl.add(sub.toImmutable());
		}
		return new Tree<T>(this.content,sibl);
	}
	
	private String toXML(int level){
		if (this.subtrees.isEmpty()){
			return Blanks.blanks(2*level)+"<"+this.getContent()+">\n";
		}
		else{
			String result = "";
			result += Blanks.blanks(2*level)+"<"+this.getContent()+">\n";
			for (MuTree<T> sub : this.subtrees){
				result += sub.toXML(level+1);
			}
			result += Blanks.blanks(2*level)+"</"+this.getContent()+">\n";
			return result;
		}
	}
	
}
