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
    compile 'com.lexing.common:common:0.3.1'
    compile 'com.lexing.common:badgeview:1.0.0'
    compile 'com.lexing.common:lrecyclerview:1.3.2'
```

## 添加Permission
```xml
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
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

## BadgeView
BadgeView 是一个右上角带有小红点或数字的控件
```xml
    <com.lexing.badgeview.BadgeView
                app:btnBadgeHeight ="14dp"//图标高度
                app:btnBadgeColor ="@color/colorAccent"//图标颜色
                android:drawableTop="@mipmap/ic_launcher" //设置图标
                app:btnBadgeText="2" //设置提示文字 不设置即为小红点 />
```

## LRecyclerView 
此模块基于[XRecyclerView](https://github.com/jianghejie/XRecyclerView).

### 设置下拉刷新和加载更多
```java
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
               //刷新数据逻辑
            }

            @Override
            public void onLoadMore() {
                //加载更多逻辑
            }
        });
```
加载完成后需要手动通知视图完成状态和适配器更新
```java
    //loadmore callback complete
    ......
    mAdapter.addAll(datas);
    mRecyclerView.loadMoreComplete();
    mAdapter.notifyDataSetChanged();

    //refresh callback complete
    ......
    mAdapter.refresh(datas);
    mRecyclerView.refreshComplete();
    mAdapter.notifyDataSetChanged();
```

### 设置加载控件样式

```java
        //下拉刷新进度条样式
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader;
        //加载更多进度条样式
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallBeat);
        //下拉箭头图形 R.drawable.iconfont_downgrey 为默认样式
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
```  


### 设置列表头部
如列表是在Fragment中,android.R.id.content也可改成Activity中装载Frangment的容器id 
可加载多个头部
```java
View header =  LayoutInflater.from(this).inflate(R.layout.recyclerview_header, (ViewGroup)findViewById(android.R.id.content),false);

mRecyclerView.addHeaderView(header);
```

### 适配器
#### 简单适配器(列表中只有一种视图类型)
适配器需要继承`BaseRecyclerAdapter<T>`并实现convert方法,并可对`RecyclerHolder`实现链式设置
注意:convert 有两个,其中一个有`List<Object> payloads`的参数的是用来实现视图局部刷新的
```java

    @Override
    protected void convert(RecyclerHolder holder, GanhuoAndroid item, int position, boolean isScrolling) {
        holder.setText(R.id.tvTitle,item.desc)
                .setText(R.id.tvAuthor,item.author)
                .setText(R.id.tvUrl,item.url)
                .itemView.setBackgroundColor(getRandomColor(position));
    }
```
#### 多类型列表适配器
首先: 让每个需要列表展示的数据类都`implements Item`
```java
ublic class TitleData implements Item {
    public TitleData(String title) {
        this.title = title;
    }

    String title;
}
```
创建将数据类与相关的视图ViewHolder链接起来的`ItemViewProvider`
```java
public class TitleItemProvider extends ItemViewProvider<TitleData, RecyclerHolder> {

    @NonNull
    @Override
    protected RecyclerHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_time, parent, false);
        return new RecyclerHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerHolder holder, @NonNull TitleData titleData) {
        ((TextView)holder.itemView).setText(titleData.title);
    }
}
```
为你的适配器注册帮助类并设置到RecyclerView中,注意List中的所有数据类都必须注册到适配器上.
```java
        mMultiTypeAdapter=new MultiTypeAdapter(getInitDatas());
        mMultiTypeAdapter.applyGlobalMultiTypePool();
        mMultiTypeAdapter.register(ImageDatas.class,new ImageItemProvider());
        mMultiTypeAdapter.register(TitleData.class,new TitleItemProvider());
        mRecyclerView.setAdapter(mMultiTypeAdapter);
```

## 示例项目
#### ganhuo:  Rxjava+retrofit
`ExecutorManager`提供基于手机cpu数量大小的线程池
`SchedlersCompat`使得线程调度的写法更加简洁
`ServiceGenerator`是提供`Retrofit`网络服务的一般性写法,使用时只用加上主域名和接口api即可使用

