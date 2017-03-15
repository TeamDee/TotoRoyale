package GameView.Viewports;

import java.util.ArrayList;

/*
    Stores active viewports in order of where they appear on the screen
 */
public class ViewportOrder {

    private static ViewportOrder viewportOrder = new ViewportOrder();
    private static ArrayList<Viewport> stack;

    public ViewportOrder() {
        stack = new ArrayList<Viewport>();
    }

    public static boolean pop() {
        if (stack.isEmpty()) return false;
        stack.remove(stack.size() - 1);
        return true;
    }

    public static Viewport top() {
        if (stack.isEmpty()) {
            System.out.println("is empty");
            return null;
        }
        if (stack.get(stack.size()-1) == null) System.out.println("is null");
        return stack.get(stack.size() - 1);
    }

    public static boolean isEmpty() {
        return stack.isEmpty();
    }

    public static void add(Viewport v) {
        System.out.println("adding viewport");
        if (v == null) System.out.println("VIEWPORT IS NULL");
        stack.add(v);
        System.out.println("Stack size is "+stack.size());
    }

    public static int size() {
        return stack.size();
    }

    public static ViewportOrder getInstance() {
        return viewportOrder;
    }

}
