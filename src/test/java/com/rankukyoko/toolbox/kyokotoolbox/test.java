package com.rankukyoko.toolbox.kyokotoolbox;

import cn.hutool.core.thread.SyncFinisher;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.*;
import java.net.Authenticator;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class test {


    public static void fun1() {
        List<String> stringList = Arrays.asList(
                "60177305763", "60166647389", "60108849684", "60107811981", "601124038453",
                "601126051795", "60125147751", "601110801680", "60139980133", "60128213503");
        List<String> phoneAddressList = Arrays.asList(
                "192.168.2.174", "192.168.2.151", "192.168.2.184", "192.168.2.168", "192.168.2.170",
                "192.168.2.155", "192.168.2.165", "192.168.2.133", "192.168.2.161", "192.168.2.159");
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < stringList.size(); i++) {
            JSONObject jsonObject = new JSONObject(true);
            jsonObject.put("ip", phoneAddressList.get(i));
            jsonObject.put("phone", stringList.get(i));
            jsonObject.put("phoneDevice", selectPhoneDeviceInfo(stringList.get(i)));
            System.out.println(jsonObject);
            jsonArray.add(jsonObject);
        }
        System.out.println(jsonArray);
        commandDevices(jsonArray);

    }


    public static void fun() {
        JSONObject jsonCommandDevices;
        jsonCommandDevices = commandDevices("am force-stop com.whatsapp");
        System.out.println(jsonCommandDevices);
        jsonCommandDevices = commandDevices("am force-stop fun.kitsunebi.kitsunebi4android");
        System.out.println(jsonCommandDevices);
        jsonCommandDevices = commandDevices("pm clear com.whatsapp");
        System.out.println(jsonCommandDevices);
        jsonCommandDevices = commandDevices("pm grant com.whatsapp android.permission.WRITE_EXTERNAL_STORAGE");
        System.out.println(jsonCommandDevices);
        jsonCommandDevices = commandDevices("pm grant com.whatsapp android.permission.READ_EXTERNAL_STORAGE");
        System.out.println(jsonCommandDevices);
        jsonCommandDevices = commandDevices("pm grant com.whatsapp android.permission.CAMERA");
        System.out.println(jsonCommandDevices);
    }


    public static void phoneDevices() {
        List<String> stringList = Arrays.asList("60177305763", "60166647389", "60108849684", "60107811981", "601124038453", "601126051795", "60125147751", "601110801680", "60139980133", "60128213503");
        JSONObject json;
        int in;
        for (String s : stringList) {
            in = getPhoneDeviceInfo("my", s);
            if (in == 0) {
                json = selectPhoneDeviceInfo(s);
                for (int x = 0; x < 60; x++) {
                    if (json != null) {
                        System.out.println(json);
                        break;
                    } else {
                        ThreadUtil.sleep(1000);
                    }
                }
            }
        }

    }


    public static int getPhoneDeviceInfo(String code, @NotNull String phone) {
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("1", "2");
        String url = "http://192.168.2.171:8889/?xpcommand=getdeviceinfo&phoneNumber=" + code + phone.replace("+", "");
        System.out.println(url);
        String result = createPost(jsonObject, url);
        System.out.println(result);
        if (result.equals("wait")) {
            return 0;
        } else {
            System.out.println(result);
            return -1;

        }
    }

    @Nullable
    public static JSONObject proxyQuire(String proxyIp, int proxyPort, String proxyUser, String proxypass) {
        String result = createGet("http://whois.pconline.com.cn/ipJson.jsp?json=true", proxyIp, proxyPort, proxyUser, proxypass);
        if (!result.equals("error")) {
            return JSON.parseObject(result);
        }
        return null;
    }

    public static int cutProxyIP(String proxyUser, String code) {
        String result = createGet("http://refresh.rola-ip.co/refresh?user=" + proxyUser + "&country=" + code);
        if (!result.equals("error")) {
            JSONObject jsonObject = JSON.parseObject(result);
            if (jsonObject.getString("Ret").equals("SUCCESS")) {
                return 0;
            }
        }
        return -1;
    }

    @NotNull
    public static String createGet(String url, String proxyIp, int proxyPort, String proxyUser, @NotNull String proxypass) {
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyIp, proxyPort));//设置socks代理服务器ip端口

        java.net.Authenticator.setDefault(new java.net.Authenticator()//由于okhttp好像没有提供socks设置Authenticator用户名密码接口，因此设置一个全局的Authenticator
        {
            private final PasswordAuthentication authentication = new PasswordAuthentication(proxyUser, proxypass.toCharArray());

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return authentication;
            }
        });
        OkHttpClient client = new OkHttpClient().newBuilder()
                .proxy(proxy)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @NotNull
    public static String createGet(String url, String proxyIp, int proxyPort) {
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyIp, proxyPort));//设置socks代理服务器ip端口
        OkHttpClient client = new OkHttpClient().newBuilder()
                .proxy(proxy)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @NotNull
    public static String createGet(String url) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }


    @NotNull
    public static String createPost(@NotNull JSONObject jsonParams, String url) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType.parse("application/json");
        RequestBody body = RequestBody.create(jsonParams.toJSONString().getBytes(StandardCharsets.UTF_8));
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }


    @Nullable
    public static JSONObject selectPhoneDeviceInfo(String phone) {
        try {
            List<Entity> phoneDeviceInfoList = Db.use().findAll(Entity.create("phone_device_infos").set("phone", phone));
            if (phoneDeviceInfoList.size() != 0) {
                if (phoneDeviceInfoList.get(0).getInt("regStatus").equals(1)) {
                    String phoneDeviceInfo = phoneDeviceInfoList.get(0).getStr("phoneDeviceInfo");
                    return JSON.parseObject(phoneDeviceInfo);
                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return null;
        }
        return null;
    }


    @NotNull
    public static JSONObject commandDevices(String phoneAddress, String command) {
        SyncFinisher syncFinisher = new SyncFinisher(1000);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "execCommand");
        jsonObject.put("command", command);
        Runnable runnable = () -> {
            String str = createPost(jsonObject, "http://" + phoneAddress + ":20390/shell");
            for (int i = 0; i < 10; i++) {
                if (str.equals("error")) {
                    str = createPost(jsonObject, "http://" + phoneAddress + ":20390/shell");
                } else {
                    break;
                }
            }
            JSONObject jsonObjectResult = new JSONObject(true);
            jsonObjectResult.put("commandPhoneResult", str);
            jsonObjectResult.put("commandPhoneResultIp", phoneAddress);
            System.out.println(jsonObjectResult);
            jsonArray.add(jsonObjectResult);
        };
        syncFinisher.addWorker(runnable);
        syncFinisher.start(true);
        syncFinisher.stop();
        System.out.println(ThreadUtil.getThreads().length);
        JSONObject json = new JSONObject(true);
        json.put("jsonArray", jsonArray);
        return json;
    }


    @NotNull
    public static JSONObject commandDevices(String command) {
        List<String> phoneAddressList = Arrays.asList(
                "192.168.2.174", "192.168.2.151", "192.168.2.184", "192.168.2.168", "192.168.2.170",
                "192.168.2.155", "192.168.2.165", "192.168.2.133", "192.168.2.161", "192.168.2.159");
        SyncFinisher syncFinisher = new SyncFinisher(1000);
        JSONArray jsonArray = new JSONArray();
        for (String phoneAddress : phoneAddressList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "execCommand");
            jsonObject.put("command", command);
            Runnable runnable = () -> {
                String str = createPost(jsonObject, "http://" + phoneAddress + ":20390/shell");
                for (int i = 0; i < 10; i++) {
                    if (str.equals("error")) {
                        str = createPost(jsonObject, "http://" + phoneAddress + ":20390/shell");
                    } else {
                        break;
                    }
                }
                JSONObject jsonObjectResult = new JSONObject(true);
                jsonObjectResult.put("commandPhoneResult", str);
                jsonObjectResult.put("commandPhoneResultIp", phoneAddress);
                System.out.println(jsonObjectResult);
                jsonArray.add(jsonObjectResult);
            };
            syncFinisher.addWorker(runnable);
        }
        syncFinisher.start(true);
        syncFinisher.stop();
        System.out.println(ThreadUtil.getThreads().length);
        JSONObject json = new JSONObject(true);
        json.put("length", phoneAddressList.size());
        json.put("jsonArray", jsonArray);
        return json;
    }


    @NotNull
    public static JSONObject commandDevices(@NotNull JSONArray jsonArrays) {
        SyncFinisher syncFinisher = new SyncFinisher(1000);
        JSONArray jsonArray = new JSONArray();
        for (int x = 0; x < jsonArrays.size(); x++) {
            String command = "echo '" + jsonArrays.getJSONObject(x).getString("phoneDevice") + "' >/sdcard/.config/DeviceInfo.json";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "execCommand");
            jsonObject.put("command", command);
            String phoneAddress = jsonArrays.getJSONObject(x).getString("ip");
            Runnable runnable = () -> {
                String str = createPost(jsonObject, "http://" + phoneAddress + ":20390/shell");
                for (int i = 0; i < 10; i++) {
                    if (str.equals("error")) {
                        str = createPost(jsonObject, "http://" + phoneAddress + ":20390/shell");
                    } else {
                        break;
                    }
                }
                JSONObject jsonObjectResult = new JSONObject(true);
                jsonObjectResult.put("commandPhoneResult", str);
                jsonObjectResult.put("commandPhoneResultIp", phoneAddress);
                System.out.println(jsonObjectResult);
                jsonArray.add(jsonObjectResult);
            };
            syncFinisher.addWorker(runnable);
        }
        syncFinisher.start(true);
        syncFinisher.stop();
        System.out.println(ThreadUtil.getThreads().length);
        JSONObject json = new JSONObject(true);
        json.put("length", jsonArrays.size());
        json.put("jsonArray", jsonArray);
        return json;
    }


    //招财猫


    //9436976859832dc1b63bf2ca51a021ce_4342
    public static void getUserToken() {
        String url = "http://web.szjct888.com/yhapi.ashx/?act=login&ApiName=1566552788895&PassWord=li5821286";
        System.out.println(createGet(url));
    }

    public static void getPhone() {

        String url = "http://web.szjct888.com/yhapi.ashx/?act=getPhone&token=9436976859832dc1b63bf2ca51a021ce_4342&iid=1092";
        System.out.println(createGet(url));

    }

    public static void getCode(String phone) {
        String url = "http://web.szjct888.com/yhapi.ashx/?act=getPhoneCode&token=9436976859832dc1b63bf2ca51a021ce_4342&iid=1092&mobile=" + phone;
        System.out.println(createGet(url));
    }


    public static void fun3(String code, String phone) {
        int in;
        in = getPhoneDeviceInfo(code, phone);
        JSONObject json = null;
        if (in == 0) {
            for (int x = 0; x < 60; x++) {
                json = selectPhoneDeviceInfo(phone);
                if (json != null) {
                    System.out.println(json);
                    break;
                } else {
                    System.out.println(json);
                    ThreadUtil.sleep(1000);
                }
            }
            assert json != null;
            String command = "echo '" + json.toJSONString() + "' >/sdcard/.config/DeviceInfo.json";
            commandDevices("192.168.2.137", command);
        }
    }

    public static void main(String[] args) throws Exception {

//        System.out.println(getPhoneDeviceInfo("my", "60139980133"));
//        String proxyUser = "li5821286_999999";
//        System.out.println(cutProxyIP(proxyUser,"my"));
//        System.out.println(proxyQuire("gate12.rola-ip.co",2084,proxyUser,"li5821286"));
        //li5821286_999995{"regionCode":"0","regionNames":"","proCode":"999999","err":"noprovince","city":"","cityCode":"0","ip":"175.138.139.1","pro":"","region":"","addr":" 马来西亚"}
        //{"regionCode":"0","regionNames":"","proCode":"999999","err":"noprovince","city":"","cityCode":"0","ip":"175.138.139.1","pro":"","region":"","addr":" 马来西亚"}
        //li5821286_999996{"regionCode":"0","regionNames":"","proCode":"999999","err":"noprovince","city":"","cityCode":"0","ip":"202.190.97.184","pro":"","region":"","addr":" 马来西亚"}
        //li5821286_999998{"regionCode":"0","regionNames":"","proCode":"999999","err":"noprovince","city":"","cityCode":"0","ip":"202.187.71.119","pro":"","region":"","addr":" 马来西亚"}
//        getUserToken();
//        System.out.println(createGet("http://whois.pconline.com.cn/ipJson.jsp?json=true", "47.57.236.182", 26001));
//        getPhone();
        fun3("mx", "524776291447");
//        getCode("541127091704");
    }

}


