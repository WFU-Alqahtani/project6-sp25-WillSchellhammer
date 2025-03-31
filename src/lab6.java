import java.util.Scanner;

public class lab6 {

    public static final boolean DEBUG = false; //Sends extra debug messages to the console and does extra sanity checks during shuffling.

    public static int num_cards_dealt = 5; //cannot be more than 26

    public static LinkedList initialize_deck() {
        LinkedList deck = new LinkedList();

        // populate linked list with a single deck of cards
        for (Card.suites s : Card.suites.values()) {
            for(Card.ranks r : Card.ranks.values()) {
                if (r != Card.ranks.NULL && s != Card.suites.NULL) {
                    Card newCard = new Card(s, r);
                    //newCard.print_card();
                    deck.add_at_tail(newCard);
                }
            }
        }

        return deck;
    }

    private static void play_blind_mans_bluff(LinkedList player1, LinkedList computer, LinkedList deck) {
        Scanner input = new Scanner(System.in);

        System.out.println("\nStarting Blind Man's Bluff with a hand of " + num_cards_dealt + " cards.");

        int wins = 0;
        int losses = 0;
        int lossesInRow = 0;
        while (!player1.isEmpty()) {
            System.out.println("\nGame " + (wins+losses+1) + ": Drawing cards... ");
            Card playerCard = player1.remove_from_head();
            Card computerCard = computer.remove_from_head();
            System.out.println("Remaining cards in hand: " + player1.size);

            System.out.print("The computer's card is a " + computerCard.getRank().toString().toUpperCase() + " of " + computerCard.getSuit() + ". ");
            boolean high = highOrLow(input);
            if (cardCompare(playerCard, computerCard, high)) {
                System.out.print("Correct! Your card was a " + playerCard.getRank().toString().toUpperCase() + " of " + playerCard.getSuit() + ". ");
                System.out.println("Wins: " + (wins++ + 1));
                lossesInRow = 0;
            }
            else {
                System.out.print("Incorrect. Your card was a " + playerCard.getRank().toString().toUpperCase() + " of " + playerCard.getSuit() + ". ");
                System.out.println("Losses: " + (losses++ + 1));
                lossesInRow++;
            }
            deck.add_at_tail(playerCard);
            deck.add_at_tail(computerCard);
            if (lossesInRow == 3) {
                rage_quit(player1, computer, deck);
                lossesInRow = 0;
            }
        }
        System.out.println("\nGame complete!");
        System.out.println("Wins: " + wins + ", Losses: " + losses);
    }

    public static void rage_quit(LinkedList player1, LinkedList computer, LinkedList deck) {
        while (!player1.isEmpty()) {
            deck.add_at_tail(player1.remove_from_head());
        }
        while (!computer.isEmpty()) {
            deck.add_at_tail(computer.remove_from_head());
        }

        deck.shuffle(512);
        if (lab6.DEBUG) deck.print();
        deck.sanity_check(); // because we can all use one

        for (int i = 0; i < num_cards_dealt; i++) {
            // player removes a card from the deck and adds to their hand
            player1.add_at_tail(deck.remove_from_head());
            computer.add_at_tail(deck.remove_from_head());
        }

        System.out.println("\nRAGE QUIT!! Hand reset!");
    }

    //interprets player input whether to guess high or low
    private static boolean highOrLow(Scanner input) {
        while (true) {
            System.out.print("Is your card higher (+) or lower (-)? ");
            String s = input.nextLine();
            switch (s) {
                case "higher", "high", "h", "+", "(+)":
                    return true;
                case "lower", "low", "l", "-", "(-)":
                    return false;
                default:
                    System.out.print("Please input high or low. ");
                    break;
            }
        }
    }

    //true means the guess of a is correct, false means the guess of a was incorrect.
    private static boolean cardCompare(Card a, Card b, boolean high) {
        if (a.getRank().ordinal() > b.getRank().ordinal()) {
            return high;
        }
        else if (a.getRank().ordinal() < b.getRank().ordinal()) {
            return !high;
        }
        else {
            if (a.getSuit().ordinal() > b.getSuit().ordinal()) {
                return high;
            }
            else if (a.getSuit().ordinal() < b.getSuit().ordinal()) {
                return !high;
            }
            else {
                System.out.println("Error: Card values are exactly the same! Exiting program...");
                System.exit(1);
                return false;
            }
        }
    }

    public static void main(String[] args) {

        // create a deck (in order)
        LinkedList deck = initialize_deck();
        if (lab6.DEBUG) deck.print();
        deck.sanity_check(); // because we can all use one

        /*
        //tests (debug)
        deck.remove_from_tail();
        deck.sanity_check();

        deck.remove_from_head();
        deck.sanity_check();

        deck.add_at_head(new Card(Card.suites.SPADES, Card.ranks.king));
        deck.sanity_check();

        deck.add_at_tail(new Card(Card.suites.SPADES, Card.ranks.king));
        deck.sanity_check();

        deck.insert_at_index(new Card(Card.suites.SPADES, Card.ranks.king), 5);
        deck.sanity_check();

        deck.remove_from_index(5);
        deck.sanity_check();

        deck.swap(5,6);
        deck.sanity_check(); */

        // shuffle the deck (random order)
        deck.shuffle(512);
        if (lab6.DEBUG) deck.print();
        deck.sanity_check(); // because we can all use one

        // cards for player 1 (hand)
        LinkedList player1 = new LinkedList();
        // cards for player 2 (hand)
        LinkedList computer = new LinkedList();

        for (int i = 0; i < num_cards_dealt; i++) {
            // player removes a card from the deck and adds to their hand
            player1.add_at_tail(deck.remove_from_head());
            computer.add_at_tail(deck.remove_from_head());
        }

        // let the games begin!
        play_blind_mans_bluff(player1, computer, deck);
    }
}
