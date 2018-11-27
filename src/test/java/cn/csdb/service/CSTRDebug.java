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

public class CSTRDebug implements Runnable {
    public static void main(String[] args) {
        CSTRDebug cstrTest = new CSTRDebug();
        Thread t1 = new Thread(cstrTest);
        t1.run();
    }

    @Override
    public void run() {
        try {
            //创建URL对象
            long start = System.currentTimeMillis();
            URL url = new URL("http://14.23.68.86:9002/CSTR:00000.11.01.616DCA959B");
            //返回一个URLConnection对象，它表示到URL所引用的远程对象的连接
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //在这里设置一些属性，详细见UrlConnection文档，HttpURLConnection是UrlConnection的子类
            //设置连接超时为5秒
            //httpURLConnection.setConnectTimeout(100000);
            //设定请求方式(默认为get)
            httpURLConnection.setRequestMethod("GET");
            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true, 默认情况下是false;
            httpURLConnection.setDoOutput(true);
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            httpURLConnection.setDoInput(true);
            // Post 请求不能使用缓存
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-type","application/x-javascript->json");
            // 连接，从上述url.openConnection()至此的配置必须要在connect之前完成，
            httpURLConnection.connect();

            //这边设置请内容
            //getOutputStream()里默认就有connect（）了，可以不用写上面的连接
            //接下来我们设置post的请求参数，可以是JSON数据，也可以是普通的数据类型
            //OutputStream outputStream = httpURLConnection.getOutputStream();
//	             DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            //String content = "username=bigdata&password=bigdata&content="+message;
//

            //String content = "";
//     dataOutputStream.writeBytes(content);
//	             dataOutputStream.flush();
//	             dataOutputStream.close();
            //outputStream.write(content.getBytes());
           // outputStream.flush();
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
