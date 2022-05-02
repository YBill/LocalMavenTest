# 添加本地 Maven 步骤

### 使用 maven-publish 插件发布 aar 到 maven 仓库有两种方式：

#### 方式一：

##### 1、新建一个 Android Module

##### 2、在新建的 Module 的 build.gradle 中添加：

```
apply plugin: 'maven-publish'
```

```
publishing {
    publications {
        publish2Local(MavenPublication) {
            groupId = "cn.bill.library"
            artifactId = "mylib"
            version = "1.0.0"

            // Android Lib 用这个生成aar，注意不写components生成的文件中没有aar文件，没法调用
            // 依赖 bundleReleaseAar 任务，并上传其产出的aar
            afterEvaluate { artifact(tasks.getByName("bundleReleaseAar")) }

            // pom文件中声明依赖，从而传递到使用方
            pom.withXml {
                def dependenciesNode = asNode().appendNode('dependencies')
                configurations.implementation.allDependencies.each {
                    // 避免出现空节点或 artifactId=unspecified 的节点
                    if (it.group != null && (it.name != null && "unspecified" != it.name) && it.version != null) {
                        println "dependency=${it.toString()}"
                        def dependencyNode = dependenciesNode.appendNode('dependency')
                        dependencyNode.appendNode('groupId', it.group)
                        dependencyNode.appendNode('artifactId', it.name)
                        dependencyNode.appendNode('version', it.version)
                        dependencyNode.appendNode('scope', 'implementation')
                    }
                }
            }
        }
    }

    repositories {
        maven {
            url = "../repo"
        }
    }
}
```

##### 注：pom.withXml 的作用是为了传递依赖，可以在生成的 pom 文件中查看有没有依赖，就是在当前要发布到 maven 的 module 中（如mylibrary）通过 implementation 引用了某个库（如mylibrary中引入的toastcompat库），然后在使用当前 maven 仓库的 module 中（如app）引用不了toastcompat库，通过 pom.withXml 处理后就可以在 app 中使用了

##### 3、完成后在 AS 右侧找到 Gradle 面板，在要发布的 Module 下的 Tasks 下的 publishing 展开，点击 publish 就可以得到本地 Maven 文件，路径就是上面 repository 中配置的 url

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

#### 方式二：

##### 1、新建一个 Android Module

##### 2、在新建的 Module 的 build.gradle 中添加：

```
apply plugin: 'maven-publish'
```

```
afterEvaluate {
    publishing {
        publications {
            publish2Local(MavenPublication) {
                groupId = "cn.bill.library"
                artifactId = "mylib"
                version = "1.0.0"

                // 使用 Android Gradle 插件生成的组件，作为发布的内容
                from components.release

            }

        }

        repositories {
            maven {
                url = "../repo"
            }
        }
    }
}
```

##### 3、完成后在 AS 右侧找到 Gradle 面板，在要发布的 Module 下的 Tasks 下的 publishing 展开，点击 publish 就可以得到本地 Maven 文件，路径就是上面 repository 中配置的 url

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

### 两种方式的区别；

方式一中 publications 闭包中的有些配置还是不够优雅的，比较繁琐，如：
- 配置发布的内容（即配置上传的 aar 文件），是通过 `afterEvaluate { artifact(tasks.getByName("bundleReleaseAar")) }`
- 依赖传递：通过手动配置的方式，即 使用 withXml 闭包往 .pom 文件中，追加 dependency 依赖信息

但是在 Android Gradle 插件 3.6.0 及更高版本（说的是这里 classpath 'com.android.tools.build:gradle:3.6.0'）之后，也支持 maven-publish 插件了，使配置可以更加简洁。

将 publications 放到 afterEvaluate 内就可以了传递依赖了，并且使用 from components.release 发布依赖就可以生成 aar 了

### 注：关于生成注释，试了感觉没生效，暂时不处理了