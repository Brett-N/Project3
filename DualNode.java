/**
 * This class represents a single element or node in a two-way linked structure.
 *
 * 
 * @author Brettn
 * @author cpiyush854
 * @version 11/1/2023
 * @param <T>
 *            The type of element contained in the node.
 */
public class DualNode<T> {
    private T elementData;
    private DualNode<T> previousElement;
    private DualNode<T> nextElement;

    /**
     * Constructor for the DualNode class
     * 
     * @param content
     *            the data in the node
     */
    public DualNode(T content) {
        this.setElementData(content);
    }


    /**
     * Gets data from the DualNode
     * 
     * @return the data in the node
     */
    public T getElementData() {
        return elementData;
    }


    /**
     * Stores data in the DualNode
     * 
     * @param elementData
     *            the data to be stored
     */
    public void setElementData(T elementData) {
        this.elementData = elementData;
    }


    /**
     * Gets the previous node
     * 
     * @return the Node representing the prevoius node
     */
    public DualNode<T> getPreviousElement() {
        return previousElement;
    }


    /**
     * Assigns previous ndoe
     * 
     * @param previousElement
     *            the node to be assigned
     */
    public void setPreviousElement(DualNode<T> previousElement) {
        this.previousElement = previousElement;
    }


    /**
     * Gets the next node
     * 
     * @return the node that is next in the list
     */
    public DualNode<T> getNextElement() {
        return nextElement;
    }


    /**
     * Assigns the next node
     * 
     * @param nextElement
     *            the node to be assigned
     */
    public void setNextElement(DualNode<T> nextElement) {
        this.nextElement = nextElement;
    }


    /**
     * Appends a new element
     * 
     * @param newElement
     *            the element to append
     */
    public void appendNewElement(DualNode<T> newElement) {
        newElement.setNextElement(this.nextElement);
        newElement.setPreviousElement(this);
        if (this.nextElement != null) {
            this.nextElement.setPreviousElement(newElement);
        }
        this.nextElement = newElement;
    }


    /**
     * Prepends a new element
     * 
     * @param newElement
     *            the element to prepend
     */
    public void prependNewElement(DualNode<T> newElement) {
        newElement.setPreviousElement(this.previousElement);
        newElement.setNextElement(this);
        if (this.previousElement != null) {
            this.previousElement.setNextElement(newElement);
        }
        this.previousElement = newElement;
    }


    /**
     * Detaches an element
     */
    public void detachElement() {
        if (this.previousElement != null) {
            this.previousElement.setNextElement(this.nextElement);
        }
        if (this.nextElement != null) {
            this.nextElement.setPreviousElement(this.previousElement);
        }
        this.nextElement = null;
        this.previousElement = null;
    }


    /**
     * Indicates if the element is the head
     * 
     * @return true if head, false if not
     */
    public boolean isInitialElement() {
        return this.previousElement == null;
    }


    /**
     * Indicates if the element if the tail
     * 
     * @return true if tail, false if not
     */
    public boolean isTerminalElement() {
        return this.nextElement == null;
    }


    /**
     * Gives a String representation of the node
     * 
     * @return String representation of the node
     */
    @Override
    public String toString() {
        return String.valueOf(elementData);
    }
}
