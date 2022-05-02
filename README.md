# 添加本地 Maven 步骤

##### 1、新建一个 Android Module

##### 2、在新建的 Module 的 build.gradle 中添加：

```
apply plugin: 'maven'
```

```
uploadArchives {
    repositories.mavenDeployer {

        // 设置本地的Maven地址
        // 这里是相对路径表示当前目录上一级目录的repo下，也可以写绝对路径
        repository(url: uri('../repo'))

        pom.artifactId = "mylib"
        pom.groupId = "cn.bill.library"
        pom.version = "1.0.0"

    }


}
```

##### 3、完成后在 AS 右侧找到 Gradle 面板，在要发布的 Module 下的 Tasks 下的 upload 展开，点击 uploadArchives 就可以得到本地 Maven 文件，路径就是上面 repository 中配置的 url

##### 注：如果在 Gradle 面板中没有，则在 AS 的 Preferences 中找到 Experimental，在 Gradle 下有个选项，"Do not build Gradle task list during Gradle Sycn"，把勾去掉就可以了

##### 4、使用：配置本地 Maven 仓库，在项目的 build.gradle 中添加：

```
allprojects {
    repositories {
        //本地仓库
        maven{
            url "$rootDir\\repo"
        }
    }
}
```
##### 在 具体 module 的 build.gradle 中添加：

```
implementation 'cn.bill.library:mylib:1.0.0'

```
