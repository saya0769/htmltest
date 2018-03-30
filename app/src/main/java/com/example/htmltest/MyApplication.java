package com.example.htmltest;

/*
下面就是如何使用我们创建的全局变量了，在MainActivity中，首先要获得MyApplication的对象，因为MyApplication继承自Application，所以使用getApplication()方法即可，然后可以调用MyApplication的get或set方法进行访问全局变量。

        MyApplication application = (MyApplication)this.getApplication();
        application.setScore(200);
        int score = application.getScore();
        　　注意，在使用MyApplication之前需要修改AndroidManifest.xml文件的<application>中的android:name = ".MyApplication"，这样就不会访问系统提供的application，而访问我们自己创建的。
public class MyApplication extends LitePalApplication {
    ...
}
但是，有些程序可能会遇到一些更加极端的情况，比如说MyApplication需要继承另外一个AnotherApplication，并且这个AnotherApplication还是在jar包当中的，不能修改它的代码。这种情况应该算是比较少见了，但是如果你遇到了的话也不用急，仍然是有解释方案的。你可以把LitePal的源码下载下来，然后把src目录下的所有代码直接拷贝到你项目的src目录下面，接着打开LitePalApplication类，将它的继承结构改成继承自AnotherApplication，再让MyApplication继承自LitePalApplication，这样所有的Application就都可以在一起正常工作了。


*/

import org.litepal.LitePalApplication;

public class MyApplication extends LitePalApplication {
    int id;
    String directoryString;
    String contentString;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContentString() {
        return contentString;
    }

    public void setContentString(String contentString) {
        this.contentString = contentString;
    }

    public String getDirectoryString() {
        return directoryString;
    }

    public void setDirectoryString(String directoryString) {
        this.directoryString = directoryString;
    }
}












