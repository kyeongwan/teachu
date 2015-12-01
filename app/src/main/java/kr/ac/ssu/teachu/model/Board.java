package kr.ac.ssu.teachu.model;

/**
 * Created by lk on 15. 11. 29..
 */
public class Board {
    public String title;
    public String imageUrl;
    public String context;


    public Board(String title, String context, String imgurl){
        this.title = title;
        this.context = context;
        this.imageUrl = imgurl;
    }
}
