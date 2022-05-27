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

        for (int i = 1; i < 9; i++) {
            try {

                Document doc = Jsoup.connect("https://zoopassage.ru/articles?PAGEN_1=" + String.valueOf(i)).get();
                Element element = doc.getElementsByClass("page-aricles").get(0);

                Elements imagesUrl = element.getElementsByClass("article__header-link");
                Elements articleTitle = element.getElementsByClass("article__title-link");

                //Log.d("MyTAG",String.valueOf(articleTitle.text()));

                for (Element elementI : articleTitle) {
                    String data = String.valueOf(elementI);

                    int indexStart = data.indexOf("href=\"");
                    int indexEnd = data.indexOf("/\">");
                    String arcticleUrl = "https://zoopassage.ru" + data.substring(indexStart + 6, indexEnd) + "/";

                    articleUrlList.add(arcticleUrl);
                    text.add(elementI.text());
                }


                for (Element elementI : imagesUrl) {
                    String data = String.valueOf(elementI);
                    int indexStart = data.indexOf("data-bgr-webp=");
                    int indexEnd = data.indexOf(" style=");
                    String url = "https://zoopassage.ru/" + data.substring(indexStart + 16, indexEnd - 1);

                    aIList.add(new ArticleIntroduction(articleUrlList.get(count),text.get(count), url));
                    count += 1;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        aIList.remove(0);
        return aIList;
    }

    public static String getArticle(String url){
        String texts = new String();

        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.getElementsByClass("article-detail-content__body");
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
