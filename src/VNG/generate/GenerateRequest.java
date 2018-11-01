/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VNG.generate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import org.json.simple.JSONObject;

/**
 *
 * @author root
 */
public abstract class GenerateRequest {
    private static final long serialVersionUID = 0L;

    private static int _lenOfUserIDString = 10;
    private static int lenOfGameIDString = 10;

    private final static String bufferToRandom = "1234567890abcdefghijklmnopqrstuvxyzwABCDEFGHIJKLMNOPQRSTUVXYZW";
    private static final int lenOfBuffer = 26;
    private final static ArrayList<String> listGameID= new ArrayList<>(Arrays.asList("epoap1mj5d",
                                                                                    "4bh9eh51hb",
                                                                                    "bb2kfbkeac",
                                                                                    "9p399iojef",
                                                                                    "fmf74pp2ed",
                                                                                    "hbpa78pddd",
                                                                                    "5gi6a17a9j",
                                                                                    "olfok5ijh8",
                                                                                    "50eb1o869c",
                                                                                    "e133cagn3a",
                                                                                    "h0j787phcb",
                                                                                    "g80o14ej9k",
                                                                                    "d1pmnknm3d",
                                                                                    "b0b0gjn0j1",
                                                                                    "aep888epef",
                                                                                    "j4pl3fn8b7",
                                                                                    "bbn980l9o0",
                                                                                    "ed3oh5o88g",
                                                                                    "828k5gljhf",
                                                                                    "34h651le9j",
                                                                                    "75b160468o",
                                                                                    "nea88hp5bp",
                                                                                    "bjk33hg49j",
                                                                                    "8gjikhn5dc",
                                                                                    "e17aacj8hk",
                                                                                    "2m1e5l3ef7",
                                                                                    "n27c2dmm12",
                                                                                    "cgp3c815md",
                                                                                    "lomlfe56bo",
                                                                                    "a6f1gi9kk8",
                                                                                    "75d5pf2p7l",
                                                                                    "mbga268o46",
                                                                                    "ecoalk58fj",
                                                                                    "342pd676dk",
                                                                                    "c9f819nm71",
                                                                                    "1n187kkpdl",
                                                                                    "apd3h4m4g9",
                                                                                    "1ih4gak7nb",
                                                                                    "7gj4baohh9",
                                                                                    "j5khndak3o",
                                                                                    "pfk3k7iocn",
                                                                                    "2k2mal53f1",
                                                                                    "l436nakk29",
                                                                                    "3ndfcgeig6",
                                                                                    "33b1oahk92",
                                                                                    "khngppeo2a",
                                                                                    "fbddbo6853",
                                                                                    "kf7a6hp5fn",
                                                                                    "84glkkigfh",
                                                                                    "p9ncj0komg",
                                                                                    "m2km44m763",
                                                                                    "aaih8bkfl0",
                                                                                    "6fipk3af23",
                                                                                    "50an9l0pgk",
                                                                                    "ofd2g8efno",
                                                                                    "d4o9mckm42",
                                                                                    "3gnkfg4hbb",
                                                                                    "fd59e8ibp4",
                                                                                    "kbefiogplc",
                                                                                    "e043g69gf7",
                                                                                    "92oj59dkk4",
                                                                                    "a414p33613",
                                                                                    "md0edm8ego",
                                                                                    "ob4gk2245h",
                                                                                    "gmf6o3gphl",
                                                                                    "887jnk6hgc",
                                                                                    "7g8i1ncbh0",
                                                                                    "b3aeebh9g2",
                                                                                    "l4b7dg8h0p",
                                                                                    "oahk1obohb",
                                                                                    "p4mia5834e",
                                                                                    "82pa98lc4o",
                                                                                    "lihi9b4bdl",
                                                                                    "eio8ofbo6g",
                                                                                    "67li66283a",
                                                                                    "j5ocobo908",
                                                                                    "o650j1pn3e",
                                                                                    "23oleeell8",
                                                                                    "ano9aah81i",
                                                                                    "lhh5jegp93",
                                                                                    "2lb23bpc5h",
                                                                                    "e3obe67a4a",
                                                                                    "25a4bgndme",
                                                                                    "ob66fmipk4",
                                                                                    "e94bighokd",
                                                                                    "7igh93h7ad",
                                                                                    "gj3k78j33b",
                                                                                    "njah6nbgml",
                                                                                    "c6njc9aokd",
                                                                                    "k4h60l2e1i",
                                                                                    "ml680oj0nk",
                                                                                    "3k87elg1ef",
                                                                                    "opi066ga9n",
                                                                                    "935174i2c1",
                                                                                    "j8o6dpn23l",
                                                                                    "9f74gf4mmg",
                                                                                    "46fjkhcf42",
                                                                                    "ncp27nde4j",
                                                                                    "7aggnim5b3",
                                                                                    "lljbl6a06d"));
    private static final int sizeOfListGameID = 100;
    
    public static void setLenOfUserIDString(int len){
        _lenOfUserIDString =  len;
    }
    
    public static Game generateGame(){
        return Game.getRandomGame();
    }
    public static String generateGameID(){
        Random random = new Random();
        int index = random.nextInt(GenerateRequest.sizeOfListGameID);
        return GenerateRequest.listGameID.get(index);
    }
    public static String generateUserID() {
        Random random = new Random();
        StringBuilder userID = new StringBuilder(_lenOfUserIDString);
        for(int i=0;i<_lenOfUserIDString;i++){
            int index = random.nextInt(lenOfBuffer);
            userID.append(bufferToRandom.charAt(index));
        }
        return userID.toString();
    }
    
    protected static enum Game {
        MUONLINE("Mu Online Web","WEBGAME"),
        VOTHANPK("Võ Thần PK","WEBGAME"),
        PHUCLONG("Phục Long","WEBGAME"),
        NGOALONG("Ngọa Long","WEBGAME"),
        THOILOAN("Thời Loạn","WEBGAME"),
        GUNNy("Gunny","WEBGAME"),
        VOLAMTRUYENKIMOBILE("Võ Lâm Truyền Kì Mobile","MOBILE"),
        GUNNYMOBILE("Gunny Mobile","MOBILE"),
        NGOALONGMOBILE("Ngọa Long Mobile","MOBILE"),
        AUMOBILE("AU Mobile","MOBILE"),
        CUUAMMOBILE("Cửu Âm Mobile","MOBILE"),
        GUNPOW("GunPow","MOBILE"),
        KIEMTHE("Kiếm Thế","DESKTOP"),
        VOLAMTRUYENKI("Võ Lâm Truyền Kì","DESKTOP"),
        VOLAMTRUYENKIMIENPHI("Võ Lâm Truyền Kì Miễn Phí","DESKTOP");
        
        private String _name;
        private String _gameID;
        
        Game(String name,String typeGame){
            _name = name;
            _gameID = typeGame;
        }
        
        public String getName(){
            return this._name;
        }
        
        public String getTypeGame(){
            return this._gameID;
        }
        
        public static Game getRandomGame() {
            Random random = new Random();
            int range = Game.values().length;
            int indexRandom = random.nextInt(range);
            return Game.values()[indexRandom];
        }
    }
    public static void main(String[] args) {
        for(;;){
            System.out.println(GenerateRequest.generateGameID());
        }
    }
}
