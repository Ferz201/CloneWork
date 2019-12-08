package com.example.wsbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class CurrencyActivity extends AppCompatActivity{

    NodeList nodeList;
    ArrayList<Product> products = new ArrayList<Product>();
    BoxAdapter boxAdapter;
    ListView lvMain;
    Context ctx;

    String URL = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=13/11/2019";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        ctx = this;
        lvMain = findViewById(R.id.lvMain);
        DownloadXML downloadXML = new DownloadXML();
        downloadXML.execute(URL);
    }


    private class DownloadXML extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... Url) {
            try{
                URL url = new URL(Url[0]);
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document doc = documentBuilder.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                nodeList = doc.getElementsByTagName("Valute");
            } catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            for(int temp = 0; temp < nodeList.getLength(); temp++){
                Node nNode = nodeList.item(temp);
                if(nNode.getNodeType() == Node.ELEMENT_NODE){
                    Element eElement = (Element)nNode;
                    String name = getNode("Name", eElement);
                    String charCode = getNode("CharCode", eElement);
                    String value = getNode("Value", eElement);

                    products.add(new Product(charCode, name, value));
                }
            }
            boxAdapter = new BoxAdapter(ctx, products);
            lvMain.setAdapter(boxAdapter);


            //super.onPostExecute(aVoid);
        }
    }

    private static String getNode(String sTag, Element eElement){
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
        Node nValue = (Node)nlList.item(0);
        return nValue.getNodeValue();
    }
}
