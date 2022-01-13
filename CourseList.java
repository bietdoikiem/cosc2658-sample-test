import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class for the solution of Learning Sequence problem
 */
public class CourseList {
  /* * MAIN * */
  public static void main(String[] args) {
    System.out.println("=== Learning Sequence Problem ===");
    Course c1 = new Course("Programming 1", "C123");
    Course c2 = new Course("Web Programming", "C456");
    Course c3 = new Course("Data Structures", "C789");
    Course c4 = new Course("Database Application", "C000");
    CourseList list = new CourseList();
    // 1. Add a course
    System.out.println("+Problem 1: Add a course");
    list.addCourse(c1);
    list.addCourse(c2);
    list.addCourse(c3);
    list.addCourse(c4);
    System.out.println("Course list after insertion: " + list.courses);
    // 2. Add a prerequisite
    System.out.println("+Problem 2: Add a prerequisite");
    list.addPrerequisite(c2, c1); // make Programming 1 a prerequisite of Web Programming
    list.addPrerequisite(c3, c1); // make Programming 1 a prerequisite of Data Structures
    list.addPrerequisite(c4, c2); // make Web Programming a prerequisite of Database Application
    // 3. Take first
    System.out.println("+Problem 3: Course that can be taken first");
    System.out.println(list.takeFirst(c1)); // true
    System.out.println(list.takeFirst(c3)); // false
    // 4. Courses taken
    System.out.println("+Problem 4: Courses must be taken in appropriate order");
    System.out.println(list.coursesTaken()); //
    // return "Programming 1, Web Programming, Data Structures, Database
    // Application"
  }

  /// Properties ///
  ArrayList<Course> courses;

  /**
   * Initialize a new list of courses (Constructor method)
   */
  public CourseList() {
    this.courses = new ArrayList<>();
  }

  /// Methods ///

  /**
   * Add a new course to the list
   * 
   * @param c Course
   */
  public void addCourse(Course c) {
    this.courses.add(c);
  }

  /**
   * 
   * @param c
   * @param pre
   */
  public void addPrerequisite(Course c, Course pre) {
    // Init prerequisites if null
    if (c.prerequisites == null) {
      c.prerequisites = new ArrayList<Course>();
    }
    c.prerequisites.add(pre);
  }

  /**
   * Determine if a course can be taken first (course without prerequisites)
   * 
   * @param c Course
   * @return True if it can be taken first || False if not
   */
  public boolean takeFirst(Course c) {
    return c.prerequisites == null;
  }

  /**
   * Display courses taken in neat order (pre-requisites are considered)
   * 
   * @return List of courses in string, separated by commas
   */
  public String coursesTaken() {
    int N = this.courses.size();
    boolean[] V = new boolean[N];
    Course[] orderings = new Course[N];
    int i = 0;
    for (int at = 0; at < N; at++) {
      if (!V[at]) {
        ArrayList<Course> visitedNodes = new ArrayList<Course>();
        dfs(at, V, visitedNodes, this.courses);
        for (Course c : visitedNodes) {
          orderings[i] = c;
          i++;
        }
      }
    }
    return Arrays.toString(orderings).replaceAll("(\\[|\\])", "");
  }

  /**
   * Depth-first search for all pre-requisites courses of the
   * 
   * @param at
   * @param V
   * @param visitedNodes
   * @param courses
   */
  public void dfs(int at, boolean[] V, ArrayList<Course> visitedNodes, ArrayList<Course> courses) {
    // Mark current chosen node as visited
    V[at] = true;
    // If course do not have any prerequisites
    if (courses.get(at).prerequisites == null) {
      visitedNodes.add(courses.get(at));
      return;
    }
    // Perform Depth-First Search
    for (Course p : courses.get(at).prerequisites) {
      int edgeIdx = courses.indexOf(p);
      if (!V[edgeIdx]) {
        dfs(edgeIdx, V, visitedNodes, courses);
      }
    }
    // System.out.println(courses.get(at));
    visitedNodes.add(courses.get(at));
    return;
  }

}

/// SUB-CLASSES ///

/**
 * Class for Course
 */
class Course {
  String name;
  String code;
  ArrayList<Course> prerequisites;

  public Course(String name, String code) {
    this.name = name;
    this.code = code;
  }

  @Override
  public String toString() {
    return String.format("%s", this.name, this.code);
  }
}
