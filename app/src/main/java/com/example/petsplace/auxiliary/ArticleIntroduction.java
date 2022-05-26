package com.example.petsplace.auxiliary;

public class ArticleIntroduction {
    private String introductionText;
    private String introductionImage;
    private String articleUrl;

    public ArticleIntroduction(String articleUrl,String introductionText, String introductionImage) {
        this.introductionText = introductionText;
        this.introductionImage = introductionImage;
        this.articleUrl = articleUrl;
    }

    public String getIntroductionText() {
        return introductionText;
    }

    public void setIntroductionText(String introductionText) {
        this.introductionText = introductionText;
    }

    public String getIntroductionImage() {
        return introductionImage;
    }

    public void setIntroductionImage(String introductionImage) {
        this.introductionImage = introductionImage;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }
}
