# LiteImageView[ ![Download](https://api.bintray.com/packages/xiaofei00/xiaofei/LiteImageView/images/download.svg) ](https://bintray.com/xiaofei00/xiaofei/LiteImageView/_latestVersion)

在展示项目中的位图资源时，能自动根据当前 ImageView 的尺寸确定采样率，加载合适尺寸的 Bitmap，从而避免加载到内存中的 Bitmap 尺寸大于当前 ImageView 的尺寸，以达到节省内存的目的。如此加载到内存中的 Bitmap 对象其尺寸总是不大于当前ImageView。

注意此控件的适用场景，只应该在你需要将设计给的切图（png 或 jpg 等**位图**资源），也就是放到当前项目资源目录中的图像，通过 ImageView 展示到界面上时。

## 预览

<img src="https://github.com/xiaofei-dev/LiteImageView/blob/master/art/preview.jpg" width="20%" height="20%">

<img src="https://github.com/xiaofei-dev/LiteImageView/blob/master/art/msg.png" width="60%" height="60%">

## 使用

就一个自定义属性：



| 属性名  |                       解释                       |
| :-----: | :----------------------------------------------: |
| srcLite | 通过此属性设置的位图会自动确定采样率加载合适尺寸 |



```xml
<com.github.xiaofeidev.liteimage.LiteImageView
    android:id="@+id/imgImg"
    android:layout_width="200dp"
    android:layout_height="wrap_content"
    app:srcLite="@drawable/img_big"
    app:layout_constraintTop_toTopOf="parent"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintEnd_toEndOf="parent"/>
```
