/**
 * Solution for the COVID Variants Problem in Sample Test 2
 */
public class VariantCollection {

  /* * MAIN * */
  public static void main(String[] args) {
    System.out.println("=== COVID Variant Problem ===");
    VariantCollection collection = new VariantCollection();
    CovidVariant c1 = new CovidVariant("Alpha", "210201A");
    CovidVariant c2 = new CovidVariant("Delta", "210311D");
    CovidVariant c3 = new CovidVariant("Beta", "210311A");
    CovidVariant c4 = new CovidVariant("Omicron", "211120D");
    // 1. Insert to collection
    System.out.println("+Problem 1: Insert a variation");
    collection.addVariant(c1);
    collection.addVariant(c2);
    collection.addVariant(c3);
    collection.addVariant(c4);
    System.out.print("Variant collection after insertions: ");
    collection.display();
    // 2. Search collection
    System.out.println("+Problem 2: Search for a variation based on its code");
    System.out.println("Search result for code 210311A: " + collection.search("210311A"));
    // 3. Previous
    System.out.println("+Problem 3: Previous variant");
    System.out.println("Previously found variant before 211120D is: " + collection.previous("211120D"));
  }

  /* * Property Declarations * */
  VariantNode root;

  /**
   * Init VariantCollection tree with empty root
   */
  public VariantCollection() {
    this.root = null;
  }

  /**
   * Add a new COVID variant to the collection tree
   * 
   * @param v COVID variant
   */
  public void addVariant(CovidVariant v) {
    this.root = recursiveAdd(this.root, v);
    // System.out.println(v + " inserted!"); // DEBUG
  }

  /**
   * Recursively add to the tree of VariantCollection
   * 
   * @param root Node root reference
   * @param v    Variant to be added
   * @return Reference to added variant
   */
  private VariantNode recursiveAdd(VariantNode root, CovidVariant v) {
    if (root == null) {
      return new VariantNode(v);
    }
    if (v.compareFoundTime(root.data) == -1) {
      root.left = recursiveAdd(root.left, v);
    } else if (v.compareFoundTime(root.data) == 1) {
      root.right = recursiveAdd(root.right, v);
    }
    // Return (unchanged) node
    return root;
  }

  /**
   * Search for a COVID Variant based on its code
   * 
   * @param code Variant's code
   * @return Found COVID Variant || Null if not found
   */
  public CovidVariant search(String code) {
    return searchRecursive(this.root, code).data;
  }

  /**
   * Recursively search for a COVID Variant
   * 
   * @param root Node root reference
   * @param code Code to be searched for
   * @return
   */
  private VariantNode searchRecursive(VariantNode root, String code) {
    // Base case
    if (root == null || compareCode(code, root.data.code) == 0) {
      return root;
    }

    if (compareCode(code, root.data.code) == -1) {
      return searchRecursive(root.left, code);
    }
    return searchRecursive(root.right, code);
  }

  /**
   * Helper function to compare codes between two variants
   * 
   * @param c1 1st COVID Variant
   * @param c2 2nd COVID Variant
   * @return 1 if current variant is newer || -1 if current variant is older || 0
   *         if found time collapsed
   */
  private int compareCode(String c1, String c2) {
    // Parse time from variant's code
    int y1 = Integer.parseInt(c1.substring(0, 2));
    int y2 = Integer.parseInt(c2.substring(0, 2));
    int m1 = Integer.parseInt(c1.substring(2, 4));
    int m2 = Integer.parseInt(c2.substring(2, 4));
    int d1 = Integer.parseInt(c1.substring(4, 6));
    int d2 = Integer.parseInt(c2.substring(4, 6));
    char o1 = c1.charAt(6);
    char o2 = c2.charAt(6);
    /// Compare each time component
    // Check duplicated case
    if (y1 == y2 && m1 == m2 && d1 == d2 && o1 == o2)
      return 0;
    // Compare Year
    if (y1 > y2) {
      return 1;
    }
    // Compare Month
    if (y1 == y2 && m1 > m2) {
      return 1;
    }
    // Compare Day
    if (y1 == y2 && m1 == m2 && d1 > d2) {
      return 1;
    }
    // Compare Order
    if (y1 == y2 && m1 == m2 && d1 == d2 && o1 > o2) {
      return 1;
    }
    return -1;
  }

  /**
   * Search for a previous found COVID Variant based on the given code
   * 
   * @param code Variant code
   * @return Previously found variant of the given code
   */
  public CovidVariant previous(String code) {
    VariantNode parent = null;
    VariantNode sooner = null;
    VariantNode current = this.root;
    VariantNode searchResult = null;
    while (current != null) {
      sooner = current.left;
      if (compareCode(code, current.data.code) == 0) {
        searchResult = sooner != null ? sooner : parent;
        break;
      } else if (compareCode(code, current.data.code) == -1) {
        parent = current;
        current = current.left;
      }
      parent = current;
      current = current.right;
    }
    if (searchResult != null) {
      return searchResult.data;
    }
    return null;
  }

  /**
   * Display from given node (In-Order)
   * 
   * @param node
   */
  public void display() {
    inOrder(this.root);
    System.out.print('\n');
  }

  /**
   * In-order traverse the collection tree
   * 
   * @param root Node root reference
   */
  private void inOrder(VariantNode root) {
    if (root == null) {
      return;
    }
    inOrder(root.left);
    System.out.print(root.data + " ");
    inOrder(root.right);
  }
}

//// SUB-CLASSES ///

/**
 * Node in the Variant Collection (Binary Search Tree)
 */
class VariantNode {
  CovidVariant data;
  VariantNode left, right;

  public VariantNode(CovidVariant data) {
    this.data = data;
  }
}

/**
 * Class for COVID variant
 */
class CovidVariant {
  String name;
  String code; // YYMMDD<Order>

  /**
   * Init COVID Variant with unique name and code
   * 
   * @param name
   * @param code
   */
  public CovidVariant(String name, String code) {
    this.name = name;
    this.code = code;
  }

  /**
   * Compare the date when the variants are found
   * 
   * @param v Compared variant
   * @return 1 if current variant is newer || -1 if current variant is older || 0
   *         if found time collapsed
   */
  public int compareFoundTime(CovidVariant v) {
    // Parse time from variant's code
    int y1 = Integer.parseInt(this.code.substring(0, 2));
    int y2 = Integer.parseInt(v.code.substring(0, 2));
    int m1 = Integer.parseInt(this.code.substring(2, 4));
    int m2 = Integer.parseInt(v.code.substring(2, 4));
    int d1 = Integer.parseInt(this.code.substring(4, 6));
    int d2 = Integer.parseInt(v.code.substring(4, 6));
    char o1 = this.code.charAt(6);
    char o2 = v.code.charAt(6);
    /// Compare each time component
    // Check duplicated case
    if (y1 == y2 && m1 == m2 && d1 == d2 && o1 == o2)
      return 0;
    // Compare Year
    if (y1 > y2) {
      return 1;
    }
    // Compare Month
    if (y1 == y2 && m1 > m2) {
      return 1;
    }
    // Compare Day
    if (y1 == y2 && m1 == m2 && d1 > d2) {
      return 1;
    }
    // Compare Order
    if (y1 == y2 && m1 == m2 && d1 == d2 && o1 > o2) {
      return 1;
    }
    return -1;
  }

  @Override
  public String toString() {
    return String.format("%s (%s)", this.name, this.code);
  }

}