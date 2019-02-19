package com.jidn.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jidn.common.baidu.speech.SpeechApi;
import com.jidn.common.oss.OssUtil;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;

import javax.activation.MimetypesFileTypeMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.io.*;


/**
 * @Copyright © 正经吉
 * @Author: Jidn
 * @Date: 2019/1/14 17:24
 * Description: 文件上传工具类
 */
public class FileUtil {


    public static String[] upload(@RequestParam("file") MultipartFile[] files) {
        String[] pathImg = new String[3];
        try {
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    MultipartFile file = files[i];
                    if(file !=null){
                        // 获取文件名
                        String filename = file.getOriginalFilename();
                        if (filename != null && filename.length() > 0) {
                            String newFileName = UUID.randomUUID() + filename.substring(filename.lastIndexOf("."));
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            //上传流
                            InputStream ossStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                            byteArrayOutputStream.close();

                            OssUtil.upload(ossStream, newFileName,true);
                            pathImg[i] =  newFileName;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pathImg;
    }

    public static String writeBytesToOssImage(InputStream ossStream, String filePath) {
        try {
            //上传流
            OssUtil.upload(ossStream, filePath,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath ;
    }

    public static String writeBytesToOssVideo(byte[] bfile, String filePath) {
        String fileName =  UUID.randomUUID()+".mp3";
        try {
            //上传流
            InputStream ossStream = new ByteArrayInputStream(bfile);
            OssUtil.upload(ossStream, filePath+"/"+fileName,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath+"/"+fileName;
    }

    public static String WeChatVoiceToOss(InputStream inputStream, String filePath) {
        try {
            //上传流
            OssUtil.upload(inputStream, filePath,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }


    public static String WeChatUpload(String filePath,String type) throws Exception {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IOException(WeChatConstants.NO_FIND_FILE);
        }
        String url = GlobalConstants.getProperties("material_url").replace("ACCESS_TOKEN", GlobalConstants.getProperties("access_token")).replace("TYPE",type);
        URL urlObj = new URL(url);
        //连接
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        //设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

        StringBuilder sb = new StringBuilder();
        sb.append("--");
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");


        byte[] head = sb.toString().getBytes("utf-8");
        //获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        //输出表头
        out.write(head);
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");
        out.write(foot);
        out.flush();
        out.close();
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        String result = null;
        try {
            //定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        JSONObject jsonObj = JSONObject.parseObject(result);
        System.out.println(jsonObj);
        String mediaId = jsonObj.getString("media_id");
        return mediaId;
    }

    public static String WeChatUploadPermanent(String filePath,String type) throws Exception {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IOException(WeChatConstants.NO_FIND_FILE);
        }
        String url = GlobalConstants.getProperties("material_permanent_url").replace("ACCESS_TOKEN", GlobalConstants.getProperties("access_token")).replace("TYPE",type);
        URL urlObj = new URL(url);
        //连接
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        //设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

        StringBuilder sb = new StringBuilder();
        sb.append("--");
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"media\";filelength=\""+file.length()+"\";filename=\"" + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");


        byte[] head = sb.toString().getBytes("utf-8");
        //获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        //输出表头
        out.write(head);
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");
        out.write(foot);
        out.flush();
        out.close();
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        String result = null;
        try {
            //定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        JSONObject jsonObj = JSONObject.parseObject(result);
        System.out.println(jsonObj);
        String mediaId = "";
        return mediaId;
    }



    public static String uploadOssVideo(String imgPath,String fileName) throws Exception {
        fileName = fileName.replace(GlobalConstants.getProperties("WRITE_FILE_SYSTEM")+"/","");
        URL url = new URL(imgPath);
        //打开链接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置请求方式为"GET"
        conn.setRequestMethod("GET");
        //超时响应时间为5秒
        conn.setConnectTimeout(5 * 1000);
        InputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            //通过输入流获取图片数据
            inStream = conn.getInputStream();
            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            byte[] data = readInputStream(inStream);
            //这个判断可以删掉，是为了兼容业务图片
            if (imgPath.contains("?")){
                imgPath = imgPath.split("\\?")[0];
            }

            //文件命名随意
            File file = new File(fileName);
            //创建输出流
            outStream = new FileOutputStream(file);
            //写入数据
            outStream.write(data);
            //删除图片
            return file.getAbsolutePath();
        }finally {
        }
    }


    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024 * 4];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }


    public static String downloadMedia(String accessToken,String mediaId) throws Exception  {
        String url = GlobalConstants.getProperties("get_material_url").replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID", mediaId);
        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(100000).setConnectTimeout(100000).build();
        httpGet.setConfig(requestConfig);
        //3.发起请求，获取响应信息
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        //4.设置本地保存的文件
        File file = null;
        try {
            //5. 发起请求，获取响应信息
            response = httpClient.execute(httpGet, new BasicHttpContext());
            System.out.println("HttpStatus.SC_OK:"+ HttpStatus.SC_OK);
            System.out.println("response.getStatusLine().getStatusCode():"+response.getStatusLine().getStatusCode());
            System.out.println("http-header:"+ JSON.toJSONString( response.getAllHeaders() ));
            System.out.println("http-filename:"+getFileName(response) );
            if(getFileName(response) == null){
                return "false";
            }
            //请求成功
            if(HttpStatus.SC_OK==response.getStatusLine().getStatusCode()){

                //6.取得请求内容
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    //这里可以得到文件的类型 如image/jpg /zip /tiff 等等 但是发现并不是十分有效，有时明明后缀是.rar但是取到的是null，这点特别说明
                    System.out.println(entity.getContentType());
                    //可以判断是否是文件数据流
                    System.out.println(entity.isStreaming());

                    //6.1.1获取文件名，拼接文件路径
                    String fileName=getFileName(response);
                    file = new File(fileName);
                    //6.1.2根据文件路径获取输出流
                    FileOutputStream output = new FileOutputStream(file);

                    //6.2 输入流：从钉钉服务器返回的文件流，得到网络资源并写入文件
                    InputStream input = entity.getContent();

                    //6.3 将数据写入文件：将输入流中的数据写入到输出流
                    byte b[] = new byte[1024];
                    int j = 0;
                    while( (j = input.read(b))!=-1){
                        output.write(b,0,j);
                    }
                    output.flush();
                    output.close();
                }
                if (entity != null) {
                    entity.consumeContent();
                }
            }
        } catch (IOException e) {
            System.out.println("request url=" + url + ", exception, msg=" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (response != null) try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file.getAbsolutePath();
    }

    public static String downloadMediaDealWithVoice(String accessToken,String mediaId) throws Exception  {
        String url = GlobalConstants.getProperties("get_material_url").replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID", mediaId);
        HttpGet httpGet = new HttpGet(url);
        //2.配置请求属性
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(100000).setConnectTimeout(100000).build();
        httpGet.setConfig(requestConfig);

        //3.发起请求，获取响应信息
        //3.1 创建httpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            //5. 发起请求，获取响应信息
            response = httpClient.execute(httpGet, new BasicHttpContext());
            System.out.println("HttpStatus.SC_OK:"+ HttpStatus.SC_OK);
            System.out.println("response.getStatusLine().getStatusCode():"+response.getStatusLine().getStatusCode());
            System.out.println("http-header:"+ JSON.toJSONString( response.getAllHeaders() ));
            System.out.println("http-filename:"+getFileName(response) );

            //请求成功
            if(HttpStatus.SC_OK==response.getStatusLine().getStatusCode()){

                //6.取得请求内容
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    //这里可以得到文件的类型 如image/jpg /zip /tiff 等等 但是发现并不是十分有效，有时明明后缀是.rar但是取到的是null，这点特别说明
                    System.out.println(entity.getContentType());
                    //可以判断是否是文件数据流
                    System.out.println(entity.isStreaming());

                    InputStream input = entity.getContent();
                    byte[] bytes = readInputStream(input);
                    return SpeechApi.recognition(bytes, GlobalConstants.getProperties("baiduSpeechApi"), GlobalConstants.getProperties("baiduSpeechApiKey"), GlobalConstants.getProperties("baiduSpeechApiSecretKey"));
                }
                if (entity != null) {
                    entity.consumeContent();
                }
            }
        } catch (IOException e) {
            System.out.println("request url=" + url + ", exception, msg=" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (response != null) try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean ifHasExitMediaId(String accessToken,String mediaId) throws Exception  {
        boolean flag = false;
        String url = GlobalConstants.getProperties("get_material_url").replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID", mediaId);
        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(100000).setConnectTimeout(100000).build();
        httpGet.setConfig(requestConfig);
        //3.发起请求，获取响应信息
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        //4.设置本地保存的文件
        File file = null;
        try {
            //5. 发起请求，获取响应信息
            response = httpClient.execute(httpGet, new BasicHttpContext());
            System.out.println("HttpStatus.SC_OK:"+ HttpStatus.SC_OK);
            System.out.println("response.getStatusLine().getStatusCode():"+response.getStatusLine().getStatusCode());
            System.out.println("http-header:"+ JSON.toJSONString( response.getAllHeaders() ));
            System.out.println("http-filename:"+getFileName(response) );
            if(getFileName(response) != null){
                flag = true;
            }
        } catch (IOException e) {
            System.out.println("request url=" + url + ", exception, msg=" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (response != null) try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return flag;
    }

    public static String getFileName(HttpResponse response) {
        Header contentHeader = response.getFirstHeader("Content-Disposition");
        String filename = null;
        if (contentHeader != null) {
            HeaderElement[] values = contentHeader.getElements();
            if (values.length == 1) {
                NameValuePair param = values[0].getParameterByName("filename");
                if (param != null) {
                    try {
                        filename = param.getValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return filename;
    }

    public static void delete(String filepath){
        try {
            File file = new File(filepath);
            if(file.exists()){
                file.delete();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }






}
