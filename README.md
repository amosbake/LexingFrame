# LexingFrame

## 简介
乐星互动公司开源的Android工具类库，主要是整合了开发中常用的工具类来帮助日常的开发和维护工作。

LexingFrame provides general purpose utilities for Android projects.focus on effciency and performance.

## 添加依赖
在项目的 build.gradle 文件中添加
```gradle
 repositories {
    ....
    maven {
            url 'https://dl.bintray.com/yhvector/maven/'
    }
 }
```

模块的 build.gradle 文件中添加
```gradle
    compile 'com.lexing.common:common:0.0.6'
```

## 添加Permission
```xml
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" >
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```


## 功能模块说明
Class | Introduction 
------ | ------ 
[AppUtils](https://github.com/amosbake/LexingFrame/blob/master/common/src/main/java/com/lexing/common/utils/AppUtils.java) | 应用相关方法，如获取应用信息，安装卸载，停止服务等 
[CipherUtils](https://github.com/amosbake/LexingFrame/blob/master/common/src/main/java/com/lexing/common/utils/CipherUtils.java) | 加密解密相关，MD5,SHA1,AES,HEX... 
[CoordinateTransformUtil](https://github.com/amosbake/LexingFrame/blob/master/common/src/main/java/com/lexing/common/utils/CoordinateTransformUtil.java) | 常用地图坐标系之间的相互转换.[详细说明](https://github.com/wandergis/coordtransform)
[DensityUtils](https://github.com/amosbake/LexingFrame/blob/master/common/src/main/java/com/lexing/common/utils/DensityUtils.java)|手机屏幕相关,sp,dp,px 的相互转换，获取屏幕高宽，特别是去除虚拟按键之后的高度
[FileUtils](https://github.com/amosbake/LexingFrame/blob/master/common/src/main/java/com/lexing/common/utils/FileUtils.java) | SD卡,Asserts的读,写,检测,解压
[L](https://github.com/amosbake/LexingFrame/blob/master/common/src/main/java/com/lexing/common/assist/L.java) | 日志的美观打印,可打印json字符串 [详细说明]( https://github.com/ZhaoKaiQiang/KLog)
[NetUtils](https://github.com/amosbake/LexingFrame/blob/master/common/src/main/java/com/lexing/common/utils/NetUtils.java)|网络状态的检测,扫描
[SPUtils](https://github.com/amosbake/LexingFrame/blob/master/common/src/main/java/com/lexing/common/utils/SPUtils.java) | SharedPreferences 的存取 `setParamDefault(context,"key","value")`
[StringUtils](https://github.com/amosbake/LexingFrame/blob/master/common/src/main/java/com/lexing/common/utils/StringUtils.java) | 字符串的转化,格式检查.钱,文件大小的格式化 比较等
[SystemTool](https://github.com/amosbake/LexingFrame/blob/master/common/src/main/java/com/lexing/common/utils/SystemTool.java) | 手机系统相关,mac地址,IMEI,UUID,SD卡剩余容量等
[TimeUtils](https://github.com/amosbake/LexingFrame/blob/master/common/src/main/java/com/lexing/common/utils/TimeUtils.java) | 解析日期帮助类
[ViewUtils](https://github.com/amosbake/LexingFrame/blob/master/common/src/main/java/com/lexing/common/utils/ViewUtils.java) | 方便视图操作(检测事件,删除子视图,截图,popupwindow)
[Check](https://github.com/amosbake/LexingFrame/blob/master/common/src/main/java/com/lexing/common/assist/Check.java) | 对字符串,集合,数组的判空方法
[CrashHandler](https://github.com/amosbake/LexingFrame/blob/master/common/src/main/java/com/lexing/common/assist/CrashHandler.java) | 程序错误跳出时给予提示并记录错误报告




