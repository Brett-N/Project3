import java.util.Iterator;

/**
 * This class represents a two-way linked structure.
 * Each element is connected doubly in a sequence.
 * 
 * @author Brettn
 * @cpiyush854
 * @version 11/01/23
 *
 * @param <T>
 *            The type of elements held in this collection.
 */
public class DoublyLinkedList<T> implements Iterable<T> {

    private DualNode<T> firstNode;
    private DualNode<T> lastNode;
    private int count;

    /**
     * Constructor for the DoublyLinkedList class
     */
    public DoublyLinkedList() {
        this.firstNode = null;
        this.lastNode = null;
        this.count = 0;
    }


    /**
     * Checks if the list is empty
     * 
     * @return true if empty, false if not
     */
    public boolean listIsEmpty() {
        return count == 0;
    }


    /**
     * Returns the current size of the list
     * 
     * @return int of size
     */
    public int listSize() {
        return count;
    }


    /**
     * Gives the first element in the list
     * 
     * @return DualNode representing first element
     */
    public DualNode<T> fetchHead() {
        return firstNode;
    }


    /**
     * Gives the last element in the list
     * 
     * @return DualNode representing last element
     */
    public DualNode<T> fetchTail() {
        return lastNode;
    }


    /**
     * Adds node to the front of the list
     * 
     * @param incomingNode
     *            the node to add
     */
    public void prependNode(DualNode<T> incomingNode) {
        if (firstNode == null) {
            lastNode = incomingNode;
        }
        else {
            firstNode.setPreviousElement(incomingNode);
            incomingNode.setNextElement(firstNode);
        }
        firstNode = incomingNode;
        count++;
    }


    /**
     * Adds element to the end of the list
     * 
     * @param incomingNode
     *            the object to add
     */
    public void appendNode(DualNode<T> incomingNode) {
        if (lastNode == null) {
            firstNode = incomingNode;
        }
        else {
            lastNode.setNextElement(incomingNode);
            incomingNode.setPreviousElement(lastNode);
        }
        lastNode = incomingNode;
        count++;
    }


    /**
     * Rearranges the list
     * 
     * @param targetNode
     *            the node to rearrange
     */
    public void elevate(DualNode<T> targetNode) {
        if (firstNode == targetNode)
            return;

        if (targetNode.getPreviousElement() != null) {
            targetNode.getPreviousElement().setNextElement(targetNode
                .getNextElement());
        }
        if (targetNode.getNextElement() != null) {
            targetNode.getNextElement().setPreviousElement(targetNode
                .getPreviousElement());
        }
        if (targetNode == lastNode) {
            lastNode = targetNode.getPreviousElement();
        }

        targetNode.setPreviousElement(null);
        targetNode.setNextElement(firstNode);
        if (firstNode != null) {
            firstNode.setPreviousElement(targetNode);
        }
        firstNode = targetNode;
    }


    /**
     * Deletes the specified node to from the list
     * 
     * @param targetNode
     *            the object to delete
     */
    public void deleteNode(DualNode<T> targetNode) {
        if (targetNode.getPreviousElement() != null) {
            targetNode.getPreviousElement().setNextElement(targetNode
                .getNextElement());
        }
        else {
            firstNode = targetNode.getNextElement();
        }
        if (targetNode.getNextElement() != null) {
            targetNode.getNextElement().setPreviousElement(targetNode
                .getPreviousElement());
        }
        else {
            lastNode = targetNode.getPreviousElement();
        }
        targetNode.setNextElement(null);
        targetNode.setPreviousElement(null);
        count--;
    }


    /**
     * Removes node from the front of the list
     * 
     * @return the first node from the list
     */
    public DualNode<T> extractFromFront() {
        if (firstNode == null)
            return null;
        DualNode<T> nodeToRemove = firstNode;
        deleteNode(nodeToRemove);
        return nodeToRemove;
    }


    /**
     * Removes node from the end of the list
     * 
     * @return the last node from the list
     */
    public DualNode<T> extractFromEnd() {
        if (lastNode == null)
            return null;
        DualNode<T> nodeToRemove = lastNode;
        deleteNode(nodeToRemove);
        return nodeToRemove;
    }


    /**
     * Indicates if the list has the element
     * 
     * @param element
     *            the element to find
     * @return true if found, false if not
     */
    public boolean hasValue(T element) {
        for (DualNode<T> current = firstNode; current != null; current = current
            .getNextElement()) {
            if (element.equals(current.getElementData())) {
                return true;
            }
        }
        return false;
    }


    /**
     * Empties the list
     */
    public void emptyList() {
        DualNode<T> traversalNode = firstNode;
        while (traversalNode != null) {
            DualNode<T> nextNode = traversalNode.getNextElement();
            traversalNode.setNextElement(null);
            traversalNode.setPreviousElement(null);
            traversalNode = nextNode;
        }
        firstNode = null;
        lastNode = null;
        count = 0;
    }


    /**
     * Iterates through the list
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private DualNode<T> currentNode = firstNode;

            @Override
            public boolean hasNext() {
                return currentNode != null;
            }


            @Override
            public T next() {
                if (currentNode == null) {
                    throw new java.util.NoSuchElementException();
                }
                T elementData = currentNode.getElementData();
                currentNode = currentNode.getNextElement();
                return elementData;
            }


            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }


    /**
     * Prints out a string representing the list
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        DualNode<T> traversalNode = firstNode;
        while (traversalNode != null) {
            builder.append(traversalNode.getElementData());
            traversalNode = traversalNode.getNextElement();
            if (traversalNode != null) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }
}
