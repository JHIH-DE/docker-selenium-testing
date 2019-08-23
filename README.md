
# 基於Docker 構建Selenium Grid 分佈式測試環境
使用Docker在單台機器上運行Selenium Grid的簡單解決方案。

以下是使用Docker設置Selenium Grid的基本步驟。 如下圖所示，有一個Selenium-hub Image分別連接到Chrome和Firefox的兩個節點Image，我們將通過代碼和幾個Docker命令進行模擬。

[![Alt text](https://i.imgur.com/74xc1W4.png)](https://i.imgur.com/74xc1W4.png)

####
## 介紹
Selenium是什麼?

> Sellenium 是為瀏覽器自動化（Browser Automation）需求所設計的一套工具集合，讓程式可以直接驅動瀏覽器進行各種網站操作。也就是你的可以透過 Selenium 模擬瀏覽器的各種操作動作，藉此來進行網站的測試。 [SeleniumHQ](https://www.seleniumhq.org/projects/remote-control/)

Selenium Grid是什麼?

> Selenium Grid是Selenium套件的一部分，它專門用於在不同的瀏覽器、操作系統和機器上並行運行多個測試。
Selenium Grid使用了一個hub節點的概念，在這個概念中，您只在一個名為hub的機器上運行測試，但是執行將由不同的稱為Node(就是節點）的機器來執行。


Docker是什麼？
> Docker 作為一個用來管理應用容器（Container）的開源平台，可以很輕鬆地為任何一個應用創建可移植的，輕量級的容器。Docker 的容器虛擬化技術，不同於虛擬機VM，因為VM 是擁有自己獨立的操作系統以及硬件資源，而Docker 創建的容器可以看做是在操作系統中相互隔離運行的進程，但是共享同一個宿主機的硬件資源，比VM 來說更加輕量級，更容易管理與配置。

Katalon Recorder是什麼？

> Katalon  Recorder和Selenium IDE非常像，在記錄對網頁的動作行為後亦可導出程式碼、介面允許用戶快速的編輯和調試等。適用於Chrome和最新版本的Firefox，所以可以在Chrome上找到此插件安裝後即可使用。(注:Selenium IDE已不再更新)	[Chrome WebStore](https://chrome.google.com/webstore/detail/katalon-recorder-selenium/ljdobmomdgdljniojadhoplhkpialdid)

TestNG是什麼？

> TestNG 是一個開源的自動化測試框架，其靈感來自JUnit 和NUnit，但它引入了一些新功能，使其功能更強大，更易於使用。TestNG 的設計目標是能夠被用於進行各種類型測試：單元測試、功能測試，端到端測試、集成測試，等等。NG 是Next Generation 的簡寫，表示下一代，意在表示其產生的目的是要超越當前所有測試框架。TestNG 類似於JUnit（特別是JUnit 4），但它不是JUnit 的擴展，而是獨立的全新設計的框架。

####
## Running the Selenium-hub
啟動Selenium Grid指令如下:

    $docker-compose up -d

關閉Selenium Grid指令如下:

    $docker-compose down

使用 Docker-Compose 提供的指令查看 Docker Container 的執行狀態:

    $docker-compose ps

Selenium Web Console. [Selenium-hub]( http://localhost:4444/grid/console)

    http://localhost:4444/grid/console      
####
## Setting up Selenium WebDriver (本機執行使用)

 Safari (適用 Safari 10, 提供原生支持):
> 1.Safari的驅動程序可通過/usr/bin/safaridriver可執行文件啟動  
> 2.第一次必須手動執行完成驗證  
> 3. Safari -> 偏好設定 -> 進階 -> 勾選 在選單列中顯示 開發 選單  
> 4. 開發 -> 選擇 允許遠端自動化

 Chrome:
> [下載]( https://sites.google.com/a/chromium.org/chromedriver/downloads)  

####
## Runs the TestCase
測試專案執行(自動完成編譯，並執行測試案例):

    selenium-testing$ gradle test

測試案例(ikm專案)

    1.訪問首頁（http://local.walsin.com）
    2.系統登錄
    3.測試在不同螢幕解析度下網頁的呈現效果
    4.文件搜尋
    5.文件建立
    6.畫面擷取，並上傳至 Minio

[參考資料]

-  [Minio Sample Code](https://docs.min.io/docs/java-client-quickstart-guide.html )
-  [Selenium Grid Using Docker](https://medium.com/@amartanwar93/selenium-grid-using-docker-ab66f15c657b )
-  [SeleniumHQ/docker-selenium (git)](https://github.com/SeleniumHQ/docker-selenium)
-  [SafariDriver]( https://webkit.org/blog/6900/webdriver-support-in-safari-10/)  
