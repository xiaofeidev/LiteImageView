# LiteImageView[ ![Download](https://api.bintray.com/packages/xiaofei00/xiaofei/LiteImageView/images/download.svg) ](https://bintray.com/xiaofei00/xiaofei/LiteImageView/_latestVersion)

在展示项目中的位图资源时，能自动根据当前 `ImageView` 的尺寸确定采样率，加载合适尺寸的 `Bitmap`，从而避免加载到内存中的 `Bitmap` 尺寸大于当前 `ImageView` 的尺寸，以达到节省内存的目的。如此加载到内存中的 `Bitmap` 对象其尺寸总是不大于当前`ImageView`。

注意此控件的适用场景，只应该在你需要将设计给的切图（`png` 或 `jpg` 等**位图**资源），也就是放到当前项目资源目录中的图像，通过 `ImageView` 展示到界面上时。

`Gradle` 依赖:

```groovy
implementation 'com.github.xiaofeidev:lite-image-view:latest_version'
```



## 预览

<img src="https://github.com/xiaofei-dev/LiteImageView/blob/master/art/preview.jpg" width="20%" height="20%">

<img src="https://github.com/xiaofei-dev/LiteImageView/blob/master/art/msg.png" width="60%" height="60%">

## 使用

就一个自定义属性：



|  属性名   |                       解释                       |
| :-------: | :----------------------------------------------: |
| `srcLite` | 通过此属性设置的位图会自动确定采样率加载合适尺寸 |



推荐的使用方式：

```xml
<com.github.xiaofeidev.liteimage.LiteImageView
    android:id="@+id/imgImg"
    android:layout_width="200dp"
    android:layout_height="wrap_content"
    app:srcLite="@drawable/img_big"/>
```

对于控件的宽高，一定要有一个是**确定的**值，然后另一个设置成 `wrap_content`。

因为在 `LiteImageView` 内部确定位图采样率的时候必须要有一个锚定的 `View` 的尺寸值。如果没有这个锚定的尺寸值，就无法确定采样率，控件就会变成和普通的 `ImageView` 没啥区别了！

