package cn.csdb.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.UUID;

public class CstrTest implements Runnable {
    public static void main(String[] args) {
        CstrTest cstrTest = new CstrTest();
        Thread t1 = new Thread(cstrTest);
        t1.run();
    }

    @Override
    public void run() {
        while(true){
            String context="";
            int size=2;
            for (int i = 1; i <= size; i++) {
                context = context
                        + "{\"action\":\"post\","
                        + "\"metadata\":{"
                        + "\"idType\":\"handle,CSTR\","  //如果有多个标识用","分隔
                        + "\"otherId\":\"doi,pid,handle\","
                        + "\"resTitle\":\"test1\","
                        + "\"upDate\":\"2018-03-30 12:01:30\","
                        + "\"descrp\":\"资源描述\","
                        + "\"keyword\":\"A,B,C\","
                        + "\"accessConst\":\"1\","
                        + "\"onlAdd\":\"URL\","
                        + "\"IdPoC\":{\"orgName\":\"组织名\",\"cntAdd\":\"提交机构\",\"postCode\":\"xxxxxx\",\"cntPhone\":\"138xxxxxxxx\",\"cntEmail\":\"xxx@xxx.com\"},"
                        + "\"ResCat\":{\"catName\":\"科技资源\",\"catCode\":\"11\",\"catStdName\":\"分类标准名称\",\"catStdVer\":\"v1.0\"},"
                        + "\"Creator\":[{\"creatOrgName\":\"创建机构\",\"creatPerInfo\":\"创建者信息\"}],"
                        + "\"pubOrgName\":\"科技资源发布机构\""
                        + "},"
                        //xiajl20180705
                        + "\"structure\":{\"type\":\"A\",\"idInter\":\"\"}"

                        + "}";
                //System.out.println(context);
                if(i<size){
                    context=context+",";
                }
            }

            String message="{\"dataModels\": ["+context+"]}";
            System.out.println(message);
            System.err.println(message.length());
            String response = "";
            try {
                //创建URL对象
                long start = System.currentTimeMillis();
                URL url = new URL("http://14.23.68.86:9001/register");
                //返回一个URLConnection对象，它表示到URL所引用的远程对象的连接
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                //在这里设置一些属性，详细见UrlConnection文档，HttpURLConnection是UrlConnection的子类
                //设置连接超时为5秒
                //httpURLConnection.setConnectTimeout(100000);
                //设定请求方式(默认为get)
                httpURLConnection.setRequestMethod("POST");
                // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
                // http正文内，因此需要设为true, 默认情况下是false;
                httpURLConnection.setDoOutput(true);
                // 设置是否从httpUrlConnection读入，默认情况下是true;
                httpURLConnection.setDoInput(true);
                // Post 请求不能使用缓存
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                // 连接，从上述url.openConnection()至此的配置必须要在connect之前完成，
                httpURLConnection.connect();

                //这边设置请内容
                //getOutputStream()里默认就有connect（）了，可以不用写上面的连接
                //接下来我们设置post的请求参数，可以是JSON数据，也可以是普通的数据类型
                OutputStream outputStream = httpURLConnection.getOutputStream();
//	             DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                String content = "username=bigdata&password=bigdata&content="+message;
//	             dataOutputStream.writeBytes(content);
//	             dataOutputStream.flush();
//	             dataOutputStream.close();
                outputStream.write(content.getBytes());
                outputStream.flush();
                //读取返回的数据
                //返回打开连接读取的输入流，输入流转化为StringBuffer类型，这一套流程要记住，常用
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line = null;
                StringBuffer stringBuffer = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    //转化为UTF-8的编码格式
                    line = new String(line.getBytes("UTF-8"));
                    stringBuffer.append(line);
                }
                bufferedReader.close();
                httpURLConnection.disconnect();
                long end = System.currentTimeMillis();
                System.err.println((end-start)+" ms");
                System.out.println(stringBuffer);
                Thread.sleep(1000);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}