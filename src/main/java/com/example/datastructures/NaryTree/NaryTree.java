package com.example.datastructures.NaryTree;

import java.util.ArrayList;

public class NaryTree<V>{

    private Node<V> root;

    public NaryTree() {
    }

    public NaryTree(Node<V> root) {
        this.root = root;
    }

    public ArrayList<V> preorderList(Node<V> current) {
        ArrayList<V> list = new ArrayList<>();
        preorderListH(current, list);
        return list;
    }

    private void preorderListH(Node<V> current, ArrayList<V> list) {
        if (current == null) {
            return;
        }
        list.add(current.getValue());
        for (Node<V> child : current.getChildren()) {
            preorderListH(child, list);
        }
    }
    public void deleteNode(V child, V dad){
        if(child.equals(root.getValue())){
            if (!root.isLeaf()){
                root = null;
            }else System.out.println("Cannot delete root");
        }else {
            Node<V> nodeDad = root.searchValue(dad);
            if (nodeDad == null) System.out.println("Not found");
            else nodeDad.deleteChild( child);
        }
    }

    public int weight (){
        if (root == null) return 0;
        else return root.weight();
    }
    public int treeHeight (){
        if (root == null) return 0;
        else return root.treeHeight();
    }

    public String preorder(Node<V> current) {
        if (current == null) {
            return "";
        }
        StringBuilder list = new StringBuilder();
        list.append(current.getValue());
        for (Node<V> child : current.getChildren()) {
            list.append(preorder(child));
        }
        return list.toString();
    }



    public void insertNode (V node, V dad){
        if (root == null) {
            root = new Node<>(node);
        }else{
            Node<V> nodeDad = root.searchValue(dad);
            if (nodeDad != null)  nodeDad.insertNode(node);
            else System.out.println("There is not dad");
        }
    }

    public Node<V> searchValue (V value){
        if (root == null)return null;
        else{
            return this.searchValue(value);
        }
    }

    public Node<V> getRoot() {
        return root;
    }

    public void setRoot(Node<V> root) {
        this.root = root;
    }
}
