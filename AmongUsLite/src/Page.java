import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;

public class Page {
    private String nameOfPage;
    private String pageAbove;
    private String pageBelow;
    private String pageLeft;
    private String pageRight;


    public Page(String nameOfPage){
        this.nameOfPage = nameOfPage;
//        this.pageAbove = pageAbove;
//        this.pageBelow = pageBelow;
//        this.pageRight = pageRight;
//        this.pageLeft = pageLeft;
    }

//    public String runPage(ImageView display){
//        //System.out.println(SYSTEMHEIGHT);
//        System.out.println(display.getTranslateY());
//        if(display.getTranslateY() > SYSTEMHEIGHT / 2.4827586){
//            display.setTranslateY(-480);
//            return pageBelow;
//        }
//        return "NULL";
//    }


    public String getNameOfPage() {
        return nameOfPage;
    }

    public String getPageAbove() {
        return pageAbove;
    }

    public String getPageBelow() {
        return pageBelow;
    }

    public String getPageLeft() {
        return pageLeft;
    }

    public String getPageRight() {
        return pageRight;
    }
}
