package com.example.datastructures.NaryTree;

import java.util.ArrayList;

public class Node<V> {

    private V value;
    private ArrayList<Node<V>> children;

    public Node<V> getDad() {
        return dad;
    }

    public void setDad(Node<V> dad) {
        this.dad = dad;
    }

    private Node<V> dad;

    public Node(V value, Node<V> dad) {
        this.value = value;
        this.dad = dad;
        children = new ArrayList<>();
    }


    public void deleteChild(V child){
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getValue().equals(child) ){
                if(children.get(i).isLeaf()) children.remove(i);
                else System.out.println("Cannot delete");
            }
        }
    }

    public int treeHeight(){
        if (isLeaf()) return 1;
        else {
            int  maxHeight = 0 ;
            for (Node<V> child : children) {
                int tempHeight = child.treeHeight();
                if ( tempHeight > maxHeight ) maxHeight = tempHeight;
            }
            return maxHeight +1;
        }
    }

    public int weight(){
        if (isLeaf()){
            return 1;
        }
        int count = 1;
        for (Node<V> child : children) {
            count += child.weight();
        }
        return count;
    }

    public ArrayList<Node<V>> preOrder(ArrayList<Node<V>> nodeList){
        if (isLeaf()){
            nodeList.add(this);
            return nodeList;
        }
        nodeList.add(this);
        for (Node<V> child : children) {
            child.preOrder(nodeList);
        }
        return nodeList;
    }
    public ArrayList<Node<V>> postOrder(ArrayList<Node<V>> nodeList){
        if (isLeaf()){
            nodeList.add(this);
            return nodeList;
        }
        for (Node<V> child : children) {
            child.postOrder(nodeList);
        }
        nodeList.add(this);
        return nodeList;
    }

    public Node<V> searchValue(V value){
        if (value.equals(this.getValue())) return this;
        if (this.isLeaf()) return null;

        for (int i  =0; i < children.size(); i++){
            Node<V> searchValue= getChildren().get(i).searchValue(value);
            if (searchValue != null) return searchValue;
        }
        return null;
    }

    public void insertNode(V toInsert, Node<V> dad){
        this.children.add(new Node<>(toInsert,dad));
    }

    public boolean isLeaf(){
        if (children.size() == 0) return true;
        else return false;
    }


    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public ArrayList<Node<V>> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Node<V>> children) {
        this.children = children;
    }
}
