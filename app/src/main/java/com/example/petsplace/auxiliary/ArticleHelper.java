package com.example.petsplace.auxiliary;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArticleHelper {
    public static ArrayList<ArticleIntroduction> list = new ArrayList<>();
    public static ArrayList<ArticleIntroduction> getArticleIntroductionList() {
        ArrayList<ArticleIntroduction> aIList = new ArrayList<ArticleIntroduction>();
        ArrayList<String> text = new ArrayList<String>();
        ArrayList<String> articleUrlList = new ArrayList<String>();
        int count = 0;

        for (int i = 1; i < 6; i++) {
            try {

                Document doc = Jsoup.connect("https://zoobazar.by/advice/?PAGEN_2=" + String.valueOf(i)).get();
                Element element = doc.getElementsByClass("news-list").get(0);
                Elements news = doc.getElementsByClass("news-item");

                for (Element newz : news){
                    String imagesUrl = newz.select("picture").first().select("source").first().attr("srcset");
                    imagesUrl = imagesUrl.substring(0, imagesUrl.indexOf(" "));
                    imagesUrl = "https://zoobazar.by" + imagesUrl;
                    Log.d("MyTAG", imagesUrl);
                    String articleUrl = "https://zoobazar.by" + newz.select("a").first().attr("href");
                    Log.d("MyTAG", articleUrl);
                    String Introduction = String.valueOf(newz.getElementsByClass("news-item__title").get(0).select("span").text());
                    Log.d("MyTAG", Introduction);
                    aIList.add(new ArticleIntroduction(articleUrl, Introduction, imagesUrl));
                }

                Elements imagesUrl = element.getElementsByClass("news-item__image-wrapper");
                Elements articleTitle = element.getElementsByClass("read-more");
            } catch (IOException e) {
                Log.d("MyTAG", String.valueOf(e));
            }
        }
        aIList.remove(0);
        return aIList;
    }

    public static String getArticle(String url){
        String texts = new String();

        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.getElementsByClass("news-detail__content");
            for (Element element : elements){
                texts += (String.valueOf(element.text().trim()));
            }


        }
        catch (IOException e){
            e.printStackTrace();
        }

        return texts;
    }
}
