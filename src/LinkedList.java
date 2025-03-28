import java.util.Random;

// linked list class for a deck of cards
public class LinkedList {

    public Node head;
    public Node tail;
    public int size = 0;

    LinkedList(){
        head = null;
        tail = null;
        size = 0;
    }

    public void shuffle(int shuffle_count) {

        Random rand = new Random();
        for(int i = 0; i < shuffle_count; i++) {
            // pick two random integers
            int r1 = rand.nextInt(52);
            int r2 = rand.nextInt(52);

            swap(r1,r2); // swap nodes at these indices
            sanity_check(); //debug
        }
    }

    // remove a card from a specific index
    public Card remove_from_index(int index) {
        if (index == 0) {
            return remove_from_head();
        }
        else if (index == size-1) {
            return remove_from_tail();
        }
        else if (index < size-1) {
            Node curr = head; //shallow copy
            for (int i=0; i<size-1; i++) {
                if (i == index) {
                    curr.prev.next = curr.next;
                    curr.next.prev = curr.prev;
                    size--;
                    return curr.data;
                }
                else {
                    curr = curr.next;
                }
            }
            System.out.println("Something is very wrong with remove_from_index.");
            return null; //won't ever run, exists to avoid compiler error
        }
        else {
            System.out.println("Index out of bounds of LinkedList. Aborting insert operation.");
            return null;
        }
    }

    // insert a card at a specific index
    public void insert_at_index(Card x, int index) {
        if (index == 0) {
            add_at_head(x);
        }
        else if (index == size) {
            add_at_tail(x);
        }
        else if (index < size) {
            Node curr = head; //shallow copy
            for (int i=0; i<size; i++) {
                if (i == index) {
                    Node t = new Node(x);
                    t.prev = curr.prev;
                    t.next = curr;
                    curr.prev.next = t;
                    curr.prev = t;
                    break;
                }
                else {
                    curr = curr.next;
                }
            }
            size++;
        }
        else {
            System.out.println("Index out of bounds of LinkedList. Aborting insert operation.");
        }
    }

    // swap two cards in the deck at the specific indices
    public void swap(int index1, int index2) {
        if (index1 > size-1 || index2 > size-1) {
            System.out.println("Index out of bounds of LinkedList. Aborting swap operation.");
        }
        else {
            //removes the farthest index first, then adds the closest index first
            if (index1 < index2) {
                Card b = remove_from_index(index2);
                Card a = remove_from_index(index1);
                insert_at_index(b, index1);
                insert_at_index(a, index2);
            }
            else if (index2 < index1) {
                Card a = remove_from_index(index1);
                Card b = remove_from_index(index2);
                insert_at_index(a, index2);
                insert_at_index(b, index1);
            }
            else {
                System.out.println("Indexes of swap are equal. Aborting swap operation.");
            }
        }
    }

    // add card at the beginning of the list
    public void add_at_head(Card data) {
        if (head == null) {
            //set head and tail to one new Node
            head = new Node(data);
            tail = head;
        }
        else {
            Node h = new Node(data); //shallow copy
            head.prev = h; //point original head's prev to new Node
            h.next = head; //point new Node's head to original head
            head = h; //reassign tail to the new Node
        }
        size++;
    }

    // add card at the end of the list
    public void add_at_tail(Card data) {
        if (head == null) {
            //set head and tail to one new Node
            head = new Node(data);
            tail = head;
        }
        else {
            Node t = new Node(data); //shallow copy
            tail.next = t; //point original tail to new Node
            t.prev = tail; //point new Node's prev to original tail
            tail = t; //reassign tail to the new Node
        }
        size++; //increase list size
    }

    // remove a card from the beginning of the list
    public Card remove_from_head() {
        if (head == null) {
            System.out.println("Head of LinkedList is null. Aborting remove operation.");
            return null;
        }
        else {
            Node h = head; //shallow copy
            head = h.next; //head points to next node
            head.prev = null; //remove pointer to h
            size--;
            return h.data; //return card at h, h is deleted from memory since there's no pointers to it
        }
    }

    // remove a card from the end of the list
    public Card remove_from_tail() {
        if (head == null) {
            System.out.println("Head of LinkedList is null. Aborting remove operation.");
            return null;
        }
        else {
            Node t = tail; //shallow copy
            tail = t.prev; //head points to previous node
            tail.next = null; //remove pointer to t
            size--;
            return t.data; //return card at t, t is deleted from memory since there's no pointers to it
        }
    }

    // check to make sure the linked list is implemented correctly by iterating forwards and backwards
    // and verifying that the size of the list is the same when counted both ways.
    // 1) if a node is incorrectly removed
    // 2) and head and tail are correctly updated
    // 3) each node's prev and next elements are correctly updated
    public void sanity_check() {
        // count nodes, counting forward
        Node curr = head;
        int count_forward = 0;
        while (curr != null) {
            curr = curr.next;
            count_forward++;
        }

        // count nodes, counting backward
        curr = tail;
        int count_backward = 0;
        while (curr != null) {
            curr = curr.prev;
            count_backward++;
        }

        // check that forward count, backward count, and internal size of the list match
        if (count_backward == count_forward && count_backward == size) {
            System.out.println("Basic sanity Checks passed");
        }
        else {
            // there was an error, here are the stats
            System.out.println("Count forward:  " + count_forward);
            System.out.println("Count backward: " + count_backward);
            System.out.println("Size of LL:     " + size);
            System.out.println("Sanity checks failed");
            System.exit(-1);
        }
    }

    // print the deck
    public void print() {
        Node curr = head;
        int i = 1;
        while(curr != null) {
            curr.data.print_card();
            if(curr.next != null)
                System.out.print(" -->  ");
            else
                System.out.println(" X");

            if (i % 7 == 0) System.out.println("");
            i = i + 1;
            curr = curr.next;
        }
        System.out.println("");
    }
}