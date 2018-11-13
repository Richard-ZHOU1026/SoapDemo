package com.wane.networkdemo.utils;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * RMI

 使用java的程序员，对于RMI（RemoteMethod Invoke，远程方法调用）一定不陌生，在java中，为了在分布式应用开发时，能够方便调用远程对象，java提供了RMI的API。在 RMI 中,远程对象按照好象它是本地行事，客户机应用程序会直接调用远程对象存根上的方法，因此，调用起来就如本地对象一样方便。RMI中封装了对象和请求的网 络传送，使得异地的对象服务直接可用。

 但RMI的使用必须是在能够识别java代码的环境下使用，即必须有JVM的支持。因此，他只适合在java程序间的对象通信
 *
 *
 * 截至目前来讲 Web Service 中最常用的就是 RPC (Remote Procedure Call，远程过程访问，更普遍地被称为 XML-RPC)
 * 和 SOAP (Simple Object Access Protocol，简单对象访问协议)。
 *
 * XML-RPC：一个远程过程调用（remote procedure call，RPC)的分布式计算协议，通过XML将调用函数封装，并使用HTTP协议作为传送机制。
 * SOAP是以HTTP的post方式做为通信协议+RPC(远程过程)调用远程过程的方式+XML作为数据格式
 *
 *
 * WSDL(Web Services Description Language)是描述web服务的，是描述怎样访问web服务的。WSDL是用来描述SOAP的，换句话说，WSDL 文件告诉你调用 SOAP 所需要知道的一切
 *
 *
 */


public class KsoapServiceUtils {
    //SOAP_ACTION
    private static String City_ACTION="http://WebXml.com.cn/getSupportCityDataset";
    private static String Weather_ACTION="http://WebXml.com.cn/getWeatherbyCityName";
    private static String PLACE_ACTION = "http://WebXml.com.cn/getMobileCodeInfo";
    //命名空间
    private static String NAMESPACE="http://WebXml.com.cn/";
    //方法
    private static String City_NAME ="getSupportCityDataset";
    private static String WEATHER_NAME = "getWeatherbyCityName";
    private static String PLACE_NAME = "getMobileCodeInfo";
    //URL
    private static String URL="http://ws.webxml.com.cn/WebServices/WeatherWS.asmx";
    private static String WEATHER_URL = "http://www.webxml.com.cn/webservices/weatherwebservice.asmx";
    private static String PLACE_URL = "http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx";

    private SoapObject soapObject;
    private SoapSerializationEnvelope envelope;
    private HttpTransportSE transportSE;
    private static KsoapServiceUtils ksoapServiceUtils;

    public static KsoapServiceUtils getInstance() {
        if(ksoapServiceUtils == null) {
            ksoapServiceUtils = new KsoapServiceUtils();
            return ksoapServiceUtils;
        }else {
            return ksoapServiceUtils;
        }

    }

    /**
     * 根据城市获得支持的城市/地区名称和与之对应的ID
     * 返回数据为Dataset
     */

    public  List<String> getInformation(String city) {
        /**
         *
         * 1.先设置SoapObject(命名空间，方法名);
         2.设置协议，使用soap1.1协议创建SoapSerializationEnvelope对象
         3.记得设置bodyout属性
         4.创建HttpTransportSE传输对象
         5.调用webservice：ht.call(命名空间+方法名,SoapSerializationEnvelope);
         6.获取服务器响应返回的SOAP消息
         */

        soapObject = new SoapObject(NAMESPACE, City_NAME);
        soapObject.addProperty("theRegionCode",city);

        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);

        //同样的功能
        //envelope.bodyOut = soapObject;
        envelope.setOutputSoapObject(soapObject);


        // 设置与.NET提供的webservice保持较好的兼容性
        envelope.dotNet = true;


        try {

            transportSE = new HttpTransportSE(URL);
            transportSE.call(City_ACTION, envelope);

            if(envelope.getResponse() !=null) {
                SoapObject response = (SoapObject) envelope.bodyIn;
                //为了拿到整个标签
                SoapObject soapChild = (SoapObject) response.getProperty(0);
                //为了获取第二个子节点
                SoapObject soapChild1 = (SoapObject) soapChild.getProperty(1);
                //获取Region 节点
                SoapObject soapChild2 = (SoapObject) soapChild1.getProperty(0);
                String CityID = null;
                String CityName = null;
                List<String> list = new ArrayList<>();

                Log.e("","");
                for(int i = 0 ; i<soapChild2.getPropertyCount() ; i++ ) {
                    //遍历所有的city节点
                    SoapObject soapChild3 = (SoapObject) soapChild2.getProperty(i);
                    CityID =  soapChild3.getProperty(0).toString();
                    CityName =  soapChild3.getProperty(1).toString();
                    list.add(CityID+CityName);
                }

                return list;
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  根据城市获得天气
     *  返回一维数组
     *  待研究
     */

    public  List<String> getWeather(String city) {
        /**
         *
         * 1.先设置SoapObject(命名空间，方法名);
         2.设置协议，使用soap1.1协议创建SoapSerializationEnvelope对象
         3.创建HttpTransportSE传输对象
         4.记得设置bodyout属性
         5.调用webservice：ht.call(命名空间+方法名,SoapSerializationEnvelope);
         6.通过SoapSerializationEnvelope获取服务器响应返回的SOAP消息
         7.通过bodyIn方法拿到数据SoapObject,再通过getProperty获取数据
         8.遍历得到的getProperty的数据
         */
        soapObject = new SoapObject(NAMESPACE, WEATHER_NAME);
        //若有参数需要传入使用addProperty
        soapObject.addProperty("theCityName",city);
        //soapObject.addPropertyIfValue("theCityName",city);
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);



        //同样的功能
        envelope.bodyOut = soapObject;
        envelope.setOutputSoapObject(soapObject);


        // 设置与.NET提供的webservice保持较好的兼容性
        envelope.dotNet = true;



        try {

            transportSE = new HttpTransportSE(WEATHER_URL);
            transportSE.call(Weather_ACTION,envelope);
            Log.e("返回数据","xx"+envelope.getResponse());

            if(envelope.getResponse()!=null) {
                SoapObject response = (SoapObject) envelope.bodyIn;
                SoapObject soapChild = (SoapObject) response.getProperty(0);
                List<String> list = new ArrayList<>();
                Log.e("xxxxxx","xxx"+soapChild.getPropertyCount());
                String result = null;
                for(int i = 0 ; i<soapChild.getPropertyCount() ; i++ ) {

                    result = soapChild.getProperty(i).toString();
                    list.add(result);
                }
                Log.e("xxx",""+response.getProperty(0).toString()+"==="+list.size());
                return list;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }


        return null;
    }

    //根据电话号码获得归属地
    public  String getPlace(String phone) {
        /**
         *
         * 1.先设置SoapObject(命名空间，方法名);
         2.设置协议，使用soap1.1协议创建SoapSerializationEnvelope对象
         3.创建HttpTransportSE传输对象
         4.记得设置bodyout属性
         5.调用webservice：ht.call(命名空间+方法名,SoapSerializationEnvelope);
         6.通过SoapSerializationEnvelope获取服务器响应返回的SOAP消息
         7.通过bodyIn方法拿到数据SoapObject,再通过getProperty获取数据
         8.遍历得到的getProperty的数据
         */
        soapObject = new SoapObject(NAMESPACE,PLACE_NAME);
        //若有参数需要传入使用addProperty
        soapObject.addProperty("mobileCode",phone);
        //theUserID = 字符串（商业用户ID） 免费用户为空字符串
        soapObject.addProperty("userID","");

        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);



        //同样的功能
        //envelope.bodyOut = soapObject;
        envelope.setOutputSoapObject(soapObject);


        // 设置与.NET提供的webservice保持较好的兼容性
        envelope.dotNet = true;



        try {
            transportSE = new HttpTransportSE(PLACE_URL);
            transportSE.call(PLACE_ACTION,envelope);

            if(envelope.getResponse()!=null) {
                SoapObject response = (SoapObject) envelope.bodyIn;
                /**
                 *
                    若返回值有复杂多个，则用SoapObject取，用getProperty来获得每个返回值
                    若返回值是单个值，则只能用SoapPrimitive取，此时若用SoapObject强制转换会出异常
                 */
                SoapPrimitive soapChild = (SoapPrimitive) response.getProperty(0);

                String result = soapChild.toString();
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }


        return null;
    }




}
