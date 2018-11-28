package com.csp.sample.adapter.dto;

public class TopDto {

    /**
     * chineseName : 文豪野犬 迷途犬怪奇谭
     * gameGrade : A
     * gameIcon : http://10.18.97.202/3e7808f6-6d7d-442a-9ddc-547677e75361.png
     * gameId : f04c6a51-bc17-4e91-8140-e062bf0b2267
     * gamePlatform : 1
     * gameRecommendText : asssssss
     * id : 5
     * recommendImage : http://10.18.97.202/3e7808f6-6d7d-442a-9ddc-547677e75361.png
     * status : 0
     * type : 0
     */

    private String chineseName;
    private String gameGrade;
    private String gameIcon;
    private String gameId;
    private String gamePlatform;
    private String gameRecommendText;
    private int id;
    private String recommendImage;
    private String status;
    private String type;
    private String gameService;

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getGameGrade() {
        return gameGrade;
    }

    public void setGameGrade(String gameGrade) {
        this.gameGrade = gameGrade;
    }

    public String getGameRecommendText() {
        return gameRecommendText;
    }

    public void setGameRecommendText(String gameRecommendText) {
        this.gameRecommendText = gameRecommendText;
    }

    public String getGameService() {
        return gameService;
    }

    public void setGameService(String gameService) {
        this.gameService = gameService;
    }
}