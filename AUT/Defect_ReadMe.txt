分類方式為 IBM 所提出的：
Orthogonal Defect Classification
詳細的說明及例子可以參考下面兩個網頁
http://www.research.ibm.com/softeng/ODC/DETODC.HTM#type
http://www.ibm.com/developerworks/cn/java/j-odc/

因為目前的 AUT 只有小算盤和 WordProcessor,
兩個都沒有用到 Thread,所以第五項不做,
而第七項的改動較大也不做,
所以只做 1,2,3,4,6 共五項
小算盤每人插入五個不同類型的 Defect 各一個,三個人共 15 個 Defect
Word Processor 每人插入五個不同類型的 Defect 各兩個,三個人共 30 個 Defect

PS.加入 Bug 時要注意
(1)是否會因為修改而導致原本的部分 code 無法執行到而導致 coverage 降低
(2)或是因此而無法執行到另外一個 bug 所在的程式碼
(3)改到別的 bug 使該 bug 反而能正常運作,或是成為不同類型的 bug
======================
1.Assignment/Initialization
說明：給值錯誤或是未給初值等情況
2.Checking
說明：邊界/條件檢查錯誤
3.Algorithm/Method
說明：演算法實作上的錯誤
4.Function/Class/Object
說明：Function/Class 設計不良
5.Timing/Serialization
說明：因為兩個 Thread 未同步所引發的問題
6.Interface/O-O Messages
說明：介面之間參數傳遞引起的錯誤
7.Relationship
說明：程式碼之間錯誤的關連引起的錯誤