import javafx.stage.Screen;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {
    private ArrayList<Page> pages = new ArrayList<>();
    private HashMap<String, Integer> nameToIndex = new HashMap<>();

    static private final double SYSTEMHEIGHT = Screen.getPrimary().getBounds().getHeight();
    static private final double SYSTEMWIDTH = Screen.getPrimary().getBounds().getWidth();
    public Game(){
        setUpPages();
        setUpHashMap();
        currentPage = pages.get(0);
        System.out.println(SYSTEMHEIGHT);
    }
    private Page currentPage;

    private void setUpPages(){
//        String[] nameOfRooms = {"Admin.jpg", "Cafeteria.jpeg", "Cafeteria_Admin_Storage_Hallway.jpeg", "Cafeteria_UpperEngine_Medbay_Hallway.jpeg", "Communications.jpg", "Electrical.jpg",
//                "LowerEngine.jpg", "LowerEngine_Electrical_Storage_Hallway.jpeg", "Medbay.jpg", "Navigation.jpg", "O2.jpg", "O2_Navigation_Shields_Hallway.jpeg", "Reactor.jpg", "Security.jpg",
//                "Security_Reactor_UpperLowerEngine_Hallway.jpg", "Shields.jpg", "Storage.jpg", "Storage_Shield_Communications_Hallway.jpeg", "UpperEngine.jpg", "Weapons.jpg"};

        pages.add(new Page("Cafeteria.jpeg"));
//        pages.add(new Page("Admin.jpg", "NULL", "NULL", "NULL","Cafeteria_Admin_Storage_Hallway.jpeg"));
//        pages.add(new Page("Cafeteria_Admin_Storage_Hallway.jpeg", "Cafeteria.jpeg", "Storage.jpg", "Admin.jpg", "NULL"));
//        pages.add(new Page("Cafeteria_UpperEngine_Medbay_Hallway.jpeg", "NULL", "Medbay.jpg", "Cafeteria.jpeg", "UpperEngine.jpg"));
//        pages.add(new Page("Communications.jpg", "Storage_Shield_Communications_Hallway.jpeg", "NULL", "NULL", "NULL"));
//        pages.add(new Page("Electrical.jpg", "NULL", "LowerEngine_Electrical_Storage_Hallway.jpeg", "NULL", "NULL"));
//        pages.add(new Page("LowerEngine.jpg", "Security_Reactor_UpperLowerEngine_Hallway.jpg", "NULL", "LowerEngine_Electrical_Storage_Hallway.jpeg", "NULL"));
//        pages.add(new Page("LowerEngine_Electrical_Storage_Hallway.jpeg", "Electrical.jpg", "NULL", "Storage.jpg", "LowerEngine.jpg"));
//        pages.add(new Page("Medbay.jpg", "Cafeteria_UpperEngine_Medbay_Hallway.jpeg", "NULL", "NULL", "NULL"));
//        pages.add(new Page("Navigation.jpg", "NULL", "NULL", "NULL", "O2_Navigation_Shields_Hallway.jpeg"));
//        pages.add(new Page("O2.jpg", "NULL", "NULL", "O2_Navigation_Shields_Hallway.jpeg", "NULL"));
//        pages.add(new Page("O2_Navigation_Shields_Hallway.jpeg", "Weapons.jpg", "Shields.jpg", "Navigation.jpg", "O2.jpg"));
//        pages.add(new Page("Reactor.jpg", "NULL", "NULL", "Security_Reactor_UpperLowerEngine_Hallway.jpg", "NULL"));
//        pages.add(new Page("Security.jpg", "NULL", "NULL", "NULL", "Security_Reactor_UpperLowerEngine_Hallway.jpg"));
//        pages.add(new Page("Security_Reactor_UpperLowerEngine_Hallway.jpg", "UpperEngine.jpg", "LowerEngine.jpg", "Security.jpg", "Reactor.jpg"));
//        pages.add(new Page("Shields.jpg", "O2_Navigation_Shields_Hallway.jpeg", "NULL", "NULL", "Storage_Shield_Communications_Hallway.jpeg"));
//        pages.add(new Page("Storage.jpg", "Cafeteria_Admin_Storage_Hallway.jpeg", "NULL", "Storage_Shield_Communications_Hallway.jpeg", "LowerEngine_Electrical_Storage_Hallway.jpeg"));
//        pages.add(new Page("Storage_Shield_Communications_Hallway.jpeg", "NULL", "Communications.jpg", "Shields.jpg", "Storage.jpg"));
//        pages.add(new Page("UpperEngine.jpg", "NULL", "Security_Reactor_UpperLowerEngine_Hallway.jpg", "Cafeteria_UpperEngine_Medbay_Hallway.jpeg", "NULL"));
//        pages.add(new Page("Weapons.jpg", "NULL", "O2_Navigation_Shields_Hallway.jpeg", "NULL", "Cafeteria.jpeg"));

    }

    private void setUpHashMap(){
        nameToIndex.put("Cafeteria.jpeg", 0);
//        nameToIndex.put("Admin.jpg", 1);
//        nameToIndex.put("Cafeteria_Admin_Storage_Hallway.jpeg", 2);
//        nameToIndex.put("Cafeteria_UpperEngine_Medbay_Hallway.jpeg", 3);
//        nameToIndex.put("Communications.jpg", 4);
//        nameToIndex.put("Electrical.jpg", 5);
//        nameToIndex.put("LowerEngine.jpg", 6);
//        nameToIndex.put("LowerEngine_Electrical_Storage_Hallway.jpeg", 7);
//        nameToIndex.put("Medbay.jpg", 8);
//        nameToIndex.put("Navigation.jpg", 9);
//        nameToIndex.put("O2.jpg", 10);
//        nameToIndex.put("O2_Navigation_Shields_Hallway.jpeg", 11);
//        nameToIndex.put("Reactor.jpg", 12);
//        nameToIndex.put("Security.jpg", 13);
//        nameToIndex.put("Security_Reactor_UpperLowerEngine_Hallway.jpg", 14);
//        nameToIndex.put("Shields.jpg", 15);
//        nameToIndex.put("Storage.jpg", 16);
//        nameToIndex.put("Storage_Shield_Communications_Hallway.jpeg", 17);
//        nameToIndex.put("UpperEngine.jpg", 18);
//        nameToIndex.put("Weapons.jpg", 19);

    }

//    public boolean runPage(ImageView character, ImageView room){
//        if(character.getTranslateY() > (SYSTEMHEIGHT / 2.4827586)){
//            if(currentPage.getPageBelow() != "NULL"){
//                currentPage = pages.get(nameToIndex.get(currentPage.getPageBelow()));
//                room.setFitHeight(Screen.getPrimary().getBounds().getHeight());
//                room.setFitWidth(Screen.getPrimary().getBounds().getWidth());
//                character.setTranslateY(SYSTEMHEIGHT / -2.5714285);
//                return true;
//            }
//        }
//        if (character.getTranslateY() < (SYSTEMHEIGHT / -2.4827586)) {
//            if(currentPage.getPageAbove() != "NULL"){
//                currentPage = pages.get(nameToIndex.get(currentPage.getPageAbove()));
//                room.setFitHeight(Screen.getPrimary().getBounds().getHeight());
//                room.setFitWidth(Screen.getPrimary().getBounds().getWidth());
//                character.setTranslateY(SYSTEMHEIGHT / 2.571428);
//                return true;
//            }
//        }
//        else if(character.getTranslateX() < (SYSTEMWIDTH / -2.1603376)){
//            if(currentPage.getPageLeft() != "NULL"){
//                currentPage = pages.get(nameToIndex.get(currentPage.getPageLeft()));
//                room.setFitHeight(Screen.getPrimary().getBounds().getHeight());
//                room.setFitWidth(Screen.getPrimary().getBounds().getWidth());
//                character.setTranslateX(SYSTEMWIDTH / 2.169492);
//                return true;
//            }
//        }
//        else if(character.getTranslateX() > (SYSTEMWIDTH / 2.1603376)){
//            if(currentPage.getPageRight() != "NULL"){
//                currentPage = pages.get(nameToIndex.get(currentPage.getPageRight()));
//                room.setFitHeight(Screen.getPrimary().getBounds().getHeight());
//                room.setFitWidth(Screen.getPrimary().getBounds().getWidth());
//                character.setTranslateX(SYSTEMWIDTH / -2.169492);
//                return true;
//            }
//        }
//        return false;
//    }

    public ArrayList<Page> getPages(){
        return pages;
    }

    public Page getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Page currentPage) {
        this.currentPage = currentPage;
    }
}
