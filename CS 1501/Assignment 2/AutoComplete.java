/**
 * An implementation of the AutoCompleteInterface using a DLB Trie.
 */

 public class AutoComplete implements AutoCompleteInterface {

  private DLBNode root;
  private StringBuilder currentPrefix;
  private DLBNode currentNode;
  private DLBNode tempNode;
  private int numberOfFakeNodes = 0;

  public AutoComplete(){
    root = null;
    currentPrefix = new StringBuilder();
    currentNode = null;
  }

  /**
   * Adds a word to the dictionary in O(alphabet size*word.length()) time
   * @param word the String to be added to the dictionary
   * @return true if add is successful, false if word already exists
   * @throws IllegalArgumentException if word is the empty string
   */
    public boolean add(String word) {
        DLBNode mover = null;
        
        if (word.isEmpty()) { //If the word is empty, throw an exception
            throw new IllegalArgumentException("Word cannot be empty");
        }

        int currentChar = 0;
        //If the root is null, we make the first branch of the trie
        if (root == null) {
            root = new DLBNode(word.charAt(currentChar));
            mover = root;
            mover.size++;
            word = word.substring(++currentChar);
            for (char x : word.toCharArray()) {
                mover.child = new DLBNode(x);
                mover.child.parent = mover;
                mover = mover.child;
                mover.size++;
            }
            mover.isWord = true;
            return true;
        }
        
        else {
            mover = root;
            if (addHelper(word, mover) == true) {
                return true;
            }
            else return false;
        }
        
    }

    public boolean addHelper (String word, DLBNode mover){
        if (mover.data == word.charAt(0) && word.length() == 1 && mover.isWord == true) { //If the whole word was already contained in the tree, and it's a word already, return false
            return false;
        }
        if (mover.data == word.charAt(0) && word.length() == 1 && mover.isWord == false) { //If the whole word was already contained in the tree, and it;s not a word already, return true
            mover.size++;
            mover.isWord = true;
            return true;
        }
        else if (mover.data == word.charAt(0)) { //If the first character of the word matches the current node, we move down the tree
            word = word.substring(1);
            if (mover.child == null) { //If there is no child, we make a new branch
                for (char x : word.toCharArray()) {
                    mover.child = new DLBNode(x);
                    mover.child.parent = mover;
                    mover.size++;
                    mover = mover.child;
                }
                mover.size++;
                mover.isWord = true;
                return true;
            }
            else { //If there is a child, we move down the tree
                if (addHelper(word, mover.child) == true) {
                    mover.size++;
                    return true;
                }
                else return false;
            }
        }
        else if (mover.nextSibling != null) { //If the first character of the word does not match the current node, we move to the next sibling
            if (addHelper(word, mover.nextSibling) ==true) {
                return true;
            }
            else return false;
        }
        else{ //If there is no match, we make a new branch
            mover.nextSibling = new DLBNode(word.charAt(0));
            mover.nextSibling.previousSibling = mover;
            mover.nextSibling.parent = mover.parent;
            mover = mover.nextSibling;
            word = word.substring(1);
            for (char x : word.toCharArray()) {
                mover.child = new DLBNode(x);
                mover.child.parent = mover;
                mover.size++;
                mover = mover.child;
            }
            mover.size++;
            mover.isWord = true;
            return true;
        }
    }

  /**
   * appends the character c to the current prefix in O(alphabet size) time. 
   * This method doesn't modify the dictionary.
   * @param c: the character to append
   * @return true if the current prefix after appending c is a prefix to a word 
   * in the dictionary and false otherwise
   */
    public boolean advance(char c){
        DLBNode mover = currentNode;
        currentPrefix.append(c);

        if (currentNode == null) {  //IF currentNode is null
            currentNode = root;
            mover = currentNode;
            tempNode = currentNode;
            if (root.data == c){
                tempNode = currentNode;
                return true;
            }
            for(;currentNode.nextSibling != null; currentNode = currentNode.nextSibling){
                if (currentNode.nextSibling.data == c){
                    tempNode = currentNode.nextSibling;
                    currentNode = currentNode.nextSibling;
                    return true;
                }
            }
            tempNode = new DLBNode(c);
            numberOfFakeNodes++;
            return false;
        }
        
        mover = getNode(mover, currentPrefix.toString(), currentPrefix.length()-2);
        if (mover != null) { //If the current prefix is in the tree
            currentNode = mover; 
            tempNode = currentNode;
            return true;
        }

        tempNode = new DLBNode(c);
        numberOfFakeNodes++;
        return false;
    }

  /**
   * removes the last character from the current prefix in O(alphabet size) time. This 
   * method doesn't modify the dictionary.
   * @throws IllegalStateException if the current prefix is the empty string
   */
    public void retreat(){
        if (currentPrefix.length() == 0) {
            throw new IllegalStateException();
        }
        currentPrefix.deleteCharAt(currentPrefix.length()-1);
        
        if(numberOfFakeNodes > 0){
            numberOfFakeNodes--;
            if (numberOfFakeNodes == 0){
                tempNode = currentNode;
            }
            return;
        }
        
        currentNode = currentNode.parent;
        tempNode = currentNode;
    }

  /**
   * resets the current prefix to the empty string in O(1) time
   */
    public void reset(){
        currentPrefix = new StringBuilder();
        currentNode = null;
        tempNode = null;
        numberOfFakeNodes = 0;

    }
    
  /**
   * @return true if the current prefix is a word in the dictionary and false
   * otherwise. The running time is O(1).
   */
    public boolean isWord(){
      return tempNode.isWord;
    }

  /**
   * adds the current prefix as a word to the dictionary (if not already a word)
   * The running time is O(alphabet size*length of the current prefix). 
   */
    public void add(){
        add(currentPrefix.toString());
        currentNode = root;
        tempNode.isWord = true;
    }

  /** 
   * @return the number of words in the dictionary that start with the current 
   * prefix (including the current prefix if it is a word). The running time is 
   * O(1).
   */
    public int getNumberOfPredictions() {
        if (currentPrefix.length() == 0) {
            return 0;
        }
        return tempNode.size;
    }
  
  /**
   * retrieves one word prediction for the current prefix. The running time is 
   * O(prediction.length())
   * @return a String or null if no predictions exist for the current prefix
   */
    public String retrievePrediction(){
        String prediction = currentPrefix.toString();
        DLBNode mover = currentNode;
        if (currentPrefix.length() == 0) {
            return null;
        }
        if (mover.isWord == true && mover.child != null) {
            return prediction + mover.data;
        }
        if(numberOfFakeNodes > 0){
            return null;
        }
        while (mover.child != null) {
            mover = mover.child;
            prediction = prediction + mover.data;
            if (mover.isWord == true) {
                return prediction;
            }
        }
        return prediction;
    }


  /* ==============================
   * Helper methods for debugging.
   * ==============================
   */

  //print the subtrie rooted at the node at the end of the start String
  public void printTrie(String start){
    System.out.println("==================== START: DLB Trie Starting from \""+ start + "\" ====================");
    if(start.equals("")){
      printTrie(root, 0);
    } else {
      DLBNode startNode = getNode(root, start, 0);
      if(startNode != null){
        printTrie(startNode.child, 0);
      }
    }
    
    System.out.println("==================== END: DLB Trie Starting from \""+ start + "\" ====================");
  }

  //a helper method for printTrie
  private void printTrie(DLBNode node, int depth){
    if(node != null){
      for(int i=0; i<depth; i++){
        System.out.print(" ");
      }
      System.out.print(node.data);
      if(node.isWord){
        System.out.print(" *");
      }
      System.out.println(" (" + node.size + ")");
      printTrie(node.child, depth+1);
      printTrie(node.nextSibling, depth);
    }
  }

  //return a pointer to the node at the end of the start String 
  //in O(start.length() - index)
  private DLBNode getNode(DLBNode node, String start, int index){
    if(start.length() == 0){
      return node;
    }
    DLBNode result = node;
    if(node != null){
      if((index < start.length()-1) && (node.data == start.charAt(index))) {
          result = getNode(node.child, start, index+1);
      } else if((index == start.length()-1) && (node.data == start.charAt(index))) {
          result = node;
      } else {
          result = getNode(node.nextSibling, start, index);
      }
    }
    return result;
  } 

  //The DLB node class
  private class DLBNode{
    private char data;
    private int size;
    private boolean isWord;
    private DLBNode nextSibling;
    private DLBNode previousSibling;
    private DLBNode child;
    private DLBNode parent;

    private DLBNode(char data){
        this.data = data;
        size = 0;
        isWord = false;
        nextSibling = previousSibling = child = parent = null;
    }
  }
}
